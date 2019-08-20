package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseEditorViewModel extends AndroidViewModel {
    public MutableLiveData<Course> mLiveCourse = new MutableLiveData<>();
    public MutableLiveData<Term> mLiveTermCourseEditor = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTermCourseEditor.postValue(term);
                return;
            }
        });
    }

    public void loadData(final int courseId,final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTermCourseEditor.postValue(term);
                for (Course innerCourse:term.getCourseList()) {
                    if(innerCourse.getCourseId()==courseId){
                        mLiveCourse.postValue(innerCourse);
                        return;
                    }
                }
                return;
            }
        });
    }

    public int saveCourse(Boolean mNewCourse, int termId,String courseName,Date startDate, Date endDate,String courseStatus,String courseMentorName, String courseMentorPhone, String courseMentorEmail, String courseNote,ArrayList<Assessment> assessmentList,Boolean sDateAlert, Boolean eDateAlert) {
        Course course;
        Term term = mLiveTermCourseEditor.getValue();
        if(mNewCourse){
            course = new Course(termId, courseName, startDate, endDate,courseStatus,courseMentorName,courseMentorPhone,courseMentorEmail,courseNote,assessmentList,sDateAlert,eDateAlert);
            term.getCourseList().add(course);
            mRepository.insertTerm(term);
        }else{
            course = mLiveCourse.getValue();
            course.setCourseName(courseName.trim());
            course.setStartDate(startDate);
            course.setEndDate(endDate);
            course.setCourseStatus(courseStatus);
            course.setCourseMentorName(courseMentorName);
            course.setCourseMentorPhone(courseMentorPhone);
            course.setCourseMentorEmail(courseMentorEmail);
            course.setCourseNote(courseNote);
            course.setAssessmentList(assessmentList);
            course.setSDateAlert(sDateAlert);
            course.setEDateAlert(eDateAlert);
            mLiveCourse.postValue(course);
            term.removeOlderCourse(course.getCourseId());
            term.getCourseList().add(course);
            Collections.sort(term.getCourseList(), new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            });
            mRepository.insertTerm(term);
            mLiveTermCourseEditor.postValue(term);
        }
        return course.getCourseId();
    }

    public void deleteCourse() {
        mLiveTermCourseEditor.getValue().getCourseList().remove(mLiveCourse.getValue());
        mRepository.insertTerm(mLiveTermCourseEditor.getValue());
    }
}

