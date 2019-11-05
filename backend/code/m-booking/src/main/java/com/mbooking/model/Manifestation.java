package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mbooking.dto.ManifestationDTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.mbooking.utility.Constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manifestation {


    public Manifestation(ManifestationDTO manifestDTO) {

        this.name = manifestDTO.getName();
        this.description = manifestDTO.getDescription();
        this.manifestationType = manifestDTO.getType();
        this.reservationsAvailable = manifestDTO.isReservationsAllowed();
        this.maxReservations = manifestDTO.getMaxReservations();
        this.reservableUntil = manifestDTO.getReservableUntil();
        //TODO: set location
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = Constants.NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = Constants.DESCR_LENGTH)
    private String description;

    @Column(nullable=false)
    private ManifestationType manifestationType;

    @Column(name="pictures")
    @ElementCollection(targetClass = String.class)
    private Set<String> pictures = new HashSet<>();

    @Column(nullable = false)
    private boolean reservationsAvailable;

    @Column(nullable = false)
    private int maxReservations;

    @Column(nullable = false)
    private Date reservableUntil;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ManifestationDay> manifestationDays;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ManifestationSection> selectedSections;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Location location;

}
