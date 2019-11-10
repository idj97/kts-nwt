package com.mbooking.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class ReportDTO {
	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy.")
	private Date startDate;

	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy.")
	private Date endDate;

	private Long locationId;
	private Long manifestationId;

	public ReportDTO() {
		super();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getManifestationId() {
		return manifestationId;
	}

	public void setManifestationId(Long manifestationId) {
		this.manifestationId = manifestationId;
	}

}
