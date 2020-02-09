package com.mbooking.service;

import com.mbooking.dto.PayPalRequestDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Customer;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;
import com.mbooking.repository.CustomerRepository;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.security.AuthenticationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidReservationId_whenMakePaymentRequest_expectNotFound() {
        Long id = -55L;
        paymentService.makePaymentRequest(id);
    }

    @Test(expected = ApiAuthException.class)
    public void givenRequesterIsNotOwner_whenMakePaymentRequest_expectAuthException() {
        Long id = 1L;
        Customer customer = customerRepository.findByEmail("ktsnwt.customer3@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customer, null));
        paymentService.makePaymentRequest(id);
    }

    @Test(expected = ApiException.class)
    @Transactional
    @Rollback
    public void givenReservationIsNotInCreatedStatus_whenMakePaymentRequest_expectException() {
        Long id = 1L;
        Reservation reservation = reservationRepository.findById(id).get();
        reservation.setStatus(ReservationStatus.EXPIRED);
        reservationRepository.save(reservation);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(reservation.getCustomer(), null));
        paymentService.makePaymentRequest(id);
    }

    @Test
    @Transactional
    @Rollback
    public void givenValidRequest_whenMakePaymentRequest_expectOk() {
        Long id = 1L;
        Reservation reservation = reservationRepository.findById(id).get();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(reservation.getCustomer(), null));
        PayPalRequestDTO payPalRequestDTO = paymentService.makePaymentRequest(id);
        Assert.assertNotNull(payPalRequestDTO);
        Assert.assertNotNull(payPalRequestDTO.getId());
        Assert.assertNotNull(payPalRequestDTO.getStatus());
        Assert.assertNotNull(payPalRequestDTO.getApproveUrl());
    }

    @Test(expected = ApiNotFoundException.class)
    @Transactional
    @Rollback
    public void givenInvalidReservationId_whenExecutePayment_expectNotFound() {
        Long id = -1L;
        paymentService.executePayment("", id);
    }

    @Test(expected = ApiException.class)
    @Transactional
    @Rollback
    public void givenPaymentRequestNotApproved_whenExecutePayment_expectException() {
        Long id = 1L;
        Reservation reservation = reservationRepository.findById(id).get();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(reservation.getCustomer(), null));
        PayPalRequestDTO payPalRequestDTO = paymentService.makePaymentRequest(id);
        paymentService.executePayment(payPalRequestDTO.getId(), id);
    }
}
