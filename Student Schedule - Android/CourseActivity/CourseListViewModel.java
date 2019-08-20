package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.ArrayList;
import java.util.List;

class CourseListViewModel extends AndroidViewModel {

    public LiveData<List<Term>> mTerms;
    private AppRepository mRepository;

    public CourseListViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }

    public void saveCoursesToTerm(Term term, List<Course> coursesData) {
        ArrayList<Course> cList = new ArrayList<>();
        for (Course course : coursesData) {
            cList.add(course);
        }
        term.setCourseList(cList);
        mRepository.insertTerm(term);
    }

}