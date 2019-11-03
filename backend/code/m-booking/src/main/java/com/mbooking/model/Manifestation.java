package com.mbooking.model;

import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    private Set<String> pictures;

    @Column(nullable = false)
    private boolean areReservationsAvailable;

    @Column(nullable = false)
    private int maxReservations;

    @Column(nullable = false)
    private Date reservableUntil;

    @OneToMany(mappedBy="manifestation", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<ManifestationDay> manifestationDays;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Location location;

}
