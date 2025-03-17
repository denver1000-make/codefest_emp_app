package com.denprog.codefestapp.destinations.employee.inbox;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denprog.codefestapp.destinations.employee.inbox.placeholder.PlaceholderContent.PlaceholderItem;
import com.denprog.codefestapp.databinding.FragmentChatThreadBinding;
import com.denprog.codefestapp.room.view.ChatThreadWithEmployeeName;

import java.util.ArrayList;
import java.util.List;
public class ChatThreadRecyclerViewAdapter extends RecyclerView.Adapter<ChatThreadRecyclerViewAdapter.ViewHolder> {

    private final List<ChatThreadWithEmployeeName> mValues = new ArrayList<>();

    private final ChatThreadInteraction chatThreadInteraction;

    public ChatThreadRecyclerViewAdapter(ChatThreadInteraction chatThreadInteraction) {
        this.chatThreadInteraction = chatThreadInteraction;
    }

    public void refreshList(List<ChatThreadWithEmployeeName> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentChatThreadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).firstName);
        holder.mContentView.setText(mValues.get(position).middleName);
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatThreadInteraction.onOpenChat(holder.mItem.employerId, holder.mItem.threadId);
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
        public ChatThreadWithEmployeeName mItem;

        public ViewHolder(FragmentChatThreadBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface ChatThreadInteraction {
        void onOpenChat(int employerId, int threadId);
    }
}