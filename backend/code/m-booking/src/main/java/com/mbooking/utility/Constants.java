package com.mbooking.utility;

public class Constants {

	public static final String DEFAULT_CURRENCY = "USD";
	
    public static final int DESCR_LENGTH = 200;
    public static final int NAME_LENGTH = 100;

    public static final String MANIFEST_NOT_FOUND_MSG = "Manifestation not found";
    public static final String LOCATION_NOT_FOUND_MSG = "Location not found";
    public static final String SECTION_NOT_FOUND_MSG = "Section not found";

    public static final String FUTURE_DATES_MSG = "All dates must be future dates";
    public static final String INVALID_RESERV_DAY_MSG = "The last day of reservation must be before manifestation dates";
    public static final String CONFLICTING_MANIFEST_DAY_MSG = "Can't have more than one manifestation in the same location at the same time";
}
