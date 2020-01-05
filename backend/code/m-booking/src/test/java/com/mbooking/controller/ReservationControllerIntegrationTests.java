package com.mbooking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.utils.DatabaseHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationControllerIntegrationTests {
	
	private static String sqlScript = "reservation_test_data.sql";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private DatabaseHelper databaseHelper;
	
	
	@After
	public void cleanDatabase() {
		databaseHelper.dropAndImport(sqlScript);
	} 
	
	@Test
	public void test_getExpectedTotalPriceForManifestationDay() {
		ResponseEntity<Double> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.getForEntity("/api/reservations/day_expected_total_price/-1", Double.class);
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(700, result.getBody());
	}
	
	@Test
	public void test_getExpectedTotalPriceForManifestation() {
		ResponseEntity<Double> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.getForEntity("/api/reservations/expected_total_price/-1", Double.class);
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(700, result.getBody());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_findAllReservationsFromCurrentUser() {
		
		ResponseEntity<List<ViewReservationDTO>> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/view", null,
						(Class<List<ViewReservationDTO>>) new ArrayList<ViewReservationDTO>().getClass());
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(4, result.getBody().size());
	}
	
	@Test
	public void test_cancelReservation_NoSuchReservation() {
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/cancel/99", null, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No such reservation", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_cancelReservation_Success() {
		ResponseEntity<CancelReservationStatusDTO> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/cancel/2", null, CancelReservationStatusDTO.class);
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	@Test
	public void test_makeReservation_NoMoreSpace() {
		ReservationDTO dto = getReservationDTO(-2L, -3L, -4L, 0, 0);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
		result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No more space", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_SeatTaken() {
		ReservationDTO dto = getReservationDTO(-1L, -1L, -1L, 1, 2);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Seat taken", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_NoSuchSeat() {
		ReservationDTO dto = getReservationDTO(-1L, -1L, -1L, 25, 255);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No such seat", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_ReservableUntil() {
		ReservationDTO dto = getReservationDTO(-4L, -7L, -7L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Time for reservations has passed", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_ManifestationNotAvailable() {
		ReservationDTO dto = getReservationDTO(-3L, -5L, -5L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Manifestation is not available", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_SectionNotFromSameManifestation() {
		ReservationDTO dto = getReservationDTO(-1L, -1L, -3L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Section not from the same manifestation", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_DuplicateSeats() {
		ReservationDTO dto = getReservationDTO(-1L, -1L, -1L, 7, 1);
		ReservationDetailsDTO rdto = getReservationDetailsDTO(-1L, -1L, 7, 1);
		dto.getReservationDetails().add(rdto);
		
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Duplicate seats", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_NoSuchSection() {
		ReservationDTO dto = getReservationDTO(-1L, -1L, -626L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No such section", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_EmptyReservationDetails() {
		ReservationDTO dto = new ReservationDTO();
		dto.setManifestationId(-1L);
		
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Nothing reservable", result.getBody().get("message").asText());
		
		result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		dto.setReservationDetails(Arrays.asList());
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Nothing reservable", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_NoSuchManifestationDay() {
		ReservationDTO dto = getReservationDTO(-1L, -88L, -1L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No such manifestation day", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_NoSuchManifestation() {
		ReservationDTO dto = getReservationDTO(-9L, -1L, -1L, 7, 1);
		ResponseEntity<JsonNode> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, JsonNode.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("No such manifestation", result.getBody().get("message").asText());
	}
	
	@Test
	public void test_makeReservation_Success() {
		ReservationDTO dto = getReservationDTO();
		ResponseEntity<MakeReservationResponseDTO> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, MakeReservationResponseDTO.class);
		
		MakeReservationResponseDTO rdto = result.getBody();
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(-1L, rdto.getManifestationId());
	}
	
	/*
	 * Auxiliary methods
	 */
	
	private ReservationDetailsDTO getReservationDetailsDTO(Long manifestationDayId, Long manifestationSectionId, int column, int row) {
		ReservationDetailsDTO resDetDTO = new ReservationDetailsDTO();
		resDetDTO.setColumn(column);
		resDetDTO.setRow(row);
		resDetDTO.setManifestationDayId(manifestationDayId);
		resDetDTO.setManifestationSectionId(manifestationSectionId);
		return resDetDTO;
	}
	
	private ReservationDTO getReservationDTO(Long manifestationId, Long manifestationDayId, Long manifestationSectionId ,int column, int row ) {
		ReservationDTO resDTO = new ReservationDTO();
		resDTO.setManifestationId(manifestationId);
		ReservationDetailsDTO resDetDTO = getReservationDetailsDTO(manifestationDayId, manifestationSectionId, column, row);
		resDTO.setReservationDetails(new ArrayList<ReservationDetailsDTO>(Arrays.asList(resDetDTO)));
		
		return resDTO;
	}
	
	private ReservationDTO getReservationDTO() {
		
		ReservationDTO resDTO = new ReservationDTO();
		resDTO.setManifestationId(-1L);
		ReservationDetailsDTO resDetDTO = new ReservationDetailsDTO();
		resDetDTO.setColumn(7);
		resDetDTO.setRow(1);
		resDetDTO.setManifestationDayId(-1L);
		resDetDTO.setManifestationSectionId(-1L);
		resDTO.setReservationDetails(new ArrayList<ReservationDetailsDTO>(Arrays.asList(resDetDTO)));
		
		return resDTO;
	}
	
}
