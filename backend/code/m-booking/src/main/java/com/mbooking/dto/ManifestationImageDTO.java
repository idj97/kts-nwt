package com.mbooking.dto;

import com.mbooking.model.ManifestationImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ManifestationImageDTO {

    private Long id;

    private String name;

    private String type;

    private byte[] image;

    public ManifestationImageDTO(ManifestationImage manifestImg) {

        this.id = manifestImg.getId();
        this.name = manifestImg.getName();
        this.type = manifestImg.getType();
        this.image = manifestImg.getImage();
    }

}
