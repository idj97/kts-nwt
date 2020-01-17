package com.mbooking.controller;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.repository.LayoutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class LayoutControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private LayoutRepository layoutRepository;

    @Test
    public void givenInvalidId_whenGetById_expectNotFound() {
        Long id = -22L;
        ResponseEntity<ApiNotFoundException> response = testRestTemplate
                .getForEntity("/api/layouts/" + id, ApiNotFoundException.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidId_whenGetById_expectReturnedDTO() {
        Long id = -1L;
        ResponseEntity<LayoutDTO> response = testRestTemplate
                .getForEntity("/api/layouts/" + id, LayoutDTO.class);
        LayoutDTO layoutDTO = response.getBody();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(layoutDTO);
        Assert.assertEquals(id, layoutDTO.getId());
        Assert.assertEquals("STADIUM", layoutDTO.getName());
        Assert.assertEquals(5, layoutDTO.getSections().size());
    }

    @Test
    public void givenEmptyName_whenFindByName_expectAllResults() {
        String name = "";
        int allLayoutSize = layoutRepository.findAll().size();
        ResponseEntity<LayoutDTO[]> response = testRestTemplate
                .getForEntity("/api/layouts?name=" + name, LayoutDTO[].class);
        Assert.assertEquals(allLayoutSize, response.getBody().length);
    }

    @Test
    public void givenInvalidName_whenFindByName_expectEmptyResults() {
        String name = "asd";
        ResponseEntity<LayoutDTO[]> response = testRestTemplate
                .getForEntity("/api/layouts?name=" + name, LayoutDTO[].class);
        Assert.assertEquals(0, response.getBody().length);
    }

    @Test
    public void givenValidName_whenFindByName_expectOneResult() {
        String name = "STADIUM";
        ResponseEntity<LayoutDTO[]> response = testRestTemplate
                .getForEntity("/api/layouts?name=" + name, LayoutDTO[].class);
        Assert.assertEquals(1, response.getBody().length);
        Assert.assertEquals(name, response.getBody()[0].getName());
    }


}
