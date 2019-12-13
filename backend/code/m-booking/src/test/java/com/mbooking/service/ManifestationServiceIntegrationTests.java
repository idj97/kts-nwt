package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiConflictException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") // -> specify properties file by name
public class ManifestationServiceIntegrationTests {


    @Autowired
    ManifestationService manifestSvc;

    @Autowired
    ManifestationRepository manifestRepo; //used for cleanup

    private ManifestationDTO testDTO;

    @Before
    public void setUpDTO() {

        //setting up a valid dto with some default values
        this.testDTO = new ManifestationDTO();
        this.testDTO.setName("test manifest");
        this.testDTO.setDescription("test description");
        this.testDTO.setType(ManifestationType.CULTURE);

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

        List<ManifestationSectionDTO> testSections = new ArrayList<>();
        testSections.add(new ManifestationSectionDTO(-1L, 50, 100));
        testSections.add(new ManifestationSectionDTO(-2L, 20, 200));

        this.testDTO.setSelectedSections(testSections);

    }

    /**********************************************************
     * Create and update manifestation tests which invoke custom exceptions
     * ********************************************************/

    @Test //expects ApiBadRequestException
    public void givenDatesFromPast_whenCreatingOrUpdatingManifest_thenThrowException() {

        //adding a past date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());


        //test the create service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while creating a manifestation");
        } catch(ApiBadRequestException ex) {
           assertEquals(Constants.FUTURE_DATES_MSG, ex.getMessage());
        }

        //test the update service method by verifying the exception message
        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while updating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.FUTURE_DATES_MSG, ex.getMessage());
        }
    }

    @Test //expects ApiBadRequestException
    public void givenLastReservDayAfterStartDate_whenCreatingOrUpdatingManifest_thenThrowException() {

        //set the last day for reservation after the manifestation days
        testDTO.setReservableUntil(
                new GregorianCalendar(2021, Calendar.DECEMBER, 13).getTime());

        //test the service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while creating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_RESERV_DAY_MSG, ex.getMessage());
        }

        //test the update service method by verifying the exception message
        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while updating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_RESERV_DAY_MSG, ex.getMessage());
        }

    }


    @Test(expected= ApiConflictException.class)
    public void givenSameDaysAndLocation_whenCreatingManifest_thenThrowException() {

        //TODO: edge case -> two same dates in the manifestDTO, reservationsAllowed edge case

        //adding an existing date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2020, Calendar.DECEMBER, 17).getTime());

        //test
        manifestSvc.createManifestation(this.testDTO);

    }


    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidLocationId_whenCreatingManifest_thenThrowException() {

        //TODO: edge case -> dates not set

        this.testDTO.setLocationId(-50L); //set an invalid location id

        manifestSvc.createManifestation(this.testDTO);

    }

    @Test
    @Transactional
    @Rollback
    public void givenValidData_whenCreatingManifest_returnNewManifest() {

        int numOfManifests = manifestRepo.findAll().size();

        Manifestation newManifest = manifestSvc.createManifestation(testDTO);

        assertNotNull(newManifest);
        assertEquals("test manifest", newManifest.getName());
        assertEquals("test description", newManifest.getDescription());

        assertEquals(numOfManifests+1, manifestRepo.findAll().size());

    }

    @Test
    @Transactional
    @Rollback
    public void givenValidData_whenUpdatingManifest_returnUpdatedManifest() {

        int numOfManifests = manifestRepo.findAll().size();

        testDTO.setManifestationId(-1L);
        testDTO.setLocationId(-2L);

        Manifestation updatedManifest = manifestSvc.updateManifestation(testDTO);

        assertEquals(-1L, updatedManifest.getId().longValue());
        assertEquals("test manifest", updatedManifest.getName());
        assertEquals("test description", updatedManifest.getDescription());
        assertEquals(-2L, updatedManifest.getLocation().getId().longValue());

        assertEquals(numOfManifests, manifestRepo.findAll().size());

    }




}
