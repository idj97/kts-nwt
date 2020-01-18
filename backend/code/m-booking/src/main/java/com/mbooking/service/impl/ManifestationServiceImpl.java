package com.mbooking.service.impl;

import com.github.rkumsher.date.DateUtils;
import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiConflictException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.*;
import com.mbooking.repository.*;
import com.mbooking.service.ConversionService;
import com.mbooking.service.ManifestationService;
import com.mbooking.service.SectionService;
import com.mbooking.utility.Constants;
import com.mbooking.utility.ManifestationDateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManifestationServiceImpl implements ManifestationService {

    @Autowired
    ManifestationRepository manifestRepo;

    @Autowired
    ManifestationDayRepository manifestDayRepo;

    @Autowired
    ReservationRepository reservationRepo;

    @Autowired
    ManifestationSectionRepository manifestSectionRepo;

    @Autowired
    LocationRepository locationRepo;

    @Autowired
    SectionService sectionSvc;

    @Autowired
    ConversionService conversionSvc;


    /*****************
     Service methods *
     *****************/

    public ManifestationDTO createManifestation(ManifestationDTO newManifestData) {

        validateManifestationDates(newManifestData);

        //check if there is already a manifestation on the specified location and date
        if(checkManifestDateAndLocation(newManifestData, false)) {
            throw new ApiConflictException(Constants.CONFLICTING_MANIFEST_DAY_MSG);
        }

        Manifestation newManifest = new Manifestation(newManifestData);

        //adding the location if it exists
        Location location = locationRepo.findById(newManifestData.getLocationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.LOCATION_NOT_FOUND_MSG));
        newManifest.setLocation(location);

        //adding days
        newManifest.setManifestationDays(createManifestDays(newManifestData.getManifestationDates(), newManifest));

        //adding pictures
        newManifest.setPictures(conversionSvc.convertListToSet(newManifestData.getImages()));

        //adding selected sections
        newManifest.setSelectedSections(createManifestationSections(newManifestData.getSelectedSections(),
                newManifest));

        newManifest = save(newManifest);
        newManifestData.setManifestationId(newManifest.getId());

        return newManifestData;
    }


    public ManifestationDTO updateManifestation(ManifestationDTO manifestData) {

        if(manifestData.getManifestationId() == null) {
            throw new ApiNotFoundException(Constants.MANIFEST_NOT_FOUND_MSG);
        }

        Manifestation manifestToUpdate = findOneById(manifestData.getManifestationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.MANIFEST_NOT_FOUND_MSG));


        validateManifestationDates(manifestData);

        if(areThereReservations(manifestToUpdate.getId())) {
            throw new ApiConflictException(Constants.CHG_MANIFEST_WITH_RESERV_MSG);
        }

        if(checkManifestDateAndLocation(manifestData, true)) {
            throw new ApiConflictException(Constants.CONFLICTING_MANIFEST_DAY_MSG);
        }

        //updating location if it exists
        Location location = locationRepo.findById(manifestData.getLocationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.LOCATION_NOT_FOUND_MSG));
        manifestToUpdate.setLocation(location);

        //updating data
        manifestToUpdate.setName(manifestData.getName());
        manifestToUpdate.setDescription(manifestData.getDescription());
        manifestToUpdate.setManifestationType(manifestData.getType());
        manifestToUpdate.setMaxReservations(manifestData.getMaxReservations());
        manifestToUpdate.setReservableUntil(manifestData.getReservableUntil());
        manifestToUpdate.setReservationsAvailable(manifestData.isReservationsAllowed());
        manifestToUpdate.setPictures(conversionSvc.convertListToSet(manifestData.getImages()));

        // memorize old days and sections in order to delete them later
        List<ManifestationDay> oldManifestationDays = manifestToUpdate.getManifestationDays();
        Set<ManifestationSection> oldManifestationSections = manifestToUpdate.getSelectedSections();

        //updating manifestation days
        manifestToUpdate.setManifestationDays(createManifestDays(manifestData.getManifestationDates(),
                manifestToUpdate));

        //updating selected sections
        manifestToUpdate.setSelectedSections(createManifestationSections(manifestData.getSelectedSections(),
                manifestToUpdate));

        // delete old days and sections if everything went well
        deleteOldManifestDays(oldManifestationDays);
        deleteOldManifestSections(oldManifestationSections);

        save(manifestToUpdate);
        return manifestData;

    }

    public List<ManifestationDTO> searchManifestations(String name, String type, String locationName,
                                                       String date, int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("name"));
        ManifestationType manifestType = conversionSvc.convertStringToManifestType(type);
        Date searchDate = parseSearchDate(date);

        if (manifestType == null && searchDate == null) {
            return searchByNameAndLocation(name, locationName, pageable);

        } else if (manifestType != null && searchDate == null) {
            return searchByNameAndTypeAndLocation(name, locationName, manifestType, pageable);

        } else if (manifestType == null && searchDate != null) {
            return searchByNameAndLocationAndDate(name, locationName, searchDate, pageable);

        } else {
            return searchByNameAndTypeAndLocationNameAndDate(name, locationName, manifestType,
                    searchDate, pageable);
        }

    }

    /*****************
    Auxiliary methods*
     *****************/

    private List<ManifestationDTO> searchByNameAndLocation(String name, String locationName, Pageable pageable) {

        return manifestRepo
                .findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateAfter(
                        name, locationName, new Date(), pageable)
                .stream()
                .map(manifestation -> new ManifestationDTO(manifestation))
                .collect(Collectors.toList());
    }

    private List<ManifestationDTO> searchByNameAndTypeAndLocation(String name, String locationName,
                                                                  ManifestationType type,
                                                                  Pageable pageable) {
        return manifestRepo
                .findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
                        name, type, locationName, new Date(), pageable)
                .stream()
                .map(manifestation -> new ManifestationDTO(manifestation))
                .collect(Collectors.toList());
    }

    private List<ManifestationDTO> searchByNameAndLocationAndDate(String name, String locationName,
                                                                  Date searchDate, Pageable pageable) {

        Date searchDateStart = DateUtils.atStartOfDay(searchDate); // date with start time 00:00
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate); // date with end time 23:59

        return manifestRepo.findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateBetween(
                name, locationName, searchDateStart, searchDateEnd, pageable)
                .stream()
                .map(manifestation -> new ManifestationDTO(manifestation))
                .collect(Collectors.toList());
    }

    private List<ManifestationDTO> searchByNameAndTypeAndLocationNameAndDate(String name, String locationName,
                                                                             ManifestationType type,
                                                                             Date searchDate,
                                                                             Pageable pageable) {

        Date searchDateStart = DateUtils.atStartOfDay(searchDate); // date with start time 00:00
        Date searchDateEnd = DateUtils.atEndOfDay(searchDate); // date with end time 23:59

        return manifestRepo.findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateBetween(
                name, type, locationName, searchDateStart, searchDateEnd, pageable)
                .stream()
                .map(manifestation -> new ManifestationDTO(manifestation))
                .collect(Collectors.toList());
    }

    private Date parseSearchDate(String searchDate) {

        if(searchDate == null || "".equals(searchDate)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(searchDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return null;

    }


    private void validateManifestationDates(ManifestationDTO manifestDTO) {

        //check if all of the dates are future dates
        if(verifyFutureDates(manifestDTO.getManifestationDates(), new Date())) {
            throw new ApiBadRequestException(Constants.FUTURE_DATES_MSG);
        }

        //check if the last date for reserving is before the manifestation dates
        if(manifestDTO.isReservationsAllowed()) {
            if(manifestDTO.getReservableUntil() == null ||
                    verifyFutureDates(manifestDTO.getManifestationDates(), manifestDTO.getReservableUntil())) {
                throw new ApiBadRequestException(Constants.INVALID_RESERV_DAY_MSG);
            }
        }

        //TODO: check if the user had sent two same dates??

        //check if the number of days is greater than the maximum defined one
        if (manifestDTO.getManifestationDates().size() > Constants.MAX_NUM_OF_DAYS
                || manifestDTO.getManifestationDates().size() < 1) {
            throw new ApiBadRequestException(Constants.INVALID_NUM_OF_DAYS_MSG);
        }


    }

    /** Returns true if there is a day before dateToCompare */
    private boolean verifyFutureDates(List<Date> manifestDates, Date dateToCompare) {

        ManifestationDateComparator comparator = new ManifestationDateComparator();
        for(Date manifDay: manifestDates) {

            if(comparator.compare(manifDay, dateToCompare) <= 0) {
                return true;
            }

        }

        return false;
    }

    private boolean checkManifestDateAndLocation(ManifestationDTO manifestData, boolean updating) {

        List<Manifestation> manifestsOnLocation =
                manifestRepo
                .findDistinctByLocationIdAndManifestationDaysDateNoTimeIn(manifestData.getLocationId(),
                        manifestData.getManifestationDates());

        // if there aren't any manifestations on location
        // or if the user is updating the same manifestation with it's previous days
        if(manifestsOnLocation.size() == 0 ||
                (updating && manifestsOnLocation.get(0).getId()
                        .equals(manifestData.getManifestationId()))) {
            return false;
        }

        return manifestsOnLocation.size() > 0;

    }


    private boolean areThereReservations(Long manifestationId) {

        return reservationRepo.findByManifestationId(manifestationId).size() > 0;

    }

    private void deleteOldManifestDays(List<ManifestationDay> oldManifestationDays) {

        for(ManifestationDay oldManifestDay: oldManifestationDays) {
            oldManifestDay.setManifestation(null);
            manifestDayRepo.deleteById(oldManifestDay.getId());
        }
    }

    private void deleteOldManifestSections(Set<ManifestationSection> oldManifestationSections) {

        for(ManifestationSection oldManifestSection: oldManifestationSections) {
            oldManifestSection.setManifestation(null);
            oldManifestSection.setSelectedSection(null);
            manifestSectionRepo.deleteById(oldManifestSection.getId());
        }
    }

    private Set<ManifestationSection> createManifestationSections(List<ManifestationSectionDTO> sections,
                                                                  Manifestation newManifest) throws ApiException {
        /*
        if(sections.size() == 0) {
            throw new ApiBadRequestException(Constants.NO_SECTIONS_SELECTED_MSG);
        }*/

        Set<ManifestationSection> selectedSections = new HashSet<>();
        Section section; //section to find

        for(ManifestationSectionDTO sectionDTO: sections) {

            section = sectionSvc.
                    findById(sectionDTO.getSectionId()).
                    orElseThrow(() -> new ApiNotFoundException(Constants.SECTION_NOT_FOUND_MSG));

            //TODO: check if the selected section size is greater than actual section size

            selectedSections.add(new ManifestationSection(sectionDTO, section, newManifest));
        }

        return selectedSections;
    }


    private List<ManifestationDay> createManifestDays(List<Date> manifestDates, Manifestation newManifest) {

        List<ManifestationDay> manifestDays = new ArrayList<>();

        for(Date mDate: manifestDates) {
            manifestDays.add(new ManifestationDay(newManifest, mDate));
        }


        return manifestDays;
    }


    /**********************************
     Repository method implementations *
     *********************************/

    public Manifestation save(Manifestation manifestation) {
        return manifestRepo.save(manifestation);
    }

    public Optional<Manifestation> findOneById(Long id) {
        return manifestRepo.findById(id);
    }

    public ManifestationDTO getManifestationById(Long id) {

        return manifestRepo.findById(id)
                .map(m -> new ManifestationDTO(m))
                .orElseThrow(() -> new ApiNotFoundException("Manifestation not found"));
    }

    public List<ManifestationDTO> findAll(int pageNum, int pageSize)
    {
        return manifestRepo
                .findAll(PageRequest.of(pageNum, pageSize))
                .stream()
                .map(manifestation -> new ManifestationDTO(manifestation))
                .collect(Collectors.toList());
    }

}
