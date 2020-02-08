package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mbooking.utility.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Where(clause = "deleted=0")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = Constants.NAME_LENGTH)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Layout layout;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JsonManagedReference
    private Set<Manifestation> manifestations = new HashSet<>();

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;

    public Location(String name, String address, Layout layout) {
        super();
        this.name = name;
        this.address = address;
        this.layout = layout;
    }
}
