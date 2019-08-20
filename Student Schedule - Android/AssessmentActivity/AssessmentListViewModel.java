package com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AssessmentListViewModel extends AndroidViewModel {

    public LiveData<List<Term>> mTerms;
    private AppRepository mRepository;

    public AssessmentListViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }

    public void saveAssessmentsToCourse(Term term, int courseId, List<Assessment> assessmentsData) {
        ArrayList<Assessment> aList = new ArrayList<>();
        ArrayList<Course> cList = term.getCourseList();
        Course course = null;
        for (Assessment assessment : assessmentsData) {
            aList.add(assessment);
        }

        for(Course innerCourse:cList){
            if (courseId == innerCourse.getCourseId()){
                course = innerCourse;
            }
        }

        if (course != null){
            course.setAssessmentList(aList);
            term.removeOlderCourse(course.getCourseId());
            term.getCourseList().add(course);
            Collections.sort(term.getCourseList(), new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            });
        }
        mRepository.insertTerm(term);
    }

}

