package com.mbooking.service.impl;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
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

    public Manifestation createManifestation(ManifestationDTO newManifestData) {

        //check if there is already a manifestation on the specified location and date
        if(checkManifestDateAndLocation(newManifestData, false)) {
            throw new ApiConflictException("Can't have more than one manifestation in the same location at the same time");
        }


        Manifestation newManifest = new Manifestation(newManifestData);

        //adding days
        newManifest.setManifestationDays(createManifestDays(newManifestData.getManifestationDates(), newManifest));

        //adding pictures
        newManifest.setPictures(conversionSvc.convertListToSet(newManifestData.getImages()));

        //adding selected sections
        newManifest.setSelectedSections(createManifestationSections(newManifestData.getSelectedSections(),
                newManifest));

        //adding the location
        Location location = locationRepo.findById(newManifestData.getLocationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.LOCATION_NOT_FOUND_MSG));
        newManifest.setLocation(location);

        return save(newManifest);
    }


    public Manifestation updateManifestation(ManifestationDTO manifestData) {

        if(manifestData.getManifestationId() == null) {
            throw new ApiNotFoundException(Constants.MANIFEST_NOT_FOUND_MSG);
        }


        Manifestation manifestToUpdate= findOneById(manifestData.getManifestationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.MANIFEST_NOT_FOUND_MSG));


        if(areThereReservations(manifestToUpdate.getId())) {
            throw new ApiConflictException("Can't alter a manifestation with reservations");
        }

        if(checkManifestDateAndLocation(manifestData, true)) {
            throw new ApiConflictException("Can't have more than one manifestation in the same location at the same time");
        }

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

        //updating location
        Location location = locationRepo.findById(manifestData.getLocationId()).
                orElseThrow(() -> new ApiNotFoundException(Constants.LOCATION_NOT_FOUND_MSG));
        manifestToUpdate.setLocation(location);

        //updating selected sections
        manifestToUpdate.setSelectedSections(createManifestationSections(manifestData.getSelectedSections(),
                manifestToUpdate));

        return save(manifestToUpdate);

    }

    public List<ManifestationDTO> searchManifestations(String name, String type, String locationName) {

        ManifestationType manifestType = conversionSvc.convertStringToManifestType(type);

        //if the manifestation type is valid, include it in the search
        if(manifestType != null) {
            return manifestRepo.findByNameContainingAndManifestationTypeAndLocationNameContaining(name, manifestType, locationName).
                    stream().map(manifestation -> new ManifestationDTO(manifestation)).collect(Collectors.toList());
        }

        //otherwise ignore it
        return manifestRepo.findByNameContainingAndLocationNameContaining(name, locationName).
                stream().map(manifestation -> new ManifestationDTO(manifestation)).collect(Collectors.toList());

    }


    /*****************
    Auxiliary methods*
     *****************/

    private boolean checkManifestDateAndLocation(ManifestationDTO manifestData, boolean updating) {

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


    public boolean areThereReservations(Long manifestationId) {

        for(Reservation reserv: reservationRepo.findAll()) {
            for(ManifestationDay md: reserv.getManifestationDays()) {
                if(md.getManifestation().getId().equals(manifestationId)) {
                    return true;
                }
            }
        }

        return false;

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

    public Set<ManifestationSection> createManifestationSections(List<ManifestationSectionDTO> sections,
                                                                  Manifestation newManifest) throws ApiException {

        Set<ManifestationSection> selectedSections = new HashSet<>();
        Section section; //section to find

        for(ManifestationSectionDTO sectionDTO: sections) {

            section = sectionSvc.
                    findById(sectionDTO.getSectionID()).
                    orElseThrow(() -> new ApiNotFoundException(Constants.SECTION_NOT_FOUND_MSG));
            selectedSections.add(new ManifestationSection(sectionDTO, section, newManifest));
        }

        return selectedSections;
    }


    public List<ManifestationDay> createManifestDays(List<Date> manifestDates, Manifestation newManifest) {

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

    public List<Manifestation> findAll() {
        return manifestRepo.findAll();
    }

}
