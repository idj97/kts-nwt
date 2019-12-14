package com.mbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
class UserServiceIntegrationTest {

	@Autowired
	private UserService uService;

	@Autowired
	private UserRepository usRepo;

	@Test
	@Transactional
	@Rollback
	void testEditProfile() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		EditProfileDTO profileDTO = new EditProfileDTO();
		profileDTO.setFirstname("imee");
		profileDTO.setLastname("prezime");
		User user = usRepo.findByEmail(email);
		user.setFirstname("imee");
      
		usRepo.save(user);
		
		//assertEquals(, user.getFirstname());
		//assertEquals(, user.geLastname());
	}

}
