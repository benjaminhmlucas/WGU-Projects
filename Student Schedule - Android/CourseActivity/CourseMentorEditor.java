package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.EDITING_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

public class CourseMentorEditor extends AppCompatActivity {

    @BindView(R.id.edit_text_mentor_name_mentor_editor)
    TextInputEditText mMentorName;
    @BindView(R.id.edit_text_mentor_phone_mentor_editor)
    TextInputEditText mMentorPhone;
    @BindView(R.id.edit_text_mentor_email_mentor_editor)
    TextInputEditText mMentorEmail;

    private CourseMentorEditorViewModel mViewModel;
    private boolean  mEditing;
    private int termId;
    private int courseId;
    private Term term;
    private Course course;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_mentor);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(CourseMentorEditorViewModel.class);
        extras = getIntent().getExtras();
        termId = extras.getInt(TERM_ID_KEY);
        courseId = extras.getInt(COURSE_ID_KEY);
        setTitle("Edit Mentor Details");
        mViewModel.loadData(courseId,termId);
        mViewModel.mLiveCourse.observe(this, new Observer<Course>() {
            @Override
            public void onChanged(@Nullable Course courseEntity) {
                if (courseEntity != null && !mEditing) {
                    course = courseEntity;
                    mMentorName.setText(courseEntity.getCourseMentorName());
                    mMentorPhone.setText(courseEntity.getCourseMentorPhone());
                    mMentorEmail.setText(courseEntity.getCourseMentorEmail());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(saveCourse()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(TERM_ID_KEY, termId);
                returnIntent.putExtra(COURSE_ID_KEY, courseId);
                returnIntent.putExtra(REFRESH_VIEW_FLAG, 123);
                returnIntent.putExtra(NEW_COURSE_FLAG,0);
                setResult(CourseEditor.RESULT_OK, returnIntent);
                finish();
                return true;
            }
            return false;
        } else if (item.getItemId() == R.id.delete_course_editor) {
            mViewModel.deleteCourse();
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
        returnIntent.putExtra(NEW_COURSE_FLAG,0);
        setResult(CourseEditor.RESULT_OK, returnIntent);
        finish();
    }

    private boolean saveCourse() {
        try{
            mViewModel.saveCourse(mMentorName.getText().toString(),mMentorPhone.getText().toString(),mMentorEmail.getText().toString());
            Toast toast = Toast.makeText(this, "Course Saved!", Snackbar.LENGTH_LONG);
            toast.show();
            return true;
        }
        //prints exception - this was used for debugging, user will never reach this error due to other in place protections.
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Course Save Error! " + e.getMessage().toString(), Snackbar.LENGTH_LONG);
            toast.show();
            return false;
        }
    }
}
