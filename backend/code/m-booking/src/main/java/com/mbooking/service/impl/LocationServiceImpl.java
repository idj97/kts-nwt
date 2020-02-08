package com.mbooking.service.impl;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.dto.reports.LocationReportRequestDTO;
import com.mbooking.dto.reports.ReportDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.*;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.repository.LocationRepository;
import com.mbooking.service.LocationService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LocationServiceImpl implements LocationService {


	@Autowired
	private LocationRepository locationRepo;

	@Autowired
	private LayoutRepository layoutRepo;

	@Autowired
	private DateService dateService;

	@Override
	public List<LocationDTO> getAllLocations() {
		return locationRepo.findAll()
				.stream()
				.map(location -> new LocationDTO(location))
				.collect(Collectors.toList());
	}

	@Override
	public LocationDTO getById(Long id) {
		Optional<Location> opt = locationRepo.findById(id);
		if (opt.isPresent()) {
			return new LocationDTO(opt.get());
		} else {
			throw new ApiNotFoundException("Location doesnt exist.");
		}
	}

	@Override
	public ResultsDTO<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Location> locationPage = locationRepo.findByNameContainingAndAddressContaining(name, address, pageable);

		List<LocationDTO> locationDTOS = locationPage
				.stream()
				.map(loc -> new LocationDTO(loc))
				.collect(Collectors.toList());

		return new ResultsDTO(locationDTOS, locationPage.getTotalPages());
	}

	@Override
	public LocationDTO createLocation(LocationDTO locationDTO) {
		Optional<Layout> layout = layoutRepo.findById(locationDTO.getLayoutId());
		if (layout.isPresent()) {
			Location location = new Location(locationDTO.getName(), locationDTO.getAddress(), layout.get());
			location = locationRepo.save(location);
			return new LocationDTO(location);
		} else {
			throw new ApiNotFoundException("Layout not found.");
		}
	}

	@Override
	public LocationDTO updateLocation(Long locationId, LocationDTO locationDTO) {
		Optional<Location> optLocation = locationRepo.findById(locationId);
		Optional<Layout> optLayout = layoutRepo.findById(locationDTO.getLayoutId());

		if (optLocation.isPresent() && optLayout.isPresent()) {
			checkIfUpdateIsPossible(optLocation.get());
			Location location = optLocation.get();
			location.setName(locationDTO.getName());
			location.setAddress(locationDTO.getAddress());
			location.setLayout(optLayout.get());
			location = locationRepo.save(location);
			return new LocationDTO(location);
		} else {
			throw new ApiNotFoundException("Location/layout not found.");
		}
	}

	@Override
	public void delete(Long locationId) {
		Optional<Location> optionalLocation = locationRepo.findById(locationId);
		if (optionalLocation.isPresent()) {
			Location location = optionalLocation.get();
			checkIfUpdateIsPossible(location);
			location.setName("deleted_" + location.getName());
			location.setDeleted(true);
			locationRepo.save(location);
		} else {
			throw new ApiNotFoundException("Location not found.");
		}
	}

	//If location have at least one unfinished manifestation layout change is not possible
	public void checkIfUpdateIsPossible(Location location) {
		for (Manifestation manf : location.getManifestations()) {
			int lastIndex = manf.getManifestationDays().size() - 1;
			Date lastDate = manf.getManifestationDays().get(lastIndex).getDate();
			if (lastDate.after(new Date())) {
				throw new ApiBadRequestException("This location have unfinished manifestations.");
			}
		}
	}

	@Override
	public ReportDTO reports(LocationReportRequestDTO locationReportRequestDTO) {
		ReportDTO reportDTO = new ReportDTO();
		Optional<Location> optionalLocation = locationRepo.findById(locationReportRequestDTO.getLocationId());
		if (optionalLocation.isPresent()) {
			Location location = optionalLocation.get();
			LocalDate startDate = dateService.toLocalDate(locationReportRequestDTO.getStartDate()).minusDays(1);
			LocalDate endDate = dateService.toLocalDate(locationReportRequestDTO.getEndDate()).plusDays(1);

			List<String> monthDates = dateService.generateDatesPerMonth(startDate, endDate);

			TreeMap<String, Double> incomeData = new TreeMap<>(dateService.getComparator());
			TreeMap<String, Long> ticketData = new TreeMap<>(dateService.getComparator());
			monthDates.forEach(md -> {
				incomeData.put(md, 0.0);
				ticketData.put(md, 0L);
			});

			location.getManifestations()
					.stream()
					.forEach(m -> m.getReservations()
							.stream()
							.filter(r -> dateService.isInsideDates(r, monthDates) && r.getStatus().equals(ReservationStatus.CONFIRMED))
							.forEach(r -> {
								reportDTO.setIncome(reportDTO.getIncome() + r.getPrice());
								reportDTO.setTicketCount(reportDTO.getTicketCount() + r.getReservationDetails().size());
								String key = dateService.formatDate(dateService.toLocalDate(r.getDateCreated()));
								incomeData.put(key, incomeData.get(key) + r.getPrice());
								ticketData.put(key, ticketData.get(key) + r.getReservationDetails().size());
							})
					);

			incomeData.keySet().stream().forEach(key -> {
				reportDTO.getLabels().add(key);
				reportDTO.getIncomeData().add(incomeData.get(key));
				reportDTO.getTicketData().add(ticketData.get(key));
			});
			return reportDTO;

		} else {
			throw new ApiNotFoundException("Location not found.");
		}
	}
}
