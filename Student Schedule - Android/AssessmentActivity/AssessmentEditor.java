package com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity.CourseList;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;
import com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.NotificationReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ALERT_ID;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_DUE_DATE_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_NAME_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_NAME_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.EDITING_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_ASSESSMENT_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_NAME_ALERT;

public class AssessmentEditor extends AppCompatActivity {

    @BindView(R.id.switch_assessment_editor)
    Switch toggleEditAssessment;
    @BindView(R.id.alert_btn_due_date_assessment_editor)
    Switch alertDueDateSwitch;
    @BindView(R.id.create_assessment_image_view)
    ImageView createAssessmentImage;
    @BindView(R.id.text_view_term_name_assessment_editor)
    TextView mTermName;
    @BindView((R.id.text_view_course_name_assessment_editor))
    TextView mCourseName;
    @BindView(R.id.edit_text_assessment_name_assessment_editor)
    TextInputEditText mAssessmentName;
    @BindView(R.id.pick_due_date_btn_assessment_editor)
    Button dueDateBtn;
    @BindView(R.id.assessment_type_performance)
    RadioButton mTypePerformance;
    @BindView(R.id.assessment_type_objective)
    RadioButton mTypeObjective;
    @BindView(R.id.edit_text_assessment_due_date_assessment_editor)
    TextInputEditText mDueDate;
    @BindView(R.id.edit_text_assessment_note_assessment_editor)
    TextInputEditText mAssessmentNote;


    private AssessmentEditorViewModel mViewModel;
    private boolean  mEditing, mNewAssessment = false;
    private int termId;
    private int courseId;
    private int assessmentId;
    private Term term;
    private Course course;
    private Assessment assessment;
    private String assessmentType;
    private Bundle extras;
    final Calendar datePicker = Calendar.getInstance();
    final Calendar timePicker = Calendar.getInstance();
    private Intent timePickerIntent;
    private Calendar alarmTime;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    CompoundButton.OnCheckedChangeListener alertDueDateListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Context context = getApplicationContext();
            timePickerIntent = new Intent(context, NotificationReceiver.class);
            term = mViewModel.mLiveTermAssessmentEditor.getValue();
            if (isChecked) {
                if(saveAssessment()){
                    assessment.setDueDateAlert(true);
                    new TimePickerDialog(AssessmentEditor.this,dueDateAlarmTimeListener,timePicker.get(Calendar.HOUR_OF_DAY),
                            timePicker.get(Calendar.MINUTE),true).show();
                } else {
                    alertDueDateSwitch.setChecked(false);
                    return;
                }
            } else {
                assessment.setDueDateAlert(false);
                PendingIntent sender = PendingIntent.getBroadcast(context, assessment.getAssessmentId()+1000000,timePickerIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(sender);
                Toast toast = Toast.makeText(context, "Course Start Alert Canceled!", Snackbar.LENGTH_LONG);
                toast.show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            datePicker.set(Calendar.YEAR, year);
            datePicker.set(Calendar.MONTH, monthOfYear);
            datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(1);
        }
    };

    TimePickerDialog.OnTimeSetListener dueDateAlarmTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timePicker.set(Calendar.MINUTE, minute);
            updateLabel(3);
            alarmTime.add(Calendar.SECOND, 5);
            timePickerIntent.putExtra(ALERT_ID, 2);
            timePickerIntent.putExtra(ASSESSMENT_NAME_ALERT,assessment.getAssessmentName());
            timePickerIntent.putExtra(COURSE_NAME_ALERT,course.getCourseName());
            timePickerIntent.putExtra(TERM_NAME_ALERT,term.getTermName());
            timePickerIntent.putExtra(ASSESSMENT_DUE_DATE_ALERT,dateFormat.format(assessment.getDueDate()));
            timePickerIntent.putExtra(ASSESSMENT_ID_KEY, assessment.getAssessmentId());
            timePickerIntent.putExtra(COURSE_ID_KEY, course.getCourseId());
            timePickerIntent.putExtra(TERM_ID_KEY, course.getTermId());
            timePickerIntent.putExtra(NEW_COURSE_FLAG,0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),assessment.getAssessmentId()+1000000,timePickerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),60000,pendingIntent);
            Toast toast = Toast.makeText(getApplicationContext(), "Course start alert set for: " + timeFormat.format(alarmTime.getTime()), Snackbar.LENGTH_LONG);
            toast.show();
        }
    };

    @OnClick(R.id.pick_due_date_btn_assessment_editor)
    void pickDueDateClickHandler(){
        new DatePickerDialog(AssessmentEditor.this, dDate, datePicker
                .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                datePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.assessment_type_objective:
                if (checked)
                    assessmentType = "Objective";
                break;
            case R.id.assessment_type_performance:
                if (checked)
                    assessmentType = "Performance";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        toggleEditAssessment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditEnabled();
                } else {
                    if (!saveAssessment()){
                        toggleEditAssessment.toggle();
                        return;
                    };
                    setEditDisabled();
                }
            }
        });
        initViewModel();
        alertDueDateSwitch.setOnCheckedChangeListener(alertDueDateListener);
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TERM_ID_KEY, termId);
        returnIntent.putExtra(COURSE_ID_KEY, courseId);
        returnIntent.putExtra(REFRESH_VIEW_FLAG, 123);
        setResult(AssessmentList.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewAssessment) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_assessment_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(saveAssessment()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(TERM_ID_KEY, termId);
                returnIntent.putExtra(COURSE_ID_KEY, courseId);
                returnIntent.putExtra(REFRESH_VIEW_FLAG, 123);
                setResult(CourseList.RESULT_OK, returnIntent);
                finish();
                return true;
            }
            return false;
        } else if (item.getItemId() == R.id.delete_assessment_editor) {
            removeAssociatedAssessmentAlert();
            mViewModel.deleteAssessment();
            Toast toast = Toast.makeText(this, "Assessment and it's alert have been deleted", Snackbar.LENGTH_LONG);
            toast.show();
            finish();
            return true;
        } else if(item.getItemId() == R.id.share_assessment_editor){
            String shareBody = mAssessmentNote.getText().toString().trim();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Horses are just Giant Dogs!"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeAssociatedAssessmentAlert() {
        if(timePickerIntent == null){
            timePickerIntent = new Intent(this, NotificationReceiver.class);
        }
        PendingIntent sender = PendingIntent.getBroadcast(this, assessment.getAssessmentId()+1000000,timePickerIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AssessmentEditorViewModel.class);
        extras = getIntent().getExtras();
        termId = extras.getInt(TERM_ID_KEY);
        courseId = extras.getInt(COURSE_ID_KEY);
        if(extras.getInt(NEW_ASSESSMENT_FLAG)==1){
            mNewAssessment = true;
            setTitle("Create New Assessment");
            toggleEditAssessment.setVisibility(View.INVISIBLE);
            createAssessmentImage.setVisibility(View.VISIBLE);
            mViewModel.loadData(courseId,termId);
            alertDueDateSwitch.setVisibility(View.INVISIBLE);
            setEditEnabled();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    term = mViewModel.mLiveTermAssessmentEditor.getValue();
                    course = mViewModel.mLiveCourseAssessmentEditor.getValue();
                    mTermName.setText("   Term Name: " + term.getTermName());
                    mCourseName.setText("   Course Name: " + course.getCourseName());
                }
            }, 200);
        } else {
            assessmentId = extras.getInt(ASSESSMENT_ID_KEY);
            setTitle("Edit Assessment Details");
            toggleEditAssessment.setVisibility(View.VISIBLE);
            createAssessmentImage.setVisibility(View.INVISIBLE);
            alertDueDateSwitch.setVisibility(View.VISIBLE);
            mViewModel.loadData(assessmentId, courseId, termId);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    assessment = mViewModel.mLiveAssessment.getValue();
                    term = mViewModel.mLiveTermAssessmentEditor.getValue();
                    course = mViewModel.mLiveCourseAssessmentEditor.getValue();
                    mTermName.setText("   Term Name: " + term.getTermName());
                    mCourseName.setText("   Course Name: " + course.getCourseName());
                    try{
                        if(assessment.getDueDateAlert()) {
                            alertDueDateSwitch.setOnCheckedChangeListener(null);
                            alertDueDateSwitch.setChecked(assessment.getDueDateAlert());
                            alertDueDateSwitch.setOnCheckedChangeListener(alertDueDateListener);
                        }
                    }
                    //This catches a user returning from a notification that was for a deleted assessment object
                    catch (NullPointerException npe){
                        Toast toast = Toast.makeText(getApplicationContext(), "Assessment has been deleted!", Snackbar.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    }
                }
            }, 200);
            setEditDisabled();
        }

        mViewModel.mLiveAssessment.observe(this, new Observer<Assessment>() {
            @Override
            public void onChanged(@Nullable Assessment assessmentEntity) {
                if (assessmentEntity != null && !mEditing) {
                    assessment = assessmentEntity;
                    mAssessmentName.setText(assessment.getAssessmentName());
                    mAssessmentNote.setText(assessment.getNote());
                    mDueDate.setText(dateFormat.format(assessmentEntity.getDueDate()));
                    assessmentType = assessmentEntity.getAssessmentType();
                    alertDueDateSwitch.setOnCheckedChangeListener(null);
                    alertDueDateSwitch.setChecked(assessmentEntity.getDueDateAlert());
                    alertDueDateSwitch.setOnCheckedChangeListener(alertDueDateListener);
                    switch (assessmentType){
                        case "Objective":
                            mTypeObjective.setChecked(true);
                            break;
                        case "Performance":
                            mTypePerformance.setChecked(true);
                            break;
                    }
                }
            }
        });

    }

    private void setEditDisabled() {
        mAssessmentName.setEnabled(false);
        mTypePerformance.setEnabled(false);
        mTypeObjective.setEnabled(false);
        mDueDate.setEnabled(false);
        mAssessmentNote.setEnabled(false);
        alertDueDateSwitch.setEnabled(false);
        dueDateBtn.setVisibility(View.INVISIBLE);
    }

    private void setEditEnabled() {
        mAssessmentName.setEnabled(true);
        mTypePerformance.setEnabled(true);
        mTypeObjective.setEnabled(true);
        mDueDate.setEnabled(false);
        mAssessmentNote.setEnabled(true);
        alertDueDateSwitch.setEnabled(true);
        dueDateBtn.setVisibility(View.VISIBLE);
    }

    private boolean saveAssessment() {
        term = mViewModel.mLiveTermAssessmentEditor.getValue();
        course = mViewModel.mLiveCourseAssessmentEditor.getValue();
        try{
            if(mAssessmentName.getText().toString().contentEquals("")||mAssessmentName == null){
                Toast toast = Toast.makeText(this, "Please enter a name for the assessment!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(!mTypePerformance.isChecked()&&!mTypeObjective.isChecked()){
                Toast toast = Toast.makeText(this, "Please choose a type for the assessment!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(mDueDate.getText().toString().contentEquals("")||mDueDate == null){
                Toast toast = Toast.makeText(this, "Please enter a due date for the assessment!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            Date dueDate = new Date(mDueDate.getText().toString());
            if(course.getEndDate().before(dueDate)) {
                Toast toast = Toast.makeText(this, ("Due date must be on or before the course end date: " + dateFormat.format(course.getEndDate())), Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            mViewModel.saveAssessment(mNewAssessment,mAssessmentName.getText().toString(),dueDate,assessmentType,mAssessmentNote.getText().toString(),alertDueDateSwitch.isChecked());
            mNewAssessment = false;
            Toast toast = Toast.makeText(this, "Assessment Saved!", Snackbar.LENGTH_LONG);
            toast.show();
            return true;
        }
        //prints exception - this was used for debugging, user will never reach this error due to other in place protections.
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Assessment Save Error! " + e.getMessage().toString(), Snackbar.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    private void updateLabel(int dateOrTimeFieldToSetCode) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        switch (dateOrTimeFieldToSetCode){
            case 1:
                mDueDate.setText(sdf.format(datePicker.getTime()));
                break;
            case 3:
                alarmTime = Calendar.getInstance();
                alarmTime.setTime(timePicker.getTime());
                break;
        }
    }
}
