package com.benjaminlucaswebdesigns.studentprogresstracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity.TermList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.main_term_button)
    void termClickHandler() {
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_exit_button)
    void exitClickHandler() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//
        ButterKnife.bind(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PeanutButter1 Channel";
            String description = "Notifications Channel for App";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
