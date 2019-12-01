package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.Reservation;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.service.impl.ManifestationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ManifestationServiceUnitTests {

    @Autowired
    private ManifestationServiceImpl manifestSvcImpl;

    @MockBean
    private ReservationRepository reservRepoMocked;

    @MockBean
    private ManifestationRepository manifestRepoMocked;

    @Before
    public void setUpRepositories() {

        //initiate test data returned in mock repository methods
        Manifestation testManifest = new Manifestation();
        testManifest.setId(1L);

        Date testDate1 = new GregorianCalendar(2020, Calendar.DECEMBER, 21).getTime();
        Date testDate2 = new GregorianCalendar(2020, Calendar.DECEMBER, 22).getTime();

        List<ManifestationDay> manifestDays = new ArrayList<>();
        manifestDays.add(new ManifestationDay(1L, testDate1, testManifest));
        manifestDays.add(new ManifestationDay(2L, testDate2, testManifest));

        testManifest.setManifestationDays(manifestDays);

        Reservation testReserv = new Reservation();
        testReserv.setManifestation(testManifest);

        //mock repository methods
        Mockito.when(reservRepoMocked.findByManifestationId(1L)).thenReturn(Collections.singletonList(testReserv));
        Mockito.when(manifestRepoMocked.findByLocationId(1L)).thenReturn(Collections.singletonList(testManifest));
    }

    /****************************************************
     * Testing private methods from service implementation
     * *************************************************/


    @Test
    public void givenManifestWithReservations_whenValidating_returnTrue() {

        assertTrue(ReflectionTestUtils.invokeMethod(manifestSvcImpl, "areThereReservations", 1L));

    }

    @Test
    public void givenExistingDaysOnLocation_whenValidatingCreateManifest_returnTrue() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setLocationId(1L);

        //add an existing date to it
        Date existingDate = new GregorianCalendar(2020, Calendar.DECEMBER, 21).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(existingDate));

        assertTrue(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "checkManifestDateAndLocation", manifestationDTO, false));

    }


    @Test
    public void givenPreviousDaysAndLocation_whenValidatingManifestUpdate_returnFalse() {

        //set up manifestation dto
        ManifestationDTO manifestationDTO = new ManifestationDTO();
        manifestationDTO.setLocationId(1L);
        manifestationDTO.setManifestationId(1L); //id of an existing manifestation

        //add an existing date to it
        Date existingDate = new GregorianCalendar(2020, Calendar.DECEMBER, 21).getTime();
        manifestationDTO.setManifestationDates(Collections.singletonList(existingDate));

        assertFalse(ReflectionTestUtils.
                invokeMethod(manifestSvcImpl, "checkManifestDateAndLocation", manifestationDTO, true));

    }


}
