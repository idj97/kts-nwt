package com.mbooking.repository;

import com.mbooking.model.ManifestationDay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test_h2.properties")
public class ManifestationDayRepositoryIntegrationTests {

    @Autowired
    ManifestationDayRepository manifDayRepo;


    @Test
    @Transactional
    @Rollback
    public void givenValidId_whenDeletingManifestationDay_removeItFromDB() {

        int numOfManifestDays = manifDayRepo.findAll().size();
        manifDayRepo.deleteById(-1L);
        assertEquals(numOfManifestDays-1, manifDayRepo.findAll().size());

    }

    @Test
    @Transactional
    @Rollback
    public void givenInvalidId_whenDeletingManifestationDay_doNothing() {

        int numOfManifestDays = manifDayRepo.findAll().size();
        manifDayRepo.deleteById(-1000L);
        assertEquals(numOfManifestDays, manifDayRepo.findAll().size());

    }

    @Test
    public void givenValidIds_returnManifestationDay() {

        Long manifestDayId = -2L;
        Long manifestId = -1L;
        ManifestationDay manifestDay = manifDayRepo.findByIdAndManifestationId(manifestDayId, manifestId);

        assertNotNull(manifestDay);
        assertEquals(manifestDayId, manifestDay.getId());
        assertEquals(manifestId, manifestDay.getManifestation().getId());
    }

    @Test
    public void givenInvalidIds_returnNull() {

        Long manifestDayId = -2000L;
        Long manifestId = -1000L;
        ManifestationDay manifestDay = manifDayRepo.findByIdAndManifestationId(manifestDayId, manifestId);

        assertNull(manifestDay);
    }

}
