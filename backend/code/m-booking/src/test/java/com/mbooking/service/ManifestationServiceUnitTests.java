package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.*;
import com.mbooking.repository.LocationRepository;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.SectionRepository;
import com.mbooking.service.impl.ManifestationServiceImpl;
import com.mbooking.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;


@RunWith(SpringRunner.class)
@ActiveProfiles("test_h2")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ManifestationServiceUnitTests {

    @Autowired
    private ManifestationServiceImpl manifestSvcImpl;

    @MockBean
    private ReservationRepository reservRepoMocked;

    @MockBean
    private ManifestationRepository manifestRepoMocked;

    @MockBean
    private SectionRepository sectionRepoMocked;

    @MockBean
    private LocationRepository locationRepoMocked;

    //to avoid having to change dates in the future
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    @Before
    public void setUpRepositories() {

        //initiate test data returned in mock repository methods
        Manifestation testManifest = new Manifestation();
        testManifest.setName("test manifest");
        testManifest.setId(1L);

        Location testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setName("test location");
        testManifest.setLocation(testLocation);

        testManifest.setManifestationType(ManifestationType.CULTURE);

        Date testDate1 = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 21).getTime();
        Date testDate2 = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 22).getTime();


        List<ManifestationDay> manifestDays = new ArrayList<>();
        manifestDays.add(new ManifestationDay(1L, testDate1, testDate1, testManifest));
        manifestDays.add(new ManifestationDay(2L, testDate2, testDate1, testManifest));

        //ManifestationSection testSection = new ManifestationSection();
        testManifest.setSelectedSections(new HashSet<>());
        testManifest.setManifestationDays(manifestDays);
        testManifest.setImages(new HashSet<>());

        Reservation testReserv = new Reservation();
        testReserv.setManifestation(testManifest);

        //mock repository methods
        Mockito.when(reservRepoMocked.findByManifestationId(1L)).thenReturn(Collections.singletonList(testReserv));

        Mockito.when(sectionRepoMocked.findById(-1L)).thenReturn(Optional.empty());
        Mockito.when(sectionRepoMocked.findById(1L)).thenReturn(Optional.of(new Section()));

        Mockito.when(locationRepoMocked.findById(1L)).thenReturn(Optional.of(new Location()));
        Mockito.when(locationRepoMocked.findById(-1L)).thenReturn(Optional.empty());

        Mockito.when(manifestRepoMocked.findById(-1L)).thenReturn(Optional.empty());
        Mockito.when(manifestRepoMocked.findById(1L)).thenReturn(Optional.of(new Manifestation()));
        Mockito.when(manifestRepoMocked.save(Mockito.any(Manifestation.class))).thenReturn(testManifest);

        Page<Manifestation> searchResult = new PageImpl<>(Collections.singletonList(testManifest));
        Mockito.when(
                manifestRepoMocked.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
                        eq("test manifest"), eq(ManifestationType.CULTURE), eq("test location"), Mockito.any(Date.class),
                        Mockito.any(Pageable.class)))
                .thenReturn(searchResult);

        Mockito.when(
                manifestRepoMocked.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
                        eq("tttt"), eq(ManifestationType.CULTURE), eq("cccc"), Mockito.any(Date.class),
                        Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));


    }

    /****************************************************
     * Testing private methods from service implementation
     * *************************************************/


    @Test
    public void givenManifestWithReservations_whenCheckingForReservs_returnTrue() {

        assertTrue(ReflectionTestUtils.invokeMethod(manifestSvcImpl, "areThereReservations", 1L));

    }

    @Test
    public void givenExistingDaysOnLocation_whenValidatingCreateManifest_returnTrue() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setLocationId(1L);

        //add an existing date to it
        Date existingDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 21).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(existingDate));

        // mocking repository method used when validating dates and location
        Manifestation existingManifest = new Manifestation();
        existingManifest.setId(100L);
        Mockito.when(
                manifestRepoMocked.findDistinctByLocationIdAndManifestationDaysDateNoTimeIn(
                        1L, Collections.singletonList(existingDate)))
                .thenReturn(Collections.singletonList(existingManifest));

        assertTrue(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "locationIsOccupied", manifestationDTO, false));

    }

    @Test
    public void givenUniqueDaysOnLocation_whenValidatingCreateOrUpdateManifest_returnFalse() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setLocationId(1L);

        //add a unique date to it
        Date uniqueDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 25).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(uniqueDate));

        Mockito.when(
                manifestRepoMocked.findDistinctByLocationIdAndManifestationDaysDateNoTimeIn(
                        1L, Collections.singletonList(uniqueDate)))
                .thenReturn(Collections.emptyList());

        //testing manifestation creation case
        assertFalse(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "locationIsOccupied", manifestationDTO, false));

        //testing manifestation update case
        assertFalse(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "locationIsOccupied", manifestationDTO, true));

    }


    @Test //if the user is updating the same manifestation at same location, leaving the same dates
    public void givenPreviousDaysAndLocation_whenValidatingManifestUpdate_returnFalse() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setLocationId(1L);
        manifestationDTO.setManifestationId(1L); //id of an existing manifestation

        //add an existing date to it
        Date existingDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 21).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(existingDate));

        assertFalse(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "locationIsOccupied", manifestationDTO, true));

    }

    @Test
    public void givenExistingDaysOnLocation_whenValidatingUpdateManifest_returnTrue() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setManifestationId(2L);
        manifestationDTO.setLocationId(1L);

        //add an existing date to it
        Date existingDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 21).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(existingDate));

        // preping data to send and return in mock
        Manifestation existingManifest = new Manifestation();
        existingManifest.setId(100L);
        ArrayList<Manifestation> manifestsOnLocation = new ArrayList<>();
        manifestsOnLocation.add(existingManifest);

        // mocking the method used to find manifestations on same location and same date
        Mockito.when(
                manifestRepoMocked.findDistinctByLocationIdAndManifestationDaysDateNoTimeIn(
                        1L, Collections.singletonList(existingDate)))
                .thenReturn(manifestsOnLocation);

        assertTrue(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "locationIsOccupied", manifestationDTO, true));

    }

    @Test
    public void givenInvalidSectionId_whenCreatingSections_throwException() {

        ManifestationDTO manifestationDTO = new ManifestationDTO();
        ManifestationSectionDTO sectionDTO = new ManifestationSectionDTO();
        sectionDTO.setSectionId(-1L);
        manifestationDTO.setSelectedSections(Collections.singletonList(sectionDTO));

        try {
            ReflectionTestUtils.invokeMethod(manifestSvcImpl,"createManifestationSections",
                    manifestationDTO.getSelectedSections(), new Manifestation());
        } catch(ApiNotFoundException ex) {
            assertEquals(Constants.SECTION_NOT_FOUND_MSG, ex.getMessage());
        }

    }

    @Test
    public void givenSectionListEmpty_whenCreatingSections_throwException() {

        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setSelectedSections(new ArrayList<>());

        try {
            ReflectionTestUtils.invokeMethod(manifestSvcImpl,"createManifestationSections",
                    manifestationDTO.getSelectedSections(), new Manifestation());
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.NO_SECTIONS_SELECTED_MSG, ex.getMessage());
        }

    }

    @Test
    public void givenValidSectionId_whenCreatingSections_returnSections() {

        ManifestationSectionDTO sectionDTO = new ManifestationSectionDTO();
        sectionDTO.setSelectedSectionId(1L);

        Set<ManifestationSection> createdSections =
                ReflectionTestUtils.invokeMethod(manifestSvcImpl,"createManifestationSections",
                        Collections.singletonList(sectionDTO), new Manifestation());


        assertNotNull(createdSections);
        assertEquals(1, createdSections.size());

    }


    @Test
    public void givenDatesFromPast_whenValidatingDates_throwException() {

        ManifestationDTO testDTO = new ManifestationDTO();
        Date today = new Date(); //edge case
        testDTO.setManifestationDates(Collections.singletonList(today));

        try {
            ReflectionTestUtils.invokeMethod(manifestSvcImpl,"validateManifestationDates", testDTO);
            fail("Failed to throw ApiBadRequest exception");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.FUTURE_DATES_MSG, ex.getMessage());
        }

    }

    @Test
    public void givenLastReservDayAfterStartDay_whenValidatingDates_throwException() {

        //set up dto
        ManifestationDTO testDTO = new ManifestationDTO();
        testDTO.setReservationsAllowed(true);

        Date startDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 27).getTime();
        testDTO.setManifestationDates(Collections.singletonList(startDate));

        Date invalidLastReservDay = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 27).getTime();
        testDTO.setReservableUntil(invalidLastReservDay);

        //verify exception message
        try {
            ReflectionTestUtils.invokeMethod(manifestSvcImpl,"validateManifestationDates", testDTO);
            fail("Failed to throw ApiBadRequest exception");
        } catch(ApiBadRequestException ex) {
            assertEquals(Constants.INVALID_RESERV_DAY_MSG, ex.getMessage());
        }

    }

    /*****
     * TESTING PUBLIC SERVICE METHODS
     */

    @Test
    public void givenInvalidLocation_whenCreatingOrUpdatingManifest_throwException() {

        ManifestationDTO testDTO = new ManifestationDTO();
        testDTO.setReservationsAllowed(false);
        testDTO.setLocationId(-1L);

        Date startDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 27).getTime();
        testDTO.setManifestationDates(Collections.singletonList(startDate));

        ManifestationSectionDTO testSection = new ManifestationSectionDTO();
        testSection.setSectionId(1L);
        testDTO.setSelectedSections(Collections.singletonList(testSection));

        try {
            manifestSvcImpl.createManifestation(testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch (ApiNotFoundException ex) {
            assertEquals(Constants.LOCATION_NOT_FOUND_MSG, ex.getMessage());
        }

        testDTO.setManifestationId(1L);

        try {
            manifestSvcImpl.updateManifestation(testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch (ApiNotFoundException ex) {
            assertEquals(Constants.LOCATION_NOT_FOUND_MSG, ex.getMessage());
        }

    }

    @Test
    public void givenInvalidManifestationId_whenUpdatingManifest_throwException() {

        ManifestationDTO testDTO = new ManifestationDTO();
        testDTO.setManifestationId(-1L);

        try {
            manifestSvcImpl.updateManifestation(testDTO);
            fail("Failed to throw ApiNotFoundException");
        } catch (ApiNotFoundException ex) {
            assertEquals(Constants.MANIFEST_NOT_FOUND_MSG, ex.getMessage());
        }
    }

    @Test
    public void givenValidData_whenCreatingOrUpdatingManifest_returnManifest() {

        //creating a valid dto
        ManifestationDTO testDTO = new ManifestationDTO();
        testDTO.setName("test manifest");
        testDTO.setReservationsAllowed(false);
        testDTO.setLocationId(1L);

        testDTO.setImages(new ArrayList<>());

        Date startDate = new GregorianCalendar(currentYear+1, Calendar.DECEMBER, 27).getTime();
        testDTO.setManifestationDates(Collections.singletonList(startDate));

        ManifestationSectionDTO testSection = new ManifestationSectionDTO();
        testSection.setSelectedSectionId(1L);
        testDTO.setSelectedSections(Collections.singletonList(testSection));

        ManifestationDTO returnedManifestation;

        //testing create manifestation
        returnedManifestation = manifestSvcImpl.createManifestation(testDTO);
        assertEquals(1L, returnedManifestation.getManifestationId().longValue());
        assertEquals("test manifest", returnedManifestation.getName());

        testDTO.setManifestationId(1L);

        returnedManifestation = manifestSvcImpl.updateManifestation(testDTO);
        assertEquals(1L, returnedManifestation.getManifestationId().longValue());
        assertEquals("test manifest", returnedManifestation.getName());

    }


    @Test
    public void givenValidParams_whenSearchingManifests_returnMatchingManifests() {

        String manifestName = "test manifest";
        String manifestType = "culture";
        String manifestLocation = "test location";

        ResultsDTO<ManifestationDTO> matchingManifests = manifestSvcImpl.searchManifestations(
                manifestName, manifestType, manifestLocation, "", 0, 4);

        assertEquals(1, matchingManifests.getPage().size());
        assertEquals(manifestName, matchingManifests.getPage().get(0).getName());
        assertEquals(ManifestationType.CULTURE,  matchingManifests.getPage().get(0).getType());
        assertEquals(1L,  matchingManifests.getPage().get(0).getLocationId().longValue());

    }

    @Test
    public void givenInvalidParams_whenSearchingManifests_returnEmptyList() {

        String manifestName = "tttt";
        String manifestType = "CULTURE";
        String manifestLocation = "cccc";

        ResultsDTO<ManifestationDTO> matchingManifests = manifestSvcImpl.searchManifestations(
                manifestName, manifestType, manifestLocation, "", 0, 4);

        assertEquals(0, matchingManifests.getPage().size());

    }

}
