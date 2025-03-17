package com.denprog.codefestapp.destinations.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.databinding.FragmentApplicationCardBinding;
import com.denprog.codefestapp.room.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationRecyclerViewAdapter extends RecyclerView.Adapter<ApplicationRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues = new ArrayList<>(Collections.emptyList());
    private ApplicationItemInterface itemInterface;

    public ApplicationRecyclerViewAdapter(ApplicationItemInterface itemInterface) {
        this.itemInterface = itemInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentApplicationCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    public void refreshList (List<User> userList) {
        this.mValues.clear();
        this.mValues.addAll(userList);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.itemView.setOnClickListener(view -> itemInterface.view(holder.mItem.userId));
        String fullName = holder.mItem.firstName + " " + holder.mItem.middleName + " " + holder.mItem.lastName;

        holder.binding.emailDisplay.setText(holder.mItem.email);
        holder.binding.fullnameDisplay.setText(fullName);
        holder.binding.roleDisplay.setText(holder.mItem.roleName);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mItem;
        public FragmentApplicationCardBinding binding;

        public ViewHolder(FragmentApplicationCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public String toString() {
            return super.toString() + " '" +mItem.middleName + "'";
        }
    }

    public interface ApplicationItemInterface {
        void view(int userId);
    }

}