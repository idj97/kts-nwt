package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiConflictException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.ManifestationType;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test_h2") // -> specify properties file by name
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ManifestationServiceIntegrationTests {


    @Autowired
    ManifestationService manifestSvc;

    @Autowired
    ManifestationRepository manifestRepo; //used for asserting after creating or updating a manifestation

    private ManifestationDTO testDTO;

    //to avoid having to change dates in the future
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    @Before
    public void setUpDTO() {

        //setting up a valid dto with some default values
        this.testDTO = new ManifestationDTO();
        this.testDTO.setManifestationId(-1L);
        this.testDTO.setName("test manifest");
        this.testDTO.setDescription("test description");
        this.testDTO.setType(ManifestationType.CULTURE);

        this.testDTO.setLocationId(-1L);
        this.testDTO.setImages(new ArrayList<>());
        this.testDTO.setReservationsAllowed(true);

        List<Date> testDays = new ArrayList<>();
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 22).getTime());
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 20).getTime());
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 25).getTime());

        this.testDTO.setManifestationDates(testDays);

        this.testDTO.setReservableUntil(
                new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 15).getTime());

        List<ManifestationSectionDTO> testSections = new ArrayList<>();
        testSections.add(new ManifestationSectionDTO(-1L, 50, 100, -1L));
        testSections.add(new ManifestationSectionDTO(-2L, 20, 200, -1L));

        this.testDTO.setSelectedSections(testSections);

    }

    /**********************************************************
     * Create and update manifestation tests which invoke custom exceptions
     * ********************************************************/

    @Test
    public void givenDatesFromPast_whenCreatingOrUpdatingManifest_throwException() {

        //adding a past date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(this.currentYear-1, Calendar.DECEMBER, 15).getTime());

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

    @Test
    public void givenEmptyDays_whenCreatingOrUpdatingManifest_throwException() {

        this.testDTO.getManifestationDates().clear();

        //test the create service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while creating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_NUM_OF_DAYS_MSG, ex.getMessage());
        }

        //test the update service method by verifying the exception message
        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while updating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_NUM_OF_DAYS_MSG, ex.getMessage());
        }

    }


    @Test
    public void givenTooManyDays_whenCreatingOrUpdatingManifest_throwException() {

        this.testDTO.getManifestationDates().clear();
        for(int i = 0; i < Constants.MAX_NUM_OF_DAYS+1; i++) {
            this.testDTO.getManifestationDates().add(
                    new GregorianCalendar(this.currentYear+2, Calendar.DECEMBER, i+1)
                            .getTime()
            );
        }

        //test the create service method by verifying the exception message
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while creating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_NUM_OF_DAYS_MSG, ex.getMessage());
        }

        //test the update service method by verifying the exception message
        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("The ApiBadRequestException was not thrown while updating a manifestation");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_NUM_OF_DAYS_MSG, ex.getMessage());
        }

    }


    @Test
    public void givenLastReservDayAfterStartDate_whenCreatingOrUpdatingManifest_throwException() {

        //set the last day for reservation after the manifestation days
        testDTO.setReservableUntil(
                new GregorianCalendar(this.currentYear+2, Calendar.DECEMBER, 13).getTime());

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

    @Test
    @Transactional
    public void givenInvalidLocationId_whenCreatingOrUpdatingManifest_throwException() {

        this.testDTO.setLocationId(-50L); //set an invalid location id

        // testing manifestation creation
        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.LOCATION_NOT_FOUND_MSG, ex.getMessage());
        }

        // testing manifestation update
        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.LOCATION_NOT_FOUND_MSG, ex.getMessage());
        }

    }

    @Test
    @Transactional
    public void givenInvalidSectionId_whenCreatingOrUpdating_throwException() {

        this.testDTO.getSelectedSections().get(0).setSelectedSectionId(-1000L);

        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.SECTION_NOT_FOUND_MSG, ex.getMessage());
        }

        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.SECTION_NOT_FOUND_MSG, ex.getMessage());
        }
    }

    @Test
    @Transactional
    public void givenEmptySections_whenCreatingOrUpdating_throwException() {

        this.testDTO.getSelectedSections().clear();

        try {
            manifestSvc.createManifestation(this.testDTO);
            fail("Failed to throw ApiBadRequestException");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.NO_SECTIONS_SELECTED_MSG, ex.getMessage());
        }

        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("Failed to throw ApiBadRequestException");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.NO_SECTIONS_SELECTED_MSG, ex.getMessage());
        }

    }



    @Test(expected = ApiConflictException.class)
    @Transactional
    public void givenExistingDaysOnLocation_whenCreatingManifest_throwException() {

        this.testDTO.setLocationId(-2L);
        //adding an existing date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2520, Calendar.JUNE, 17).getTime());

        //test
        manifestSvc.createManifestation(this.testDTO);


    }

    @Test
    public void givenInvalidManifestId_whenUpdatingManifest_throwException() {

        this.testDTO.setManifestationId(-100L);

        try {
            manifestSvc.updateManifestation(this.testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.MANIFEST_NOT_FOUND_MSG, ex.getMessage());
        }


    }

    @Test(expected = ApiConflictException.class)
    @Transactional
    public void givenExistingDaysOnLocation_whenUpdatingManifest_throwException() {

        this.testDTO.setManifestationId(-1L);
        this.testDTO.setLocationId(-2L);

        //adding an existing date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2520, Calendar.JUNE, 17).getTime());

        manifestSvc.updateManifestation(this.testDTO);

    }


    /*****
     * TESTS WITH VALID DATA
     */

    @Test
    @Transactional
    @Rollback
    public void givenValidData_whenCreatingManifest_returnNewManifest() {

        int numOfManifests = manifestRepo.findAll().size();

        ManifestationDTO newManifest = manifestSvc.createManifestation(testDTO);

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

        ManifestationDTO updatedManifest = manifestSvc.updateManifestation(testDTO);

        assertEquals(-1L, updatedManifest.getManifestationId().longValue());
        assertEquals("test manifest", updatedManifest.getName());
        assertEquals("test description", updatedManifest.getDescription());
        assertEquals(-2L, updatedManifest.getLocationId().longValue());

        assertEquals(numOfManifests, manifestRepo.findAll().size());

    }

    @Test
    @Transactional
    @Rollback
    public void givenSameDaysForSameManifest_whenUpdatingManifest_returnUpdatedManifest() {

        int numOfManifests = manifestRepo.findAll().size();

        testDTO.setManifestationId(-1L);
        testDTO.setLocationId(-1L);

        ManifestationDTO updatedManifest = manifestSvc.updateManifestation(testDTO);

        assertEquals(-1L, updatedManifest.getManifestationId().longValue());
        assertEquals("test manifest", updatedManifest.getName());
        assertEquals("test description", updatedManifest.getDescription());
        assertEquals(-1L, updatedManifest.getLocationId().longValue());

        assertEquals(numOfManifests, manifestRepo.findAll().size());

    }

    @Test
    @Transactional
    public void givenManifestationType_whenSearchingManifests_returnMatchingFutureManifests() {

        String manifestationType = "culture";
        ResultsDTO<ManifestationDTO> matchingManifests =
                manifestSvc.searchManifestations("", manifestationType,
                        "", "", 0, 4);

        assertEquals(2, matchingManifests.getPage().size());

        for(ManifestationDTO manifestDTO: matchingManifests.getPage()) {
            assertEquals(ManifestationType.CULTURE, manifestDTO.getType());
        }

    }

    @Test
    @Transactional
    public void givenManifestationNameAndLocation_whenSearchingManifests_returnMatchingFutureManifests() {

        String manifestationName = "Test manifest";
        String locationName = "Test location 2";

        ResultsDTO<ManifestationDTO>  matchingManifests =
                manifestSvc.searchManifestations(manifestationName, "",
                        locationName, "", 0, 4);

        assertEquals(3, matchingManifests.getPage().size());

        for(ManifestationDTO manifestDTO: matchingManifests.getPage()) {
            assertTrue(manifestDTO.getName().contains(manifestationName));
            assertEquals(-2L, manifestDTO.getLocationId().longValue());
        }

    }

    @Test
    @Transactional
    public void givenManifestationDate_whenSearchingManifests_returnMatchingManifests() {

        String searchDate = "2520-06-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ResultsDTO<ManifestationDTO> matchingManifests =
                manifestSvc.searchManifestations("", "",
                        "", searchDate, 0, 4);

        assertEquals(1, matchingManifests.getPage().size());

        // verify that the search date is indeed among the manifestation dates
        boolean dateFound = false;
        for(Date date: matchingManifests.getPage().get(0).getManifestationDates()) {
            if(sdf.format(date).equals(searchDate)) {
                dateFound = true;
            }
        }

        assertTrue(dateFound);

    }

    @Test
    @Transactional
    public void givenDefaultParams_whenSearchingManifests_returnAllFutureManifests() {

        ResultsDTO<ManifestationDTO> matchingManifests =
                manifestSvc.searchManifestations("", "", "",
                        "", 0, 4);

        assertEquals(3, matchingManifests.getPage().size());

    }

    @Test
    public void givenInvalidParam_whenSearchingManifests_returnEmptyList() {

        ResultsDTO<ManifestationDTO> matchingManifests =
                manifestSvc.searchManifestations("qwertyuuio", "",
                        "test location", "", 0, 4);

        assertEquals(0, matchingManifests.getPage().size());

    }

}
