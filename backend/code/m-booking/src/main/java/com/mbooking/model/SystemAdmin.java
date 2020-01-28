package com.mbooking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SYSTEM_ADMIN")
@Getter
@Setter
@NoArgsConstructor
public class SystemAdmin extends User {
    private static final long serialVersionUID = 1L;
}
