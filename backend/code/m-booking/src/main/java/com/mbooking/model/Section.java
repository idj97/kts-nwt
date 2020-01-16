package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = Constants.NAME_LENGTH)
    private String name;
    
    @Column(nullable = false)
    private boolean isSeating;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sectionRows;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sectionColumns;
}
