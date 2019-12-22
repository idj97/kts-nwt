package com.mbooking.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.Customer;
import com.mbooking.model.User;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test_h2")
public class UserRepositoryTest {
	@Autowired
	public UserRepository userRepository;

	@Test
	public void testFindByEmail_NotExists() {
		
		String email="aaaaa@gmail.com";
		User user = userRepository.findByEmail(email);
		assertNull(user);
	}

	@Test
	public void testFindByEmail_successfull() {
		String email = "ktsnwt.customer@gmail.com";
		String name = "Petar";
		String lastname = "Petrovic";
		
		User user = userRepository.findByEmail(email);

		assertEquals(name, user.getFirstname());
		assertEquals(lastname, user.getLastname());
		assertEquals(email, user.getEmail());

		assertNotNull(user);
		assertTrue(user.isEnabled());
	}

	/*
	 * @Test public void testFindByUsername() { User user =
	 * userRepository.findByUsername("username"); assertNull(user); }
	 * 
	 * @Test public void testFindByUsername1() { User user =
	 * userRepository.findByUsername("userU"); assertEquals("userU",
	 * user.getFirstname()); assertEquals("userF", user.getLastname());
	 * assertTrue(user.isEnabled()); }
	 */

}
