package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiConflictException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") // -> specify properties file by name
//@TestPropertySource("classpath:application-test.properties") // -> alternative to directly specify properties file
public class ManifestationServiceIntegrationTests {


    @Autowired
    ManifestationService manifestSvc;

    private ManifestationDTO testDTO;

    @Before
    public void setUpDTO() {

        //setting up a valid dto with some default values
        this.testDTO = new ManifestationDTO();

        this.testDTO.setLocationId(-1L);
        this.testDTO.setImages(new ArrayList<>());
        this.testDTO.setReservationsAllowed(true);

        List<Date> testDays = new ArrayList<>();
        testDays.add(new GregorianCalendar(2020, Calendar.DECEMBER, 22).getTime());
        testDays.add(new GregorianCalendar(2020, Calendar.DECEMBER, 20).getTime());
        testDays.add(new GregorianCalendar(2020, Calendar.DECEMBER, 25).getTime());

        this.testDTO.setManifestationDates(testDays);

        this.testDTO.setReservableUntil(
                new GregorianCalendar(2020, Calendar.DECEMBER, 15).getTime());

        //TODO: add sections and test them

    }

    @Test(expected=ApiConflictException.class)
    public void givenSameDaysAndLocation_whenCreatingManifest_thenThrowException() {

        //TODO: edge case -> two same dates in the manifestDTO, reservationsAllowed edge case

        //adding an existing date
        this.testDTO.getManifestationDates().add(
               new GregorianCalendar(2020, Calendar.DECEMBER, 17).getTime());

        //test
        manifestSvc.createManifestation(this.testDTO);

    }

    @Test //expects ApiBadRequestException
    public void givenDatesFromPast_whenCreatingManifest_thenThrowException() {

        //adding a past date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());


        //test the service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown");
        } catch(ApiBadRequestException ex) {
           assertEquals(Constants.FUTURE_DATES_MSG, ex.getMessage());
        }

    }

    @Test //expects ApiBadRequestException
    public void givenLastReservDayAfterStartDate_whenCreatingManifest_thenThrowException() {

        //set the last day for reservation after the manifestation days
        testDTO.setReservableUntil(
                new GregorianCalendar(2021, Calendar.DECEMBER, 13).getTime());

        //test the service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_RESERV_DAY_MSG, ex.getMessage());
        }


    }

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidLocationId_whenCreatingManifest_thenThrowException() {

        //TODO: edge case -> dates not set

        this.testDTO.setLocationId(-50L); //set an invalid location id

        manifestSvc.createManifestation(this.testDTO);

    }


}
