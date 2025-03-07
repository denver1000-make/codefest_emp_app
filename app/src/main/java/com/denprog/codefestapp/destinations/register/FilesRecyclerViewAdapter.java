package com.denprog.codefestapp.destinations.register;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FileItemBinding;
import com.denprog.codefestapp.util.SelectedFile;

import java.util.ArrayList;
import java.util.List;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.MyViewHolder> {

    List<SelectedFile> selectedFiles = new ArrayList<>();
    ItemAction itemAction;

    public FilesRecyclerViewAdapter(ItemAction itemAction, List<SelectedFile> selectedFiles) {
        this.itemAction = itemAction;
        this.selectedFiles = selectedFiles;
    }

    public List<SelectedFile> getSelectedFiles () {
        return selectedFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelectedFile selectedFile = selectedFiles.get(position);
        holder.binding.fileNameTitle.setText(selectedFile.fileName);
        holder.binding.removeFileBtn.setOnClickListener(view -> {
            itemAction.onFileRemoved(position);
        });
    }

    public void fileDeleted(int index) {
        selectedFiles.remove(index);
        this.notifyItemRemoved(index);
    }

    public void addFile(SelectedFile selectedFile) {
        selectedFiles.add(selectedFile);
        notifyItemInserted(selectedFiles.size() - 1);
    }

    @Override
    public int getItemCount() {
        return selectedFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public FileItemBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = FileItemBinding.bind(itemView);
        }
    }

    public interface ItemAction {
        void onFileRemoved(int index);
    }

}
