package com.mbooking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CUSTOMER")

@NoArgsConstructor
public class Customer extends User {
	private static final long serialVersionUID = 1L;
	private boolean banned;
	private boolean emailConfirmed;
	private String emailConfirmationId;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Reservation> reservations;

	public Customer(boolean banned, boolean emailConfirmed, String emailConfirmationId
			) {
		super();
		this.banned = banned;
		this.emailConfirmed = emailConfirmed;
		this.emailConfirmationId = emailConfirmationId;
	
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public String getEmailConfirmationId() {
		return emailConfirmationId;
	}

	public void setEmailConfirmationId(String emailConfirmationId) {
		this.emailConfirmationId = emailConfirmationId;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}




}
