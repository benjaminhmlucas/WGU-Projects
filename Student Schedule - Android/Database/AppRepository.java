package com.benjaminlucaswebdesigns.studentprogresstracker.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;
    private static int lastInsertedTermId;
    public LiveData<List<Term>> mTerms;
    public LiveData<List<Course>> mCourses;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTerms = getAllTerms();
    }

    //TERMS--------------------------------------------------->

    public LiveData<List<Term>> getAllTerms() {
        return mDb.termDao().getAll();
    }

    public void deleteAllTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteAll();
            }
        });
    }

    public Term getTermById(int id) {
        return mDb.termDao().getTermById(id);
    }

    public void insertTerm(final Term term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Long id = mDb.termDao().insertTerm(term);
                lastInsertedTermId = id.intValue();
            }
        });
    }

    public int getLastInsertedTermId(){
        return lastInsertedTermId;
    }

    public void deleteTerm(final Term term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteTerm(term);
            }
        });
    }

    public void addSampleTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insertAll(SampleData.getTerms());
            }
        });
    }

}
