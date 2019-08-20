package com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses;

import java.util.Date;

public class Assessment {

    private int assessmentId;
    private int courseId;
    private int termId;
    private String assessmentName;
    private String assessmentType;
    private Date dueDate;
    private Boolean dueDateAlert = false;
    private String note = "";
    private static int nextId = 1;
    public Assessment() {
    }

    public Assessment(int courseId, int termId, String assessmentName, String assessmentType, Date dueDate, String note, Boolean dueDateAlert) {
        this.assessmentId = nextId++;
        this.courseId = courseId;
        this.termId = termId;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.dueDate = dueDate;
        this.note = note;
        this.dueDateAlert = dueDateAlert;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {this.assessmentId = assessmentId;}

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getTermId() { return termId; }

    public void setTermId(int termId) { this.termId = termId; }

    public String getAssessmentName() { return assessmentName; }

    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDueDateAlert() {
        return dueDateAlert;
    }

    public void setDueDateAlert(Boolean dueDateAlert) {
        this.dueDateAlert = dueDateAlert;
    }
}
