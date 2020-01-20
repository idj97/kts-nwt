package com.mbooking.model;

import com.mbooking.utility.Constants;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class ManifestationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = Constants.NAME_LENGTH)
    private String name;

    @Column
    private String type;

    @Lob
    @Column
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Manifestation manifestation;


    public ManifestationImage(String name, String type, byte[] image) {
        this.name = name;
        this.type = type;
        this.image = image;
    }


}
