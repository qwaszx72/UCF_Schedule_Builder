package com.poos.cop.schedulebuilder;

/**
 * Created by timothy on 3/27/17.
 */

public class SearchQuery {
    private String term;
    private String nbr;
    private String subject;
    private String courseNumber;
    private String courseName;
    private String location;
    private String modeOfInstruction;
    private String instructorFirstName;
    private String instructorLastName;
    private boolean openClassesOnly;
    private boolean includeLabs;

    public SearchQuery() {

    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getNbr() {
        return nbr;
    }

    public void setNbr(String nbr) {
        this.nbr = nbr;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModeOfInstruction() {
        return modeOfInstruction;
    }

    public void setModeOfInstruction(String modeOfInstruction) {
        this.modeOfInstruction = modeOfInstruction;
    }

    public String getInstructorFirstName() {
        return instructorFirstName;
    }

    public void setInstructorFirstName(String instructorFirstName) {
        this.instructorFirstName = instructorFirstName;
    }

    public String getInstructorLastName() {
        return instructorLastName;
    }

    public void setInstructorLastName(String instructorLastName) {
        this.instructorLastName = instructorLastName;
    }

    public boolean getOpenClassesOnly() {
        return openClassesOnly;
    }

    public void setOpenClassesOnly(boolean openClassesOnly) {
        this.openClassesOnly = openClassesOnly;
    }

    public boolean getIncludeLabs() {
        return includeLabs;
    }

    public void setIncludeLabs(boolean includeLabs) {
        this.includeLabs = includeLabs;
    }

    public String toString() {
        return String.format("{ Term: %s, Subject: %s, Course Number: %s, Course Name: %s, Location: %s, Mode of Instruction: %s, Instructor First Name: %s, Instructor Last Name: %s, Show Open Classes Only: %s }", term, subject, courseNumber, courseName, location, modeOfInstruction, instructorFirstName, instructorLastName, "" + openClassesOnly);
    }
}
