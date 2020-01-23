package io.nessus.weka.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {

    public static final SimpleDateFormat TSTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        TSTAMP_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Hide ctor
    private DateUtils() {};
    
    public static String format(Date tstamp) {
        return TSTAMP_FORMAT.format(tstamp);
    }

    public static String format(Date tstamp, boolean dateOnly) {
        SimpleDateFormat sdf = dateOnly ? new SimpleDateFormat("yyyy-MM-dd") : TSTAMP_FORMAT;
        return sdf.format(tstamp);
    }

	public static String format(TimeRange trange) {
		return format(trange, false);
	}
	
	public static String format(TimeRange trange, boolean dateOnly) {
		String tstart = format(trange.getStartTime(), dateOnly);
        String tend = format(trange.getEndTime(), dateOnly);
        return String.format("[%s - %s]", tstart, tend);
	}
	
    public static Date parse(String tstr) {
        try {
            if (tstr.contains(" "))
                return TSTAMP_FORMAT.parse(tstr);
            else
                return DATE_FORMAT.parse(tstr);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}