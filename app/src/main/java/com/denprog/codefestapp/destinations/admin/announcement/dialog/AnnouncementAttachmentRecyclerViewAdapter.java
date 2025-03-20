package com.denprog.codefestapp.destinations.admin.announcement.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentAddAnnouncementDialogBinding;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAttachmentRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementAttachmentRecyclerViewAdapter.ViewHolder> {

    private final List<AnnouncementAttachment> mValues = new ArrayList<>();

    public void refreshList(List<AnnouncementAttachment> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentAddAnnouncementDialogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.fileName);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public AnnouncementAttachment mItem;

        public ViewHolder(FragmentAddAnnouncementDialogBinding binding) {
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