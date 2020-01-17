package com.mbooking.repository;

import com.mbooking.model.Layout;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test_h2")
public class LayoutRepositoryIntegrationTest {

    @Autowired
    private LayoutRepository layoutRepository;


    @Test
    public void givenEmptyName_whenFindByName_expectAllResults() {
        String name = "";
        int allLayoutSize = layoutRepository.findAll().size();
        List<Layout> layouts = layoutRepository.findByNameContaining(name);
        Assert.assertEquals(allLayoutSize, layouts.size());
    }

    @Test
    public void givenInvalidName_whenFindByName_expectEmptyResults() {
        String name = "asd";
        List<Layout> layouts = layoutRepository.findByNameContaining(name);
        Assert.assertEquals(0, layouts.size());
    }

    @Test
    public void givenValidName_whenFindByName_expectOneResult() {
        String name = "STADIUM";
        List<Layout> layouts = layoutRepository.findByNameContaining(name);
        Assert.assertEquals(1, layouts.size());
        Assert.assertEquals(name, layouts.get(0).getName());
    }

}
