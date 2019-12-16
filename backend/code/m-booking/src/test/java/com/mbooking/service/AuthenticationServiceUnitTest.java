package com.mbooking.service;

import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.model.Customer;
import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;
import com.mbooking.security.AuthenticationService;
import com.mbooking.security.impl.JwtUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_unit")
public class AuthenticationServiceUnitTest {

    @Autowired
    private AuthenticationService authService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private JwtUtils jwtUtils;

    @Test(expected = ApiAuthException.class)
    public void when_LoginAndCredentialsInvalid() {
        String email = "email@email.com";
        String password = "12412412";
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Mockito.when(authManager.authenticate(authToken)).thenThrow(new BadCredentialsException("msg not important"));
        authService.login(email, password);
    }

    @Test
    public void when_Login_Success() {
        String email = "email@email.com";
        String password = "12412412";
        String token = "token";
        User user = new Customer();
        user.setEmail(email);
        user.setPassword(password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        Mockito.when(authManager.authenticate(authToken)).thenReturn(authToken);
        Mockito.when(userRepo.findByEmail(email)).thenReturn(user);
        Mockito.when(jwtUtils.generateToken(email)).thenReturn(token);

        UserDTO loggedUser = authService.login(email, password);
        Assert.assertEquals(token, loggedUser.getToken());
    }

    @Test(expected = ApiAuthException.class)
    public void when_ChangePassword_And_PasswordsNotEqual() {
        String email = "email@email.com";
        String oldPassword = "1";
        String enteredPassword = "1";
        String newPassword = "2";
        User user = new Customer();
        user.setEmail(email);
        user.setPassword(oldPassword);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, oldPassword);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        Mockito.when(userRepo.findByEmail(email)).thenReturn(user);
        Mockito.when(passwordEncoder.matches(enteredPassword, user.getPassword())).thenReturn(false);

        authService.changePassword(newPassword, oldPassword);
    }

    @Test
    public void when_ChangePassword_Success() {
        String email = "email@email.com";
        String oldPassword = "1";
        String enteredPassword = "1";
        String newPassword = "2";
        User user = new Customer();
        user.setEmail(email);
        user.setPassword(oldPassword);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, oldPassword);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        Mockito.when(userRepo.findByEmail(email)).thenReturn(user);
        Mockito.when(passwordEncoder.matches(enteredPassword, user.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        authService.changePassword(newPassword, oldPassword);
        Assert.assertEquals(newPassword, user.getPassword());
    }

}
