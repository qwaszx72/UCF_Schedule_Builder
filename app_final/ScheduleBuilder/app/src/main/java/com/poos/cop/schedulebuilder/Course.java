package com.poos.cop.schedulebuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
	public Subject subject;
	public String courseNumber;
	public ArrayList<Section> sections;
	public HashMap<Integer, Section> sectionsMap;
    
    public Course(Subject subject, String courseNumber) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        sections = new ArrayList<>();
        sectionsMap = new HashMap<>();
    }
    
    public Section getSection(int nbr) {
        return sectionsMap.get(nbr);
    }
    
    public Section findSection(int nbr) {
        return getSection(nbr);
    }
    
    public void addSection(Section s) {
        sections.add(s);
        sectionsMap.put(s.nbr, s);
    }
    
    public void processSection(Section s) {
        addSection(s);
        s.course = this;
    }
}
