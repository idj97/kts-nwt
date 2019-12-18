package com.mbooking.service;

import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.model.Customer;
import com.mbooking.repository.CustomerRepository;
import com.mbooking.security.AuthenticationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test(expected = ApiAuthException.class)
    public void when_LoginCredentialsInvalid() {
        String email = "email@email.com";
        String password = "12412412";
        authenticationService.login(email, password);
    }

    @Test(expected = ApiAuthException.class)
    @Transactional
    @Rollback
    public void when_LoginCredentialsValid_And_EmailNotConfirmed() {
        String email = "email@email.com";
        String password = "12345";
        String encodedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer();
        customer.setFirstname("");
        customer.setLastname("");
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setEmailConfirmed(false);
        customer.setBanned(false);
        customerRepository.save(customer);

        try {
            authenticationService.login(email, password);
        } catch (ApiAuthException ex) {
            Assert.assertEquals("Please confirm registration!", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = ApiAuthException.class)
    @Transactional
    @Rollback
    public void when_LoginCredentialsValid_And_UserIsBanned() {
        String email = "email@email.com";
        String password = "12345";
        String encodedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer();
        customer.setFirstname("");
        customer.setLastname("");
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setEmailConfirmed(true);
        customer.setBanned(true);
        customerRepository.save(customer);

        try {
            authenticationService.login(email, password);
        } catch (ApiAuthException ex) {
            Assert.assertEquals("Your account is banned!", ex.getMessage());
            throw ex;
        }
    }

    @Test
    @Transactional
    @Rollback
    public void when_LoginCredentialsValid() {
        String email = "email@email.com";
        String password = "12345";
        String encodedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer();
        customer.setFirstname("");
        customer.setLastname("");
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setEmailConfirmed(true);
        customer.setBanned(false);
        customer = customerRepository.save(customer);

        UserDTO loggedUser = authenticationService.login(email, password);
        Assert.assertEquals(customer.getEmail(), loggedUser.getEmail());
        Assert.assertEquals(customer.getFirstname(), loggedUser.getFirstname());
        Assert.assertEquals(customer.getLastname(), loggedUser.getLastname());
        Assert.assertNotNull(loggedUser.getToken());
    }

    @Test(expected = ApiAuthException.class)
    @Transactional
    @Rollback
    public void when_ChangePassword_And_PasswordsNotEqual() {
        String email = "email@email.com";
        String password = "12345";
        String enteredPassword = "111111";
        String newPassword = "54321";
        String encodedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer();
        customer.setFirstname("");
        customer.setLastname("");
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setEmailConfirmed(true);
        customer.setBanned(false);
        customerRepository.save(customer);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, encodedPassword);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        try {
            authenticationService.changePassword(newPassword, enteredPassword);
        } catch (ApiAuthException ex) {
            Assert.assertEquals("Please enter current password to verify ownership of account.", ex.getMessage());
            throw ex;
        }
    }

    @Test
    @Transactional
    @Rollback
    public void when_ChangePassword_Success() {
        String email = "email@email.com";
        String password = "12345";
        String enteredPassword = password;
        String newPassword = "54321";
        String encodedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer();
        customer.setFirstname("");
        customer.setLastname("");
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setEmailConfirmed(true);
        customer.setBanned(false);
        customerRepository.save(customer);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, encodedPassword);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        authenticationService.changePassword(newPassword, enteredPassword);
        customer = customerRepository.findByEmail(email);
        Assert.assertTrue(passwordEncoder.matches(newPassword, customer.getPassword()));
    }
}
