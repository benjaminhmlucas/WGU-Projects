package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

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
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity.AssessmentList;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;
import com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.NotificationReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ALERT_ID;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_NAME_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_START_DATE_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.EDITING_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.REFRESH_VIEW_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_NAME_ALERT;

public class CourseEditor extends AppCompatActivity {

    @BindView(R.id.create_course_image_view)
    ImageView createCourseView;
    @BindView(R.id.edit_text_course_name_course_editor)
    TextInputEditText mCourseName;
    @BindView(R.id.edit_text_course_start_date_course_editor)
    TextInputEditText mCourseStart;
    @BindView(R.id.edit_text_course_end_date_course_editor)
    TextInputEditText mCourseEnd;
    @BindView(R.id.text_view_term_name_course_editor)
    TextView mTermName;
    @BindView(R.id.text_view_course_instructor_info_course_editor)
    TextView mCourseMentor;
    @BindView(R.id.edit_text_course_note_course_editor)
    TextView mCourseNote;
    @BindView(R.id.text_view_course_assessment_info_course_editor)
    TextView mCourseAssessmentList;

    @BindView(R.id.switch_course_editor)
    Switch toggleEditCourse;
    @BindView(R.id.pick_start_date_btn_course_editor)
    Button pickStartDateBtn;
    @BindView(R.id.alert_switch_start_date_course_editor)
    Switch alertStartDateSwitch;
    @BindView(R.id.pick_end_date_btn_course_editor)
    Button pickEndDateBtn;
    @BindView(R.id.alert_switch_end_date_course_editor)
    Switch alertEndDateSwitch;
    @BindView(R.id.edit_mentor_button)
    Button editInstructorBtn;
    @BindView(R.id.edit_assessment_button)
    Button editAssessmentListBtn;

    @BindView(R.id.course_status_group)
    RadioGroup statusGroup;
    @BindView(R.id.course_status_plan_to_take)
    RadioButton statusPlanToTake;
    @BindView(R.id.course_status_in_progress)
    RadioButton statusInProgress;
    @BindView(R.id.course_status_complete)
    RadioButton statusComplete;
    @BindView(R.id.course_status_dropped)
    RadioButton statusDropped;

    private CourseEditorViewModel mViewModel;
    private boolean  mEditing,mNewCourse;
    private int termId;
    private int courseId;
    private Term term;
    private Course course;
    private Bundle extras;
    private String courseStatus;
    final Calendar datePicker = Calendar.getInstance();
    final Calendar timePicker = Calendar.getInstance();
    private Intent timePickerIntent;
    private Calendar alarmTime;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    CompoundButton.OnCheckedChangeListener alertStartDateListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Context context = getApplicationContext();
            timePickerIntent = new Intent(context, NotificationReceiver.class);
            term = mViewModel.mLiveTermCourseEditor.getValue();
            if (isChecked) {
                if(saveAndReturn(false)){
                    course.setSDateAlert(true);
                    new TimePickerDialog(CourseEditor.this,startDateAlarmTimeListener,timePicker.get(Calendar.HOUR_OF_DAY),
                            timePicker.get(Calendar.MINUTE),true).show();
                } else {
                    alertStartDateSwitch.setChecked(false);
                    return;
                }
            } else {
                course.setSDateAlert(false);
                PendingIntent sender = PendingIntent.getBroadcast(context, course.getCourseId(),timePickerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(sender);
                Toast toast = Toast.makeText(context, "Course Start Alert Canceled!", Snackbar.LENGTH_LONG);
                toast.show();
            }
        }
    };
    CompoundButton.OnCheckedChangeListener alertEndDateListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Context context = getApplicationContext();
            timePickerIntent = new Intent(context, NotificationReceiver.class);
            term = mViewModel.mLiveTermCourseEditor.getValue();
            if (isChecked) {
                if(saveAndReturn(false)){
                    course.setEDateAlert(true);
                    new TimePickerDialog(CourseEditor.this,endDateAlarmTimeListener,timePicker.get(Calendar.HOUR_OF_DAY), timePicker.get(Calendar.MINUTE),true).show();
                } else {
                    alertEndDateSwitch.setChecked(false);
                    return;
                }
            } else {
                course.setEDateAlert(false);
                PendingIntent sender = PendingIntent.getBroadcast(context, course.getCourseId()+10000,timePickerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(sender);
                Toast toast = Toast.makeText(context, "Course End Alert Canceled!", Snackbar.LENGTH_LONG);
                toast.show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            datePicker.set(Calendar.YEAR, year);
            datePicker.set(Calendar.MONTH, monthOfYear);
            datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(1);
        }
    };

    TimePickerDialog.OnTimeSetListener startDateAlarmTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timePicker.set(Calendar.MINUTE, minute);
            updateLabel(3);
            alarmTime.add(Calendar.SECOND, 5);
            timePickerIntent.putExtra(ALERT_ID, 0);
            timePickerIntent.putExtra(COURSE_NAME_ALERT,course.getCourseName());
            timePickerIntent.putExtra(TERM_NAME_ALERT,term.getTermName());
            timePickerIntent.putExtra(COURSE_START_DATE_ALERT,dateFormat.format(course.getStartDate()));
            timePickerIntent.putExtra(COURSE_ID_KEY, course.getCourseId());
            timePickerIntent.putExtra(TERM_ID_KEY, course.getTermId());
            timePickerIntent.putExtra(NEW_COURSE_FLAG,0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),course.getCourseId(),timePickerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),60000,pendingIntent);
            Toast toast = Toast.makeText(getApplicationContext(), "Course start alert set for: " + timeFormat.format(alarmTime.getTime()), Snackbar.LENGTH_LONG);
            toast.show();
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

    TimePickerDialog.OnTimeSetListener endDateAlarmTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timePicker.set(Calendar.MINUTE, minute);
            updateLabel(3);
            alarmTime.add(Calendar.SECOND, 5);
            timePickerIntent.putExtra(COURSE_ID_KEY, course.getCourseId());
            timePickerIntent.putExtra(ALERT_ID, 1);
            timePickerIntent.putExtra(COURSE_NAME_ALERT,course.getCourseName());
            timePickerIntent.putExtra(TERM_NAME_ALERT,term.getTermName());
            timePickerIntent.putExtra(COURSE_START_DATE_ALERT,dateFormat.format(course.getStartDate()));
            timePickerIntent.putExtra(TERM_ID_KEY, course.getTermId());
            timePickerIntent.putExtra(NEW_COURSE_FLAG,0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),course.getCourseId()+10000,timePickerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime.getTimeInMillis(),60000,pendingIntent);
            Toast toast = Toast.makeText(getApplicationContext(), "Course end alert set for: " + timeFormat.format(alarmTime.getTime()), Snackbar.LENGTH_LONG);
            toast.show();
        }
    };

    @OnClick(R.id.pick_start_date_btn_course_editor)
    void pickStartDateClickHandler(){
        new DatePickerDialog(CourseEditor.this, sDate, datePicker
                .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                datePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.pick_end_date_btn_course_editor)
    void pickEndDateClickHandler(){
        new DatePickerDialog(CourseEditor.this, eDate, datePicker
                .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                datePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.edit_mentor_button)
    void editMentorClickHandler(){
        if(saveAndReturn(false)){
            Intent intent = new Intent(this, CourseMentorEditor.class);
            intent.putExtra(TERM_ID_KEY, term.getId());
            intent.putExtra(COURSE_ID_KEY, courseId);
            intent.putExtra(REFRESH_VIEW_FLAG, 123);
            intent.putExtra(NEW_COURSE_FLAG,0);
            startActivityForResult(intent, 123);
        }
    }

    @OnClick(R.id.edit_assessment_button)
    void editAssessmentListClickHandler(){
        if(saveAndReturn(false)){
            Intent intent = new Intent(this, AssessmentList.class);
            intent.putExtra(TERM_ID_KEY, term.getId());
            intent.putExtra(COURSE_ID_KEY, courseId);
            intent.putExtra(REFRESH_VIEW_FLAG, 123);
            intent.putExtra(NEW_COURSE_FLAG,0);
            startActivityForResult(intent, 123);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.course_status_plan_to_take:
                if (checked)
                    courseStatus = "Plan To Take";
                break;
            case R.id.course_status_in_progress:
                if (checked)
                    courseStatus = "In Progress";
                break;
            case R.id.course_status_complete:
                if (checked)
                    courseStatus = "Complete";
                break;
            case R.id.course_status_dropped:
                if (checked)
                    courseStatus = "Dropped";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        initViewModel();
        toggleEditCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditEnabled();
                } else {
                    if (!saveAndReturn(false)){
                        toggleEditCourse.setChecked(true);
                        setEditEnabled();
                        return;
                    };
                    setEditDisabled();
                    editInstructorBtn.setEnabled(true);
                    editAssessmentListBtn.setEnabled(true);
                }
            }
        });
        alertStartDateSwitch.setOnCheckedChangeListener(alertStartDateListener);
        alertEndDateSwitch.setOnCheckedChangeListener(alertEndDateListener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if(resultCode == CourseEditor.RESULT_OK){
                if(data.getIntExtra(REFRESH_VIEW_FLAG,1)==123){
                    Intent intent = new Intent(this, CourseEditor.class);
                    intent.putExtra(TERM_ID_KEY, term.getId());
                    intent.putExtra(COURSE_ID_KEY, courseId);
                    intent.putExtra(REFRESH_VIEW_FLAG, 123);
                    intent.putExtra(NEW_COURSE_FLAG,0);
                    startActivityForResult(intent, 123);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_course_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn(true);
            return true;
        } else if (item.getItemId() == R.id.delete_course_editor) {
            removeAssociatedCourseAlerts();
            mViewModel.deleteCourse();
            Toast toast = Toast.makeText(this, "Course and it's alerts have been deleted", Snackbar.LENGTH_LONG);
            toast.show();
            finish();
            return true;
        } else if (item.getItemId() ==  R.id.share_course_editor){
            String shareBody = mCourseNote.getText().toString().trim();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Horses are just Giant Dogs!"));
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(CourseEditorViewModel.class);
        extras = getIntent().getExtras();
        termId = extras.getInt(TERM_ID_KEY);
        if (extras.getInt(NEW_COURSE_FLAG) == 1) {
            setTitle("Create New Course");
            mNewCourse = true;
            mViewModel.loadData(termId);
            toggleEditCourse.setVisibility(View.INVISIBLE);
            createCourseView.setVisibility(View.VISIBLE);
            setEditEnabled();
            statusPlanToTake.setChecked(true);
            courseStatus = "Plan To Take";
            alertStartDateSwitch.setVisibility(View.INVISIBLE);
            alertEndDateSwitch.setVisibility(View.INVISIBLE);
            editInstructorBtn.setVisibility(View.VISIBLE);
            editAssessmentListBtn.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    term = mViewModel.mLiveTermCourseEditor.getValue();
                    mTermName.setText("   Term Name: "+term.getTermName());
                }
            }, 300);
        } else {
            setTitle("Edit Course");
            mNewCourse = false;
            termId = extras.getInt(TERM_ID_KEY);
            courseId = extras.getInt(COURSE_ID_KEY);
            mViewModel.loadData(courseId,termId);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    toggleEditCourse.setChecked(false);
                    toggleEditCourse.setVisibility(View.VISIBLE);
                    createCourseView.setVisibility(View.INVISIBLE);
                    setEditDisabled();
                    course = mViewModel.mLiveCourse.getValue();
                    term = mViewModel.mLiveTermCourseEditor.getValue();
                    mTermName.setText("   Term Name: "+term.getTermName());
                    try{
                        if(course.getSDateAlert()) {
                            alertStartDateSwitch.setOnCheckedChangeListener(null);
                            alertStartDateSwitch.setChecked(true);
                            alertStartDateSwitch.setOnCheckedChangeListener(alertStartDateListener);
                        }
                        if(course.getEDateAlert()){
                            alertEndDateSwitch.setOnCheckedChangeListener(null);
                            alertEndDateSwitch.setChecked(true);
                            alertEndDateSwitch.setOnCheckedChangeListener(alertEndDateListener);
                        }
                    }
                    //This catches a user returning from a notification that was for a deleted course object
                    catch (NullPointerException npe){
                        Toast toast = Toast.makeText(getApplicationContext(), "Course has been deleted!", Snackbar.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    }
                }
            }, 300);
        }
        mViewModel.mLiveCourse.observe(this, new Observer<Course>() {
            @Override
            public void onChanged(@Nullable Course courseEntity) {
                if (courseEntity != null && !mEditing) {
                    course = courseEntity;
                    mCourseName.setText(courseEntity.getCourseName());
                    mCourseStart.setText(dateFormat.format(courseEntity.getStartDate()));
                    mCourseEnd.setText(dateFormat.format(courseEntity.getEndDate()));
                    mCourseMentor.setText(courseEntity.mentorToString());
                    mCourseNote.setText(courseEntity.getCourseNote());
                    mCourseAssessmentList.setText(courseEntity.assessmentListToString());
                    alertStartDateSwitch.setOnCheckedChangeListener(null);
                    alertStartDateSwitch.setChecked(courseEntity.getSDateAlert());
                    alertStartDateSwitch.setOnCheckedChangeListener(alertStartDateListener);
                    alertEndDateSwitch.setOnCheckedChangeListener(null);
                    alertEndDateSwitch.setChecked(courseEntity.getEDateAlert());
                    alertEndDateSwitch.setOnCheckedChangeListener(alertEndDateListener);
                    courseStatus = courseEntity.getCourseStatus();
                    switch (courseStatus){
                        case "In Progress":
                            statusInProgress.setChecked(true);
                            break;
                        case "Complete":
                            statusComplete.setChecked(true);
                            break;
                        case "Dropped":
                            statusDropped.setChecked(true);
                            break;
                        case "Plan To Take":
                            statusPlanToTake.setChecked(true);
                            break;

                    }
                }
            }
        });
    }

    private void removeAssociatedCourseAlerts() {
        if(timePickerIntent == null){
            timePickerIntent = new Intent(this, NotificationReceiver.class);
        }
        PendingIntent sender1 = PendingIntent.getBroadcast(this, course.getCourseId(),timePickerIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager1.cancel(sender1);
        PendingIntent sender2 = PendingIntent.getBroadcast(this, course.getCourseId()+10000,timePickerIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.cancel(sender2);
        if(course.getAssessmentList().size() > 0){
            for(Assessment assessment:course.getAssessmentList()){
                PendingIntent senderLoop = PendingIntent.getBroadcast(this, assessment.getAssessmentId() + 1000000,timePickerIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManagerLoop = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManagerLoop.cancel(senderLoop);
            }
        }
    }

    private Boolean saveAndReturn(Boolean returnToPreviousActivity) {
        term = mViewModel.mLiveTermCourseEditor.getValue();
        course = mViewModel.mLiveCourse.getValue();
        try {
            if(mCourseName.getText().toString().contentEquals("")||mCourseName == null){
                Toast toast = Toast.makeText(this, "Please enter a name for the course!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(!statusComplete.isChecked()&&!statusDropped.isChecked()&&!statusPlanToTake.isChecked()&&!statusInProgress.isChecked()){
                Toast toast = Toast.makeText(this, "Please choose a status for the course!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(!statusInProgress.isChecked()&&!statusPlanToTake.isChecked()&&!statusDropped.isChecked()&&!statusComplete.isChecked()){
                Toast toast = Toast.makeText(this, "Please choose a type for the assessment!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(mCourseStart.getText().toString().contentEquals("")||mCourseStart == null){
                Toast toast = Toast.makeText(this, "Please enter a start date for the course!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(mCourseEnd.getText().toString().contentEquals("")||mCourseEnd == null){
                Toast toast = Toast.makeText(this, "Please enter an end date for the course!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            Date date1 = new Date(mCourseStart.getText().toString());
            Date date2 = new Date(mCourseEnd.getText().toString());
            if(date2.before(date1)){
                Toast toast = Toast.makeText(this, "Start date must be before end date!", Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(date1.before(term.getStartDate())||date2.before(term.getStartDate())||term.getEndDate()
                    .before(date1)||term.getEndDate().before(date2)){
                Toast toast = Toast.makeText(this, ("Start/End dates must be between "+ dateFormat.format(term.getStartDate())+ " & " + dateFormat.format(term.getEndDate())), Snackbar.LENGTH_LONG);
                toast.show();
                return false;
            }
            if(course == null){
                courseId = mViewModel.saveCourse(mNewCourse,termId,mCourseName.getText().toString().trim(), date1, date2,courseStatus,"","","",mCourseNote.getText().toString().trim(),new ArrayList<Assessment>(),false,false);
                mNewCourse = false;
                mViewModel.mLiveTermCourseEditor.postValue(term);
                Toast toast = Toast.makeText(this, "Course Saved!", Snackbar.LENGTH_LONG);
                toast.show();
            }else{

                courseId = mViewModel.saveCourse(mNewCourse, courseId,mCourseName.getText().toString().trim(), date1, date2,courseStatus,course.getCourseMentorName(),course.getCourseMentorPhone(),course.getCourseMentorEmail(),mCourseNote.getText().toString().trim(),course.getAssessmentList(),course.getSDateAlert(),course.getEDateAlert());
                mNewCourse = false;
                mViewModel.mLiveTermCourseEditor.postValue(term);
                mViewModel.mLiveCourse.postValue(course);
                Toast toast = Toast.makeText(this, "Course Saved!", Snackbar.LENGTH_LONG);
                toast.show();
            }

            if(returnToPreviousActivity){
                finish();
                return true;
            }
            return true;
            //prints exception - this was used for debugging, user will never reach this error due to other in place protections.
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, ("Input Error: "+e.toString()), Snackbar.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
            return false;
        }

    }

    public void setEditEnabled(){
        mCourseName.setEnabled(true);
        statusInProgress.setEnabled(true);
        statusComplete.setEnabled(true);
        statusDropped.setEnabled(true);
        statusPlanToTake.setEnabled(true);
        mCourseStart.setEnabled(true);
        mCourseEnd.setEnabled(true);
        mCourseNote.setEnabled(true);
        alertStartDateSwitch.setEnabled(true);
        alertEndDateSwitch.setEnabled(true);
        editAssessmentListBtn.setVisibility(View.INVISIBLE);
        editInstructorBtn.setVisibility(View.INVISIBLE);
        pickStartDateBtn.setVisibility(View.VISIBLE);
        pickEndDateBtn.setVisibility(View.VISIBLE);
    }

    public void setEditDisabled(){
        mCourseName.setEnabled(false);
        statusInProgress.setEnabled(false);
        statusComplete.setEnabled(false);
        statusDropped.setEnabled(false);
        statusPlanToTake.setEnabled(false);
        mCourseStart.setEnabled(false);
        mCourseEnd.setEnabled(false);
        mCourseNote.setEnabled(false);
        alertStartDateSwitch.setEnabled(false);
        alertEndDateSwitch.setEnabled(false);
        editAssessmentListBtn.setVisibility(View.VISIBLE);
        editInstructorBtn.setVisibility(View.VISIBLE);
        pickStartDateBtn.setVisibility(View.INVISIBLE);
        pickEndDateBtn.setVisibility(View.INVISIBLE);
    }

    private void updateLabel(int dateOrTimeFieldToSetCode) {
        String myFormatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormatDate, Locale.US);
        switch (dateOrTimeFieldToSetCode){
            case 1:
                mCourseStart.setText(sdf.format(datePicker.getTime()));
                break;
            case 2:
                mCourseEnd.setText(sdf.format(datePicker.getTime()));
                break;
            case 3:
                alarmTime = Calendar.getInstance();
                alarmTime.setTime(timePicker.getTime());
                break;
        }
    }
}
