package com.mbooking.service;

import com.mbooking.model.Reservation;

import java.io.ByteArrayOutputStream;

public interface PDFCreatorService {
	public void createTestPDF();
	public ByteArrayOutputStream createReservationPDF(Reservation reservation);
}
