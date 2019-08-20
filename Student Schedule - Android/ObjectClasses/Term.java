package com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.ObjectToSQLConverter;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "terms")
public class Term {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "term_id")
    private int id;
    @ColumnInfo(name = "term_name")
    private String termName;
    @TypeConverters(ObjectToSQLConverter.class)
    private ArrayList<Course> courseList = new ArrayList<>();
    private Date startDate;
    private Date endDate;

    @Ignore
    public Term() {
    }

    @Ignore
    public Term(int termId) {
        this.id = termId;
    }

    @Ignore
    public Term(String termName, Date startDate, Date endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Term(int id, String termName, Date startDate, Date endDate) {
        this.id = id;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public Term(String termName, ArrayList<Course> courseList, Date startDate, Date endDate) {
        this.termName = termName;
        this.courseList = courseList;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Ignore
    public Term(int id, String termName, ArrayList<Course> courseList, Date startDate, Date endDate) {
        this.id = id;
        this.termName = termName;
        this.courseList = courseList;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public StringBuilder getCourseNameList() {
        StringBuilder sb = new StringBuilder("");
        if(courseList.size()>0){

            for (Course course : courseList){
                sb.append(course.getCourseName()+"\n");
            }
        } else {
            sb.append("Add Courses!");
        }
        return sb;
    }

    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", termName='" + termName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public boolean removeOlderCourse(int courseId){
        for (Course course:courseList) {
            if(course.getCourseId()==courseId){
                courseList.remove(course);
                return true;
            }
        }
        return false;
    }
}
