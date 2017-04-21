package com.poos.cop.schedulebuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;

public class Section implements Comparable<Section> {
    public String term;
    public String name, courseName, status, session;
    public String subjectStr, courseNumberStr;
    public String keyDescription;
    public Course course;
    public int nbr;
    public int units;
    public String unitsStr;
    public String instructionMode;
    public String classComponents;
    public String courseId;
    public String career;
    public String dates;
    public String grading;
    public String location;
    public String campus;
    
    public String meetingTimes;
    public String room;
    public String instructorStr;
    public String meetingDates;
    
    public String attributesStr;
    
    public int capacity;
    public int enrollmentTotal;
    public int availableSeats;
    public int waitlistCapacity;
    public int waitlistTotal;
    
    public String notes;
    public String description;
    
    public boolean isLab = false;
    public boolean isHonors = false;
    public ArrayList<String> instructors;
    public ArrayList<String> attributes;

    public boolean inCart = false;

    public String[] dayStartTimes, dayEndTimes;

    public Section(Tag t) {
        term = "";
        name = getString(t, "DERIVED_CLSRCH_DESCR200", "");
        status = getString(t, "SSR_CLS_DTL_WRK_SSR_DESCRSHORT", "");
        session = getString(t, "PSXLATITEM_XLATLONGNAME$31$", "");
        keyDescription = getString(t, "DERIVED_CLSRCH_SSS_PAGE_KEYDESCR", "");
        instructionMode = getString(t, "INSTRUCT_MODE_DESCR", "");
        classComponents = getString(t, "win1divSSR_CLS_DTL_WRK_SSR_COMPONENT_LONG", "");
        courseId = getString(t, "SSR_CLS_DTL_WRK_CRSE_ID", "");
        career = getString(t, "PSXLATITEM_XLATLONGNAME", "");
        dates = getString(t, "SSR_CLS_DTL_WRK_SSR_DATE_LONG", "");
        grading = getString(t, "GRADE_BASIS_TBL_DESCRFORMAL", "");
        location = getString(t, "CAMPUS_LOC_VW_DESCR", "");
        campus = getString(t, "CAMPUS_TBL_DESCR", "");
        meetingTimes = getString(t, "MTG_SCHED$0", "");
        room = getString(t, "MTG_LOC$0", "");
        instructorStr = getString(t, "MTG_INSTR$0", "");
        meetingDates = getString(t, "MTG_DATE$0", "");
        
        nbr = getInt(t, "SSR_CLS_DTL_WRK_CLASS_NBR", -1);
        unitsStr = getString(t, "SSR_CLS_DTL_WRK_UNITS_RANGE", "");
        
        attributesStr = getString(t, "SSR_CLS_DTL_WRK_SSR_CRSE_ATTR_LONG", "");
        
        capacity = getInt(t, "SSR_CLS_DTL_WRK_ENRL_CAP", -1);
        enrollmentTotal = getInt(t, "SSR_CLS_DTL_WRK_ENRL_TOT", -1);
        availableSeats = getInt(t, "SSR_CLS_DTL_WRK_AVAILABLE_SEATS", -1);
        waitlistCapacity = getInt(t, "SSR_CLS_DTL_WRK_WAIT_CAP", -1);
        waitlistTotal = getInt(t, "SSR_CLS_DTL_WRK_WAIT_TOT", -1);
        
        notes = getString(t, "DERIVED_CLSRCH_SSR_CLASSNOTE_LONG", "");
        description = getString(t, "DERIVED_CLSRCH_DESCRLONG", "");
        
        {
            name = name.replace("\n", " - ");

            String[] tmp = name.split(" ");
            if(tmp.length >= 2) {
                subjectStr = tmp[0];
                courseNumberStr = tmp[1];
            } else {
                subjectStr = "";
                courseNumberStr = "";
            }

            tmp = name.split("   ");
            if(tmp.length == 2)
                courseName = tmp[1];
            else
                courseName = name;
        }
        
        {
            String[] tmp = unitsStr.split(" ");
            units = -1;
            try {
                if(tmp.length >= 1 && tmp[0] != null) {
                    units = Integer.parseInt(tmp[0]);
                }
            } catch(Exception e) {
                
            }
        }
        
        if(units == 0) {
            isLab = true;
        }
        
        {
            instructors = new ArrayList<>();
            instructorStr = instructorStr.replace("\n", " ");
            if(!instructorStr.equals("Staff")) {
                String[] tmp = instructorStr.split(", ");
                for(String a : tmp)
                    instructors.add(a.trim().replace("\n", ""));
            }
        }
        {
            attributes = new ArrayList<>();
            String[] tmp = attributesStr.split("\n");
            for(String a : tmp)
                attributes.add(a.trim().replace("\n", ""));
        }
        
        isHonors = attributes.contains("Honors Classes");

        {
            dayStartTimes = new String[5];
            dayEndTimes = new String[5];
            try {
                Scanner scanner = new Scanner(meetingTimes);
                String days = scanner.next();
                String startTime = scanner.next();
                scanner.next();
                String endTime = scanner.next();
                String[] dayArr = new String[]{"Mo", "Tu", "We", "Th", "Fr"};
                for(int i = 0; i < 5; i++) {
                    if(days.contains(dayArr[i])) {
                        dayStartTimes[i] = startTime;
                        dayEndTimes[i] = endTime;
                    }
                }
            } catch (Exception e) {
                dayStartTimes = new String[5];
                dayEndTimes = new String[5];
            }
        }

        updateInCartStatus();
    }

    public Section(String jsonStr) {
    	Gson gson = new Gson();
    	@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) gson.fromJson(jsonStr, HashMap.class);
        term = getString(map, "term", "");
    	name = getString(map, "name", "");
        status = getString(map, "status", "");
        session = getString(map, "session", "");
        keyDescription = getString(map, "keyDescription", "");
        instructionMode = getString(map, "instructionMode", "");
        classComponents = getString(map, "classComponents", "");
        courseId = getString(map, "courseId", "");
        career = getString(map, "career", "");
        dates = getString(map, "dates", "");
        grading = getString(map, "grading", "");
        location = getString(map, "location", "");
        campus = getString(map, "campus", "");
        meetingTimes = getString(map, "meetingTimes", "");
        room = getString(map, "room", "");
        instructorStr = getString(map, "instructorStr", "");
        meetingDates = getString(map, "meetingDates", "");
        
        nbr = getInt(map, "nbr", -1);
        unitsStr = getString(map, "unitsStr", "");
        
        attributesStr = getString(map, "attributesStr", "");
        
        capacity = getInt(map, "capacity", -1);
        enrollmentTotal = getInt(map, "enrollmentTotal", -1);
        availableSeats = getInt(map, "availableSeats", -1);
        waitlistCapacity = getInt(map, "waitlistCapacity", -1);
        waitlistTotal = getInt(map, "waitlistTotal", -1);
        
        notes = getString(map, "notes", "");
        description = getString(map, "description", "");
        
        {
            name = name.replace("\n", " - ");

            String[] tmp = name.split(" ");
            if(tmp.length >= 2) {
                subjectStr = tmp[0];
                courseNumberStr = tmp[1];
            } else {
                subjectStr = "";
                courseNumberStr = "";
            }

            tmp = name.split("   ");
            if(tmp.length == 2)
                courseName = tmp[1];
            else
                courseName = name;
        }
        
        {
            String[] tmp = unitsStr.split(" ");
            units = -1;
            try {
                if(tmp.length >= 1 && tmp[0] != null) {
                    units = Integer.parseInt(tmp[0]);
                }
            } catch(Exception e) {
                
            }
        }
        
        if(units == 0) {
            isLab = true;
        }
        
        {
            instructors = new ArrayList<>();
            if(!instructorStr.equals("Staff")) {
                String[] tmp = instructorStr.split(",\n");
                for(String a : tmp)
                    instructors.add(a.trim().replace("\n", ""));
            }
        }
        {
            attributes = new ArrayList<>();
            String[] tmp = attributesStr.split("\n");
            for(String a : tmp)
                attributes.add(a.trim().replace("\n", ""));
        }
        
        isHonors = attributes.contains("Honors Classes");

        {
            dayStartTimes = new String[5];
            dayEndTimes = new String[5];
            try {
                Scanner scanner = new Scanner(meetingTimes);
                String days = scanner.next();
                String startTime = scanner.next();
                scanner.next();
                String endTime = scanner.next();
                String[] dayArr = new String[]{"Mo", "Tu", "We", "Th", "Fr"};
                for(int i = 0; i < 5; i++) {
                    if(days.contains(dayArr[i])) {
                        dayStartTimes[i] = startTime;
                        dayEndTimes[i] = endTime;
                    }
                }
            } catch (Exception e) {
                dayStartTimes = new String[5];
                dayEndTimes = new String[5];
            }
        }

        updateInCartStatus();
    }

    public static String getString(Tag t, String id, String d) {
    	Tag tmp = t.findById(id);
    	if(tmp == null) return d;
    	String str = cleanString(tmp.contents);
    	return str;
    }
    
    public static int getInt(Tag t, String id, int d) {
    	Tag tmp = t.findById(id);
    	if(tmp == null) return d;
    	String str = tmp.contents;
    	try {
    		int val = Integer.parseInt(str);
    		return val;
    	} catch(Exception e) {
    		
    	}
    	return d;
    }
    
    public static String getString(HashMap<String, String> map, String key, String d) {
    	String str = map.get(key);
    	if(str == null) return d;
    	return cleanString(str);
    }
    
    public static int getInt(HashMap<String, String> map, String key, int d) {
    	String str = map.get(key);
    	if(str == null) return d;
    	try {
    		int val = Integer.parseInt(str);
    		return val;
    	} catch(Exception e) {
    		
    	}
    	return d;
    }

    public void updateInCartStatus() {
        if(ClassUtils.CART == null) return;
        inCart = ClassUtils.CART.contains(new SectionPair(this));
    }

    public boolean isSameCourse(Section s) {
        String tmpCourseNumberStr = courseNumberStr;
        if(courseNumberStr.length() > 0) {
            char c = courseNumberStr.charAt(courseNumberStr.length() - 1);
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                tmpCourseNumberStr = courseNumberStr.substring(0, courseNumberStr.length() - 1);
        }

        String tmpCourseNumberStr2 = s.courseNumberStr;
        if(s.courseNumberStr.length() > 0) {
            char c = s.courseNumberStr.charAt(s.courseNumberStr.length() - 1);
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                tmpCourseNumberStr2 = s.courseNumberStr.substring(0, s.courseNumberStr.length() - 1);
        }

        if(term.equals(s.term) && tmpCourseNumberStr.equals(tmpCourseNumberStr2))
            return true;

        return false;
    }

    public boolean conflictsWith(Section s) {
        if(isSameCourse(s))
            return true;


        for(int i = 0; i < 5; i++) {
            if(dayStartTimes[i] == null || s.dayStartTimes[i] == null)
                continue;
            if(StringUtils.compareTimes(dayEndTimes[i], s.dayStartTimes[i]) < 0)
                continue;
            if(StringUtils.compareTimes(s.dayEndTimes[i], dayStartTimes[i]) < 0)
                continue;
            return true;
        }

        return false;
    }

    public boolean matches(ScheduleQuery scheduleQuery) {
        if(scheduleQuery.getTerm() != null && scheduleQuery.getTerm().length() > 0 && !StringUtils.matches(scheduleQuery.getTerm(), term))
            return false;
        if(scheduleQuery.isOpenOnly() && (availableSeats == 0 || StringUtils.matches(status, "closed")))
            return false;
        for(int i = 0; i < 5; i++) {
            if(dayStartTimes[i] != null && scheduleQuery.getEarliestStartTime(i) != null && StringUtils.compareTimes(dayStartTimes[i], scheduleQuery.getEarliestStartTime(i)) < 0)
                return false;
            if(dayEndTimes[i] != null && scheduleQuery.getLatestEndTime(i) != null && StringUtils.compareTimes(dayEndTimes[i], scheduleQuery.getLatestEndTime(i)) > 0)
                return false;
        }

        return true;
    }

    public boolean matches(SearchQuery searchQuery) {
        if(!searchQuery.getIncludeLabs() && isLab)
            return false;

        if(searchQuery.getOpenClassesOnly() && (availableSeats == 0 || StringUtils.matches(status, "closed")))
            return false;

        if(searchQuery.getTerm().length() > 0 && !StringUtils.matches(searchQuery.getTerm(), term))
            return false;
        if(searchQuery.getSubject().length() > 0 && !StringUtils.matches(searchQuery.getSubject(), subjectStr))
            return false;
        if(searchQuery.getNbr().length() > 0 && !StringUtils.matches(searchQuery.getNbr(), "" + nbr))
            return false;
        if(searchQuery.getCourseNumber().length() > 0 && !StringUtils.matches(searchQuery.getCourseNumber(), courseNumberStr))
            return false;
        if(searchQuery.getCourseName().length() > 0 && !StringUtils.approximatelyTokenMatches(searchQuery.getCourseName(), courseName))
            return false;
        if(searchQuery.getLocation().length() > 0 && !StringUtils.matches(searchQuery.getLocation(), location))
            return false;
        if(searchQuery.getModeOfInstruction().length() > 0 && !StringUtils.matches(searchQuery.getModeOfInstruction(), instructionMode))
            return false;

        boolean instructorFirstNameMatches = searchQuery.getInstructorFirstName().length() == 0;
        for(String instructorStr : instructors) {
            if(instructorFirstNameMatches) break;
            String[] tokens = instructorStr.split(" ");
            String str = "";
            for(int i = 0; i < tokens.length; i++) {
                str = str + (i > 0 ? " " : "") + tokens[i];
                if(StringUtils.approximatelyMatches(searchQuery.getInstructorFirstName(), str)) {
                    instructorFirstNameMatches = true;
                    break;
                }
            }
        }

        if(!instructorFirstNameMatches)
            return false;

        boolean instructorLastNameMatches = searchQuery.getInstructorLastName().length() == 0;
        for(String instructorStr : instructors) {
            if(instructorLastNameMatches) break;
            String[] tokens = instructorStr.split(" ");
            String str = "";
            for(int i = tokens.length - 1; i > 0; i--) {
                str = tokens[i] + (i < tokens.length - 1 ? " " : "") + str;
                if(StringUtils.approximatelyMatches(searchQuery.getInstructorLastName(), str)) {
                    instructorLastNameMatches = true;
                    break;
                }
            }
        }

        if(!instructorLastNameMatches)
            return false;

        return true;
    }
    
    public String toString() {
        return String.format("{\n   Term: %s\n   Name: %s\n   Type: %s\n   Honors: %s\n   Status: %s\n   NBR: %d\n   Session: %s\n   Units: %s [%d]\n   InstructionMode: %s\n   Class Components: %s\n   Course Id: %s\n   Career: %s\n   Dates: %s\n   Grading: %s\n   Location: %s\n   Campus: %s\n   Meeting Times: %s\n   Room: %s\n   Instructors: %s\n   Meeting Dates: %s\n   Attributes: %s\n   Capacity: %d\n   Enrollment Total: %d\n   Available Seats: %d\n   Waitlist Capacity: %d\n   Waitlist Total: %d\n   Notes: %s\n   Description: %s\n}", term, name, isLab ? "LAB" : "LEC", isHonors, status, nbr, session, unitsStr, units, instructionMode, classComponents, courseId, career, dates, grading, location, campus, meetingTimes, room, instructors, meetingDates, attributes, capacity, enrollmentTotal, availableSeats, waitlistCapacity, waitlistTotal, notes, description);
    }

    public int compareTo(Section s) {
        int res = subjectStr.compareTo(s.subjectStr);
        if(res == 0)
            res = courseNumberStr.compareTo(s.courseNumberStr);
        if(res == 0)
            res = courseName.compareTo(s.courseName);
        if(res == 0)
            res = instructionMode.compareTo(s.instructionMode);
        if(res == 0)
            res = instructorStr.compareTo(s.instructorStr);
        if(res == 0)
            res = nbr < s.nbr ? -1 : (nbr > s.nbr ? 1 : 0);
        return res;
    }
    
    public static String toJson(Section s) {
    	Gson gson = new Gson();
    	HashMap<String, String> map = new HashMap<>();
        map.put("term", s.term);
    	map.put("name", s.name);
    	map.put("status", s.status);
    	map.put("session", s.session);
    	map.put("subjectStr", s.subjectStr);
    	map.put("courseNumberStr", s.courseNumberStr);
    	map.put("keyDescription", s.keyDescription);
    	map.put("nbr", "" + s.nbr);
    	map.put("unitsStr", s.unitsStr);
    	map.put("instructionMode", s.instructionMode);
    	map.put("classComponents", s.classComponents);
    	map.put("courseId", s.courseId);
    	map.put("career", s.career);
    	map.put("dates", s.dates);
    	map.put("grading", s.grading);
    	map.put("location", s.location);
    	map.put("campus", s.campus);
    	map.put("meetingTimes", s.meetingTimes);
    	map.put("room", s.room);
    	map.put("instructorStr", s.instructorStr);
    	map.put("meetingDates", s.meetingDates);
    	map.put("attributesStr", s.attributesStr);
    	map.put("capacity", "" + s.capacity);
    	map.put("enrollmentTotal", "" + s.enrollmentTotal);
    	map.put("availableSeats", "" + s.availableSeats);
    	map.put("waitlistCapacity", "" + s.waitlistCapacity);
    	map.put("waitlistTotal", "" + s.waitlistTotal);
    	map.put("notes", s.notes);
    	map.put("description", s.description);
    	return gson.toJson(map);
    }

    public static String cleanString(String str) {
        str = str.replace("&nbsp;", " ");
        str = str.replace("&quot;", "\"");
        str = str.replace("&#039;", "'");
        return str;
    }


}
