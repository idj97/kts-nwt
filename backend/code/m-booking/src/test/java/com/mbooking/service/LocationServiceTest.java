package com.mbooking.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
import com.mbooking.model.Section;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.repository.LocationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationServiceTest {

	@MockBean
	private LayoutRepository layoutRepo;

	@MockBean
	private LocationRepository lrepos;

	@Autowired
	private LocationService ls;

	@Test(expected = ApiNotFoundException.class)
	public void testGetById() {
		Mockito.when(lrepos.findById(1L)).thenReturn(Optional.empty());
		ls.getById(1L);

	}

	@Test
	public void testGetById1() {
		Location loc = new Location();
		loc.setId(1L);
		loc.setName("a");
		loc.setLayout(new Layout());
		loc.getLayout().setId(1L);
		loc.setAddress("a");
		loc.setManifestations(new HashSet<Manifestation>());
		Mockito.when(lrepos.findById(1L)).thenReturn(Optional.of(loc));
		LocationDTO locDTO= ls.getById(1L);
		assertNotNull(locDTO);

	}
	
	
	@Test(expected=ApiNotFoundException.class)
	public void testCreateLocation() {
		Mockito.when(layoutRepo.findById(1L)).thenReturn(Optional.empty());
		LocationDTO loc = new LocationDTO();
		loc.setLayoutId(1L);
		ls.createLocation(loc);
	}
	@Test
	public void testCreateLocation1() {
		Location loc = new Location();
		loc.setId(1L);
		loc.setName("a");
		loc.setLayout(new Layout());
		loc.getLayout().setId(1L);
		loc.setAddress("a");
		loc.setManifestations(new HashSet<Manifestation>());
		
		
		
		Mockito.when(lrepos.findById(1L)).thenReturn(Optional.of(loc));
		LocationDTO locDTO= ls.getById(1L);
		assertNotNull(locDTO);
		
		
		
		
	}

	@Test(expected=ApiNotFoundException.class)
	public void testUpdateLocation() {
		Mockito.when(layoutRepo.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(lrepos.findById(1L)).thenReturn(Optional.empty());
		
		LocationDTO loc = new LocationDTO();
		loc.setLayoutId(1L);
		ls.createLocation(loc);
	}
	
	@Test
	public void testUpdateLocation1() {
	
		Layout lay=new Layout();
		lay.setId(1L);
		lay.setName("name");
		lay.setSections(new HashSet<Section>());
		
		Location loc = new Location();
		loc.setId(1L);
		loc.setName("a");
		loc.setLayout(new Layout());
		loc.getLayout().setId(1L);
		loc.setAddress("a");
		loc.setManifestations(new HashSet<Manifestation>());
		
		
		Mockito.when(layoutRepo.findById(1L)).thenReturn(Optional.of(lay));
		Mockito.when(lrepos.findById(1L)).thenReturn(Optional.of(loc));
		
		
		
		LocationDTO locDTO= ls.getById(1L);
		assertNotNull(locDTO);
	}




/*	@Test
	public void testGetByNameOrAddress() {
		
	}*/
	
}
