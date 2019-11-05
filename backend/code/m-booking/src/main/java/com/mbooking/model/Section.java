package com.mbooking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mbooking.utility.Constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = Constants.NAME_LENGTH)
    private String name;
    
    //private SectionType type;
    @Column(nullable = false)
    private boolean isSeating;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sectionRows;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sectionColumns;
}
