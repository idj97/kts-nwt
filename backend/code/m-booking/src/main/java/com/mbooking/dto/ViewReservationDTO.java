package com.mbooking.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewReservationDTO {
	private String manifestationName;
	private String mnifestationDescription;
	private ManifestationType manifestationType;
	private Date dateCreated;
	private Date expirationDate;
	private double price;
	private ReservationStatus status;
	private List<ReservationDetailsDTO> reservationDetails = new ArrayList<>();
	
	public ViewReservationDTO(Reservation res) {
		this.setDateCreated(res.getDateCreated());
		this.setExpirationDate(res.getExpirationDate());
		this.setPrice(res.getPrice());
		Manifestation manifest = res.getManifestationDays().get(0).getManifestation();
		this.setManifestationName(manifest.getName());
		this.setManifestationType(manifest.getManifestationType());
		this.setMnifestationDescription(manifest.getDescription());
		List<ReservationDetailsDTO> resDetailsDTO = new ArrayList<>();
		for (ReservationDetails details : res.getReservationDetails()) {
			ReservationDetailsDTO detailDTO = new ReservationDetailsDTO();
			detailDTO.setColumn(details.getColumn());
			detailDTO.setRow(details.getRow());
			detailDTO.setManifestationSectionId(details.getManifestationSection().getId());
			detailDTO.setSeating(details.isSeating());
			resDetailsDTO.add(detailDTO);
		}
		this.setReservationDetails(resDetailsDTO);
	}
	
}
