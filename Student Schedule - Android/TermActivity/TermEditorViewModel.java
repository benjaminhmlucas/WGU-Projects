package com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermEditorViewModel extends AndroidViewModel {

    public MutableLiveData<Term> mLiveTerm = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = mRepository.getTermById(termId);
                mLiveTerm.postValue(term);
            }
        });
    }

    public int getLastInsertedTermId(){
        return mRepository.getLastInsertedTermId();
    }

    public int saveTerm(Boolean mNewTerm, String termName, ArrayList<Course> courseList, Date startDate, Date endDate) {
        int termId;
        if(mNewTerm){
            mRepository.insertTerm(new Term(termName, courseList, startDate, endDate));
            termId = mRepository.getLastInsertedTermId();
        }else{
            Term term = mLiveTerm.getValue();
            term.setTermName(termName.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
            term.setCourseList(courseList);
            mRepository.insertTerm(term);
            mLiveTerm.postValue(term);
            termId = mRepository.getLastInsertedTermId();
        }
        return termId;
    }
    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerm.getValue());
    }

}
