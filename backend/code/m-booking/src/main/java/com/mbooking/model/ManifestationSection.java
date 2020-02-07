package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mbooking.dto.ManifestationSectionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ManifestationSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int size;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Manifestation manifestation;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ReservationDetails> reservationsDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="section_id") //to avoid a separate table
    private Section selectedSection;

    public ManifestationSection(ManifestationSectionDTO manifestSectionDTO,
                                Section selectedSection, Manifestation manifestation) {

        this.price = manifestSectionDTO.getPrice();
        this.size = manifestSectionDTO.getSize();
        this.selectedSection = selectedSection;
        this.manifestation = manifestation;

    }
}
