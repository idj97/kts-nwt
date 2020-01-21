package com.mbooking.dto;

import com.mbooking.model.ManifestationImage;
import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class ManifestationImageDTO {

    private Long id;

    @Size(max = Constants.IMAGE_NAME_LENGTH,
            message="Description can't contain more than " + Constants.IMAGE_NAME_LENGTH + " characters")
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
