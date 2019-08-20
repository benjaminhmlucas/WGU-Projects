package com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses;

import java.util.ArrayList;
import java.util.Date;


public class Course {

    private int courseId;
    private int termId;
    private String courseName;
    private Date startDate;
    private Date endDate;
    private Boolean eDateAlert = false, sDateAlert = false;
    private String courseStatus;
    private String courseMentorName;
    private String courseMentorPhone;
    private String courseMentorEmail;
    private String courseNote;
    private static int nextId = 1;
    private ArrayList<Assessment> assessmentList = new ArrayList<>();

    public Course() {
    }

    public Course(int courseId) {
        this.courseId = courseId;
    }

    public Course(String courseName, Date startDate, Date endDate, String courseStatus, String courseMentorName, String courseMentorPhone, String courseMentorEmail) {
        this.courseId = nextId++;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseStatus = courseStatus;
        this.courseMentorName = courseMentorName;
        this.courseMentorPhone = courseMentorPhone;
        this.courseMentorEmail = courseMentorEmail;
    }

    public Course(int termId, String courseName, Date startDate, Date endDate, String courseStatus, String courseMentorName, String courseMentorPhone, String courseMentorEmail) {
        this.courseId = nextId++;
        this.termId = termId;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseStatus = courseStatus;
        this.courseMentorName = courseMentorName;
        this.courseMentorPhone = courseMentorPhone;
        this.courseMentorEmail = courseMentorEmail;
        this.assessmentList = new ArrayList<>();
    }

    public Course(int termId, String courseName, Date startDate, Date endDate, String courseStatus, String courseMentorName, String courseMentorPhone, String courseMentorEmail, String courseNote,ArrayList<Assessment> assessmentList,Boolean sDateAlert, Boolean eDateAlert) {
        this.courseId = nextId++;
        this.termId = termId;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseStatus = courseStatus;
        this.courseMentorName = courseMentorName;
        this.courseMentorPhone = courseMentorPhone;
        this.courseMentorEmail = courseMentorEmail;
        this.courseNote = courseNote;
        this.assessmentList = assessmentList;
        this.sDateAlert = sDateAlert;
        this.eDateAlert = eDateAlert;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTermId() { return termId; }

    public void setTermId(int termId) { this.termId = termId; }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseMentorName() {
        return courseMentorName;
    }

    public void setCourseMentorName(String courseMentorName) {
        this.courseMentorName = courseMentorName;
    }

    public Boolean getEDateAlert() {
        return eDateAlert;
    }

    public void setEDateAlert(Boolean eDateAlert) {
        this.eDateAlert = eDateAlert;
    }

    public Boolean getSDateAlert() {
        return sDateAlert;
    }

    public void setSDateAlert(Boolean sDateAlert) {
        this.sDateAlert = sDateAlert;
    }

    public String getCourseMentorPhone() {
        return courseMentorPhone;
    }

    public void setCourseMentorPhone(String courseMentorPhone) {
        this.courseMentorPhone = courseMentorPhone;
    }

    public String getCourseMentorEmail() {
        return courseMentorEmail;
    }

    public void setCourseMentorEmail(String courseMentorEmail) {
        this.courseMentorEmail = courseMentorEmail;
    }

    public ArrayList<Assessment> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(ArrayList<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    public StringBuilder assessmentListToString(){
        StringBuilder assessments = new StringBuilder();
        if(assessmentList.size()>0){
            for (Assessment assessment:assessmentList) {
                assessments.append(assessment.getAssessmentName()+"\n");
            }
        } else {
            assessments.append("Add Assessments!");
        }
        return assessments;
    }

    public StringBuilder mentorToString(){
        StringBuilder mentorInfo = new StringBuilder();
        if(getCourseMentorName().trim().length()==0&&getCourseMentorPhone().trim().length()==0&&getCourseMentorEmail().trim().length()==0){
            mentorInfo.append("Add Mentor Info!");
        } else{
            mentorInfo.append(getCourseMentorName()).append("\n").append(getCourseMentorEmail()).append("\n").append(getCourseMentorPhone()).toString();
        }
        return mentorInfo;
    }

    public boolean removeOlderAssessment(int assessmentId){
        for (Assessment assessment:assessmentList) {
            if(assessment.getAssessmentId()==assessmentId){
                assessmentList.remove(assessment);
                return true;
            }
        }
        return false;
    }

}
