package com.mbooking.controller;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationType;
import com.mbooking.repository.LocationRepository;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.utils.DatabaseHelper;
import com.mbooking.utils.DateHelper;
import com.mbooking.utils.SecurityHelper;
import com.mbooking.utils.TransactionalService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class LocationControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private ManifestationRepository manifestationRepo;

    @Autowired
    private TransactionalService transactionalService;

    @Test
    public void when_getById_AndLocationNotExist_NotFound() {
        Long id = 5L;
        ResponseEntity<ApiNotFoundException> response = restTemplate.getForEntity("/api/locations/" + id, ApiNotFoundException.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void when_getById_AndLocationExist_Ok() {
        Long id = -1L;
        ResponseEntity<LocationDTO> response = restTemplate.getForEntity("/api/locations/" + id, LocationDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocationDTO retLocationDTO = response.getBody();
        assertEquals(id, retLocationDTO.getId());
    }

    @Test
    public void when_getByNameOrAddress_NotFound() {
        String name = "Anfield";
        String address = "Liverpool";
        int pageNum = 0;
        int pageSize = 10;

        ResponseEntity<LocationDTO[]> response = restTemplate.getForEntity(
                "/api/locations?name={name}&address={address}&pageNum={pageNum}&pageSize={pageSize}",
                LocationDTO[].class,
                name, address, pageNum, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LocationDTO> returnedDTOs = Arrays.asList(response.getBody());
        assertEquals(0, returnedDTOs.size());
    }

    @Test
    public void when_getByNameOrAddress_Found() {
        String partOfName = "Som";
        String partOfAddress = "ever";
        int pageNum = 0;
        int pageSize = 10;

        ResponseEntity<LocationDTO[]> response = restTemplate.getForEntity(
                "/api/locations?name={partOfName}&address={partOfAddress}&pageNum={pageNum}&pageSize={pageSize}",
                LocationDTO[].class,
                partOfName, partOfAddress, pageNum, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LocationDTO> returnedDTOs = Arrays.asList(response.getBody());
        assertEquals(1, returnedDTOs.size());
        assertTrue(returnedDTOs.get(0).getName().contains(partOfName));
        assertTrue(returnedDTOs.get(0).getAddress().contains(partOfAddress));
    }

    @Test
    public void when_createLocation_NotAuthenticated() {
        ResponseEntity<String> response = restTemplate.exchange("/api/locations", HttpMethod.POST, new HttpEntity<>(null, null), String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    public void when_createLocation_NotAuthorized() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("ktsnwt.customer@gmail.com", "user", restTemplate);
        LocationDTO dto = new LocationDTO(null, "1", "1", 1L, null);
        ResponseEntity<String> response = restTemplate.exchange("/api/locations", HttpMethod.POST, new HttpEntity<>(dto, headers), String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void when_createLocation_UnpopulatedDTO() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        LocationDTO dto = new LocationDTO();
        ResponseEntity<String> response = restTemplate.exchange("/api/locations", HttpMethod.POST, new HttpEntity<>(dto, headers), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void when_createLocation_LayoutNotExists() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        LocationDTO dto = new LocationDTO("1", "1", 105L);
        ResponseEntity<ApiNotFoundException> response = restTemplate.exchange("/api/locations", HttpMethod.POST, new HttpEntity<>(dto, headers), ApiNotFoundException.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Layout not found.", response.getBody().getMessage());
    }

    @Test
    public void when_createLocation_Created() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        LocationDTO requestDTO = new LocationDTO("1", "1", -1L);
        int sizeBeforeCreate = locationRepo.findAll().size();

        ResponseEntity<LocationDTO> response = restTemplate.exchange("/api/locations", HttpMethod.POST, new HttpEntity<>(requestDTO, headers), LocationDTO.class);
        LocationDTO responseDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sizeBeforeCreate + 1, locationRepo.findAll().size());
        assertEquals(requestDTO.getName(), responseDTO.getName());
        assertEquals(requestDTO.getAddress(), responseDTO.getAddress());
        assertEquals(requestDTO.getLayoutId(), responseDTO.getLayoutId());
        assertEquals(0, responseDTO.getManifestationIds().size());
        assertNotNull(responseDTO.getId());

        databaseHelper.dropAndImport();
    }

    @Test
    public void when_updateLocation_locationNotFound() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        Long locationId = 150L;
        Long layoutId = -1L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        ResponseEntity<ApiNotFoundException> response = restTemplate.exchange("/api/locations/{id}", HttpMethod.PUT, new HttpEntity<>(requestDTO, headers), ApiNotFoundException.class, locationId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Location/layout not found.", response.getBody().getMessage());
    }

    @Test
    public void when_updateLocation_layoutNotFound() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        Long locationId = -1L;
        Long layoutId = 150L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        ResponseEntity<ApiNotFoundException> response = restTemplate.exchange("/api/locations/{id}", HttpMethod.PUT, new HttpEntity<>(requestDTO, headers), ApiNotFoundException.class, locationId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Location/layout not found.", response.getBody().getMessage());
    }

    @Test
    public void when_updateLocation_AndUpdateNotPossible() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        Long locationId = -1L;
        Long layoutId = -2L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        transactionalService.runInNewTransaction(() -> add(locationId, "20/01/2030 13:30"));
        ResponseEntity<ApiBadRequestException> response = restTemplate.exchange("/api/locations/{id}", HttpMethod.PUT, new HttpEntity<>(requestDTO, headers), ApiBadRequestException.class, locationId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This location have unfinished manifestations.", response.getBody().getMessage());
        databaseHelper.dropAndImport();
    }

    @Test
    public void when_updateLocation_AndSuccess() {
        HttpHeaders headers = SecurityHelper.loginAndCreateHeaders("testadmin@example.com", "admin", restTemplate);
        Long locationId = -1L;
        Long layoutId = -2L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        transactionalService.runInNewTransaction(() -> add(locationId, "20/01/2000 13:30"));

        ResponseEntity<LocationDTO> response = restTemplate.exchange("/api/locations/{id}", HttpMethod.PUT, new HttpEntity<>(requestDTO, headers), LocationDTO.class, locationId);
        LocationDTO responseDTO = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationId, responseDTO.getId());
        assertEquals(layoutId, responseDTO.getLayoutId());
        assertEquals("1", responseDTO.getName());
        assertEquals("1", responseDTO.getAddress());
        databaseHelper.dropAndImport();
    }

    public void add(Long locationId, String date) {
        Location loc = locationRepo.findById(locationId).get();
        Manifestation manf = new Manifestation();
        manf.setName("testManf");
        manf.setDescription("testDesc");
        manf.setManifestationType(ManifestationType.CULTURE);
        ManifestationDay[] dates = {new ManifestationDay(manf, DateHelper.getDate(date))};
        manf.setManifestationDays(Arrays.asList(dates));
        manf.setLocation(loc);
        loc.getManifestations().add(manf);
        locationRepo.save(loc);
    }
 }
