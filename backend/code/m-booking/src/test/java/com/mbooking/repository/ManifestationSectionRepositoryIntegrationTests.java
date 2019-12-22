package com.mbooking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test_h2.properties")
public class ManifestationSectionRepositoryIntegrationTests {

    @Autowired
    ManifestationSectionRepository manifestSectionRepo;

    @Autowired
    SectionRepository sectionRepo; //used to perform check for cascade delete

    @Test
    @Transactional
    @Rollback
    public void givenValidId_whenDeletingSection_removeSectionFromDB() {

        Long manifestSectionId = -1L;
        int numOfSections = sectionRepo.findAll().size();
        int numOfManifestSections = manifestSectionRepo.findAll().size();

        manifestSectionRepo.deleteById(manifestSectionId);
        assertEquals(numOfManifestSections-1, manifestSectionRepo.findAll().size());

        // verify that the section wasn't deleted by cascade
        assertEquals(numOfSections, sectionRepo.findAll().size());

    }

    @Test
    @Transactional
    @Rollback
    public void givenInvalidId_whenDeletingSection_doNothing() {

        Long manifestSectionId = -1000L;
        int numOfManifestSections = manifestSectionRepo.findAll().size();

        manifestSectionRepo.deleteById(manifestSectionId);
        assertEquals(numOfManifestSections, manifestSectionRepo.findAll().size());

    }
}
