package com.mbooking.controller;

import com.mbooking.dto.ChangePasswordRequestDTO;
import com.mbooking.dto.LoginRequestDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.model.Customer;
import com.mbooking.repository.AuthorityRepository;
import com.mbooking.repository.CustomerRepository;
import com.mbooking.utils.DatabaseHelper;
import com.mbooking.utils.SecurityHelper;
import com.mbooking.utils.TransactionalService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void givenInvalidCredetials_whenLogin_expectUnauthorized() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("email@email.com", "12412412");
        ResponseEntity<ApiAuthException> response = testRestTemplate.postForEntity("/api/auth/login", loginRequestDTO, ApiAuthException.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Invalid credentials.", response.getBody().getMessage());
    }

    @Test
    public void givenValidCredentialsButUnconfirmedEmail_whenLogin_expectUnauthorized() {
        String email = "email@email.com";
        String password = "12345";
        String encodedPassword = passwordEncoder.encode(password);

        transactionalService.runInNewTransaction(() -> {
            Customer customer = new Customer();
            customer.setFirstname("");
            customer.setLastname("");
            customer.setEmail(email);
            customer.setPassword(encodedPassword);
            customer.setEmailConfirmed(false);
            customer.setBanned(false);
            customerRepository.save(customer);
        });

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        ResponseEntity<ApiAuthException> response = testRestTemplate.postForEntity("/api/auth/login", loginRequestDTO, ApiAuthException.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Please confirm registration!", response.getBody().getMessage());
        databaseHelper.dropAndImport();
    }


    @Test
    public void givenValidCredentialsButUserIsBanned_whenLogin_expectUnauthorized() {
        String email = "email@email.com";
        String password = "12345";
        String encodedPassword = passwordEncoder.encode(password);

        transactionalService.runInNewTransaction(() -> {
            Customer customer = new Customer();
            customer.setFirstname("");
            customer.setLastname("");
            customer.setEmail(email);
            customer.setPassword(encodedPassword);
            customer.setEmailConfirmed(true);
            customer.setBanned(true);
            customerRepository.save(customer);
        });

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        ResponseEntity<ApiAuthException> response = testRestTemplate.postForEntity("/api/auth/login", loginRequestDTO, ApiAuthException.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Your account is banned!", response.getBody().getMessage());
        databaseHelper.dropAndImport();
    }


    @Test
    public void givenValidCredentials_whenLogin_expectOk() {
        String email = "email@email.com";
        String password = "12345";
        String firstname = "Asd";
        String lastname = "adsadsa";
        String encodedPassword = passwordEncoder.encode(password);

        transactionalService.runInNewTransaction(() -> {
            Customer customer = new Customer();
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
            customer.setEmail(email);
            customer.setPassword(encodedPassword);
            customer.setEmailConfirmed(true);
            customer.setBanned(false);
            customerRepository.save(customer);
        });

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        ResponseEntity<UserDTO> response = testRestTemplate.postForEntity("/api/auth/login", loginRequestDTO, UserDTO.class);
        UserDTO userDTO = response.getBody();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(email, userDTO.getEmail());
        Assert.assertEquals(firstname, userDTO.getFirstname());
        Assert.assertEquals(lastname, userDTO.getLastname());
        Assert.assertNotNull(userDTO.getToken());
        databaseHelper.dropAndImport();
    }

    @Test
    public void givenCurrentAndEnteredPasswordNotMatch_whenChangePassword_expectUnauthorized() {
        String email = "email@email.com";
        String password = "12345";
        String enteredPassword = "111111";
        String newPassword = "54321";
        String encodedPassword = passwordEncoder.encode(password);

        transactionalService.runInNewTransaction(() -> {
            Customer customer = new Customer();
            customer.setFirstname("");
            customer.setLastname("");
            customer.setEmail(email);
            customer.setPassword(encodedPassword);
            customer.setEmailConfirmed(true);
            customer.setBanned(false);
            customer.getCollectionOfAuthorities().add(authorityRepository.findByName("ROLE_CUSTOMER"));
            customerRepository.save(customer);
        });

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO(enteredPassword, newPassword);
        ResponseEntity<ApiAuthException> response = testRestTemplate
                .withBasicAuth(email, password)
                .exchange("/api/auth/change_password", HttpMethod.POST, new HttpEntity<>(changePasswordRequestDTO), ApiAuthException.class);

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Please enter current password to verify ownership of account.", response.getBody().getMessage());
        databaseHelper.dropAndImport();
    }

    @Test
    public void givenValidPasswords_whenChangePassword_expectOk() {
        String email = "email@email.com";
        String password = "12345";
        String enteredPassword = password;
        String newPassword = "54321";
        String encodedPassword = passwordEncoder.encode(password);

        transactionalService.runInNewTransaction(() -> {
            Customer customer = new Customer();
            customer.setFirstname("");
            customer.setLastname("");
            customer.setEmail(email);
            customer.setPassword(encodedPassword);
            customer.setEmailConfirmed(true);
            customer.setBanned(false);
            customer.getCollectionOfAuthorities().add(authorityRepository.findByName("ROLE_CUSTOMER"));
            customerRepository.save(customer);
        });

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO(enteredPassword, newPassword);
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(email, password)
                .exchange("/api/auth/change_password", HttpMethod.POST, new HttpEntity<>(changePasswordRequestDTO), String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer customer = customerRepository.findByEmail(email);
        Assert.assertTrue(passwordEncoder.matches(newPassword, customer.getPassword()));
        databaseHelper.dropAndImport();
    }
}
