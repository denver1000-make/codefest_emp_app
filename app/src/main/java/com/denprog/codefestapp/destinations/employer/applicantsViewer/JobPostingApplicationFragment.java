package com.denprog.codefestapp.destinations.employer.applicantsViewer;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentJobPostingViewerBinding;
import com.denprog.codefestapp.destinations.employee.dialog.JobPostingApplicationDialogFragmentArgs;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;
import com.denprog.codefestapp.destinations.employer.applicantsViewer.placeholder.PlaceholderContent;
import com.denprog.codefestapp.destinations.login.LoginFragment;
import com.denprog.codefestapp.room.entity.JobPostingApplication;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

import java.util.List;

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

        if (employerId != -1) {
            this.viewModel.mutableEmployerIdLiveData.setValue(new UIState.Success<>(employerId));
        } else {
            this.viewModel.mutableEmployerIdLiveData.setValue(new UIState.Fail<>("Employer Not Logged In"));
        }

        this.viewModel.mutableEmployerIdLiveData.observe(getViewLifecycleOwner(), integerUIState -> {
            if (integerUIState instanceof UIState.Success) {
                Integer employerIdResult = ((UIState.Success<Integer>) integerUIState).data;
                setupRcv(employerIdResult);
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

    public void setupRcv(int employerId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_employer);
        this.adapter = new JobPostingApplicationRecyclerViewAdapter(new JobPostingApplicationRecyclerViewAdapter.UserItemInteraction() {
            @Override
            public void onChat(int employeeId) {
                viewModel.checkIfAThreadExist(employeeId, employerId, new OnOperationSuccessful<Integer>() {
                    @Override
                    public void onSuccess(Integer threadId) {
                        navController.navigate(JobPostingApplicationFragmentDirections.actionJobPostingApplicationFragmentToChatFragment(employeeId, employerId, threadId));
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