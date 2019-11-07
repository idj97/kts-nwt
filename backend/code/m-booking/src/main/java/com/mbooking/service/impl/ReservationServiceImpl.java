package com.mbooking.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Customer;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationSection;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;
import com.mbooking.repository.ManifestationDayRepository;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.repository.ManifestationSectionRepository;
import com.mbooking.repository.ReservationDetailsRepository;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.ReservationService;

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
		else throw new ApiException("No such email", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	

	@Override
	public CancelReservationStatusDTO cancelReservation(Long id) {
		Optional<Reservation> optRes = resRep.findById(id);
		
		if (optRes.isPresent()) {
			Reservation reservation = optRes.get();
			
			if (reservation.getStatus() != ReservationStatus.CREATED)
				throw new ApiException("Reservation cannot be canceled", HttpStatus.INTERNAL_SERVER_ERROR);
			
			reservation.setStatus(ReservationStatus.CANCELED);
			resRep.save(reservation);
			
			CancelReservationStatusDTO dto = new CancelReservationStatusDTO(
					"Canceled", "Successfully canceled reservation");
			
			return dto;
		}
		else throw new ApiException("No such reservation", HttpStatus.BAD_REQUEST);
	}


	@Override
	public JsonNode makeReservation(ReservationDTO dto) {
		
		if (CheckIfDuplicateSeats(dto.getReservationDetails())) 
			throw new ApiException("Duplicate seats", HttpStatus.BAD_REQUEST);
		
		double totalPrice = 0;
		
		Optional<Manifestation> manOpt = manifestRep.findById(dto.getManifestationId());
		if (!manOpt.isPresent()) throw new ApiException("No such manifestation", HttpStatus.BAD_REQUEST);
		
		Manifestation manifestation = manOpt.get();
		
		List<ManifestationSection> manifestationSections = new ArrayList<>();
		for (ReservationDetailsDTO details : dto.getReservationDetails()) {
			Optional<ManifestationSection> section = manifestSectionRep.findById(details.getManifestationSectionId());
			if (!section.isPresent()) throw new ApiException("No such section", HttpStatus.BAD_REQUEST);
			else {
				ManifestationSection mSection = section.get();
				
				for (ReservationDetailsDTO d1 : dto.getReservationDetails()) {
					if (details == d1 || !mSection.getSelectedSection().isSeating()) 
						continue;
					
					if (details.getManifestationSectionId() == d1.getManifestationSectionId() &&
							details.getManifestationDayId() == d1.getManifestationDayId()) {
						if (details.getRow() == d1.getRow() &&
								details.getColumn() == d1.getColumn())
							throw new ApiException("Duplicate seats", HttpStatus.BAD_REQUEST);
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
				throw new ApiException("Sections are not from the same manifestation", HttpStatus.BAD_REQUEST);
		}
		
		if (new Date().after(manifestation.getReservableUntil())) throw new ApiException(
				"Manifestation is not reservable", HttpStatus.BAD_REQUEST);
		
		if (manifestation.getMaxReservations() < dto.getReservationDetails().size())
			throw new ApiException("Reservation limit reached", HttpStatus.BAD_REQUEST);
		
		List<ManifestationDay> days = new ArrayList<>();
		for (ReservationDetailsDTO detail : dto.getReservationDetails()) {
			ManifestationDay day = manDayRep.findByIdAndManifestationId(
					detail.getManifestationDayId(), manifestation.getId());
			if (day != null)
				days.add(day);
			else
				throw new ApiException("No such manifestation day", HttpStatus.BAD_REQUEST);
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
							throw new ApiException("No such seat", HttpStatus.BAD_REQUEST);
						
						ReservationDetails rd = resDetRep.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getRow(), 
								details.getColumn(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						if (rd != null)
							throw new ApiException("Seat taken", HttpStatus.BAD_REQUEST, "err1");
					}
					
					else {
						List<ReservationDetails> rds = resDetRep.findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						
						if (rds.size() + initialSize > ms.getSize())
							throw new ApiException("No more space", HttpStatus.BAD_REQUEST);
					}
				}
			}
		}
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Customer customer = (Customer) userRep.findByEmail(currentPrincipalName);
		if (customer == null) throw new ApiException("No such user", HttpStatus.INTERNAL_SERVER_ERROR);
		
		
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
		reservation.setManifestationDays(days);
		reservation.setPrice(totalPrice);
		reservation.setReservationDetails(reservationDetailsCol);
		reservation.setStatus(ReservationStatus.CREATED);
		resRep.save(reservation);
		manifestSectionRep.saveAll(sections);
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode retVal = mapper.createObjectNode();
		retVal.put("message", "Successful reservation");
		retVal.put("manifestation", manifestation.getName());
		retVal.put("manifestationId", manifestation.getId());
		retVal.put("expirationDate", new SimpleDateFormat("dd.MM.yyyy HH:mm")
				.format(calendar.getTime()));
		
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