package com.denprog.codefestapp.destinations.employer.chat_with_employee;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentChatItemBinding;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private final List<PrivateChatItemText> mValues = new ArrayList<PrivateChatItemText>(Collections.emptyList());
    int senderId;

    public ChatRecyclerViewAdapter(int senderId) {
        this.senderId = senderId;
    }

    public void refreshList(List<PrivateChatItemText> items) {
        this.mValues.clear();
        this.mValues.addAll(items);
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentChatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).chatContent);

        if (senderId == holder.mItem.senderId) {
            holder.binding.parentLayout.setGravity(Gravity.END);
        } else {
            holder.binding.parentLayout.setGravity(Gravity.START);
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PrivateChatItemText mItem;
        public FragmentChatItemBinding binding;

        public ViewHolder(FragmentChatItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            this.binding = binding;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}