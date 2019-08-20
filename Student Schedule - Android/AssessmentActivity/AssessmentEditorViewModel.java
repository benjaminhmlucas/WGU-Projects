package com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentEditorViewModel extends AndroidViewModel {
    MutableLiveData<Course> mLiveCourseAssessmentEditor = new MutableLiveData<>();
    MutableLiveData<Term> mLiveTermAssessmentEditor = new MutableLiveData<>();
    MutableLiveData<Assessment> mLiveAssessment = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    void loadData(final int assessmentId, final int courseId,final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTermAssessmentEditor.postValue(term);
                for (Course innerCourse:term.getCourseList()) {
                    if(innerCourse.getCourseId()==courseId){
                        mLiveCourseAssessmentEditor.postValue(innerCourse);
                        for (Assessment assessment:innerCourse.getAssessmentList()) {
                            if(assessment.getAssessmentId()==assessmentId){
                                mLiveAssessment.postValue(assessment);
                                return;
                            }
                        }
                    }
                }
            }
        });
    }

    void loadData(final int courseId,final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTermAssessmentEditor.postValue(term);
                for (Course innerCourse:term.getCourseList()) {
                    if(innerCourse.getCourseId()==courseId){
                        mLiveCourseAssessmentEditor.postValue(innerCourse);
                        return;
                    }
                }
            }
        });
    }


    void saveAssessment(boolean newAssessment,String assessmentName, Date dueDate ,String assessmentType, String assessmentNote, boolean dueDateAlert) {
        Course course = mLiveCourseAssessmentEditor.getValue();
        Term term = mLiveTermAssessmentEditor.getValue();
        Assessment assessment;
        if(newAssessment){
            assessment = new Assessment(course.getCourseId(),term.getId(),assessmentName,assessmentType,dueDate,assessmentNote,dueDateAlert);
            course.getAssessmentList().add(assessment);
            term.removeOlderCourse(course.getCourseId());
            term.getCourseList().add(course);
            Collections.sort(term.getCourseList(), new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            });
            mRepository.insertTerm(term);
        } else{
            assessment = mLiveAssessment.getValue();
            assessment.setAssessmentName(assessmentName);
            assessment.setNote(assessmentNote);
            assessment.setAssessmentType(assessmentType);
            assessment.setDueDate(dueDate);
            assessment.setDueDateAlert(dueDateAlert);
            course.removeOlderAssessment(assessment.getAssessmentId());
            course.getAssessmentList().add(assessment);
            Collections.sort(course.getAssessmentList(), new Comparator<Assessment>() {
                @Override
                public int compare(Assessment o1, Assessment o2) {
                    return o1.getDueDate().compareTo(o2.getDueDate());
                }
            });
            term.removeOlderCourse(course.getCourseId());
            term.getCourseList().add(course);
            Collections.sort(term.getCourseList(), new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            });
            mRepository.insertTerm(term);
        }

    }

    void deleteAssessment() {
        mLiveCourseAssessmentEditor.getValue().getAssessmentList().remove(mLiveAssessment.getValue());
        mLiveTermAssessmentEditor.getValue().removeOlderCourse(mLiveCourseAssessmentEditor.getValue().getCourseId());
        mLiveTermAssessmentEditor.getValue().getCourseList().add(mLiveCourseAssessmentEditor.getValue());
        Collections.sort(mLiveCourseAssessmentEditor.getValue().getAssessmentList(), new Comparator<Assessment>() {
            @Override
            public int compare(Assessment o1, Assessment o2) {
                return o1.getAssessmentName().compareTo(o2.getAssessmentName());
            }
        });
        mRepository.insertTerm(mLiveTermAssessmentEditor.getValue());
    }
}

