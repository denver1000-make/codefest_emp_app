package com.denprog.codefestapp.destinations.employer.applicantsViewer;

import static com.denprog.codefestapp.HomeActivityViewModel.EMAIL_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.databinding.FragmentJobPostingViewerBinding;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

public class JobPostingApplicationFragment extends Fragment implements JobApplicantViewerViewModel.FileDownloadProgress {
    private FragmentJobPostingViewerBinding binding;
    private JobApplicantViewerViewModel viewModel;
    private JobPostingApplicationRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentJobPostingViewerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(JobApplicantViewerViewModel.class);
        JobPostingApplicationFragmentArgs args = JobPostingApplicationFragmentArgs.fromBundle(getArguments());
        int jobPostingId = args.getJobPostingId();
        int employerId = requireActivity().getIntent().getIntExtra(EMPLOYER_ID_BUNDLE_KEY, -1);
        String email = requireActivity().getIntent().getStringExtra(EMAIL_ID_BUNDLE_KEY);
        if (employerId != -1 && email != null) {
            // Lol
            // TODO: Make Dedicated Class to contain email and id.
            this.viewModel.mutableEmployerIdLiveData.setValue(new UIState.Success<>(employerId));
        } else {
            this.viewModel.mutableEmployerIdLiveData.setValue(new UIState.Fail<>("Employer Not Logged In"));
        }

        this.viewModel.mutableEmployerIdLiveData.observe(getViewLifecycleOwner(), integerUIState -> {
            if (integerUIState instanceof UIState.Success) {
                Integer employerIdResult = ((UIState.Success<Integer>) integerUIState).data;
                // TODO: Remove Insane Bypass HAHAHAHHA
                setupRcv(employerIdResult, email);
            } else if (integerUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<Integer>) integerUIState).message, Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), "Login not performed properly.", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        this.viewModel
                .getAllJobPostingApplication(jobPostingId)
                .observe(getViewLifecycleOwner(), jobPostingApplications -> {
                    jobPostingApplications.size();
                    adapter.refreshList(jobPostingApplications);
                });
    }

    public void setupRcv(int employerId, String email) {
        NavController navController = NavHostFragment.findNavController(requireParentFragment());
        this.adapter = new JobPostingApplicationRecyclerViewAdapter(new JobPostingApplicationRecyclerViewAdapter.UserItemInteraction() {
            // TODO: Fix Incorrectly passed email_ parameter (returns the email of the message received rather than sender email)
            @Override
            public void onChat(int employeeId, String email_) {
                viewModel.checkIfAThreadExist(employeeId, employerId, new OnOperationSuccessful<Integer>() {
                    @Override
                    public void onSuccess(Integer threadId) {
                        navController.navigate(JobPostingApplicationFragmentDirections.actionJobPostingApplicationFragmentToChatFragment(employeeId, employerId, threadId, email
                        ));
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading() {
                        Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onDownloadFiles(int jobPostingApplicationId) {
                viewModel.downloadApplicationFiles(jobPostingApplicationId, requireContext(), JobPostingApplicationFragment.this);
            }

            @Override
            public void onShowDecisionDialog() {

            }
        });
        this.binding.list.setAdapter(adapter);
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onLoading() {
        Toast.makeText(requireContext(), "Downloading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        Toast.makeText(requireContext(), "Finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String errorMessage) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}