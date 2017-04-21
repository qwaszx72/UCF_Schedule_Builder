package com.poos.cop.schedulebuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Subject {
	public String code;
	public Term term;
	public HashMap<String, Course> coursesMap;
	public ArrayList<Course> courses;
	
    public Subject(Term term, String code) {
        this.term = term;
        this.code = code;
        coursesMap = new HashMap<>();
        courses = new ArrayList<>();
    }
    
    public Course getCourse(String courseNumber) {
        return coursesMap.get(courseNumber);
    }
    
    public Section findSection(int nbr) {
        for(Course c : courses) {
            Section tmp = c.findSection(nbr);
            if(tmp != null) return tmp;
        }
        return null;
    }
    
    public void addCourse(Course c) {
        if(coursesMap.containsKey(c.courseNumber)) return;
        coursesMap.put(c.courseNumber, c);
        courses.add(c);
    }
    
    public void processSection(Section s) {
        String courseNumber = s.courseNumberStr;
        if(!coursesMap.containsKey(courseNumber)) {
            addCourse(new Course(this, courseNumber));
        }
        coursesMap.get(courseNumber).processSection(s);
    }
}
