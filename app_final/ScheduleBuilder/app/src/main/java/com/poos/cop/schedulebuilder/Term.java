package com.poos.cop.schedulebuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Term {
	public HashMap<String, Subject> subjectsMap;
	public ArrayList<Subject> subjects;
	public int id;
    
    public Term(int id) {
    	this.id = id;
        subjectsMap = new HashMap<>();
        subjects = new ArrayList<>();
    }
    
    public Subject getSubject(String code) {
        return subjectsMap.get(code);
    }
    
    public Section findSection(int nbr) {
        for(Subject s : subjects) {
            Section tmp = s.findSection(nbr);
            if(tmp != null) return tmp;
        }
        return null;
    }
    
    public void addSubject(Subject s) {
        if(subjectsMap.containsKey(s.code)) return;
        subjectsMap.put(s.code, s);
        subjects.add(s);
    }
    
    public void processSection(Section s) {
        String code = s.subjectStr;
        if(!subjectsMap.containsKey(code)) {
            addSubject(new Subject(this, code));
        }
        subjectsMap.get(code).processSection(s);
    }
}
