package com.mbooking.service;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.repository.LayoutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class LayoutServiceIntegrationTest {

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private LayoutRepository layoutRepository;

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidId_whenGetById_expectNotFoundException() {
        Long id = -22L;
        layoutService.getById(id);
    }

    @Test
    public void givenValidId_whenGetById_expectReturnedDTO() {
        Long id = -1L;
        LayoutDTO layoutDTO = layoutService.getById(id);
        Assert.assertNotNull(layoutDTO);
        Assert.assertEquals(id, layoutDTO.getId());
        Assert.assertEquals("STADIUM", layoutDTO.getName());
        Assert.assertEquals(5, layoutDTO.getSections().size());
    }

    @Test
    public void givenEmptyName_whenFindByName_expectAllResults() {
        String name = "";
        int allLayoutSize = layoutRepository.findAll().size();
        List<LayoutDTO> layoutsDTO = layoutService.getByName(name);
        Assert.assertEquals(allLayoutSize, layoutsDTO.size());
    }

    @Test
    public void givenInvalidName_whenFindByName_expectEmptyResults() {
        String name = "asd";
        List<LayoutDTO> layoutsDTO = layoutService.getByName(name);
        Assert.assertEquals(0, layoutsDTO.size());
    }

    @Test
    public void givenValidName_whenFindByName_expectOneResult() {
        String name = "STADIUM";
        List<LayoutDTO> layoutsDTO = layoutService.getByName(name);
        Assert.assertEquals(1, layoutsDTO.size());
        Assert.assertEquals(name, layoutsDTO.get(0).getName());
    }

}
