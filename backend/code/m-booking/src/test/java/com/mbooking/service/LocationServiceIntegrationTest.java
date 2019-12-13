package com.mbooking.service;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationType;
import com.mbooking.repository.LocationRepository;
import com.mbooking.utils.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class LocationServiceIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepo;

    @Test(expected = ApiNotFoundException.class)
    public void when_getById_AndLocationNotExist_NotFound() {
        Long id = 5L;
        locationService.getById(id);
    }

    @Test
    public void when_getById_AndLocation_Found() {
        Long id = -1L;
        LocationDTO dto = locationService.getById(id);
        assertEquals(id, dto.getId());
    }

    @Test
    public void when_getByNameOrAddress_NotFound() {
        String name = "Anfield";
        String address = "Liverpool";
        int pageNum = 0;
        int pageSize = 10;

        List<LocationDTO> locations = locationService.getByNameOrAddress(name, address, pageNum, pageSize);
        assertEquals(0, locations.size());
    }

    @Test
    public void when_getByNameOrAddress_Found() {
        String partOfName = "Som";
        String partOfAddress = "ever";
        int pageNum = 0;
        int pageSize = 10;

        List<LocationDTO> returnedDTOs = locationService.getByNameOrAddress(partOfName, partOfAddress, pageNum, pageSize);
        assertEquals(1, returnedDTOs.size());
        assertTrue(returnedDTOs.get(0).getName().contains(partOfName));
        assertTrue(returnedDTOs.get(0).getAddress().contains(partOfAddress));
    }

    @Test(expected = ApiNotFoundException.class)
    public void when_createLocation_LayoutNotExists() {
        LocationDTO dto = new LocationDTO("1", "1", 105L);
        locationService.createLocation(dto);
    }

    @Test
    @Transactional
    @Rollback
    public void when_createLocation_Created() {
        LocationDTO requestDTO = new LocationDTO("1", "1", -1L);
        int sizeBeforeCreate = locationRepo.findAll().size();

        LocationDTO createdDTO = locationService.createLocation(requestDTO);

        assertEquals(sizeBeforeCreate + 1, locationRepo.findAll().size());
        assertEquals(requestDTO.getName(), createdDTO.getName());
        assertEquals(requestDTO.getAddress(), createdDTO.getAddress());
        assertEquals(requestDTO.getLayoutId(), createdDTO.getLayoutId());
        assertEquals(0, createdDTO.getManifestationIds().size());
        assertNotNull(createdDTO.getId());
    }

    @Test(expected = ApiNotFoundException.class)
    public void when_updateLocation_locationNotFound() {
        Long locationId = 150L;
        Long layoutId = -1L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        locationService.updateLocation(locationId, requestDTO);
    }

    @Test(expected = ApiNotFoundException.class)
    public void when_updateLocation_layoutNotFound() {
        Long locationId = -1L;
        Long layoutId = 150L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
        locationService.updateLocation(locationId, requestDTO);
    }

    @Test(expected = ApiBadRequestException.class)
    @Transactional
    @Rollback
    public void when_updateLocation_AndUpdateNotPossible() {
        Long locationId = -1L;
        Long layoutId = -2L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);

        Location loc = locationRepo.findById(locationId).get();
        Manifestation manf = new Manifestation();
        manf.setName("testManf");
        manf.setDescription("testDesc");
        manf.setManifestationType(ManifestationType.CULTURE);
        ManifestationDay[] dates = {new ManifestationDay(manf, DateHelper.getDate("20/01/2030 13:30"))};
        manf.setManifestationDays(Arrays.asList(dates));
        manf.setLocation(loc);
        loc.getManifestations().add(manf);
        locationRepo.saveAndFlush(loc);
        locationService.updateLocation(locationId, requestDTO);
    }

    @Test
    @Transactional
    @Rollback
    public void when_updateLocation_AndSuccess() {
        Long locationId = -1L;
        Long layoutId = -2L;
        LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);

        Location loc = locationRepo.findById(locationId).get();
        Manifestation manf = new Manifestation();
        manf.setName("testManf");
        manf.setDescription("testDesc");
        manf.setManifestationType(ManifestationType.CULTURE);
        ManifestationDay[] dates = {new ManifestationDay(manf, DateHelper.getDate("20/01/2000 13:30"))};
        manf.setManifestationDays(Arrays.asList(dates));
        manf.setLocation(loc);
        loc.getManifestations().add(manf);
        locationRepo.saveAndFlush(loc);

        LocationDTO locationDTO = locationService.updateLocation(locationId, requestDTO);
        assertEquals(locationId, locationDTO.getId());
        assertEquals(layoutId, locationDTO.getLayoutId());
        assertEquals("1", locationDTO.getName());
        assertEquals("1", locationDTO.getAddress());
    }
}
