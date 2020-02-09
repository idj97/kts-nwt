package com.mbooking.service;

import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Customer;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class PaymentServiceUnitTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private UserRepository userRepository;

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidReservationId_whenMakePaymentRequest_expectNotFound() {
        Long id = 1L;

        Mockito.when(reservationRepository.findById(id)).thenReturn(Optional.empty());
        paymentService.makePaymentRequest(id);
    }

    @Test(expected = ApiAuthException.class)
    public void givenRequesterIsNotOwner_whenMakePaymentRequest_expectNotFound() {
        Long id = 1L;

        Customer requester = new Customer();
        requester.setEmail("2");
        Mockito.when(userRepository.findByEmail(requester.getEmail())).thenReturn(requester);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("2", null));

        Customer owner = new Customer();
        owner.setReservations(new ArrayList<>());
        owner.setEmail("1");
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setCustomer(owner);
        owner.getReservations().add(reservation);

        Mockito.when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        paymentService.makePaymentRequest(id);
    }

    @Test(expected = ApiException.class)
    public void givenReservationIsNotInCreatedStatus_whenMakePaymentRequest_expectException() {
        Long id = 1L;

        Customer owner = new Customer();
        owner.setReservations(new ArrayList<>());
        owner.setEmail("1");

        Mockito.when(userRepository.findByEmail(owner.getEmail())).thenReturn(owner);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("1", null));

        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setCustomer(owner);
        reservation.setStatus(ReservationStatus.EXPIRED);
        owner.getReservations().add(reservation);

        Mockito.when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        paymentService.makePaymentRequest(id);
    }

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidReservationId_whenExecutePayment_throwNotFound() {
        Long id = 1L;
        Mockito.when(reservationRepository.findById(id)).thenReturn(Optional.empty());
        paymentService.executePayment("", id);
    }
}
