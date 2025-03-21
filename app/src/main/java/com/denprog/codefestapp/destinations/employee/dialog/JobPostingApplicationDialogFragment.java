package com.denprog.codefestapp.destinations.employee.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.databinding.FragmentJobPostingApplicationDialogListBinding;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;
import com.denprog.codefestapp.util.FileUtil;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

public class JobPostingApplicationDialogFragment extends Fragment {
    public static final String EMPLOYEE_ID_BUNDLE_KEY = "EMPLOYEE_ID";
    public static final String JOB_POSTING_ID_BUNDLE_KEY = "JOB_POSTING_ID";
    JobPostingDialogViewModel viewModel;
    ActivityResultLauncher<Intent> filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
        if (o.getResultCode() == Activity.RESULT_OK && o.getData() != null) {
            if (o.getData().getData() != null) {
                Uri uri = o.getData().getData();
                String filePath = FileUtil.getFileName(requireContext(), uri);
                JobPostingApplicationFile jobPostingApplicationFile = new JobPostingApplicationFile(filePath);
                jobPostingApplicationFile.setUri(uri);
                viewModel.addFile(jobPostingApplicationFile);
            }
        }
    });
    FragmentJobPostingApplicationDialogListBinding binding;
    ApplicationFileRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJobPostingApplicationDialogListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewModel = new ViewModelProvider(requireActivity()).get(JobPostingDialogViewModel.class);

        JobPostingApplicationDialogFragmentArgs args = JobPostingApplicationDialogFragmentArgs.fromBundle(getArguments());
        int employeeId = args.getEmployeeId();
        int jobPostingId = args.getJobPostingId();
        if (employeeId != -1 && jobPostingId != -1) {
            this.viewModel.credentialStatus.setValue(new UIState.Success<>(new JobPostingIdAndEmployeeId(employeeId, jobPostingId)));
        }

        adapter = new ApplicationFileRecyclerViewAdapter(new ApplicationFileRecyclerViewAdapter.ApplicationFileRecyclerViewInterface() {
            @Override
            public void onAdd(JobPostingApplicationFile jobPostingApplicationFile) {

            }

            @Override
            public void onRemove(int index) {
                viewModel.remove(index);
            }
        });

        this.viewModel.credentialStatus.observe(getViewLifecycleOwner(), jobPostingIdAndEmployeeIdUIState -> {
            if (jobPostingIdAndEmployeeIdUIState instanceof UIState.Success) {
                JobPostingIdAndEmployeeId jobPostingIdAndEmployeeId = ((UIState.Success<JobPostingIdAndEmployeeId>) jobPostingIdAndEmployeeIdUIState).data;
                int employeeId1 = jobPostingIdAndEmployeeId.employeeId;
                int jobPostingId1 = jobPostingIdAndEmployeeId.jobPostingId;

                viewModel.jobPostingApplicationFiles.observe(requireActivity(),

                        jobPostingApplicationFiles -> adapter.refreshList(jobPostingApplicationFiles));

                binding.addFileAction.setOnClickListener(view2 -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    filePicker.launch(intent);
                });

                binding.applyForJob.setOnClickListener(view1 -> viewModel.insertApplication(employeeId1, jobPostingId1, requireContext(), new OnOperationSuccessful<>() {
                    @Override
                    public void onSuccess(Void data) {
                        binding.addFileAction.setEnabled(true);
                    }

                    @Override
                    public void onError(String message) {
                        binding.addFileAction.setEnabled(true);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading() {
                        binding.addFileAction.setEnabled(false);
                    }
                }));
                binding.list.setAdapter(adapter);
                binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));

            } else if (jobPostingIdAndEmployeeIdUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<JobPostingIdAndEmployeeId>) jobPostingIdAndEmployeeIdUIState).message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static final class JobPostingIdAndEmployeeId {
        public int employeeId;
        public int jobPostingId;

        public JobPostingIdAndEmployeeId(int employeeId, int jobPostingId) {
            this.employeeId = employeeId;
            this.jobPostingId = jobPostingId;
        }
    }
}