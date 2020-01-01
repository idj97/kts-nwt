package com.mbooking.controller;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.service.ManifestationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class ManifestationControllerIntegrationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ManifestationService manifestSvc;


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
    public void givenInvalidDTO_testCreateManifest() {

        ManifestationDTO testDTO = new ManifestationDTO();
        testDTO.setSelectedSections(new ArrayList<>());
        testDTO.setManifestationDates(new ArrayList<>());

        // testing dto validation constraints
        ResponseEntity<String> response =
                testRestTemplate.withBasicAuth("testadmin@example.com", "admin")
                .postForEntity("/api/manifestation", testDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
