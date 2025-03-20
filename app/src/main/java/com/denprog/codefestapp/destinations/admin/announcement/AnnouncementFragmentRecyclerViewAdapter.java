package com.denprog.codefestapp.destinations.admin.announcement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentAnnouncementBinding;
import com.denprog.codefestapp.destinations.admin.announcement.placeholder.PlaceholderContent.PlaceholderItem;
import com.denprog.codefestapp.room.entity.Announcement;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AnnouncementFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<Announcement> mValues = new ArrayList<>();
    private AnnouncementInteraction announcementInteraction;

    public AnnouncementFragmentRecyclerViewAdapter(AnnouncementInteraction announcementInteraction) {
        this.announcementInteraction = announcementInteraction;
    }

    public void refreshAdapter(List<Announcement> announcements) {
        this.mValues.clear();
        this.mValues.addAll(announcements);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAnnouncementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).announcementName);
        holder.fragmentAnnouncementBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementInteraction.onOpen(holder.mItem.announcementId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Announcement mItem;
        public FragmentAnnouncementBinding fragmentAnnouncementBinding;

        public ViewHolder(FragmentAnnouncementBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            this.fragmentAnnouncementBinding = binding;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface AnnouncementInteraction {
        void onOpen(int announcementId);
    }

}