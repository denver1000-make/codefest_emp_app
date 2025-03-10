package com.denprog.codefestapp.destinations.employee.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentJobPostingApplicationDialogListBinding;
import com.denprog.codefestapp.destinations.employee.dialog.placeholder.PlaceholderContent;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;
import com.denprog.codefestapp.util.FileUtil;

import java.util.List;

public class JobPostingApplicationDialogFragment extends DialogFragment {

    ActivityResultLauncher<Intent> filePicker = requireActivity().registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK && o.getData() != null) {
                if (o.getData().getData() != null) {
                    String filePath = FileUtil.getFileName(requireContext(), o.getData().getData());
                    viewModel.addFile(new JobPostingApplicationFile(filePath));
                }
            }
        }
    });

    FragmentJobPostingApplicationDialogListBinding binding;
    ApplicationFileRecyclerViewAdapter adapter;
    JobPostingDialogViewModel viewModel;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        this.viewModel = new ViewModelProvider(requireActivity()).get(JobPostingDialogViewModel.class);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
        binding = FragmentJobPostingApplicationDialogListBinding.inflate(getLayoutInflater());

        adapter = new ApplicationFileRecyclerViewAdapter(new ApplicationFileRecyclerViewAdapter.ApplicationFileRecyclerViewInterface() {
            @Override
            public void onAdd(JobPostingApplicationFile jobPostingApplicationFile) {

            }

            @Override
            public void onRemove(int index) {
                viewModel.remove(index);
            }
        });

        viewModel.jobPostingApplicationFiles.observe(requireActivity(), jobPostingApplicationFiles -> adapter.refreshList(jobPostingApplicationFiles));

        binding.addFileAction.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            filePicker.launch(intent);
        });

        alertDialogBuilder.setView(binding.getRoot());
        return alertDialogBuilder.create();
    }
}