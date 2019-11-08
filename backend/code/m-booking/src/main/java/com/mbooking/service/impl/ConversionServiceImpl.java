package com.mbooking.service.impl;

import com.mbooking.model.ManifestationType;
import com.mbooking.service.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConversionServiceImpl implements ConversionService {

    public <T> Set<T> convertListToSet(List<T> list) {

        Set<T> set = new HashSet<>();
        set.addAll(list);
        return set;
    }

    public ManifestationType convertStringToManifestType(String strType) {

        try {
            return ManifestationType.valueOf(strType.toUpperCase());
        } catch(IllegalArgumentException | NullPointerException ex) {
            return null;
        }
    }

}
