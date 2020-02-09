package com.mbooking.service;

import java.util.*;

import com.mbooking.dto.ResultsDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.model.*;
import com.mbooking.utils.DateHelper;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.repository.LocationRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_unit")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LocationServiceUnitTest {

	@MockBean
	private LayoutRepository layoutRepository;

	@MockBean
	private LocationRepository locationRepository;

	@Autowired
	private LocationService locationService;

	@Test(expected = ApiNotFoundException.class)
	public void givenInvalidLocationId_whenGetById_expectNotFoundException() {
		Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());
		locationService.getById(1L);
	}

	@Test
	public void givenValidLocationId_whenGetById_expectIdsEqual() {
		Long id = 1L;
		Location loc = new Location();
		loc.setId(id);
		Layout layout=new Layout();
		layout.setId(5L);
		loc.setLayout(layout);
		Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(loc));
		LocationDTO locDTO = locationService.getById(1L);
		assertNotNull(locDTO);
		assertEquals(id, locDTO.getId());
	}

	@Test
	public void givenInvalidNameAndAddress_whenGetByNameOrAddress_expectResultsEmpty() {
		String name = "Anfield";
		String address = "Liverpool";
		int pageNum = 0;
		int pageSize = 10;

		Mockito.when(locationRepository.findByNameContainingAndAddressContaining(name, address, PageRequest.of(pageNum, pageSize)))
				.thenReturn(new PageImpl<>(new ArrayList<>()));

		List<LocationDTO> locations = locationService.getByNameOrAddress(name, address, pageNum, pageSize).getPage();
		assertEquals(0, locations.size());
	}

	@Test
	public void givenValidNameAndAddress_whenGetByNameOrAddress_expectResultsNotEmpty() {
		String partOfName = "Som";
		String partOfAddress = "ever";
		int pageNum = 0;
		int pageSize = 10;

		Layout layout=new Layout();
		layout.setId(5L);

		Location loc = new Location();
		loc.setLayout(layout);

		ArrayList<Location> locations = new ArrayList<>();
		locations.add(loc);

		PageImpl<Location> page = new PageImpl<Location>(locations);

		Mockito.when(locationRepository.findByNameContainingAndAddressContaining(partOfName, partOfAddress, PageRequest.of(pageNum, pageSize)))
				.thenReturn(page);

		ResultsDTO<LocationDTO> returnedDTOs = locationService.getByNameOrAddress(partOfName, partOfAddress, pageNum, pageSize);
		assertEquals(1, returnedDTOs.getPage().size());
	}

	@Test(expected = ApiNotFoundException.class)
	public void givenInvalidLayoutIdInDTO_whenCreateLocation_expectNotFoundException() {
		LocationDTO dto = new LocationDTO("1", "1", 105L);

		Mockito.when(layoutRepository.findById(105L)).thenReturn(Optional.empty());
		locationService.createLocation(dto);
	}

	@Test
	public void givenValidDTO_whenCreateLocation_expectLocationCreated() {
		LocationDTO requestDTO = new LocationDTO("1", "1", -1L);
		Layout layout = new Layout();
		layout.setId(-1L);

		Mockito.when(layoutRepository.findById(-1L)).thenReturn(Optional.of(layout));
		Location location = new Location(requestDTO.getName(), requestDTO.getAddress(), layout);
		location.setId(1L);
		Mockito.when(locationRepository.save(Mockito.any())).thenReturn(location);

		LocationDTO createdDTO = locationService.createLocation(requestDTO);
		assertEquals(requestDTO.getName(), createdDTO.getName());
		assertEquals(requestDTO.getAddress(), createdDTO.getAddress());
		assertEquals(requestDTO.getLayoutId(), createdDTO.getLayoutId());
		assertEquals(0, createdDTO.getManifestationIds().size());
		assertNotNull(createdDTO.getId());
	}

	@Test(expected = ApiNotFoundException.class)
	public void givenInvalidLocationIdInDTO_whenUpdateLocation_expectNotFoundException() {
		Long locationId = 150L;
		Long layoutId = -1L;
		LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);

		Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.empty());
		locationService.updateLocation(locationId, requestDTO);
	}

	@Test(expected = ApiNotFoundException.class)
	public void givenInvalidLayoutIdInDTO_whenUpdateLocation_expectNotFoundException() {
		Long locationId = -1L;
		Long layoutId = 150L;
		LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);
		Mockito.when(layoutRepository.findById(layoutId)).thenReturn(Optional.empty());
		locationService.updateLocation(locationId, requestDTO);
	}

	@Test(expected = ApiBadRequestException.class)
	public void givenCurrentlyUnupdateableLocation_whenUpdateLocation_expectBadRequestException() {
		Long locationId = -1L;
		Long layoutId = -2L;
		LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);

		Layout layout = new Layout();
		layout.setId(layoutId);

		Location loc = new Location();
		Manifestation manf = new Manifestation();
		manf.setName("testManf");
		manf.setDescription("testDesc");
		manf.setManifestationType(ManifestationType.CULTURE);
		ManifestationDay[] dates = {new ManifestationDay(manf, DateHelper.getDate("20/01/2030 13:30"))};
		manf.setManifestationDays(Arrays.asList(dates));
		manf.setLocation(loc);
		loc.getManifestations().add(manf);

		Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(loc));
		Mockito.when(layoutRepository.findById(layoutId)).thenReturn(Optional.of(layout));

		locationService.updateLocation(locationId, requestDTO);
	}

	@Test
	public void givenValidLocationId_whenUpdateLocation_expectLocationUpdated() {
		Long locationId = -1L;
		Long layoutId = -2L;
		LocationDTO requestDTO = new LocationDTO("1", "1", layoutId);

		Layout layout = new Layout();
		layout.setId(layoutId);

		Location loc = new Location();
		loc.setId(locationId);
		loc.setName("1");
		loc.setAddress("1");
		loc.setLayout(layout);

		Manifestation manf = new Manifestation();
		manf.setName("testManf");
		manf.setDescription("testDesc");
		manf.setManifestationType(ManifestationType.CULTURE);
		ManifestationDay[] dates = {new ManifestationDay(manf, DateHelper.getDate("20/01/2000 13:30"))};
		manf.setManifestationDays(Arrays.asList(dates));
		manf.setLocation(loc);
		loc.getManifestations().add(manf);

		Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(loc));
		Mockito.when(layoutRepository.findById(layoutId)).thenReturn(Optional.of(layout));
		Mockito.when(locationRepository.save(Mockito.any())).thenReturn(loc);


		LocationDTO locationDTO = locationService.updateLocation(locationId, requestDTO);
		assertEquals(locationId, locationDTO.getId());
		assertEquals(layoutId, locationDTO.getLayoutId());
		assertEquals("1", locationDTO.getName());
		assertEquals("1", locationDTO.getAddress());
	}

}
