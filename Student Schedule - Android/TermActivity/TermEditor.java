package com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity.CourseList;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.EDITING_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

public class TermEditor extends AppCompatActivity{

    @BindView(R.id.create_term_image_view)
    ImageView createTermView;
    @BindView(R.id.toggle_edit_term)
    Switch toggleEditTerm;
    @BindView(R.id.edit_text_term_name_term_editor)
    TextView mTermName;
    @BindView(R.id.edit_text_start_date_term_editor)
    TextView mTermStart;
    @BindView(R.id.edit_text_end_date_term_editor)
    TextView mTermEnd;
    @BindView(R.id.list_view_course_list_term_editor)
    TextView mTermCourses;

    @BindView(R.id.edit_course_list_btn)
    Button editCourseBtn;
    @BindView(R.id.pick_start_date_btn_term_editor)
    Button pickStartDateBtn;
    @BindView(R.id.pick_end_date_btn_term_editor)
    Button pickEndDateBtn;

    private TermEditorViewModel mViewModel;
    private boolean  mEditing,mNewTerm;
    private int termId;
    private ArrayList<Course> courseArrayList;
    private Term term;
    private Bundle extras;
    final Calendar datePicker = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            datePicker.set(Calendar.YEAR, year);
            datePicker.set(Calendar.MONTH, monthOfYear);
            datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(1);
        }
    };

    DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            datePicker.set(Calendar.YEAR, year);
            datePicker.set(Calendar.MONTH, monthOfYear);
            datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(2);
        }
    };

    @OnClick(R.id.pick_start_date_btn_term_editor)
    void pickStartDateClickHandler(){
        new DatePickerDialog(TermEditor.this, sDate, datePicker
                .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                datePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.pick_end_date_btn_term_editor)
    void pickEndDateClickHandler(){
        new DatePickerDialog(TermEditor.this, eDate, datePicker
                .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                datePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.edit_course_list_btn)
    void editCourseListClickHandler(){
        Boolean saved = saveAndReturn(false);
        if(saved){
            final Intent intent = new Intent(this, CourseList.class);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    intent.putExtra(TERM_ID_KEY, mViewModel.getLastInsertedTermId());
                    startActivityForResult(intent, 123);            }
            }, 200);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        toggleEditTerm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditEnabled();
                } else {
                    if (!saveAndReturn(false)){
                        toggleEditTerm.toggle();
                        return;
                    };
                    setEditDisabled();
                    editCourseBtn.setEnabled(true);
                }
            }
        });
        initViewModel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if(resultCode == TermEditor.RESULT_OK){
                if(data.getIntExtra(REFRESH_VIEW_FLAG,0)==123){
                    Intent intent = new Intent(this, TermEditor.class);
                    intent.putExtra(TERM_ID_KEY, mViewModel.getLastInsertedTermId());
                    startActivityForResult(intent, 123);
                    finish();
                }
            }
        }
    }
    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(TermEditorViewModel.class);
        mTermStart.setEnabled(false);
        mTermEnd.setEnabled(false);
        if(extras == null) {extras = getIntent().getExtras();}
        if (extras == null) {
            setTitle("Create New Term");
            mNewTerm = true;
            toggleEditTerm.setVisibility(View.INVISIBLE);
            createTermView.setVisibility(View.VISIBLE);
            setEditEnabled();
            editCourseBtn.setVisibility(View.VISIBLE);
        } else {
            setTitle("Edit Term");
            termId = extras.getInt(TERM_ID_KEY);
            mNewTerm = false;
            mViewModel.loadData(termId);
            toggleEditTerm.setChecked(false);
            toggleEditTerm.setVisibility(View.VISIBLE);
            createTermView.setVisibility(View.INVISIBLE);
            setEditDisabled();
        }
        mViewModel.mLiveTerm.observe(this, new Observer<Term>() {
            @Override
            public void onChanged(@Nullable Term termEntity) {
                if (termEntity != null && !mEditing) {
                    term = termEntity;
                    termId = termEntity.getId();
                    courseArrayList = termEntity.getCourseList();
                    mTermName.setText(termEntity.getTermName());
                    mTermStart.setText(dateFormat.format(termEntity.getStartDate()));
                    mTermEnd.setText(dateFormat.format(termEntity.getEndDate()));
                    mTermCourses.setText(termEntity.getCourseNameList());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn(true);
            return true;
        } else if (item.getItemId() == R.id.delete_term_editor) {
            if(mViewModel.mLiveTerm.getValue().getCourseList() == null||mViewModel.mLiveTerm.getValue().getCourseList().size() == 0){
                mViewModel.deleteTerm();
                finish();
                return true;
            } else {
                Toast toast = Toast.makeText(this, "You must delete all courses before deleting term!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mNewTerm){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_term_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private Boolean saveAndReturn(Boolean returnToPreviousActivity) {
        try {
            if(mTermName.getText().toString().contentEquals("")||mTermName == null){
                Toast toast = Toast.makeText(this, "Please enter a name for the term!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(mTermStart.getText().toString().contentEquals("")||mTermStart == null){
                Toast toast = Toast.makeText(this, "Please enter a start date for the term!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(mTermEnd.getText().toString().contentEquals("")||mTermEnd == null){
                Toast toast = Toast.makeText(this, "Please enter an end date for the term!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            Date date1 = new Date(mTermStart.getText().toString());
            Date date2 = new Date(mTermEnd.getText().toString());
            if(date2.before(date1)){
                Toast toast = Toast.makeText(this, "Start date must be before end date!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(term == null){
                courseArrayList = new ArrayList<>();
                mViewModel.saveTerm(mNewTerm,mTermName.getText().toString().trim(), courseArrayList, date1, date2);
                mNewTerm = false;
                Toast toast = Toast.makeText(this, "Term Saved!", Snackbar.LENGTH_LONG);
                toast.show();

            } else{
                termId = term.getId();
                termId = mViewModel.saveTerm(mNewTerm,mTermName.getText().toString().trim(), courseArrayList, date1, date2);
                mNewTerm = false;
                Toast toast = Toast.makeText(this, "Term Saved!", Snackbar.LENGTH_LONG);
                toast.show();
            }
            if(returnToPreviousActivity){
                finish();
                return true;
            }
            return true;
            //prints exception - this was used for debugging, user will never reach this error due to other in place protections.
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Invalid date! Please 'MM/DD/YYYY' format", Snackbar.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
            return false;
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    public void setEditEnabled(){
        mTermName.setEnabled(true);
        editCourseBtn.setVisibility(View.INVISIBLE);
        pickStartDateBtn.setVisibility(View.VISIBLE);
        pickEndDateBtn.setVisibility(View.VISIBLE);
    }

    public void setEditDisabled(){
        mTermName.setEnabled(false);
        editCourseBtn.setVisibility(View.VISIBLE);
        pickStartDateBtn.setVisibility(View.INVISIBLE);
        pickEndDateBtn.setVisibility(View.INVISIBLE);
    }

    private void updateLabel(int dateFieldToSetCode) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(dateFieldToSetCode == 1){
            mTermStart.setText(sdf.format(datePicker.getTime()));
        } else {
            mTermEnd.setText(sdf.format(datePicker.getTime()));
        }
    }
}


