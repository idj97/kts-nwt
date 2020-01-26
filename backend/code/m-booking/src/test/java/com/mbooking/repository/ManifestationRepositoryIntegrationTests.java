package com.mbooking.repository;

import com.github.rkumsher.date.DateUtils;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test_h2.properties")
public class ManifestationRepositoryIntegrationTests {

    @Autowired
    ManifestationRepository manifestRepo;


    /** Used to verify that the data specified during search matches the data
     * in the matching manifestations which were returned */
    private void compareResults(List<Manifestation> searchedManifests, int expectedSize,
                                String searchedName, String searchedLocationName,
                                ManifestationType searchedType, Date searchDate) {

        assertEquals(searchedManifests.size(), expectedSize);

        for(Manifestation manifest: searchedManifests) {
            assertTrue(manifest.getName().contains(searchedName));
            assertTrue(manifest.getLocation().getName().contains(searchedLocationName));

            if(searchedType != null) {
                assertEquals(manifest.getManifestationType(), searchedType);
            }

            if(searchDate != null) {
                assertTrue(compareDaysWithSearchDate(manifest.getManifestationDays(), searchDate));
            }
        }

    }

    /** If the user searched by date, make sure that there is a manifestation day
     * containing that date */
    private boolean compareDaysWithSearchDate(List<ManifestationDay> manifestationDays,
                                              Date searchDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(ManifestationDay day: manifestationDays) {
            if(sdf.format(day.getDate()).equals(sdf.format(searchDate))) {
                return true;
            }
        }

        return false;
    }


    /** Testing search with manifestation name and location name **/

    @Test
    public void givenExistingNames_whenSearching_returnMatchingFutureManifestations() {

        String nameSequence = "Test"; // manifestation and location names contain 'Test'
        Pageable pageable = PageRequest.of(0, 4);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateAfter(
                        nameSequence, nameSequence, new Date(), pageable);


        compareResults(matchingManifests, 3, nameSequence,
                nameSequence, null, null);

    }


    @Test
    public void givenNonExistingNames_whenSearching_returnEmptyList() {
        String nameSequence = "klqwr";
        Pageable pageable = PageRequest.of(0, 4);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateAfter(
                        nameSequence, nameSequence, new Date(), pageable);

        assertEquals(0, matchingManifests.size());
    }

    /** Testing search with manifestation name, manifestation type and location name **/

    @Test
    public void givenExistingNamesAndType_whenSearching_returnMatchingFutureManifests() {
        String manifestName = "Test manif";
        String locationName = "location";
        ManifestationType type = ManifestationType.CULTURE;
        Pageable pageable = PageRequest.of(0, 4);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
                        manifestName, type, locationName, new Date(), pageable);

        compareResults(matchingManifests, 2, manifestName,
                locationName, type, null);
    }

    @Test
    public void givenNonExistingNamesOrType_whenSearching_returnEmptyList() {
        String invalidName = "klkrprsat";
        ManifestationType type = ManifestationType.CULTURE;
        Pageable pageable = PageRequest.of(0, 4);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
                        invalidName, type, invalidName, new Date(), pageable);

        assertEquals(0, matchingManifests.size());

    }


    /** Testing search with manifestation name, manifestation date and location name **/

    @Test
    public void givenExistingNamesAndDate_whenSearching_returnMatchingManifests() {

        String name = "Test";
        Date searchDate = new GregorianCalendar(2520, Calendar.JUNE, 15).getTime();
        Pageable pageable = PageRequest.of(0, 4);

        Date searchDateStart = DateUtils.atStartOfDay(searchDate);
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateBetween(
                        name, name, searchDateStart, searchDateEnd, pageable);

        compareResults(matchingManifests, 1, name, name, null, searchDate);

    }

    @Test
    public void givenInvalidNamesOrDate_whenSearching_returnEmptyList() {
        String name = "Test";
        Date searchDate = new GregorianCalendar(2520, Calendar.JUNE, 18).getTime();
        Pageable pageable = PageRequest.of(0, 4);

        Date searchDateStart = DateUtils.atStartOfDay(searchDate);
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateBetween(
                        name, name, searchDateStart, searchDateEnd, pageable);

        assertEquals(0, matchingManifests.size());
    }


    /** Testing search with all parameters - manifestaion name, manifestation type,
     * location name and manifestation date */


    @Test
    public void givenValidNamesAndTypeAndDate_whenSearching_returnMatchingManifestations() {

        String name = "Test";
        String locationName = "loc";
        ManifestationType type = ManifestationType.CULTURE;
        Date searchDate = new GregorianCalendar(2520, Calendar.DECEMBER, 15).getTime();
        Pageable pageable = PageRequest.of(0, 4);

        Date searchDateStart = DateUtils.atStartOfDay(searchDate);
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateBetween(
                        name, type, locationName, searchDateStart, searchDateEnd, pageable);

        compareResults(matchingManifests, 1, name, locationName, type, searchDate);

    }

    @Test
    public void givenInvalidNamesOrTypeOrDate_whenSearching_returnEmptyList() {

        String name = "";
        ManifestationType type = ManifestationType.ENTERTAINMENT;
        Date searchDate = new GregorianCalendar(2520, Calendar.DECEMBER, 15).getTime();
        Pageable pageable = PageRequest.of(0, 4);

        Date searchDateStart = DateUtils.atStartOfDay(searchDate);
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate);

        List<Manifestation> matchingManifests =
                manifestRepo.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateBetween(
                        name, type, name, searchDateStart, searchDateEnd, pageable);

        assertEquals(0, matchingManifests.size());

    }
}
