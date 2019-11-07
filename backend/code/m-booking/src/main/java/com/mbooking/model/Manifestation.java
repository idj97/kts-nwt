package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mbooking.dto.ManifestationDTO;
import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Column(name="pictures")
    @ElementCollection(targetClass = String.class)
    private Set<String> pictures = new HashSet<>();

    @Column(nullable = false)
    private boolean reservationsAvailable;

    @Column(nullable = false)
    private int maxReservations;

    @Column(nullable = false)
    private Date reservableUntil;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ManifestationDay> manifestationDays = new ArrayList<>();
    
    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ManifestationSection> selectedSections;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Location location;
    
    public Manifestation(ManifestationDTO manifestDTO) {
        this.name = manifestDTO.getName();
        this.description = manifestDTO.getDescription();
        this.manifestationType = manifestDTO.getType();
        this.reservationsAvailable = manifestDTO.isReservationsAllowed();
        this.maxReservations = manifestDTO.getMaxReservations();
        this.reservableUntil = manifestDTO.getReservableUntil();
    }
}
