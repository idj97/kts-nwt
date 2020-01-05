package com.mbooking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static Date getDate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy mm:hh");
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
