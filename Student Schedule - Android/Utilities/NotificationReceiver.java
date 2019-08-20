package com.benjaminlucaswebdesigns.studentprogresstracker.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity.AssessmentEditor;
import com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity.CourseEditor;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ALERT_ID;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_DUE_DATE_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_NAME_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.CHANNEL_ID;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_NAME_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_START_DATE_ALERT;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_NAME_ALERT;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent returningIntent;
        PendingIntent pendingIntent;
        Bundle extras = intent.getExtras();
        String title;
        String message;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        switch(extras.getInt(ALERT_ID)){
            case 0:
                returningIntent = new Intent(context, CourseEditor.class);
                returningIntent.putExtra(TERM_ID_KEY,extras.getInt(TERM_ID_KEY));
                returningIntent.putExtra(COURSE_ID_KEY,extras.getInt(COURSE_ID_KEY));
                returningIntent.putExtra(NEW_COURSE_FLAG,extras.getInt(NEW_COURSE_FLAG));
                pendingIntent = PendingIntent.getActivity(context,extras.getInt(COURSE_ID_KEY),returningIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                returningIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                title = "Course Starting Alert!";
                message = "Course: " + extras.getString(COURSE_NAME_ALERT).trim()+ " in term: " + extras.getString(TERM_NAME_ALERT).trim()  + "\n, begins on: " + extras.get(COURSE_START_DATE_ALERT).toString().trim() + "! Have fun with that!";

                builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_event_note_white)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);
                notificationManager.notify(extras.getInt(COURSE_ID_KEY),builder.build());
                break;
            case 1:
                returningIntent = new Intent(context, CourseEditor.class);
                returningIntent.putExtra(TERM_ID_KEY,extras.getInt(TERM_ID_KEY));
                returningIntent.putExtra(COURSE_ID_KEY,extras.getInt(COURSE_ID_KEY));
                returningIntent.putExtra(NEW_COURSE_FLAG,extras.getInt(NEW_COURSE_FLAG));
                pendingIntent = PendingIntent.getActivity(context,extras.getInt(COURSE_ID_KEY)+10000,returningIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                returningIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                title = "Course Ending Alert!";
                message = "Course: " + extras.getString(COURSE_NAME_ALERT).trim()+ " in term: " + extras.getString(TERM_NAME_ALERT).trim()  + ", \nends on: " + extras.get(COURSE_START_DATE_ALERT).toString().trim() + "! Have fun with that!";
                builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_event_note_white)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);
                notificationManager.notify(extras.getInt(COURSE_ID_KEY)+10000,builder.build());
                break;
            case 2:
                returningIntent = new Intent(context, AssessmentEditor.class);
                returningIntent.putExtra(TERM_ID_KEY,extras.getInt(TERM_ID_KEY));
                returningIntent.putExtra(COURSE_ID_KEY,extras.getInt(COURSE_ID_KEY));
                returningIntent.putExtra(ASSESSMENT_ID_KEY,extras.getInt(ASSESSMENT_ID_KEY));
                returningIntent.putExtra(NEW_COURSE_FLAG,extras.getInt(NEW_COURSE_FLAG));
                pendingIntent = PendingIntent.getActivity(context,extras.getInt(ASSESSMENT_ID_KEY)+1000000,returningIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                returningIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                title = "Upcoming Assessment Alert!";
                message = "Assessment: " + extras.getString(ASSESSMENT_NAME_ALERT)+ " from Course: " + extras.getString(COURSE_NAME_ALERT).trim()+ " \nin term: " + extras.getString(TERM_NAME_ALERT).trim()  + ", is due on: " + extras.get(ASSESSMENT_DUE_DATE_ALERT).toString().trim();
                builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_event_note_white)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);
                notificationManager.notify(extras.getInt(ASSESSMENT_ID_KEY)+1000000,builder.build());
                break;
        }




    }
}
