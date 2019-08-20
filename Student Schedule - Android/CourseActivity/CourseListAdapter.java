package com.benjaminlucaswebdesigns.studentprogresstracker.CourseActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder>{

    public static final String NEW_COURSE_FLAG = "new_course_flag";
    private final List<Course> mCourses;
    private final Context mContext;

    public CourseListAdapter(List<Course> mCourses, Context mContext) {
        this.mCourses = mCourses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Course course = mCourses.get(position);
        holder.mTextView.setText(course.getCourseName());

        holder.mEditCourseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseEditor.class);
                intent.putExtra(COURSE_ID_KEY, course.getCourseId());
                intent.putExtra(TERM_ID_KEY, course.getTermId());
                intent.putExtra(NEW_COURSE_FLAG,0);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return mCourses.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_name_in_list)
        TextView mTextView;
        @BindView(R.id.edit_course_fab)
        FloatingActionButton mEditCourseFab;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

