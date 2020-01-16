package com.mbooking.service;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.model.Section;
import com.mbooking.repository.LayoutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_unit")
public class LayoutServiceUnitTest {

    @Autowired
    private LayoutService layoutService;

    @MockBean
    private LayoutRepository layoutRepository;

    @Test(expected = ApiNotFoundException.class)
    public void givenInvalidId_whenGetById_expectNotFoundException() {
        Long id = -1L;
        Mockito.when(layoutRepository.findById(id)).thenReturn(Optional.empty());
        layoutService.getById(id);
    }

    @Test
    public void givenValidId_whenGetById_expectReturnedDTO() {
        Long id = 1L;
        String name = "layout_name";
        Set<Section> sections = new HashSet<>();

        Mockito.when(layoutRepository.findById(id)).thenReturn(Optional.of(new Layout(id, name, sections)));
        LayoutDTO layoutDTO = layoutService.getById(id);

        Assert.assertNotNull(layoutDTO);
        Assert.assertEquals(id, layoutDTO.getId());
        Assert.assertEquals(name, layoutDTO.getName());
        Assert.assertEquals(sections.size(), layoutDTO.getSections().size());
    }

    @Test
    public void givenEmptyName_whenGetByName_expectAllResults() {
        String name = "";
        ArrayList<Layout> layouts = new ArrayList<>();
        layouts.add(new Layout(null, null, new HashSet<Section>()));
        layouts.add(new Layout(null, null, new HashSet<Section>()));
        layouts.add(new Layout(null, null, new HashSet<Section>()));

        Mockito.when(layoutRepository.findByNameContaining(name)).thenReturn(layouts);
        List<LayoutDTO> layoutDTOS = layoutService.getByName(name);
        Assert.assertEquals(layouts.size(), layoutDTOS.size());
    }

}
