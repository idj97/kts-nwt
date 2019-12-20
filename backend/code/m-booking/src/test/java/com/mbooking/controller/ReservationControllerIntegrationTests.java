package com.mbooking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationControllerIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void test_makeReservation_Success() {
		ReservationDTO dto = getReservationDTO();
		ResponseEntity<MakeReservationResponseDTO> result = restTemplate.withBasicAuth("ktsnwt.customer@gmail.com", "user")
				.postForEntity("/api/reservations/reserve", dto, MakeReservationResponseDTO.class);
		
		MakeReservationResponseDTO rdto = result.getBody();
		
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(-1L, rdto.getManifestationId());
	}
	
	private ReservationDTO getReservationDTO() {
		
		ReservationDTO resDTO = new ReservationDTO();
		resDTO.setManifestationId(-1L);
		ReservationDetailsDTO resDetDTO = new ReservationDetailsDTO();
		resDetDTO.setColumn(7);
		resDetDTO.setRow(1);
		resDetDTO.setManifestationDayId(-1L);
		resDetDTO.setManifestationSectionId(-1L);
		
		resDTO.setReservationDetails(Arrays.asList(resDetDTO));
		
		return resDTO;
	}
	
}
