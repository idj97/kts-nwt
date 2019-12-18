package com.mbooking.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Admin;
import com.mbooking.model.Authority;
import com.mbooking.model.Customer;
import com.mbooking.model.User;
import com.mbooking.repository.AuthorityRepository;
import com.mbooking.repository.CustomerRepository;
import com.mbooking.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class UserServiceUnitTest {

	@Autowired
	private UserService userService;
	
	
	@MockBean
	private UserRepository userRepo;

	@MockBean
	private CustomerRepository customerRepo;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AuthorityRepository authorityRepo;

	@MockBean
	private EmailSenderService emailSenderService;
	
	@Test
	public void testRegister() {
		UserDTO userDTO=new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email2");
		
		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());
		Mockito.when(userRepo.findByEmail("email2")).thenReturn(null);

		UserDTO rez=userService.register(userDTO);
		assertNotNull(rez);
		
		
	}
	@Test(expected=ApiException.class)
	public void testRegister1() {
		UserDTO userDTO=new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");
		
		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());
		Mockito.when(userRepo.findByEmail("email")).thenReturn(customer);

		userService.register(userDTO);
	}
	
	@Test(expected=ApiAuthException.class)
	public void testConfirmRegistration1() {
		Customer cus=new Customer();
		cus.setEmail("email");
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);
	
		Mockito.when(customerRepo.findByEmail("email")).thenReturn(null);
		userService.confirmRegistration("email", "eid");
		Mockito.verify(customerRepo,Mockito.times(0)).save(cus);
	}

	@Test
	public void testConfirmRegistration() {
		Customer cus=new Customer();
		cus.setEmail("email");
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);
		cus.isEmailConfirmed();
		
		Mockito.when(customerRepo.findByEmail("email")).thenReturn(cus);
		
		userService.confirmRegistration("email", "eid");
		Mockito.verify(customerRepo).save(cus);
		
	    
	}
	


	@Test
	public void testCreateAdmin() {
		UserDTO userDTO=new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");
		userDTO.setId(1L);
		userDTO.setAuthorities(new ArrayList());
		
		Admin admin = new Admin();
		admin.setAuthorities(new HashSet<Authority>());
		admin.setId(1L);
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());
		admin.setPassword("pasw");
		
		Authority authority=new Authority();
		Mockito.when(userRepo.findByEmail("email")).thenReturn(null);
		Mockito.when(passwordEncoder.encode("pasw")).thenReturn("a");
		Mockito.when(authorityRepo.findByName("name")).thenReturn(authority);
		
		UserDTO rez=userService.createAdmin(userDTO);
		assertNotNull(rez);
		Mockito.verify(userRepo).save(admin);
	}
	
	
	
	@Test(expected=ApiException.class)
	public void testCreateAdmin1() {
		UserDTO userDTO=new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");
		
		
		Admin admin = new Admin();
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());
		
		Mockito.when(userRepo.findByEmail("email")).thenReturn(admin);
		
		userService.createAdmin(userDTO);	}
	

	@Test
	public void testEditProfile() {
		EditProfileDTO eDTO=new EditProfileDTO();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();  //???

		eDTO.setFirstname("firstame");
		eDTO.setLastname("lastname");
		
		User userToEdit = (User) new Customer();
		User user = (User) new Customer();
		
		user.setUsername("username");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		
		Mockito.when(userRepo.findByEmail(email)).thenReturn(user);
		UserDTO rezultat=userService.editProfile(eDTO);
		assertNotNull(rezultat);
	}

}
