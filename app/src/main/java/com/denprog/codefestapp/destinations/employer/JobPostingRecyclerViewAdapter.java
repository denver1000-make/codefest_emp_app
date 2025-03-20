package com.denprog.codefestapp.destinations.employer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentJobPostingItemBinding;
import com.denprog.codefestapp.room.entity.JobPosting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobPostingRecyclerViewAdapter extends RecyclerView.Adapter<JobPostingRecyclerViewAdapter.ViewHolder> {

    private final List<JobPosting> mValues = new ArrayList<>(Collections.emptyList());
    private EmployerJobPostingInterface employerJobPostingInterface;
    public interface EmployerJobPostingInterface {
        void onOpen(int jobPostingId);
    }

    public JobPostingRecyclerViewAdapter(EmployerJobPostingInterface employerJobPostingInterface) {
        this.employerJobPostingInterface = employerJobPostingInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentJobPostingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void refreshList(List<JobPosting> jobPostingList) {
        mValues.clear();
        mValues.addAll(jobPostingList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.binding.jobPostingName.setText(holder.mItem.postingName);
        holder.binding.jobPostingDesc.setText(holder.mItem.postingDescription);
        holder.binding.minSalary.setText(holder.mItem.minSalary + "Pesos");
        holder.binding.maxSalary.setText(holder.mItem.maxSalary + "Pesos");
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employerJobPostingInterface.onOpen(holder.mItem.postingId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public JobPosting mItem;

        FragmentJobPostingItemBinding binding;
        public ViewHolder(FragmentJobPostingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + binding.jobPostingName + "'";
        }
    }
}