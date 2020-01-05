package com.mbooking.service.impl;

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

        deleteOldManifestDays(manifestToUpdate);
        deleteOldManifestSections(manifestToUpdate);

        //updating manifestation days
        manifestToUpdate.setManifestationDays(createManifestDays(manifestData.getManifestationDates(),
                manifestToUpdate));

        //updating selected sections
        manifestToUpdate.setSelectedSections(createManifestationSections(manifestData.getSelectedSections(),
                manifestToUpdate));

        save(manifestToUpdate);
        return manifestData;

    }

    public List<ManifestationDTO> searchManifestations(String name, String type, String locationName,
                                                       int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("name"));
        ManifestationType manifestType = conversionSvc.convertStringToManifestType(type);

        //if the manifestation type is valid, include it in the search
        if(manifestType != null) {
            return manifestRepo.findByNameContainingAndManifestationTypeAndLocationNameContaining(
                    name, manifestType, locationName, pageable)
                    .stream().map(manifestation -> new ManifestationDTO(manifestation)).collect(Collectors.toList());
        }

        //otherwise ignore it
        return manifestRepo.findByNameContainingAndLocationNameContaining(name, locationName, pageable)
                .stream().map(manifestation -> new ManifestationDTO(manifestation)).collect(Collectors.toList());

    }


    /*****************
    Auxiliary methods*
     *****************/

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

        Collections.sort(manifestData.getManifestationDates()); //required for binary search

        //loop through manifestations at the same location
        for(Manifestation manifest: manifestRepo.findByLocationId(manifestData.getLocationId())) {

            for(ManifestationDay manifDay: manifest.getManifestationDays()) {

                //if the date for that location already exists
                if(Collections.binarySearch(manifestData.getManifestationDates(), manifDay.getDate(),
                        new ManifestationDateComparator()) >= 0) {

                    //when updating, the user may leave the same dates
                    if(updating && manifest.getId().equals(manifestData.getManifestationId())) {
                        continue;
                    }

                    return true;

                }

            }

        }

        return false;
    }


    private boolean areThereReservations(Long manifestationId) {

        return reservationRepo.findByManifestationId(manifestationId).size() > 0;

    }

    private void deleteOldManifestDays(Manifestation manifestToUpdate) {

        for(ManifestationDay oldManifestDay: manifestToUpdate.getManifestationDays()) {
            oldManifestDay.setManifestation(null);
            manifestDayRepo.deleteById(oldManifestDay.getId());
        }

       // manifestDayRepo.deleteAll(manifestToUpdate.getManifestationDays());

    }

    private void deleteOldManifestSections(Manifestation manifestToUpdate) {

        for(ManifestationSection oldManifestSection: manifestToUpdate.getSelectedSections()) {
            oldManifestSection.setManifestation(null);
            oldManifestSection.setSelectedSection(null);
            manifestSectionRepo.deleteById(oldManifestSection.getId());
        }

        //manifestSectionRepo.deleteAll(manifestToUpdate.getSelectedSections());
    }

    private Set<ManifestationSection> createManifestationSections(List<ManifestationSectionDTO> sections,
                                                                  Manifestation newManifest) throws ApiException {

        if(sections.size() == 0) {
            throw new ApiBadRequestException(Constants.NO_SECTIONS_SELECTED_MSG);
        }

        Set<ManifestationSection> selectedSections = new HashSet<>();
        Section section; //section to find

        for(ManifestationSectionDTO sectionDTO: sections) {

            section = sectionSvc.
                    findById(sectionDTO.getSectionID()).
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

    public List<ManifestationDTO> findAll(int pageNum, int pageSize)
    {
        return manifestRepo.findAll(PageRequest.of(pageNum, pageSize, Sort.by("name")))
                .stream().map(manifestation -> new ManifestationDTO(manifestation)).collect(Collectors.toList());
    }

}
