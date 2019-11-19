package com.mbooking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiInternalServerErrorException;
import com.mbooking.model.*;
import com.mbooking.repository.*;
import com.mbooking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	ReservationRepository resRep;
	
	@Autowired
	UserRepository userRep;
	
	@Autowired
	ManifestationRepository manifestRep;
	
	@Autowired
	ManifestationSectionRepository manifestSectionRep;
	
	@Autowired
	ReservationDetailsRepository resDetRep;
	
	@Autowired
	ManifestationDayRepository manDayRep;
	
	@Override
	public double getCurrentTotalPriceForManifestationDay(Long id) {
		double totalPrice = 0;
		List<Reservation> reservations = resRep.findDistinctByReservationDetailsInAndStatusIn(
				resDetRep.findByManifestationDayId(id), 
				Arrays.asList(ReservationStatus.CONFIRMED));
		for (Reservation res : reservations) {
			totalPrice += res.getPrice();
		}
		return totalPrice;
	}
	
	@Override
	public double getExpectedTotalPriceForManifestationDay(Long id) {
		
		double totalPrice = 0;
		List<Reservation> reservations = resRep.findDistinctByReservationDetailsInAndStatusIn(
				resDetRep.findByManifestationDayId(id), 
				Arrays.asList(ReservationStatus.CREATED, ReservationStatus.CONFIRMED));
		for (Reservation res : reservations) {
			totalPrice += res.getPrice();
		}
		return totalPrice;
	}
	
	@Override 
	public double getCurrentTotalPriceForManifestation(Long id) {
		double totalPrice = 0;
		List<Reservation> reservations = resRep.findByManifestationIdAndStatusNotIn(id, 
				Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED, ReservationStatus.CREATED));
		for (Reservation res : reservations) {
			totalPrice += res.getPrice();
		}
		return totalPrice;
	}
	
	@Override
	public double getExpectedTotalPriceForManifestation(Long id) {
		double totalPrice = 0;
		List<Reservation> reservations = resRep.findByManifestationIdAndStatusNotIn(id, 
				Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
		for (Reservation res : reservations) {
			totalPrice += res.getPrice();
		}
		return totalPrice;
	}
	
	@Override
	public List<ViewReservationDTO> findAllReservations() {
		List<Reservation> allReservations = resRep.findAll();
		List<ViewReservationDTO> reservationsDTO = new ArrayList<>();
		for (Reservation res : allReservations) {
			ViewReservationDTO resDTO = new ViewReservationDTO(res);
			reservationsDTO.add(resDTO);
		}
		return reservationsDTO;
	}
	
	@Override
	public List<ViewReservationDTO> findAllByUserEmail(String email) {
		Customer customer = (Customer) userRep.findByEmail(email);
		if (customer != null) {
			List<Reservation> reservations = resRep.findAllByCustomer(customer);
			List<ViewReservationDTO> reservationsDTO = new ArrayList<>();
			for (Reservation res : reservations) {
				ViewReservationDTO resDTO = new ViewReservationDTO(res);
				reservationsDTO.add(resDTO);
			}
			return reservationsDTO;
		}
		else throw new ApiInternalServerErrorException("No such email");
		
	}
	

	@Override
	public CancelReservationStatusDTO cancelReservation(Long id) {
		Optional<Reservation> optRes = resRep.findById(id);
		
		if (optRes.isPresent()) {
			Reservation reservation = optRes.get();
			
			if (reservation.getStatus() != ReservationStatus.CREATED)
				throw new ApiInternalServerErrorException("Reservation cannot be canceled");
			
			reservation.setStatus(ReservationStatus.CANCELED);
			resRep.save(reservation);
			
			CancelReservationStatusDTO dto = new CancelReservationStatusDTO(
					"Canceled", "Successfully canceled reservation");
			
			return dto;
		}
		else throw new ApiBadRequestException("No such reservation");
	}


	@Override
	public JsonNode makeReservation(ReservationDTO dto) {
		
		if (CheckIfDuplicateSeats(dto.getReservationDetails())) 
			throw new ApiBadRequestException("Duplicate seats");
		
		double totalPrice = 0;
		
		Optional<Manifestation> manOpt = manifestRep.findById(dto.getManifestationId());
		if (!manOpt.isPresent()) throw new ApiBadRequestException("No such manifestation");
		
		Manifestation manifestation = manOpt.get();
		
		List<ManifestationSection> manifestationSections = new ArrayList<>();
		for (ReservationDetailsDTO details : dto.getReservationDetails()) {
			Optional<ManifestationSection> section = manifestSectionRep.findById(details.getManifestationSectionId());
			if (!section.isPresent()) throw new ApiBadRequestException("No such section");
			else {
				ManifestationSection mSection = section.get();
				
				for (ReservationDetailsDTO d1 : dto.getReservationDetails()) {
					if (details == d1 || !mSection.getSelectedSection().isSeating()) 
						continue;
					
					if (details.getManifestationSectionId() == d1.getManifestationSectionId() &&
							details.getManifestationDayId() == d1.getManifestationDayId()) {
						if (details.getRow() == d1.getRow() &&
								details.getColumn() == d1.getColumn())
							throw new ApiBadRequestException("Duplicate seats");
					}
				}
				
				totalPrice += mSection.getPrice();
				if (this.containsSameId(manifestationSections, mSection.getId())) 
					continue;
				
				manifestationSections.add(mSection);
			}
		}
		
		for (int i = 0; i < manifestationSections.size(); i++) {
			if (manifestationSections.get(i).getManifestation().getId() != manifestation.getId())
				throw new ApiBadRequestException("Sections are not from the same manifestation");
		}
		
		if (!manifestation.isReservationsAvailable() ||
				new Date().after(manifestation.getReservableUntil())) {
			throw new ApiException("Manifestation is not reservable", HttpStatus.BAD_REQUEST);
		}
		
		if (manifestation.getMaxReservations() < dto.getReservationDetails().size())
			throw new ApiBadRequestException("Reservation limit reached");
		
		List<ManifestationDay> days = new ArrayList<>();
		for (ReservationDetailsDTO detail : dto.getReservationDetails()) {
			ManifestationDay day = manDayRep.findByIdAndManifestationId(
					detail.getManifestationDayId(), manifestation.getId());
			if (day != null)
				days.add(day);
			else
				throw new ApiBadRequestException("No such manifestation day");
		}
		
		for (ManifestationSection ms : manifestationSections) {
			int initialSize = 0;
			List<ReservationDetailsDTO> dtoDetails = dto.getReservationDetails();
			for (ReservationDetailsDTO details : dtoDetails) {
				if (details.getManifestationSectionId() == ms.getId()) {
					initialSize++;
					if (ms.getSelectedSection().isSeating()) {
						
						if (ms.getSelectedSection().getSectionColumns() < details.getColumn() ||
								ms.getSelectedSection().getSectionRows() < details.getRow())
							throw new ApiBadRequestException("No such seat");
						
						ReservationDetails rd = resDetRep.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getRow(), 
								details.getColumn(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						if (rd != null)
							throw new ApiBadRequestException("Seat taken");
					}
					
					else {
						List<ReservationDetails> rds = resDetRep.findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						
						if (rds.size() + initialSize > ms.getSize())
							throw new ApiBadRequestException("No more space");
					}
				}
			}
		}
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Customer customer = (Customer) userRep.findByEmail(currentPrincipalName);
		if (customer == null) throw new ApiInternalServerErrorException("No such user");
		
		
		Reservation reservation = new Reservation();
		List<ReservationDetails> reservationDetailsCol = new ArrayList<ReservationDetails>();
		
		List<ManifestationSection> sections = new ArrayList<>();
		for (ReservationDetailsDTO details : dto.getReservationDetails()) {
			ManifestationSection manSection = manifestSectionRep.findById(details.getManifestationSectionId()).get();
			ReservationDetails reservationDetails = new ReservationDetails();
			if (manSection.getSelectedSection().isSeating()) {
				reservationDetails.setColumn(details.getColumn());
				reservationDetails.setRow(details.getRow());
				reservationDetails.setSeating(manSection.getSelectedSection().isSeating());
			}
			else {
				reservationDetails.setColumn(0);
				reservationDetails.setRow(0);
				reservationDetails.setSeating(manSection.getSelectedSection().isSeating());
			}
			
			reservationDetails.setReservation(reservation);
			reservationDetails.setManifestationSection(manSection);
			reservationDetails.setManifestationDay(manDayRep.findById(details.getManifestationDayId()).get());
			manSection.getReservationsDetails().add(reservationDetails);
			reservationDetailsCol.add(reservationDetails);
			sections.add(manSection);
		}
		
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, 3); //TODO manifestation needs reservation period
		
		
		reservation.setCustomer(customer);
		reservation.setDateCreated(currentDate);
		reservation.setExpirationDate(calendar.getTime()); 
		//TODO: remove reservation.setManifestationDays(days);
		reservation.setPrice(totalPrice);
		reservation.setReservationDetails(reservationDetailsCol);
		reservation.setStatus(ReservationStatus.CREATED);
		reservation.setManifestation(manifestation);
		reservation = resRep.save(reservation);
		manifestSectionRep.saveAll(sections);
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode retVal = mapper.createObjectNode();
		retVal.put("message", "Successful reservation");
		retVal.put("manifestation", manifestation.getName());
		retVal.put("manifestationId", manifestation.getId());
		retVal.put("expirationDate", new SimpleDateFormat("dd.MM.yyyy HH:mm")
				.format(calendar.getTime()));
		retVal.put("reservationId", reservation.getId());
		
		return retVal;
	}

	
	
	//Utility
	public boolean containsSameId(final List<ManifestationSection> list, Long id){
	    return list.stream().filter(o -> o.getId() == id).findFirst().isPresent();
	}
	
	public List<ReservationDetails> getReservationDetailsForDayId(Long id) {
		return resDetRep.findByManifestationDayId(id);
	}
	
	public boolean CheckIfDuplicateSeats(List<ReservationDetailsDTO> details) {
		
		for (ReservationDetailsDTO d0 : details) {
			for (ReservationDetailsDTO d1 : details) {
				if (d0 == d1 || !d0.isSeating() || !d1.isSeating()) 
					continue;
				
				if (d0.getManifestationSectionId() == d1.getManifestationSectionId() &&
						d0.getManifestationDayId() == d1.getManifestationDayId()) {
					if (d0.getRow() == d1.getRow() &&
							d0.getColumn() == d1.getColumn())
						return true;
				}
			}
		}
		
		return false;
	}

	
	
	
	
	
}
