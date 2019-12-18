package com.mbooking.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityRepository authorityRepo;

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	public void testRegister() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email2");

		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());

		UserDTO rez = userService.register(userDTO);
		
		//??
		String subject = "Email confirmation for MBooking";
		String text = "Confirm registration by clicking on this link:\n";
		String link = "http://localhost:8080/api/users/confirm_registration/" + customer.getEmail() + "/" + customer.getEmailConfirmationId();  
		
		emailSenderService.sendSimpleMessage(customer.getEmail(),subject, text + link);
		
		assertEquals(customer.getEmail(), rez.getEmail());
		assertEquals(customer.getFirstname(), rez.getFirstname());
		assertEquals(customer.getLastname(), rez.getLastname());

	}

	@Test(expected = ApiException.class)
	public void testRegister1() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");

		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());

		userService.register(userDTO);
	}

	@Test
	public void testConfirmRegistration() {
		Customer cus = new Customer();
		cus.setEmail("email");
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);
		cus.isEmailConfirmed();

		userService.confirmRegistration("email", "eid");
		customerRepo.save(cus);

	}

	@Test(expected = ApiAuthException.class)
	public void testConfirmRegistration1() {
		Customer cus = new Customer();
		cus.setEmail("email");
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);

		userService.confirmRegistration("email", "eid");

	}

	@Test
	public void testCreateAdmin() {
		UserDTO userDTO = new UserDTO();

		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");
		userDTO.setPassword("pasw");
		userDTO.setAuthorities(new ArrayList());

		Admin admin = new Admin();

		admin.setAuthorities(new HashSet<Authority>());
		admin.setId(1L);
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());
		admin.setPassword("pasw");

		Authority authority = new Authority();

		int sizeBefore = userRepo.findAll().size();
		UserDTO rez = userService.createAdmin(userDTO);

		assertEquals(sizeBefore + 1, userRepo.findAll().size());
		assertEquals(userDTO.getId(), rez.getId());
		assertEquals(userDTO.getFirstname(), rez.getFirstname());
		assertEquals(userDTO.getLastname(), rez.getLastname());
		assertEquals(userDTO.getEmail(), rez.getEmail());
		assertEquals(userDTO.getAuthorities(), rez.getAuthorities());  //rola??
		assertEquals(userDTO.getPassword(), rez.getPassword());
		userRepo.save(admin);
	}

	@Test(expected = ApiException.class)
	public void testCreateAdmin1() {
		UserDTO userDTO = new UserDTO();

		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email");

		Admin admin = new Admin();
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());

		userService.createAdmin(userDTO);
	}

	@Test
	public void testEditProfile() {
		EditProfileDTO eDTO = new EditProfileDTO();
		String email = SecurityContextHolder.getContext().getAuthentication().getName(); // ???

		eDTO.setFirstname("firstame");
		eDTO.setLastname("lastname");

		User user = (User) new Customer();

		user.setUsername("username");
		user.setFirstname("firstname");
		user.setLastname("lastname");

		UserDTO rezultat = userService.editProfile(eDTO);
		assertEquals(eDTO.getFirstname(), rezultat.getFirstname());
		assertEquals(eDTO.getLastname(), rezultat.getLastname());

	}

}
