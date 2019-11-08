package com.mbooking.service;

import com.mbooking.model.ManifestationType;

import java.util.List;
import java.util.Set;

public interface ConversionService {

    //converts any type of list to set
    <T> Set<T> convertListToSet(List<T> list);

    ManifestationType convertStringToManifestType(String strType);
}
