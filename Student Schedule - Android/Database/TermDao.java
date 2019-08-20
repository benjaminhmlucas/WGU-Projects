package com.benjaminlucaswebdesigns.studentprogresstracker.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTerm(Term termEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Term> terms);

    @Delete
    void deleteTerm(Term termEntity);

    @Query("SELECT * FROM terms WHERE term_id = :id")
    Term getTermById(int id);

    @Query("SELECT * FROM terms ORDER BY startDate DESC")
    LiveData<List<Term>> getAll();

    @Query("DELETE FROM terms")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM terms")
    int getCount();

}
