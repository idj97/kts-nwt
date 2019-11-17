package com.mbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration_date", nullable = false)
	private Date expirationDate;
	
	@Column(name = "price")
	private double price;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "status")
	private ReservationStatus status;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ReservationDetails> reservationDetails;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Manifestation manifestation;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Customer customer;
	
	
	
}
