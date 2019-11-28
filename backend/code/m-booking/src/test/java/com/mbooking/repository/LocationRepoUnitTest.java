package com.mbooking.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.Location;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationRepoUnitTest {

	@Autowired
	LocationRepository repo;

	public List<Location> saveLocations() {
		List<Location> locations = new ArrayList<>();
		locations.add(new Location("ll1", "aa1", null));
		locations.add(new Location("ll2", "aa2", null));
		locations.add(new Location("l3", "a3", null));
		return repo.saveAll(locations);
	}

	@Test
	public void whenFindByNameOrAddressExact_thenReturnOne() {
		saveLocations();
		Pageable pageable = PageRequest.of(0, 10);
		List<Location> locations = repo.findByNameContainingAndAddressContaining("l1", "a1", pageable);
		assertEquals(1, locations.size());
		assertEquals("ll1", locations.get(0).getName());
		assertEquals("aa1", locations.get(0).getAddress());
	}

	@Test
	public void whenFindByNameAndAddressEmpty_thenReturnAll() {
		List<Location> savedLocations = saveLocations();
		Pageable pageable = PageRequest.of(0, 10);
		List<Location> locations = repo.findByNameContainingAndAddressContaining("", "", pageable);
		assertEquals(savedLocations.size(), locations.size());
	}

	@Test
	public void whenFindByNameOrAddressWrong_thenReturnNone() {
		saveLocations();
		Pageable pageable = PageRequest.of(0, 10);
		List<Location> locations = repo.findByNameContainingAndAddressContaining("asdasd", "asdasd", pageable);
		assertEquals(0, locations.size());
	}

}
