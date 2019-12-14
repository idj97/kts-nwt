package com.mbooking.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.utils.SecurityHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;



	@Test
	public void testEditUser_Successfull() {
		HttpHeaders headers = new HttpHeaders();
		EditProfileDTO eDTO = new EditProfileDTO();
		eDTO.setFirstname("ime");
		eDTO.setLastname("prezime");

		UserDTO userDto = new UserDTO();
		userDto.setFirstname(eDTO.getFirstname());
		userDto.setLastname(eDTO.getLastname());
		userDto.setEmail("email");
		
		// HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		HttpHeaders head = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", testRestTemplate);
		
		
		ResponseEntity<UserDTO> responseEntity = testRestTemplate.postForEntity("/api/users", eDTO,UserDTO.class);
		UserDTO responseDTO = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
		assertEquals(userDto.getFirstname(), responseDTO.getFirstname());
		assertEquals(userDto.getLastname(), responseDTO.getLastname());
		
		assertEquals(userDto.getEmail(), responseDTO.getEmail());
	}
}
