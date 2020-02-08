package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mbooking.dto.ManifestationDTO;
import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Manifestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = Constants.NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = Constants.DESCR_LENGTH)
    private String description;

    @Column(nullable=false)
    private ManifestationType manifestationType;

    @OneToMany(mappedBy = "manifestation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ManifestationImage> images;

    @Column(nullable = false)
    private boolean reservationsAvailable;

    @Column(nullable = false)
    private int maxReservations;

    @Column
    private Date reservableUntil;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ManifestationDay> manifestationDays;
    
    @OneToMany(mappedBy="manifestation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ManifestationSection> selectedSections;
    
    @OneToMany(mappedBy = "manifestation", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Reservation> reservations;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Location location;

    public Manifestation() {
        this.selectedSections = new HashSet<>();
        this.reservations = new ArrayList<>();
        this.manifestationDays = new ArrayList<>();
    }
    
    public Manifestation(ManifestationDTO manifestDTO) {
        this.name = manifestDTO.getName();
        this.description = manifestDTO.getDescription();
        this.manifestationType = manifestDTO.getType();
        this.reservationsAvailable = manifestDTO.isReservationsAllowed();
        this.maxReservations = manifestDTO.getMaxReservations();
        this.reservableUntil = manifestDTO.getReservableUntil();
    }
}
