package com.mbooking.repository;

import com.mbooking.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LocationRepositoryIntegrationTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void when_locationNotExists_Then_Empty() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Location> locations = locationRepository.findByNameContainingAndAddressContaining("1", "1", pageable);
        assertEquals(0, locations.size());
    }

    @Test
    public void when_locationExists_Then_Present() {
        Location location1 = new Location("11", "11", null );
        Location location2 = new Location("12", "12", null );
        Location location3 = new Location("23", "23", null );
        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);
        Pageable pageable = PageRequest.of(0, 10);

        assertEquals(0, locationRepository.findByNameContainingAndAddressContaining("44", "44", pageable).size());
        assertEquals(1, locationRepository.findByNameContainingAndAddressContaining("11", "11", pageable).size());
        assertEquals(2, locationRepository.findByNameContainingAndAddressContaining("1", "1", pageable).size());
        assertEquals(3, locationRepository.findByNameContainingAndAddressContaining("", "", pageable).size());
    }
}
