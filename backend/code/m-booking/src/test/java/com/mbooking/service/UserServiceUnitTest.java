package com.mbooking.service;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.security.test.context.support.WithMockUser;
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
	public void testRegister_successful() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail("email2");

		Customer customer = new Customer();

		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());

		Mockito.when(userRepo.findByEmail("email2")).thenReturn(null);
		Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);
		Mockito.doNothing().when(emailSenderService).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		UserDTO rez = userService.register(userDTO);
		assertNotNull(rez);

	}

	@Test(expected = ApiException.class)
	public void testRegister_throwException() {
		UserDTO userDTO = new UserDTO();
		String email = "email@mail.com";
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail(email);

		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());
		Mockito.when(userRepo.findByEmail(email)).thenReturn(customer);

		userService.register(userDTO);
	}

	@Test(expected = ApiAuthException.class)
	public void testConfirmRegistration_throwException() {
		Customer cus = new Customer();
		String email = "email@mail.com";
		cus.setEmail(email);
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);

		Mockito.when(customerRepo.findByEmail(email)).thenReturn(null);
		userService.confirmRegistration("email", "eid");
		Mockito.verify(customerRepo, Mockito.times(0)).save(cus);
	}

	@Test
	public void testConfirmRegistration_successful() {
		Customer cus = new Customer();
		String email = "email@mail.com";
		cus.setEmail(email);
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastname");
		cus.setEmailConfirmed(false);
		cus.isEmailConfirmed();

		Mockito.when(customerRepo.findByEmail(email)).thenReturn(cus);

		userService.confirmRegistration(email, "eid");
		Mockito.verify(customerRepo).save(cus);

		assertEquals(cus.getEmail(), email);
		assertEquals(cus.getFirstname(), "name");
		assertEquals(cus.getLastname(), "lastname");
	
	}

	@Test
	public void testCreateAdmin_successful() {

		UserDTO userDTO = new UserDTO();

		String email = "email@mail.com";
		String password = "pabcde";
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail(email);
		userDTO.setId(1L);
		userDTO.setPassword(password);
		userDTO.setAuthorities(new ArrayList<String>(Arrays.asList("ROLE_ADMIN")));

		Admin admin = new Admin();
		admin.setAuthorities(new HashSet<Authority>());
		admin.setId(1L);
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());
		admin.setPassword(password);

		Authority authority = new Authority();
		authority.setName("ROLE_ADMIN");
		Mockito.when(userRepo.findByEmail(email)).thenReturn(null);
		Mockito.when(passwordEncoder.encode(password)).thenReturn("p");
		Mockito.when(authorityRepo.findByName("ROLE_ADMIN")).thenReturn(authority);
		Mockito.when(userRepo.save(Mockito.any(Admin.class))).thenReturn(admin);

		UserDTO rez = userService.createAdmin(userDTO);
		assertNotNull(rez);
		// Mockito.verify(userRepo).save(admin);
	}

	@Test(expected = ApiException.class)
	public void testCreateAdmin_throwException() {
		UserDTO userDTO = new UserDTO();
		String email = "email@mail.com";
		userDTO.setId(1L);
		userDTO.setFirstname("name");
		userDTO.setLastname("lastname");
		userDTO.setEmail(email);

		Admin admin = new Admin();
		admin.setEmail(userDTO.getEmail());
		admin.setFirstname(userDTO.getFirstname());
		admin.setLastname(userDTO.getLastname());

		Mockito.when(userRepo.findByEmail(email)).thenReturn(admin);

		userService.createAdmin(userDTO);
	}

	@Test
	@WithMockUser(username = "email")
	public void testEditProfile_successful() {
		EditProfileDTO eDTO = new EditProfileDTO();

		eDTO.setFirstname("firstame");
		eDTO.setLastname("lastname");
		eDTO.setEmail("email");

		User user = (User) new Customer();

		user.setEmail("email");

		Mockito.when(userRepo.findByEmail("email")).thenReturn(user);
		Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

		UserDTO rezultat = userService.editProfile(eDTO);
		assertNotNull(rezultat);
		// userRepo.save(user);
	}

}
