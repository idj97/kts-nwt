package com.mbooking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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

	
	@Test(expected = DataIntegrityViolationException.class)
	public void testRegister() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setFirstname("user5");
		userDTO.setLastname("user5");
		userDTO.setEmail("email2@gmail.com");
		userDTO.setPassword("user");
		
		Customer customer = new Customer();
		customer.setEmail(userDTO.getEmail());
		customer.setFirstname(userDTO.getFirstname());
		customer.setLastname(userDTO.getLastname());
		customer.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		UserDTO rez = userService.register(userDTO);
		
		
		//emailSenderService.sendSimpleMessage(customer.getEmail(),"sub", "tex");
		
		
		assertEquals(customer.getEmail(), rez.getEmail());
		assertEquals(customer.getFirstname(), rez.getFirstname());
		assertEquals(customer.getLastname(), rez.getLastname());
		
		customerRepo.save(customer);
		authorityRepo.findByName("ROLE_CUSTOMER");
	}

	@Test(expected = ApiException.class)
	public void testRegister__throwException() {
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
	public void testCreateAdmin_successful() {
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


		int sizeBefore = userRepo.findAll().size();
		UserDTO rez = userService.createAdmin(userDTO);

		//assertTrue(passwordEncoder.matches(userDTO.getPassword(),rez.getPassword()));
		
		assertEquals(sizeBefore + 1, userRepo.findAll().size());
		assertEquals(userDTO.getId(), rez.getId());
		assertEquals(userDTO.getFirstname(), rez.getFirstname());
		assertEquals(userDTO.getLastname(), rez.getLastname());
		assertEquals(userDTO.getEmail(), rez.getEmail());
		//assertEquals(userDTO.getAuthorities(), rez.getAuthorities());  //rola??
		//assertEquals(userDTO.getPassword(), rez.getPassword());
		userRepo.save(admin);
		assertNotNull(rez.getId());
	}

	@Test(expected = ApiException.class)
	public void testCreateAdmin_throwException() {
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

	
	
	
	@Test(expected = ApiAuthException.class)
	public void testConfirmRegistration_throwException() {
		
		Customer cus = new Customer();
		
		String email="email@mail.rs";
		cus.setEmail(email);
		cus.setEmailConfirmationId("eid");
		cus.setFirstname("name");
		cus.setLastname("lastnaaame");
		cus.setEmailConfirmed(true);
		
		Customer customer = customerRepo.findByEmail(email);
		userService.confirmRegistration(email, "eid");
		
	}

	
	@Test(expected = DataIntegrityViolationException.class)
	public void testConfirmRegistration() {
		Customer cus = new Customer();
		String emailconfId = "eid";
        String name="Peta";
        String ln="Petrovi";
        
		cus.setEmail("ktsnwt.custome@gmail.com");
		cus.setEmailConfirmationId(emailconfId);
		cus.setFirstname(name);
		cus.setLastname(ln);
		cus.setEmailConfirmed(false);
		cus.isEmailConfirmed();

		userService.confirmRegistration("ktsnwt.custome@gmail.com", "eid");
		customerRepo.save(cus);
		 //assertEquals(expected, actual);
	}
	
	@Test
	@WithMockUser(username = "ktsnwt.customer@gmail.com")
	public void testEditProfile_successful() {
		EditProfileDTO eDTO = new EditProfileDTO();
		//String email = SecurityContextHolder.getContext().getAuthentication().getName(); // ???

		eDTO.setFirstname("firstame");
		eDTO.setLastname("lastname");

		User user = new Customer();

		//user.setUsername("username");
		user.setFirstname("firstname");
		user.setLastname("lastname");

		UserDTO rezultat = userService.editProfile(eDTO);
		
		assertNotNull(rezultat);
		
		assertEquals(eDTO.getFirstname(), rezultat.getFirstname());
		assertEquals(eDTO.getLastname(), rezultat.getLastname());

	}

}
