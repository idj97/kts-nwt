package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test_h2.properties")
public class ManifestationRepositoryIntegrationTests {

    @Autowired
    ManifestationRepository manifestRepo;

    @Test
    public void givenValidId_returnManifestsOnLocation() {

        Long locationId = -1L;

        List<Manifestation> manifestsOnLocation = manifestRepo.findByLocationId(locationId);

        assertEquals(2, manifestsOnLocation.size());

        for(Manifestation manifest: manifestsOnLocation) {
            assertEquals(locationId, manifest.getLocation().getId());
        }
    }



}
