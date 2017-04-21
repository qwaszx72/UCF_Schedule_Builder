package com.poos.cop.schedulebuilder;

import java.util.Arrays;

/**
 * Created by timothy on 3/27/17.
 */

public class ScheduleQuery {
    private String term;
    private String[] earliestStartTimes, latestEndTimes;
    private boolean openOnly;

    public ScheduleQuery() {
        earliestStartTimes = new String[5];
        latestEndTimes = new String[5];
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setEarliestStartTime(int day, String timeStr) {
        earliestStartTimes[day] = timeStr;
    }

    public String getEarliestStartTime(int day) {
        return earliestStartTimes[day];
    }

    public void setLatestEndTime(int day, String timeStr) {
        latestEndTimes[day] = timeStr;
    }

    public String getLatestEndTime(int day) {
        return latestEndTimes[day];
    }

    public void setOpenOnly(boolean openOnly) {
        this.openOnly = openOnly;
    }

    public boolean isOpenOnly() {
        return openOnly;
    }

    public String toString() {
        return String.format("{ Term: %s, Earliest Start Times: %s, Latest End Times: %s }", term, Arrays.toString(earliestStartTimes), Arrays.toString(latestEndTimes));
    }
}
