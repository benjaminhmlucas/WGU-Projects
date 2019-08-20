package com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity;


import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.view.Menu;
import android.view.MenuItem;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;
import com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.NotificationReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermList extends AppCompatActivity {

    @BindView(R.id.recycler_view_term_list)
    RecyclerView mRecyclerView;

    private List<Term> termsData = new ArrayList<>();
    private TermListAdapter tAdapter;
    private TermListViewModel tViewModel;
    private MenuItem addSample;
    private MenuItem deleteAll;
    private MenuItem debugToggleOn;
    private MenuItem debugToggleOff;
    private Boolean debugOn = false;

    @OnClick(R.id.add_term_fab_term_list)
    void addTermClickHandler(){
        Intent intent = new Intent(this, TermEditor.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        ButterKnife.bind(this);
        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        final Observer<List<Term>> termsObserver =
                new Observer<List<Term>>() {
                    @Override
                    public void onChanged(@Nullable List<Term> termEntities) {
                        termsData.clear();
                        termsData.addAll(termEntities);

                        if (tAdapter == null) {
                            tAdapter = new TermListAdapter(termsData,
                                    TermList.this);
                            mRecyclerView.setAdapter(tAdapter);
                        } else {
                            tAdapter.notifyDataSetChanged();
                        }

                    }
                };

        tViewModel = ViewModelProviders.of(this)
                .get(TermListViewModel.class);
        tViewModel.mTerms.observe(this, termsObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        tAdapter = new TermListAdapter(termsData,this);
        mRecyclerView.setAdapter(tAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        addSample = menu.findItem(R.id.add_sample_terms);
        deleteAll = menu.findItem(R.id.delete_all_data);
        debugToggleOn = menu.findItem(R.id.debug_mode_toggle_on);
        debugToggleOff = menu.findItem(R.id.debug_mode_toggle_off);
        if(debugOn){
            debugToggleOn.setChecked(true);
            debugToggleOff.setChecked(false);
            deleteAll.setVisible(true);
            addSample.setVisible(true);
        } else {
            debugToggleOn.setChecked(false);
            debugToggleOff.setChecked(true);
            deleteAll.setVisible(false);
            addSample.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.debug_mode_toggle_on){
            addSample.setVisible(true);
            deleteAll.setVisible(true);
            debugToggleOff.setChecked(false);
            debugToggleOn.setChecked(true);
            debugOn = true;
            return true;
        }
        if (id == R.id.debug_mode_toggle_off){
            addSample.setVisible(false);
            deleteAll.setVisible(false);
            debugToggleOff.setChecked(true);
            debugToggleOn.setChecked(false);
            debugOn = false;
            return true;
        }
        if (id == R.id.add_sample_terms) {
            addSampleTerms();
            return true;
        } else if (id == R.id.delete_all_data) {
            removeAllAlerts();
            deleteAllTerms();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTerms() {
        tViewModel.deleteAllTerms();
    }

    private void addSampleTerms() {
        tViewModel.addSampleTerms();
    }

    void removeAllAlerts(){
        Intent intent = new Intent(this, NotificationReceiver.class);
        for(Term term : termsData){
            for(Course course:term.getCourseList()){
                PendingIntent sender1 = PendingIntent.getBroadcast(this, course.getCourseId(),intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager1.cancel(sender1);
                PendingIntent sender2 = PendingIntent.getBroadcast(this, course.getCourseId()+10000,intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager2.cancel(sender2);
                if(course.getAssessmentList().size() > 0){
                    for(Assessment assessment:course.getAssessmentList()){
                        PendingIntent senderLoop = PendingIntent.getBroadcast(this, assessment.getAssessmentId() + 1000000,intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManagerLoop = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManagerLoop.cancel(senderLoop);
                    }
                }
            }
        }
    }
}
