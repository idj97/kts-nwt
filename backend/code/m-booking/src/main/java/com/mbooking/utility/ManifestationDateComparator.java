package com.mbooking.utility;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ManifestationDateComparator implements Comparator<Date> {

    /** Compares dates without their time */

    private SimpleDateFormat sdf;

    public ManifestationDateComparator() {
        this.sdf = new SimpleDateFormat("dd.MM.yyyy.");
    }

    public int compare(Date d1, Date d2) {
        return sdf.format(d1).compareTo(sdf.format(d2));
    }
}
