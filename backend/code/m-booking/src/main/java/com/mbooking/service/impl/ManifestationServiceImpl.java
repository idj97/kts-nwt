package com.mbooking.service.impl;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationSectionDTO;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ManifestationSection;
import com.mbooking.model.Section;
import com.mbooking.repository.ManifestationRepository;
import com.mbooking.service.ConversionService;
import com.mbooking.service.ManifestationService;
import com.mbooking.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ManifestationServiceImpl implements ManifestationService {

    @Autowired
    ManifestationRepository manifestRepo;

    @Autowired
    SectionService sectionSvc;

    @Autowired
    ConversionService conversionSvc;


    /*****************
     Service methods *
     *****************/

    public Manifestation createManifestation(ManifestationDTO newManifestData) {

        Manifestation newManifest = new Manifestation(newManifestData);

        //adding days
        newManifest.setManifestationDays(createManifestDays(newManifestData.getStartDate(),
                newManifestData.getEndDate(), newManifest));

        //adding pictures
        newManifest.setPictures(conversionSvc.convertListToSet(newManifestData.getImages()));

        //adding selected sections
        newManifest.setSelectedSections(createManifestationSections(newManifestData.getSelectedSections()));

        return save(newManifest);
    }


    /*****************
    Auxiliary methods*
     *****************/

    private Set<ManifestationSection> createManifestationSections(List<ManifestationSectionDTO> sections) throws ApiException {

        Set<ManifestationSection> selectedSections = new HashSet<>();
        Section section; //section to find

        for(ManifestationSectionDTO sectionDTO: sections) {

            section = sectionSvc.
                    findById(sectionDTO.getSectionID()).
                    orElseThrow(() -> new ApiException("Section not found", HttpStatus.NOT_FOUND));
            selectedSections.add(new ManifestationSection(sectionDTO, section));
        }

        return selectedSections;
    }


    private Set<ManifestationDay> createManifestDays(Date start, Date end, Manifestation newManifest) {

        Set<ManifestationDay> manifestDays = new HashSet<ManifestationDay>();
        long numOfDays = getDifferenceDays(start, end);

        //if the manifestation has the same start and end date
        if(numOfDays == 0) {
            numOfDays += 1; //it lasts for a day
        }

        for(int i = 0; i < numOfDays; i++) {
            manifestDays.add(new ManifestationDay(newManifest));
        }


        return manifestDays;
    }

    private long getDifferenceDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }



    /**********************************
     Repository method implementations *
     *********************************/

    public Manifestation save(Manifestation manifestation) {
        return manifestRepo.save(manifestation);
    }

}
