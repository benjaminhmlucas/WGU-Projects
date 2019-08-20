package com.benjaminlucaswebdesigns.studentprogresstracker.TermActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Course;
import com.benjaminlucaswebdesigns.studentprogresstracker.ObjectClasses.Term;
import com.benjaminlucaswebdesigns.studentprogresstracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.benjaminlucaswebdesigns.studentprogresstracker.Utilities.Constants.TERM_ID_KEY;

class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ViewHolder>{

    private final List<Term> mTerms;
    private final Context mContext;

    public TermListAdapter(List<Term> mTerms, Context mContext) {
        this.mTerms = mTerms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Term term = mTerms.get(position);
        holder.mTermName.setText(term.getTermName());
        int totalCourses = term.getCourseList().size();
        int totalCompleteCourses = 0;
        if(term.getCourseList().size()>0){
            for(Course course:term.getCourseList()){
                if (course.getCourseStatus().equals("Complete")){
                    totalCompleteCourses++;
                }
            }
        }
        holder.pBarTermComplete.setMax(totalCourses);
        holder.pBarTermComplete.setProgress(totalCompleteCourses);
        holder.coursesCompletedTextView.setText(totalCompleteCourses+" out of "+totalCourses+" courses completed");
        holder.editTermFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TermEditor.class);
                intent.putExtra(TERM_ID_KEY, term.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return mTerms.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.term_name_in_list)
        TextView mTermName;
        @BindView(R.id.edit_term_fab)
        FloatingActionButton editTermFab;
        @BindView(R.id.progress_bar_term_list)
        ProgressBar pBarTermComplete;
        @BindView(R.id.text_view_term_list)
        TextView coursesCompletedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
