package com.benjaminlucaswebdesigns.studentprogresstracker.Utilities;

import android.arch.persistence.room.TypeConverter;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ObjectToSQLConverter {

    @TypeConverter
    public static ArrayList<Course> stringToCourseList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<Course>();
        }

        Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String courseListToString(ArrayList<Course> someCourses) {
        Gson gson = new Gson();
        return gson.toJson(someCourses);
    }


}
