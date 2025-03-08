package com.denprog.codefestapp.destinations.employer;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denprog.codefestapp.destinations.placeholder.PlaceholderContent.PlaceholderItem;
import com.denprog.codefestapp.databinding.FragmentJobPostingItemBinding;
import com.denprog.codefestapp.room.entity.JobPosting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobPostingRecyclerViewAdapter extends RecyclerView.Adapter<JobPostingRecyclerViewAdapter.ViewHolder> {

    private final List<JobPosting> mValues = new ArrayList<>(Collections.emptyList());
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
        holder.mIdView.setText(mValues.get(position).postingId + "");
        holder.mContentView.setText(mValues.get(position).postingName);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public JobPosting mItem;

        public ViewHolder(FragmentJobPostingItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}