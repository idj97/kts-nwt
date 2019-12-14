package com.mbooking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.model.Customer;
import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class UserServiceUnitTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepoMock;

	@Test
	public void testEditProfile() {
		EditProfileDTO eDTO=new EditProfileDTO();
		User user=(User)new  Customer();
		user.setFirstname("ime");
		user.setLastname("prezime");
		user.setUsername("username");
		
		assertNull(userService.editProfile(eDTO));
		/*String email = SecurityContextHolder.getContext().getAuthentication().getName();

		eDTO.setEmail(email);
		eDTO.setFirstname("ime");
		eDTO.setLastname("prezime");
		Mockito.when(userRepoMock.findByEmail(email)).thenReturn(user);
		
		UserDTO uDTO=userService.editProfile(eDTO);
		assertNotNull(uDTO);*/
	}
	
	
	@Test
	public void testEditProfile1() {
		EditProfileDTO eDTO=new EditProfileDTO();
		User user = (User) new Customer();
		user.setFirstname("");
		user.setLastname("");
		
		user.setUsername("username");
		Mockito.when(userRepoMock.findByEmail("mail")).thenReturn(null);
		UserDTO result = userService.editProfile(eDTO);
		assertEquals(" us does not exist.", result);
	}
	
	@Test
	public void editProfileSuccessfull() {
		EditProfileDTO eDTO=new EditProfileDTO();

		User userToEdit = (User) new Customer();
		User user = (User) new Customer();
		user.setUsername("username");
		Mockito.when(userRepoMock.findByUsername(user.getUsername())).thenReturn(userToEdit);
		assertNull(userService.editProfile(eDTO));
	
	}

}
