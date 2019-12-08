package com.mbooking.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.mbooking.model.Location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {
	private Long id;
	
	@NotNull(message = "Specify location name")
	private String name;
	
	@NotNull(message = "Specify location address")
	private String address;
	
	@NotNull(message = "Specify layout id")
	private Long layoutId;
	
	private List<Long> manifestationIds;

	public LocationDTO(Long id, String name, String address, Long layoutId, List<Long> manifestationIds) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.layoutId = layoutId;
		this.manifestationIds = manifestationIds;
	}

	public LocationDTO(Location location) {
		this.id = location.getId();
		this.name = location.getName();
		this.address = location.getAddress();
		this.layoutId = location.getLayout().getId();
		this.manifestationIds = location.getManifestations()
				.stream().map(mnfst -> mnfst.getId()).collect(Collectors.toList());
	}
}
