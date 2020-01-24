package com.mbooking.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ManifestationDateComparator implements Comparator<Date> {

    /** Compares dates without their time */

    private SimpleDateFormat sdf;

    public ManifestationDateComparator() {
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    public int compare(Date d1, Date d2) {

        try {
            return sdf.parse(sdf.format(d1)).compareTo(sdf.parse(sdf.format(d2)));
        } catch(ParseException ex) {
            return 0;
        }

    }
}
