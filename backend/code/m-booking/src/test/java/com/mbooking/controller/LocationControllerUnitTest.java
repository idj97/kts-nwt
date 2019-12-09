package com.mbooking.controller;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class LocationControllerUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LocationService locationService;

    @Test
    public void when_getById_AndLocationNotExist_NotFound() {
        Long id = 5L;
        when(locationService.getById(id)).thenThrow(new ApiNotFoundException());
        ResponseEntity<ApiNotFoundException> response = restTemplate.getForEntity("/api/locations/" + id, ApiNotFoundException.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void when_getById_AndLocationExist_Ok() {
        Long id = 5L;
        LocationDTO locationDTO = new LocationDTO(id, "1", "1", 1L, null);

        when(locationService.getById(id)).thenReturn(locationDTO);
        ResponseEntity<LocationDTO> response = restTemplate.getForEntity("/api/locations/" + id, LocationDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocationDTO retLocationDTO = response.getBody();
        assertEquals(locationDTO.getId(), retLocationDTO.getId());
        assertEquals(locationDTO.getName(), retLocationDTO.getName());
        assertEquals(locationDTO.getAddress(), retLocationDTO.getAddress());
        assertEquals(locationDTO.getLayoutId(), retLocationDTO.getLayoutId());
    }

    @Test
    public void when_getByNameOrAddress() {
        String name = "1";
        String address = "1";
        int pageNum = 0;
        int pageSize = 10;

        List<LocationDTO> dtoList = new ArrayList<>();
        dtoList.add(new LocationDTO(1L, "1", "1", 1L, null));

        when(locationService.getByNameOrAddress(name, address, pageNum, pageSize)).thenReturn(dtoList);
        ResponseEntity<LocationDTO[]> response = restTemplate.getForEntity(
                "/api/locations?name={name}&address={address}&pageNum={pageNum}&pageSize={pageSize}",
                LocationDTO[].class,
                name, address, pageNum, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LocationDTO> returnedDTOs = Arrays.asList(response.getBody());
        assertEquals(dtoList.size(), returnedDTOs.size());
        assertEquals(dtoList.get(0).getName(), returnedDTOs.get(0).getName());
        assertEquals(dtoList.get(0).getId(), returnedDTOs.get(0).getId());
        assertEquals(dtoList.get(0).getAddress(), returnedDTOs.get(0).getAddress());
        assertEquals(dtoList.get(0).getLayoutId(), returnedDTOs.get(0).getLayoutId());
    }

    @Test
    public void when_createLocation_AndNotAuthorized_Forbiden() {
        LocationDTO dto = new LocationDTO(null, "1", "1", 1L, null);

        ResponseEntity<HttpServletResponse> response = restTemplate.postForEntity(
                "/api/locations", dto,
                HttpServletResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
        System.out.println(response.getBody());
    }


}
