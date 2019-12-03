package com.mbooking.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiInternalServerErrorException;
import com.mbooking.model.Customer;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationSection;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;
import com.mbooking.model.Section;
import com.mbooking.repository.ManifestationDayRepository;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.repository.ManifestationSectionRepository;
import com.mbooking.repository.ReservationDetailsRepository;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceUnitTests {
	
	@Autowired
	private ReservationService reservationService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@MockBean
	private ReservationRepository reservationRepositoryMocked;
	
	@MockBean
	private ReservationDetailsRepository reservationDetailsRepositoryMocked;
	
	@MockBean
	private UserRepository userRepositoryMocked;
	
	@MockBean
	private ManifestationRepository manifestationRepositoryMocked;
	
	@MockBean
	private ManifestationSectionRepository manifestationSectionRepositoryMocked;
	
	@MockBean
	private ManifestationDayRepository manifestationDayRepositoryMocked;
	
	@Test
	public void testMakeReservation() {
		
		Customer customer = new Customer();
		customer.setId(-1l);
		customer.setUsername("username");
		customer.setPassword("password");
		customer.setEmail("ex@gmail.com");
		
		ReservationDTO rDTO = new ReservationDTO();
		rDTO.setManifestationId(-1l);
		List<ReservationDetailsDTO> rDDTOS = new ArrayList<ReservationDetailsDTO>();
		ReservationDetailsDTO rDDTO1 = new ReservationDetailsDTO();
		rDDTO1.setColumn(1);
		rDDTO1.setRow(1);
		rDDTO1.setManifestationDayId(-1l);
		rDDTO1.setManifestationSectionId(-1l);
		
		ReservationDetailsDTO rDDTO2 = new ReservationDetailsDTO();
		rDDTO2.setColumn(2);
		rDDTO2.setRow(1);
		rDDTO2.setManifestationDayId(-1l);
		rDDTO2.setManifestationSectionId(-1l);
		
		rDDTOS.add(rDDTO1);
		rDDTOS.add(rDDTO2);
		rDTO.setReservationDetails(rDDTOS);
		
		Location location = new Location();
		ManifestationDay md1 = new ManifestationDay();
		md1.setDate(new Date());
		md1.setId(-1l);
		
		ManifestationDay md2 = new ManifestationDay();
		md2.setDate(new Date());
		md2.setId(-2l);
		
		List<ManifestationDay> mDays = Arrays.asList(md1, md2);
		
		Manifestation manifestation = new Manifestation();
		manifestation.setId(rDTO.getManifestationId());
		manifestation.setDescription("Mock");
		manifestation.setName("Mock");
		manifestation.setLocation(location);
		manifestation.setManifestationDays(mDays);
		manifestation.setMaxReservations(5);
		manifestation.setReservationsAvailable(true);
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, 3);
		manifestation.setReservableUntil(calendar.getTime());
		
		Section ss1 = new Section();
		ss1.setSectionColumns(10);
		ss1.setSectionRows(10);
		ss1.setSeating(true);
		Section ss2 = new Section();
		ss2.setSectionColumns(10);
		ss2.setSectionRows(10);
		ss2.setSeating(false);
		
		ManifestationSection ms1 = new ManifestationSection();
		ms1.setId(rDDTO1.getManifestationSectionId());
		ms1.setSelectedSection(ss1);
		ms1.setManifestation(manifestation);
		ms1.setReservationsDetails(new ArrayList<ReservationDetails>());
		
		Optional<Manifestation> manifestationOPT = Optional.ofNullable(manifestation);
		Optional<ManifestationSection> mSectionOPT = Optional.ofNullable(ms1);
		
		Mockito.when(manifestationRepositoryMocked.findById(rDTO.getManifestationId()))
		.thenReturn(manifestationOPT);
		
		Mockito.when(manifestationSectionRepositoryMocked.findById(Mockito.anyLong()))
		.thenReturn(mSectionOPT);
		
		Mockito.when(manifestationDayRepositoryMocked.findByIdAndManifestationId(md1.getId(), manifestation.getId()))
		.thenReturn(md1);
		
		Mockito.when(manifestationDayRepositoryMocked.findByIdAndManifestationId(md2.getId(), manifestation.getId()))
		.thenReturn(md2);
		
		Mockito.when(manifestationDayRepositoryMocked.findById(md1.getId())).thenReturn(Optional.ofNullable(md1));
		Mockito.when(manifestationDayRepositoryMocked.findById(md2.getId())).thenReturn(Optional.ofNullable(md2));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		Mockito.when(authentication.getName()).thenReturn(customer.getEmail());
		
		Mockito.when(userRepositoryMocked.findByEmail(customer.getEmail()))
		.thenReturn(customer);
		
		
		for (int i = 0; i < rDDTOS.size(); i++) {
			
			ReservationDetails d = new ReservationDetails();
			d.setColumn(rDDTOS.get(i).getColumn());
			d.setRow(rDDTOS.get(i).getRow());
			d.setManifestationSection(ms1);
			d.setSeating(ms1.getSelectedSection().isSeating());
			
			Mockito.when(reservationDetailsRepositoryMocked.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
					rDDTOS.get(i).getManifestationSectionId(), 
					rDDTOS.get(i).getRow(), 
					rDDTOS.get(i).getColumn(), 
					rDDTOS.get(i).getManifestationDayId(), 
					Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED)))
					.thenReturn(null);
			
		}
		
		Reservation res = new Reservation();
		res.setId(-1l);
		res.setCustomer(customer);
		
		Mockito.when(reservationRepositoryMocked.save(Mockito.any(Reservation.class))).thenReturn(res);
		
		
		MakeReservationResponseDTO rFinal = reservationService.makeReservation(rDTO);
		
		assertEquals("Successful reservation", rFinal.getMessage());
		
	}
	
	@Test
	public void testFindAllByUserEmail() {
		Reservation r1 = new Reservation();
		r1.setDateCreated(new Date());
		r1.setExpirationDate(new Date());
		r1.setPrice(100);
		r1.setManifestation(new Manifestation());
		r1.setReservationDetails(new ArrayList<ReservationDetails>());
		
		Reservation r2 = new Reservation();
		r2.setDateCreated(new Date());
		r2.setExpirationDate(new Date());
		r2.setPrice(100);
		r2.setManifestation(new Manifestation());
		r2.setReservationDetails(new ArrayList<ReservationDetails>());
		
		List<Reservation> reservations = Arrays.asList(r1, r2);
		Customer c = new Customer();
		c.setEmail("ex@gmail.com");
		c.setReservations(reservations);
		
		r1.setCustomer(c);
		r2.setCustomer(c);
		
		Mockito.when(userRepositoryMocked.findByEmail(c.getEmail()))
			.thenReturn(c);
		
		Mockito.when(reservationRepositoryMocked.findAllByCustomer(c))
			.thenReturn(c.getReservations());
		
		
		List<ViewReservationDTO> reses = reservationService.findAllByUserEmail(c.getEmail());
		
		assertEquals(2, reses.size());
		assertEquals(100, reses.get(0).getPrice());
		assertEquals(100, reses.get(1).getPrice());
		
		
		
	}
	
	@Test
	public void testCancelReservation() {
		Reservation rCreated = new Reservation();
		Reservation rConfirmed = new Reservation();
		
		rCreated.setStatus(ReservationStatus.CREATED);
		rConfirmed.setStatus(ReservationStatus.CONFIRMED);
		
		Optional<Reservation> resOptCreated = Optional.ofNullable(rCreated);
		Optional<Reservation> resOptConfirmed = Optional.ofNullable(rConfirmed);
		Optional<Reservation> resOptNotPresent = Optional.ofNullable(null);
		
		Mockito.when(reservationRepositoryMocked.findById(Mockito.anyLong()))
		.thenReturn(resOptCreated)
		.thenReturn(resOptConfirmed)
		.thenReturn(resOptNotPresent);
		
		CancelReservationStatusDTO dto1 = reservationService
				.cancelReservation(Mockito.anyLong());
		
		assertNotNull(dto1);
		
		expectedException.expect(ApiInternalServerErrorException.class);
		expectedException.expectMessage("Reservation cannot be canceled");
		
		reservationService.cancelReservation(Mockito.anyLong());
		
		expectedException.expect(ApiBadRequestException.class);
		expectedException.expectMessage("No such reservation");
		
		reservationService.cancelReservation(Mockito.anyLong());
		
	}
	
	@Test
	public void testGetCurrentTotalPriceForManifestationDay() {
		
		Long id = 1l;
		
		Reservation r1 = new Reservation();
		r1.setPrice(200);
		Reservation r2 = new Reservation();
		r2.setPrice(400);
		
		List<ReservationDetails> reservationDetails = Arrays.asList(mock(ReservationDetails.class));
		
		
		Mockito.when(reservationDetailsRepositoryMocked.findByManifestationDayId(Mockito.anyLong()))
			.thenReturn(reservationDetails);
		
		Mockito.when(reservationRepositoryMocked.findDistinctByReservationDetailsInAndStatusIn(
				reservationDetails,
				Arrays.asList(ReservationStatus.CONFIRMED)))
			.thenReturn(Arrays.asList(r1, r2))
			.thenReturn(Arrays.asList(r1));
		
		assertEquals(600, reservationService.getCurrentTotalPriceForManifestationDay(id));
		assertEquals(200, reservationService.getCurrentTotalPriceForManifestationDay(id));
	}
	
	@Test
	public void testGetExpectedTotalPriceForManifestationDay() {
		
		Long id = 1l;
		
		Reservation r1 = new Reservation();
		r1.setPrice(200);
		Reservation r2 = new Reservation();
		r2.setPrice(400);
		
		List<ReservationDetails> reservationDetails = Arrays.asList(mock(ReservationDetails.class));
		
		
		Mockito.when(reservationDetailsRepositoryMocked.findByManifestationDayId(Mockito.anyLong()))
			.thenReturn(reservationDetails);
		
		Mockito.when(reservationRepositoryMocked.findDistinctByReservationDetailsInAndStatusIn(
				reservationDetails,
				Arrays.asList(ReservationStatus.CREATED, ReservationStatus.CONFIRMED)))
			.thenReturn(Arrays.asList(r1, r2))
			.thenReturn(Arrays.asList(r1));
		
		
		assertEquals(600, reservationService.getExpectedTotalPriceForManifestationDay(id));
		assertEquals(200, reservationService.getExpectedTotalPriceForManifestationDay(id));
	}
	
	
	
}
