package com.mbooking.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.User;
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
	@Autowired
	public UserRepository userRepository;

	@Test
	public void testFindByEmail() {
		User user = userRepository.findByEmail("email");
		assertNull(user);
	}
	
	
	
	@Test
	public void testFindByEmail_successfull() {
		User user = userRepository.findByEmail("email");
		
		assertEquals("userU", user.getUsername());
		assertEquals("userF", user.getFirstname());
		assertEquals("userL", user.getLastname());
		assertEquals("ktsnwt.customer@gmail.com", user.getEmail());
		assertTrue(user.isEnabled());
	}
	//ne treba?
	@Test
	public void testFindByUsername() {
		User user = userRepository.findByUsername("username");
		assertNull(user);
	}
	@Test
	public void testFindByUsername1() {
		User user = userRepository.findByUsername("user5");
		assertEquals("userU", user.getFirstname());
		assertEquals("userF", user.getLastname());
		assertEquals("ktsnwt.customer@gmail.com", user.getEmail());
		assertTrue(user.isEnabled());
	}
    

}
