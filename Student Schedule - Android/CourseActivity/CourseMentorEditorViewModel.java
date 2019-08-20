package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseMentorEditorViewModel extends AndroidViewModel {
    public MutableLiveData<Course> mLiveCourse = new MutableLiveData<>();
    public MutableLiveData<Term> mLiveTermMentorEditor = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseMentorEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int courseId, final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTermMentorEditor.postValue(term);
                for (Course innerCourse : term.getCourseList()) {
                    if (innerCourse.getCourseId() == courseId) {
                        mLiveCourse.postValue(innerCourse);
                        return;
                    }
                }
                return;
            }
        });
    }


    public void saveCourse(String courseMentorName, String courseMentorPhone, String courseMentorEmail) {
        Course course = mLiveCourse.getValue();
        Term term = mLiveTermMentorEditor.getValue();
        course.setCourseMentorName(courseMentorName);
        course.setCourseMentorPhone(courseMentorPhone);
        course.setCourseMentorEmail(courseMentorEmail);
        term.removeOlderCourse(course.getCourseId());
        term.getCourseList().add(course);
        Collections.sort(term.getCourseList(), new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        mRepository.insertTerm(term);
        return;
    }

    public void deleteCourse() {
        mLiveTermMentorEditor.getValue().getCourseList().remove(mLiveCourse.getValue());
        mRepository.insertTerm(mLiveTermMentorEditor.getValue());
    }
}
