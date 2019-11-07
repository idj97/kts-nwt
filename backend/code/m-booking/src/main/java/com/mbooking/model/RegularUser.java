package com.mbooking.model;

public class RegularUser extends User {
	private static final long serialVersionUID = 1L;

	

	public RegularUser() {
		
	}
	public RegularUser(long id, String firstname, String lastname, String username, String email, String password) {
		super(id,firstname, lastname, username, email, password);
	}



	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
