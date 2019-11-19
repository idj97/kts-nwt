package com.mbooking.service;

import java.io.ByteArrayOutputStream;

import com.mbooking.model.Reservation;

public interface PDFCreatorService {
	public void createTestPDF();
	public ByteArrayOutputStream createReservationPDF(Reservation reservation);
}
