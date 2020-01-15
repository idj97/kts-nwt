package com.mbooking.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReservationDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "seat_row")
	private int row;
	
	@Column(name = "seat_column")
	private int column;
	
	@Column(name = "is_seating")
	@Type(type = "org.hibernate.type.NumericBooleanType")	//Needed for h2 when exporting data via MySQL
	private boolean isSeating;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Reservation reservation;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private ManifestationSection manifestationSection;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private ManifestationDay manifestationDay;
}
