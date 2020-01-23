package io.nessus.weka.utils;

import java.text.ParseException;
import java.util.Date;

public class TimeRange {
    
    private final Date startTime;
    private final Date endTime;
    
    public TimeRange(String startTime, String endTime) throws ParseException {
        this(startTime != null ? DateUtils.parse(startTime) : null, endTime != null ? DateUtils.parse(endTime) : null);
    }
    
    public TimeRange(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = 31 * result + ((startTime == null) ? 0 : startTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TimeRange))return false;
        TimeRange other = (TimeRange) obj;
        if (endTime == null && other.endTime != null) return false; 
        if (endTime != null && !endTime.equals(other.endTime)) return false;
        if (startTime == null && other.startTime != null) return false; 
        if (startTime != null && !startTime.equals(other.startTime)) return false;
        return true;
    }

    public String toString() {
        String start = startTime != null ? DateUtils.format(startTime) : null;
        String end = endTime != null ? DateUtils.format(endTime) : null;
        if (start != null && end != null) {
        	int idx1 = start.indexOf(" 00:00:00");
        	int idx2 = end.indexOf(" 00:00:00");
        	if (idx1 > 0 && idx2 > 0) {
        		start = start.substring(0, idx1);
        		end = end.substring(0, idx2);
        	}
        }
        return String.format("[%s - %s]", start, end); 
    }
}