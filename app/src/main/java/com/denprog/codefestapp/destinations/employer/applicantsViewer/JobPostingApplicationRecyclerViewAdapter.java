package com.denprog.codefestapp.destinations.employer.applicantsViewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentJobPostingApplicationBinding;
import com.denprog.codefestapp.room.view.JobPostingApplicationAndEmployeeInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobPostingApplicationRecyclerViewAdapter extends RecyclerView.Adapter<JobPostingApplicationRecyclerViewAdapter.ViewHolder> {

    private final List<JobPostingApplicationAndEmployeeInfo> mValues = new ArrayList<>(Collections.emptyList());

    private final UserItemInteraction userItemInteraction;

    public JobPostingApplicationRecyclerViewAdapter(UserItemInteraction userItemInteraction) {
        this.userItemInteraction = userItemInteraction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentJobPostingApplicationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    public void refreshList(List<JobPostingApplicationAndEmployeeInfo> jobPostingApplications) {
        this.mValues.clear();
        this.mValues.addAll(jobPostingApplications);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).email);
        holder.downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userItemInteraction.onDownloadFiles(holder.mItem.jobPostingApplicationId);
            }
        });
        holder.binding.openChatAction.setOnClickListener(view -> {
             userItemInteraction.onChat(holder.mItem.employeeId);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button downloadFile;
        public final FragmentJobPostingApplicationBinding binding;
        public JobPostingApplicationAndEmployeeInfo mItem;

        public ViewHolder(FragmentJobPostingApplicationBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            this.downloadFile = binding.downloadFilesAction;
            this.binding = binding;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface UserItemInteraction {
        void onChat(int employeeId);
        void onDownloadFiles(int jobPostingApplication);
        void onShowDecisionDialog();
    }
}