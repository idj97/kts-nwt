package com.mbooking.model;

import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = Constants.NAME_LENGTH)
    private String name;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Manifestation> manifestations;
}
