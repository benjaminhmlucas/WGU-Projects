package com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity.CourseEditor;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

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
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_ASSESSMENT_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

public class AssessmentList extends AppCompatActivity {

    @BindView(R.id.recycler_view_assessment_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_assessment_fab_assessment_list)
    FloatingActionButton mAddAssessmentBtn;

    private List<Term> termsData = new ArrayList<>();
    private List<Assessment> assessmentsData = new ArrayList<>();
    private AssessmentListAdapter aAdapter;
    private AssessmentListViewModel aViewModel;
    private Bundle extras;
    private int termId;
    private int courseId;
    private Term term;

    Executor executor = Executors.newSingleThreadExecutor();

    @OnClick(R.id.add_assessment_fab_assessment_list)
    void addAssessmentClickHandler(){
        if(assessmentsData.size()>=5){
            Toast toast = Toast.makeText(this, "Courses cannot have more than 5 assessments!", Snackbar.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent(this, AssessmentEditor.class);
        intent.putExtra(TERM_ID_KEY,termId);
        intent.putExtra(COURSE_ID_KEY,courseId);
        intent.putExtra(NEW_ASSESSMENT_FLAG,1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

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
                        assessmentsData.clear();
                        termsData = aViewModel.mTerms.getValue();
                        for (Term innerTerm:termsData) {
                            if (innerTerm.getId() == termId){
                                term = innerTerm;
                                for (Course course:innerTerm.getCourseList()) {
                                    if(course.getCourseId()==courseId){
                                        for(Assessment assessment:course.getAssessmentList())
                                            assessmentsData.add(assessment);
                                    }
                                }
                                Collections.sort(assessmentsData, new Comparator<Assessment>() {
                                    @Override
                                    public int compare(Assessment o1, Assessment o2) {
                                        return o1.getAssessmentName().compareTo(o2.getAssessmentName());
                                    }
                                });
                            }
                        }

                        if (aAdapter == null) {
                            aAdapter = new AssessmentListAdapter(assessmentsData,
                                    AssessmentList.this);
                            mRecyclerView.setAdapter(aAdapter);
                        } else {
                            aAdapter.notifyDataSetChanged();
                        }
                    }
                };
        aViewModel = ViewModelProviders.of(this)
                .get(AssessmentListViewModel.class);
        aViewModel.mTerms.observe(this, coursesObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        aAdapter = new AssessmentListAdapter(assessmentsData,this);
        mRecyclerView.setAdapter(aAdapter);

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
                    saveAndReturn(term, courseId,assessmentsData);
                }
            });
            Intent returnIntent = new Intent();
            returnIntent.putExtra(TERM_ID_KEY,termId);
            returnIntent.putExtra(COURSE_ID_KEY,courseId);
            returnIntent.putExtra(REFRESH_VIEW_FLAG,123);
            returnIntent.putExtra(NEW_COURSE_FLAG,0);
            setResult(CourseEditor.RESULT_OK,returnIntent);
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
        returnIntent.putExtra(COURSE_ID_KEY, courseId);
        returnIntent.putExtra(REFRESH_VIEW_FLAG, 123);
        setResult(CourseEditor.RESULT_OK, returnIntent);
        finish();
    }

    private void saveAndReturn(Term term, int courseId , List<Assessment> assessmentsData) {
        aViewModel.saveAssessmentsToCourse(term, courseId,assessmentsData);
    }

}

