package com.mbooking.service;

import java.util.List;
import java.util.Set;

public interface ConversionService {

    //converts any type of list to set
    <T> Set<T> convertListToSet(List<T> list);
}
