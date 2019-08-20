package com.benjaminlucaswebdesigns.studentprogresstracker.AssessmentActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Assessment;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.ASSESSMENT_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.COURSE_ID_KEY;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.NEW_COURSE_FLAG;
import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.ViewHolder>{

    private final List<Assessment> mAssessments;
    private final Context mContext;

    public AssessmentListAdapter(List<Assessment> mAssessments, Context mContext) {
        this.mAssessments = mAssessments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Assessment assessment = mAssessments.get(position);
        holder.mTextView.setText(assessment.getAssessmentName());
        holder.mEditAssessmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AssessmentEditor.class);
                intent.putExtra(COURSE_ID_KEY, assessment.getCourseId());
                intent.putExtra(TERM_ID_KEY, assessment.getTermId());
                intent.putExtra(ASSESSMENT_ID_KEY, assessment.getAssessmentId());
                intent.putExtra(NEW_COURSE_FLAG,0);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return mAssessments.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.assessment_name_in_list)
        TextView mTextView;
        @BindView(R.id.edit_assessment_fab)
        FloatingActionButton mEditAssessmentFab;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
