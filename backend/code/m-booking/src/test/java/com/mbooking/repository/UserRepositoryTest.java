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
		User user = userRepository.findById(1L);
		assertEquals("user1", user.getUsername());
		assertEquals("user1", user.getFirstname());
		assertEquals("user1", user.getLastname());
		assertEquals("ktsnwt.customer@gmail.com", user.getEmail());
		assertTrue(user.isEnabled());
	}
      //nonFound
	@Test
	public void testFindByUsername() {
		User user = userRepository.findByUsername("username");
		assertNull(user);
	}
	@Test
	public void testFindByUsername1() {
		User user = userRepository.findByUsername("user5");
		assertEquals("user5", user.getFirstname());
		assertEquals("user5", user.getLastname());
		assertEquals("ktsnwt.customer@gmail.com", user.getEmail());
		assertTrue(user.isEnabled());
	}

	
     //nonFound
	@Test
	public void testFindByIdLong() {
		User user = userRepository.findById(111L);
		assertNull(user);
	}
	
	@Test
	public void testFindByIdLong1() {
		User user = userRepository.findById(1L);
		assertEquals("user5", user.getUsername());
		assertEquals("user5", user.getFirstname());
		assertEquals("user5", user.getLastname());
		assertEquals("ktsnwt.customer@gmail.com", user.getEmail());
		assertTrue(user.isEnabled());
	}
	
	@Test
	public void testFindObyIdUsername() {
		User user = userRepository.findOneByUsername("usernamee");
		assertNull(user);
	}

}
