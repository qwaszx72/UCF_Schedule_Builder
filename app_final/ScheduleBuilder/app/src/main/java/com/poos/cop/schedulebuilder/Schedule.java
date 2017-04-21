package com.poos.cop.schedulebuilder;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by timothy on 3/27/17.
 */

public class Schedule implements Comparable<Schedule> {
    private ScheduleQuery scheduleQuery;
    private ArrayList<Section> sections;
    private int numSectionsMissing;

    // The maximum number of schedules to return for a query
    public final static int MAX_SCHEDULES = 10;

    public Schedule(ScheduleQuery scheduleQuery, ArrayList<Section> sections) {
        this.scheduleQuery = scheduleQuery;
        this.sections = sections;
        this.numSectionsMissing = 0;
    }

    public Schedule(ScheduleQuery scheduleQuery, ArrayList<Section> sections, int numSectionsMissing) {
        this(scheduleQuery, sections);
        this.numSectionsMissing = numSectionsMissing;
    }

    public ScheduleQuery getScheduleQuery() {
        return scheduleQuery;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public boolean conflictsWith(Section section) {
        if(!section.matches(scheduleQuery))
            return true;
        for(Section s : sections)
            if(s.conflictsWith(section))
                return true;
        return false;
    }

    public boolean canAdd(Section section) {
        return !conflictsWith(section);
    }

    public void setNumRequestedCourses(int numRequestedCourses) {
        this.numSectionsMissing = Math.max(0, numRequestedCourses - getNumSections());
    }

    public String getScheduleInfoString() {
        int numSections = getNumSections();
        if(numSectionsMissing == 0)
            return "Schedule with all requested classes";
        return String.format("Schedule missing %d class%s", numSectionsMissing, numSectionsMissing != 1 ? "es" : "");
    }

    public String getStatusString() {
        if (sections == null || sections.size() == 0)
            return "All classes are open";
        int numOpen = 0;
        int numClosed = 0;
        int numWaitlist = 0;
        for (Section s : sections) {
            if (s.availableSeats == 0 || StringUtils.matches(s.status, "closed")) {
                if (s.waitlistCapacity > s.waitlistTotal)
                    numWaitlist++;
                else
                    numClosed++;
            } else {
                numOpen++;
            }
        }
        if(numClosed > 0)
            return "Some classes are closed";
        if(numWaitlist > 0)
            return "Some classes are waitlist only";
        return "All classes are open";
    }

    public String getInstructionModesString() {
        TreeSet<String> instructionModes = new TreeSet<>();
        if(sections != null)
            for(Section s : sections)
                instructionModes.add(s.instructionMode);
        StringBuilder res = new StringBuilder();
        for(String s : instructionModes) {
            if(res.length() > 0)
                res.append(", ");
            res.append(s);
        }
        return res.toString();
    }

    public String getLocationsString() {
        TreeSet<String> locations = new TreeSet<>();
        if(sections != null)
            for(Section s : sections)
                locations.add(s.location);
        StringBuilder res = new StringBuilder();
        for(String s : locations) {
            if(res.length() > 0)
                res.append(", ");
            res.append(s);
        }
        return res.toString();
    }

    public String getNbrsString() {
        if(sections == null || sections.size() == 0)
            return "";
        StringBuilder res = new StringBuilder();
        for(Section s : sections) {
            if(res.length() > 0)
                res.append(", ");
            res.append(s.nbr);
        }
        return res.toString();
    }

    public String getTerm() {
        if(sections == null || sections.size() == 0)
            return "";
        return sections.get(0).term;
    }

    public int getNumSections() {
        return sections == null ? 0 : sections.size();
    }

    public int getTotalUnits() {
        if(sections == null) return 0;
        int res = 0;
        for(Section s : sections)
            res += s.units;
        return res;
    }

    public int compareTo(Schedule schedule) {
        if(sections.size() == schedule.sections.size())
            return 0;
        return schedule.sections.size() - sections.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Section s : sections)
            sb.append(s.nbr + ", ");
        return String.format("{ Sections: %s }", sb.toString());
    }

    public static ArrayList<Schedule> buildSchedules(ScheduleQuery scheduleQuery, ArrayList<Section> tmpArr) {
        ArrayList<Section> sections = new ArrayList<>();
        Collections.sort(tmpArr);
        int numOrigDifferentCourses = tmpArr.size() > 0 ? 1 : 0;
        for(int i = 1; i < tmpArr.size(); i++)
            if(!tmpArr.get(i).isSameCourse(tmpArr.get(i - 1)))
                numOrigDifferentCourses++;

        for(Section section : tmpArr)
            if(section.matches(scheduleQuery))
                sections.add(section);

        ArrayList<Schedule> schedules = new ArrayList<>();

        ArrayList<Section> curScheduleArr = new ArrayList<>();

        int numDifferentCourses = sections.size() > 0 ? 1 : 0;
        for(int i = 1; i < sections.size(); i++)
            if(!sections.get(i).isSameCourse(sections.get(i - 1)))
                numDifferentCourses++;

        buildSchedulesRecursive(sections, 0, curScheduleArr, scheduleQuery, schedules);

        Collections.sort(schedules);

        ArrayList<Schedule> res = new ArrayList<>();
        for(int i = 0; i < schedules.size() && i < MAX_SCHEDULES; i++) {
            if(schedules.get(i).sections.size() < Math.max(1, numDifferentCourses - 1))
                break;
            schedules.get(i).setNumRequestedCourses(numOrigDifferentCourses);
            res.add(schedules.get(i));
        }

        return res;
    }

    public static void buildSchedulesRecursive(ArrayList<Section> sections, int curSection, ArrayList<Section> curScheduleArr, ScheduleQuery scheduleQuery, ArrayList<Schedule> res) {
        if(curSection == sections.size()) {
            res.add(new Schedule(scheduleQuery, (ArrayList<Section>) curScheduleArr.clone()));
            return;
        }
        Section nextSection = sections.get(curSection);
        boolean canAdd = true;
        for(int i = curScheduleArr.size() - 1; canAdd && i >= 0; i--) {
            canAdd &= !curScheduleArr.get(i).conflictsWith(nextSection);
        }
        if(canAdd) {
            curScheduleArr.add(nextSection);
            buildSchedulesRecursive(sections, curSection + 1, curScheduleArr, scheduleQuery, res);
            curScheduleArr.remove(curScheduleArr.size() - 1);
        }
        buildSchedulesRecursive(sections, curSection + 1, curScheduleArr, scheduleQuery, res);
    }
}
