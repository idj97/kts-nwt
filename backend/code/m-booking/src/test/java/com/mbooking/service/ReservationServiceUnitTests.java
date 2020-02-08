package com.mbooking.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ReservationDetailsRequestDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiInternalServerErrorException;
import com.mbooking.exception.ReservationCannotBeCanceledException;
import com.mbooking.exception.ReservationNotFromCurrentCustomerException;
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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationServiceUnitTests {
	
	@Autowired
	private ReservationService reservationService;
	
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
	
	@MockBean
	private EmailSenderService emailSenderService;
	
	@MockBean
	private PDFCreatorService pdfCreator;
	
	private static final String email = "ktsnwt.customer@gmail.com";
	
	@Before
	public void setUp() {
		Mockito.when(pdfCreator.createReservationPDF(Mockito.any(Reservation.class)))
			.thenReturn(new ByteArrayOutputStream());
		Mockito.doNothing().when(emailSenderService).sendMessageWithAttachment(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(ByteArrayResource.class));
	}
	
	@Test
	public void test_MakeReservation() {
		
		Customer customer = new Customer();
		customer.setId(-1l);
		//customer.setUsername("username");
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
	public void test_FindAllByUserEmail() {
		Reservation r1 = new Reservation();
		r1.setDateCreated(new Date());
		r1.setExpirationDate(new Date());
		r1.setPrice(100);
		r1.setStatus(ReservationStatus.CREATED);
		r1.setManifestation(new Manifestation());
		r1.setReservationDetails(new ArrayList<ReservationDetails>());
		
		Reservation r2 = new Reservation();
		r2.setDateCreated(new Date());
		r2.setExpirationDate(new Date());
		r2.setPrice(100);
		r2.setStatus(ReservationStatus.CREATED);
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
	public void test_findAllReservations() {
		
		Manifestation manifestation = new Manifestation();
		ReservationDetails reservationDetails = new ReservationDetails();
		ManifestationSection manifestationSection = new ManifestationSection();
		reservationDetails.setManifestationSection(manifestationSection);
		Reservation r1 = new Reservation();
		Reservation r2 = new Reservation();
		Reservation r3 = new Reservation();
		r1.setManifestation(manifestation);
		r2.setManifestation(manifestation);
		r3.setManifestation(manifestation);
		r1.setReservationDetails(Arrays.asList(reservationDetails));
		r2.setReservationDetails(Arrays.asList(reservationDetails));
		r3.setReservationDetails(Arrays.asList(reservationDetails));
		
		Mockito.when(reservationRepositoryMocked.findAll())
			.thenReturn(Arrays.asList(r1, r2, r3));
		
		List<ViewReservationDTO> reservationsDTO = reservationService.findAllReservations();
		
		assertEquals(3, reservationsDTO.size());
		
	}
	
	@Test(expected = ReservationCannotBeCanceledException.class)
	@WithMockUser(username = email)
	public void test_CanceReservation_CannotBeCanceled() {
		Reservation rConfirmed = new Reservation();
		Customer customer = new Customer();
		customer.setEmail("ktsnwt.customer@gmail.com");
		rConfirmed.setCustomer(customer);
		rConfirmed.setStatus(ReservationStatus.CONFIRMED);
		
		Optional<Reservation> resOptConfirmed = Optional.ofNullable(rConfirmed);
		
		Mockito.when(reservationRepositoryMocked.findById(Mockito.anyLong()))
		.thenReturn(resOptConfirmed);
		
		reservationService.cancelReservation(Mockito.anyLong());
	}
	
	@Test(expected = ReservationNotFromCurrentCustomerException.class)
	@WithMockUser(username = "mock.customer@gmail.com")
	public void test_CanceReservation_NoSuchReservation() {
		Customer customer = new Customer();
		customer.setEmail(email);
		Reservation rCreated = new Reservation();
		rCreated.setCustomer(customer);
		rCreated.setStatus(ReservationStatus.CREATED);
		
		Optional<Reservation> resOptCreated = Optional.ofNullable(rCreated);
		
		Mockito.when(reservationRepositoryMocked.findById(Mockito.anyLong()))
		.thenReturn(resOptCreated);
		
		reservationService.cancelReservation(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username = "ktsnwt.customer@gmail.com", password = "user")
	public void test_CancelReservation() {
		Customer customer = new Customer();
		customer.setEmail("ktsnwt.customer@gmail.com");
		Reservation rCreated = new Reservation();
		rCreated.setCustomer(customer);
		rCreated.setStatus(ReservationStatus.CREATED);
		
		Optional<Reservation> resOptCreated = Optional.ofNullable(rCreated);
		
		Mockito.when(reservationRepositoryMocked.findById(Mockito.anyLong()))
		.thenReturn(resOptCreated);
		
		CancelReservationStatusDTO dto1 = reservationService
				.cancelReservation(Mockito.anyLong());
		
		assertNotNull(dto1);
		
	}
	
	@Test
	public void test_GetCurrentTotalPriceForManifestationDay() {
		
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
	public void test_GetExpectedTotalPriceForManifestationDay() {
		
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
	
	@Test
	@WithMockUser(username = email)
	public void test_getTotalCustomerReservationDetailsByManifestationAndManifestationDay() {
		ReservationDetailsRequestDTO rdrdto = new ReservationDetailsRequestDTO();
		rdrdto.setManifestationDayId(1L);
		rdrdto.setManifestationId(1L);
		
		ManifestationDay d1 = new ManifestationDay();
		d1.setId(1L);
		Manifestation m1 = new Manifestation();
		ManifestationSection ms1 = new ManifestationSection();
		
		Optional<ManifestationDay> od1 = Optional.ofNullable(d1);
		Optional<Manifestation> om1 = Optional.ofNullable(m1);
		
		Reservation r1 = new Reservation();
		Customer customer = new Customer();
		customer.setEmail(email);
		
		ReservationDetails rd1 = new ReservationDetails();
		ReservationDetails rd2 = new ReservationDetails();
		rd1.setManifestationDay(d1);
		rd2.setManifestationDay(d1);
		rd1.setManifestationSection(ms1);
		rd2.setManifestationSection(ms1);
		
		r1.setReservationDetails(Arrays.asList(rd1, rd2));
		
		Mockito.when(manifestationDayRepositoryMocked.findById(rdrdto.getManifestationDayId()))
			.thenReturn(od1);
		
		Mockito.when(manifestationRepositoryMocked.findById(rdrdto.getManifestationId()))
			.thenReturn(om1);
		
		Mockito.when(userRepositoryMocked.findByEmail(email)).thenReturn(customer);
		
		Mockito.when(reservationDetailsRepositoryMocked.findByReservationCustomerAndManifestationDayAndReservationManifestationAndReservationStatusNotIn(
				customer, d1, m1, Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED)))
				.thenReturn(Arrays.asList(rd1, rd2));
		
		List<ReservationDetailsDTO> result = reservationService.getTotalCustomerReservationDetailsByManifestationAndManifestationDay(rdrdto);
		
		assertEquals(2, result.size());
		result.forEach(n -> assertEquals(1L, n.getManifestationDayId()));
	}
	
	@Test
	@WithMockUser(username = email)
	public void test_getAllReservationsDetailsByManifestationAndManifestationDay() {
		ReservationDetailsRequestDTO rdrdto = new ReservationDetailsRequestDTO();
		rdrdto.setManifestationDayId(1L);
		rdrdto.setManifestationId(1L);
		
		ManifestationDay d1 = new ManifestationDay();
		d1.setId(1L);
		Manifestation m1 = new Manifestation();
		ManifestationSection ms1 = new ManifestationSection();
		
		Optional<ManifestationDay> od1 = Optional.ofNullable(d1);
		Optional<Manifestation> om1 = Optional.ofNullable(m1);
		
		Reservation r1 = new Reservation();
		Customer customer = new Customer();
		customer.setEmail(email);
		
		ReservationDetails rd1 = new ReservationDetails();
		ReservationDetails rd2 = new ReservationDetails();
		rd1.setManifestationDay(d1);
		rd2.setManifestationDay(d1);
		rd1.setManifestationSection(ms1);
		rd2.setManifestationSection(ms1);
		
		r1.setReservationDetails(Arrays.asList(rd1, rd2));
		
		Mockito.when(manifestationDayRepositoryMocked.findById(rdrdto.getManifestationDayId()))
			.thenReturn(od1);
		
		Mockito.when(manifestationRepositoryMocked.findById(rdrdto.getManifestationId()))
			.thenReturn(om1);
		
		Mockito.when(userRepositoryMocked.findByEmail(email)).thenReturn(customer);
		
		Mockito.when(reservationDetailsRepositoryMocked.findByManifestationDayAndReservationManifestationAndReservationStatusNotIn(
				d1, m1, Arrays.asList(ReservationStatus.CANCELED, ReservationStatus.EXPIRED)))
				.thenReturn(Arrays.asList(rd1, rd2));
		
		List<ReservationDetailsDTO> result = reservationService.getAllReservationsDetailsByManifestationAndManifestationDay(rdrdto);
		
		assertEquals(2, result.size());
		result.forEach(n -> assertEquals(1L, n.getManifestationDayId()));
	}
	
}
















