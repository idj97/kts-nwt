package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mbooking.dto.ManifestationSectionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ManifestationSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int size;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Manifestation manifestation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="section_id") //da se ne bi kreirala medjutabela
    private Section selectedSection;

    public ManifestationSection(ManifestationSectionDTO manifestSectionDTO,
                                Section selectedSection) {

        this.price = manifestSectionDTO.getPrice();
        this.size = manifestSectionDTO.getSize();
        this.selectedSection = selectedSection;

    }
}
