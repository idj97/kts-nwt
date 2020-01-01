package com.mbooking.controller;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.model.ManifestationType;
import com.mbooking.service.ManifestationService;
import com.mbooking.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class ManifestationControllerIntegrationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ManifestationService manifestSvc;

    private ManifestationDTO testDTO;

    //to avoid having to change dates in the future
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);


    @Before
    public void prepValidDTO() {
        //setting up a valid dto with some default values
        this.testDTO = new ManifestationDTO();
        this.testDTO.setManifestationId(-1L);
        this.testDTO.setName("test manifest");
        this.testDTO.setDescription("test description");
        this.testDTO.setType(ManifestationType.CULTURE);

        this.testDTO.setLocationId(-1L);
        this.testDTO.setImages(new ArrayList<>());
        this.testDTO.setReservationsAllowed(true);
        this.testDTO.setMaxReservations(3);

        List<Date> testDays = new ArrayList<>();
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 22).getTime());
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 20).getTime());
        testDays.add(new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 25).getTime());

        this.testDTO.setManifestationDates(testDays);

        this.testDTO.setReservableUntil(
                new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 15).getTime());

        List<ManifestationSectionDTO> testSections = new ArrayList<>();
        testSections.add(new ManifestationSectionDTO(-1L, 50, 100));
        testSections.add(new ManifestationSectionDTO(-2L, 20, 200));

        this.testDTO.setSelectedSections(testSections);
    }

    @Test
    public void testGetAllManifestations() {

        ResponseEntity<ManifestationDTO[]> response =
                testRestTemplate.getForEntity("/api/manifestation",
                        ManifestationDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ManifestationDTO> responseData = Arrays.asList(response.getBody());
        assertEquals(4, responseData.size());
    }


    @Test
    public void givenInvalidDTO_testCreateManifest_expectBadRequest() {

        ManifestationDTO invalidDTO = new ManifestationDTO();
        invalidDTO.setSelectedSections(new ArrayList<>());
        invalidDTO.setManifestationDates(new ArrayList<>());

        // testing dto validation constraints
        ResponseEntity<String> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                .postForEntity("/api/manifestation", invalidDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenPastDates_testCreateManifest_expectBadRequest() {

        // prep invalid data
        List<Date> testDates = new ArrayList<>();
        testDates.add((new GregorianCalendar(currentYear-1, Calendar.JUNE, 10).getTime()));
        testDates.add((new GregorianCalendar(currentYear-1, Calendar.JUNE, 12).getTime()));
        testDTO.setManifestationDates(testDates);

        ResponseEntity<ApiBadRequestException> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                        .postForEntity("/api/manifestation", testDTO, ApiBadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.FUTURE_DATES_MSG, response.getBody().getMessage());

    }

    @Test
    public void givenPastDates_testUpdateManifest_expectBadRequest() {

        // prep invalid data
        List<Date> testDates = new ArrayList<>();
        testDates.add((new GregorianCalendar(currentYear-1, Calendar.JUNE, 10).getTime()));
        testDates.add((new GregorianCalendar(currentYear-1, Calendar.JUNE, 12).getTime()));
        testDTO.setManifestationDates(testDates);

        ResponseEntity<ApiBadRequestException> response =
            testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                    .exchange("/api/manifestation", HttpMethod.PUT,
                            new HttpEntity<>(testDTO), ApiBadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.FUTURE_DATES_MSG, response.getBody().getMessage());
    }

    @Test
    public void givenInvalidReservDay_whenCreatingManifest_expectBadRequest() {

        // set up a last reservation day after start date
        testDTO.setReservableUntil(
                new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 20).getTime());

        ResponseEntity<ApiBadRequestException> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                        .postForEntity("/api/manifestation", testDTO, ApiBadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.INVALID_RESERV_DAY_MSG, response.getBody().getMessage());

    }

    @Test
    public void givenInvalidReservDay_whenUpdatingManifest_expectBadRequest() {

        // set up a last reservation day after start date
        testDTO.setReservableUntil(
                new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 21).getTime());

        ResponseEntity<ApiBadRequestException> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                        .exchange("/api/manifestation", HttpMethod.PUT,
                                new HttpEntity<>(testDTO), ApiBadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.INVALID_RESERV_DAY_MSG, response.getBody().getMessage());

    }

    @Test
    public void givenExistingDays_whenCreatingManifest_expectConflict() {

        this.testDTO.getManifestationDates().clear();

        //adding an existing date
        this.testDTO.getManifestationDates().add(
                new GregorianCalendar(2520, Calendar.DECEMBER, 17).getTime());

        ResponseEntity<ApiBadRequestException> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                        .postForEntity("/api/manifestation", testDTO, ApiBadRequestException.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.CONFLICTING_MANIFEST_DAY_MSG, response.getBody().getMessage());

    }


}
