package com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.benjaminlucaswebdesigns.studentprogresstracker.Database.AppRepository;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.List;

class TermListViewModel extends AndroidViewModel {

    public LiveData<List<Term>> mTerms;
    private AppRepository mRepository;

    public TermListViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }

    public void deleteAllTerms() {
        mRepository.deleteAllTerms();
    }

    public void addSampleTerms()  {
        mRepository.addSampleTerms();
    }
}
