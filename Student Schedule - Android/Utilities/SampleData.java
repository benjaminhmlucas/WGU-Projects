package com.benjaminlucaswebdesigns.studentprogresstracker.Utilities;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.sampleTermId;

public class SampleData {
    public static ArrayList<Term> getTerms() {
        ArrayList<Term> terms = new ArrayList<>();
        ArrayList<Course> courseListFall = new ArrayList<>();
        ArrayList<Course> courseListSpring = new ArrayList<>();
        ArrayList<Course> courseListSummer = new ArrayList<>();
        Date sDateFall = new Date("2019/08/02 09:00:00");
        Date eDateFall = new Date("2019/12/01 09:00:00");
        Date sDateSpring = new Date("2020/01/01 09:00:00");
        Date eDateSpring = new Date("2020/5/01 09:00:00");
        Date sDateSummer = new Date("2020/06/01 09:00:00");
        Date eDateSummer = new Date("2020/08/01 09:00:00");
        Term fallTerm = new Term(sampleTermId++,"Fall Term",courseListFall,sDateFall,eDateFall);
        Term springTerm = new Term(sampleTermId++,"Spring Term",courseListSpring,sDateSpring,eDateSpring);
        Term summerTerm = new Term(sampleTermId++,"Summer Term",courseListSummer,sDateSummer,eDateSummer);
        courseListFall.addAll(getCourses(fallTerm.getId(),fallTerm.getStartDate(),fallTerm.getEndDate()));
        courseListSpring.addAll(getCourses(springTerm.getId(),springTerm.getStartDate(),springTerm.getEndDate()));
        courseListSummer.addAll(getCourses(summerTerm.getId(),summerTerm.getStartDate(),summerTerm.getEndDate()));
        terms.add(fallTerm);
        terms.add(springTerm);
        terms.add(summerTerm);
        return terms;
    }

    public static List<Course> getCourses(int termId,Date sDate, Date eDate) {
        ArrayList<Course> courseList = new ArrayList<>();

        Course c1 = new Course(termId,"Bio101",sDate,eDate,"Complete", "Professor Plumb", "421-121-2231", "PPlumb@uni.edu");
        Course c2 = new Course(termId,"Math101",sDate,eDate,"In Progress", "Professor Green", "421-121-2231", "PGreen@uni.edu");
        Course c3 = new Course(termId,"HardMath401",sDate,eDate,"Complete", "Professor Yellow", "421-121-2231", "PYellow@uni.edu");
        c1.setAssessmentList((ArrayList<Assessment>) getAssessments(c1.getCourseId(),termId,eDate));
        c2.setAssessmentList((ArrayList<Assessment>) getAssessments(c2.getCourseId(),termId,eDate));
        c3.setAssessmentList((ArrayList<Assessment>) getAssessments(c3.getCourseId(),termId,eDate));
        courseList.add(c1);
        courseList.add(c2);
        courseList.add(c3);
        return courseList;
    }

    public static List<Assessment> getAssessments(int courseId,int termId,Date dueDate){
        ArrayList<Assessment> assessmentList = new ArrayList<>();
        assessmentList.add(new Assessment(courseId,termId,"Assessment-1P","Performance",dueDate,"You will perform!",false));
        assessmentList.add(new Assessment(courseId,termId,"Assessment-2O","Objective",dueDate,"You will object!",false));
        return assessmentList;
    }
}

