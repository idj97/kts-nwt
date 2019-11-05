package com.mbooking.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private boolean areReservationsAvailable;

    @Column(nullable = false)
    private int maxReservations;

    @Column(nullable = false)
    private Date reservableUntil;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ManifestationDay> manifestationDays = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Location location;
}
