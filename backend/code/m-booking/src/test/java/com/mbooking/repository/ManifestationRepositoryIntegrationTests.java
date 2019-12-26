package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
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
    public void givenValidLocationId_returnManifestsOnLocation() {

        Long locationId = -1L;
        List<Manifestation> manifestsOnLocation = manifestRepo.findByLocationId(locationId);
        assertEquals(2, manifestsOnLocation.size());

        for(Manifestation manifest: manifestsOnLocation) {
            assertEquals(locationId, manifest.getLocation().getId());
        }
    }

    @Test
    public void givenInvalidLocationId_returnEmptyList() {

        Long locationId = -100L;
        List<Manifestation> manifestsOnLocation = manifestRepo.findByLocationId(locationId);
        assertEquals(0, manifestsOnLocation.size());

    }

    @Test
    public void givenExistingSequence_returnMatchingManifestations() {

        String seq = "Test"; // manifestation and location names contain 'Test'
        List<Manifestation> matchingManifests = manifestRepo.findByNameContainingAndLocationNameContaining(seq, seq);
        assertEquals(3, matchingManifests.size());

    }

    @Test
    public void givenNonExistingSequence_returnEmptyList() {
        String seq = "klqwr";
        List<Manifestation> matchingManifests = manifestRepo.findByNameContainingAndLocationNameContaining(seq, seq);
        assertEquals(0, matchingManifests.size());
    }

    @Test
    public void givenExistingNamesAndType_returnMatchingManifests() {
        String manifestName = "Test manif";
        String locationName = "location";
        ManifestationType type = ManifestationType.CULTURE;

        List<Manifestation> matchingManifests =
                manifestRepo.findByNameContainingAndManifestationTypeAndLocationNameContaining(manifestName,
                        type, locationName);
        assertEquals(2, matchingManifests.size());
    }

    @Test
    public void givenNonExistingNamesOrType_returnEmptyList() {
        String invalidName = "klkrprsat";
        ManifestationType type = ManifestationType.ENTERTAINMENT;

        List<Manifestation> matchingManifests =
                manifestRepo.findByNameContainingAndManifestationTypeAndLocationNameContaining(invalidName,
                        type, invalidName);
        assertEquals(0, matchingManifests.size());

    }

}
