package com.denprog.codefestapp.destinations.employee.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentJobPostingApplicationItemBinding;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ApplicationFileRecyclerViewAdapter extends RecyclerView.Adapter<ApplicationFileRecyclerViewAdapter.ViewHolder> {

    private final List<JobPostingApplicationFile> mValues = new ArrayList<>(Collections.emptyList());
    private final ApplicationFileRecyclerViewInterface applicationFileRecyclerViewInterface;
    public ApplicationFileRecyclerViewAdapter (ApplicationFileRecyclerViewInterface applicationFileRecyclerViewInterface) {
        this.applicationFileRecyclerViewInterface = applicationFileRecyclerViewInterface;
    }

    public void removeItem(int index) {
        mValues.remove(index);
        notifyItemRemoved(index);
    }

    public void refreshList(List<JobPostingApplicationFile> jobPostingApplicationFiles) {
        mValues.clear();
        mValues.addAll(jobPostingApplicationFiles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentJobPostingApplicationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.binding.fileName.setText(holder.mItem.pathOfFile);
        holder.binding.button.setOnClickListener(view -> {
            applicationFileRecyclerViewInterface.onRemove(position);
        });
    }

    public interface ApplicationFileRecyclerViewInterface {
        void onAdd(JobPostingApplicationFile jobPostingApplicationFile);
        void onRemove(int index);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public JobPostingApplicationFile mItem;
        public FragmentJobPostingApplicationItemBinding binding;

        public ViewHolder(FragmentJobPostingApplicationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mItem.jobPostingApplicationFileId + "'";
        }
    }
}