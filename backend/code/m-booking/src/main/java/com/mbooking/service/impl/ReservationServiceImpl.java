package com.mbooking.service.impl;

import com.mbooking.dto.*;
import com.mbooking.exception.*;
import com.mbooking.model.*;
import com.mbooking.repository.*;
import com.mbooking.service.EmailSenderService;
import com.mbooking.service.PDFCreatorService;
import com.mbooking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Autowired 
	EmailSenderService emailSender;
	
	@Autowired
	PDFCreatorService pdfCreator;
	
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
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			
			if (!reservation.getCustomer().getEmail().equals(currentPrincipalName)) 
				throw new ReservationNotFromCurrentCustomerException();
			
			if (reservation.getStatus() != ReservationStatus.CREATED)
				throw new ApiInternalServerErrorException("Reservation cannot be canceled");
			
			reservation.setStatus(ReservationStatus.CANCELED);
			resRep.save(reservation);
			
			CancelReservationStatusDTO dto = new CancelReservationStatusDTO(
					"Canceled", "Successfully canceled reservation");
			
			return dto;
		}
		else throw new NoSuchReservationException();
	}


	/*
	 * IDEA
	 * Cannot make reservation when:
	 * - Reservation details are null or empty
	 * - False manifestation id
	 * - False manifestation section id
	 * - Selected seats are duplicates for selected manifestation day
	 * - Manifestation section exists but not from the same manifestation
	 * - Manifestation is marked as not reservable
	 * - Current date greater then reservable until date
	 * - Reservation details exceeds max reservations per reservation for selected manifestation day
	 * - Customer reservation details exceeds max reservations for selected manifestation day
	 * - No such user is found
	 * - No such selected seat is found
	 * - No such manifestation day is found for selected manifestation
	 * - Seat is taken for selected manifestation day
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MakeReservationResponseDTO makeReservation(ReservationDTO dto) {
		
		if (dto.getReservationDetails() == null || dto.getReservationDetails().size() == 0) 
			throw new EmptyReservationDetailsException();
		
		double totalPrice = 0;
		
		Optional<Manifestation> manOpt = manifestRep.findById(dto.getManifestationId());
		if (!manOpt.isPresent()) throw new NoSuchManifestationException();
		
		Manifestation manifestation = manOpt.get();
		
		List<ManifestationSection> manifestationSections = new ArrayList<>();
		for (ReservationDetailsDTO details : dto.getReservationDetails()) {
			Optional<ManifestationSection> section = manifestSectionRep.findById(details.getManifestationSectionId());
			if (!section.isPresent()) throw new NoSuchSectionException();
			else {
				ManifestationSection mSection = section.get();
				
				for (ReservationDetailsDTO d1 : dto.getReservationDetails()) {
					if (details == d1 || !mSection.getSelectedSection().isSeating()) 
						continue;
					
					if (details.getManifestationSectionId() == d1.getManifestationSectionId() &&
							details.getManifestationDayId() == d1.getManifestationDayId()) {
						if (details.getRow() == d1.getRow() &&
								details.getColumn() == d1.getColumn())
							throw new DuplicateSeatsException();
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
				throw new SectionNotFromSameManifestationException();
		}
		
		if (!manifestation.isReservationsAvailable()) 
			throw new ManifestationReservationsAvailableException();
		
		if (new Date().after(manifestation.getReservableUntil())) 
			throw new ReservableUntilException();
		
		if (manifestation.getMaxReservations() < dto.getReservationDetails().size())
			throw new MaxReservationsException();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Customer customer = (Customer) userRep.findByEmail(currentPrincipalName);
		if (customer == null) throw new NoSuchUserException();
		
		
		List<ManifestationDay> days = new ArrayList<>();
		for (ReservationDetailsDTO detail : dto.getReservationDetails()) {
			ManifestationDay day = manDayRep.findByIdAndManifestationId(
					detail.getManifestationDayId(), manifestation.getId());
			if (day != null) {
				days.add(day);
				int numOfDetails = getCustomerTotalReservationDetailsForManifestation(customer, day, manifestation);
				if (numOfDetails + dto.getReservationDetails().size() > manifestation.getMaxReservations())
					throw new MaxReservationsException();
			}
			else
				throw new NoSuchManifestationDayException();
		}
		
		for (ManifestationSection ms : manifestationSections) {
			int initialSize = 0;
			List<ReservationDetailsDTO> dtoDetails = dto.getReservationDetails();
			for (ReservationDetailsDTO details : dtoDetails) {
				if (details.getManifestationSectionId() == ms.getId()) {
					initialSize++;
					if (ms.getSelectedSection().isSeating()) {
						
						if (ms.getSelectedSection().getSectionColumns() < details.getColumn() ||
								ms.getSelectedSection().getSectionRows() < details.getRow() ||
								details.getRow() < 0 ||
								details.getColumn() < 0)
							throw new NoSuchSeatException();
						
						ReservationDetails rd = resDetRep.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getRow(), 
								details.getColumn(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						if (rd != null)
							throw new SeatTakenException();
					}
					
					else {
						List<ReservationDetails> rds = resDetRep.findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
								details.getManifestationSectionId(),
								details.getManifestationDayId(),
								Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED));
						
						if (rds.size() + initialSize > ms.getSize())
							throw new NoMoreSpaceException();
					}
				}
			}
		}
		
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
		
		
		MakeReservationResponseDTO retVal = new MakeReservationResponseDTO();
		retVal.setMessage("Successful reservation");
		retVal.setManifestation(manifestation.getName());
		retVal.setManifestationId(manifestation.getId());
		retVal.setExpirationDate(new SimpleDateFormat("dd.MM.yyyy HH:mm")
				.format(calendar.getTime()));
		retVal.setReservationId(reservation.getId());
		
		
		/*ObjectMapper mapper = new ObjectMapper();
		ObjectNode retVal = mapper.createObjectNode();
		retVal.put("message", "Successful reservation");
		retVal.put("manifestation", manifestation.getName());
		retVal.put("manifestationId", manifestation.getId());
		retVal.put("expirationDate", new SimpleDateFormat("dd.MM.yyyy HH:mm")
				.format(calendar.getTime()));
		retVal.put("reservationId", reservation.getId());*/
		
		//SENDING EMAIL WITH PDF ATTACHED
//		ByteArrayResource bytes = new ByteArrayResource(pdfCreator.createReservationPDF(reservation).toByteArray());
//		emailSender.sendMessageWithAttachment(
//				"milosmalidza@gmail.com",
//				"Reservation",
//				"Thank you for making reservation in m-booking.",
//				"Reservation.pdf",
//				bytes);
		
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

	public int getCustomerTotalReservationDetailsForManifestation(Customer customer, ManifestationDay manifestationDay, Manifestation manifestation) {
		List<ReservationDetails> resDets = resDetRep.findByReservationCustomerAndManifestationDayAndReservationManifestation(customer, manifestationDay, manifestation);
		
		return resDets.size();
	}
	
	
	
	
	
	
}
