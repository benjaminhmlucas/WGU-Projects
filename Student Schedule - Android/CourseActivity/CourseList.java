package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;
import com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity.TermEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

public class CourseList extends AppCompatActivity {

    @BindView(R.id.recycler_view_course_list)
    RecyclerView mRecyclerView;

    private List<Course> coursesData = new ArrayList<>();
    private List<Term> termsData = new ArrayList<>();
    private CourseListAdapter cAdapter;
    private CourseListViewModel cViewModel;
    private Bundle extras;
    private int termId;
    private int courseId;
    private Term term;
    Executor executor = Executors.newSingleThreadExecutor();

    @OnClick(R.id.add_course_fab_course_list)
    void addTermClickHandler(){
        Intent intent = new Intent(this, CourseEditor.class);
        intent.putExtra(TERM_ID_KEY,termId);
        intent.putExtra(NEW_COURSE_FLAG,1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        extras = getIntent().getExtras();
        if(extras != null){
            termId = extras.getInt(TERM_ID_KEY);
            courseId = extras.getInt(COURSE_ID_KEY);
        }
        final Observer<List<Term>> coursesObserver =
                new Observer<List<Term>>() {
                    @Override
                    public void onChanged(@Nullable List<Term> termEntities) {
                        coursesData.clear();
                        termsData = cViewModel.mTerms.getValue();
                        for (Term innerTerm:termsData) {
                            if (innerTerm.getId() == termId){
                                term = innerTerm;
                                for (Course course:innerTerm.getCourseList()) {
                                    coursesData.add(course);
                                }
                                Collections.sort(coursesData, new Comparator<Course>() {
                                    @Override
                                    public int compare(Course o1, Course o2) {
                                        return o1.getStartDate().compareTo(o2.getStartDate());
                                    }
                                });
                            }
                        }

                        if (cAdapter == null) {
                            cAdapter = new CourseListAdapter(coursesData,
                                    CourseList.this);
                            mRecyclerView.setAdapter(cAdapter);
                        } else {
                            cAdapter.notifyDataSetChanged();
                        }
                    }
                };
        cViewModel = ViewModelProviders.of(this)
                .get(CourseListViewModel.class);
        cViewModel.mTerms.observe(this, coursesObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        cAdapter = new CourseListAdapter(coursesData,this);
        mRecyclerView.setAdapter(cAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    saveAndReturn(term, coursesData);
                }
            });
            Intent returnIntent = new Intent();
            returnIntent.putExtra(TERM_ID_KEY,termId);
            returnIntent.putExtra(COURSE_ID_KEY, courseId);
            returnIntent.putExtra(REFRESH_VIEW_FLAG,123);
            setResult(TermEditor.RESULT_OK,returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TERM_ID_KEY, termId);
        returnIntent.putExtra(REFRESH_VIEW_FLAG, 123);
        setResult(TermEditor.RESULT_OK, returnIntent);
        finish();
    }

    private void saveAndReturn(Term term, List<Course> coursesData) {
        cViewModel.saveCoursesToTerm(term, coursesData);
    }

}
