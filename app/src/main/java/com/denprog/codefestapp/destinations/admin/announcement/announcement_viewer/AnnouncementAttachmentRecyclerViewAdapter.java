package com.denprog.codefestapp.destinations.admin.announcement.announcement_viewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.AnnouncementAttachmentItemInteractibleBinding;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAttachmentRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementAttachmentRecyclerViewAdapter.ViewHolder> {

    List<AnnouncementAttachment> announcementAttachmentList = new ArrayList<>();
    AttachmentInteraction attachmentInteraction;

    public AnnouncementAttachmentRecyclerViewAdapter(AttachmentInteraction attachmentInteraction) {
        this.attachmentInteraction = attachmentInteraction;
    }

    public void refreshList(List<AnnouncementAttachment> announcementAttachmentList) {
        this.announcementAttachmentList.clear();;
        this.announcementAttachmentList.addAll(announcementAttachmentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AnnouncementAttachmentItemInteractibleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnnouncementAttachment announcementAttachment = announcementAttachmentList.get(position);
        holder.binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentInteraction.onDownload(announcementAttachment.filePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcementAttachmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AnnouncementAttachmentItemInteractibleBinding binding;

        public ViewHolder(@NonNull AnnouncementAttachmentItemInteractibleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface AttachmentInteraction {
        void onDownload(String filePath);
    }

}
