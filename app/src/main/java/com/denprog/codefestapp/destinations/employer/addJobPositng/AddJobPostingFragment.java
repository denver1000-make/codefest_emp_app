package com.denprog.codefestapp.destinations.employer.addJobPositng;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAddJobPostingBinding;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employer.EmployerHomeFragment;
import com.denprog.codefestapp.destinations.employer.EmployerHomeViewModel;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

public class AddJobPostingFragment extends DialogFragment {
    public static final String[] listOfCategories = new String[]{"Finance", "IT", "Art"};
    public static final String resultKey = "ID_OF_JOB_POSTING";
    public static final String RESULT_KEY_OF_UPDATE = "UPDATE_RESULT";
    public static final String bundleOfIntegerId = "ID_OF_ADDED_JOB_POSTING";
    public static final String bundleOfUpdatedInteger = "ID_OF_UPDATED_INTEGER_ID";
    public static final String REDIRECT_TO_APPLICANTS = "REDIRECT_TO_APPLICANTS";
    public static final String JOB_POSTING_REDIRECT_BUNDLE_KEY = "JOB_POSTING_ID_KEY_FOR_REDIRECT";

    private AddJobPostingViewModel mViewModel;
    private EmployerHomeViewModel employerHomeViewModel;
    private FragmentAddJobPostingBinding binding;

    public static AddJobPostingFragment newInstance() {
        return new AddJobPostingFragment();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        this.binding = FragmentAddJobPostingBinding.inflate(getLayoutInflater());
        alertDialog.setView(binding.getRoot());
        this.mViewModel = new ViewModelProvider(requireActivity()).get(AddJobPostingViewModel.class);
        this.employerHomeViewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        binding.setViewModel(mViewModel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int postingIdKey = getArguments().getInt(EmployerHomeFragment.POSTING_ID_BUNDLE_KEY, -1);
            if (postingIdKey != -1) {
                binding.addPostingAction.setText("Update");
                mViewModel.loadData(postingIdKey);
                setUpViewApplicantButton(postingIdKey);
                mViewModel.jobPosting.observe(requireActivity(), jobPostingUIState -> {
                    if (jobPostingUIState instanceof UIState.Success) {
                        JobPosting jobPosting = ((UIState.Success<JobPosting>) jobPostingUIState).data;
                        mViewModel.postingDescription.set(jobPosting.postingDescription);
                        mViewModel.postingName.set(jobPosting.postingName);
                        mViewModel.postingMinSalary.set(jobPosting.minSalary);
                        mViewModel.postingMaxSalary.set(jobPosting.maxSalary);
                        mViewModel.postingCategory.set(jobPosting.postingCategory);
                    }
                });
                binding.addPostingAction.setOnClickListener(view -> mViewModel.updateJobPosting(new OnOperationSuccessful<>() {
                    @Override
                    public void onSuccess(Integer data) {
                        Bundle updateResultBundle = new Bundle();
                        updateResultBundle.putInt(bundleOfUpdatedInteger, data);
                        getParentFragmentManager().setFragmentResult(RESULT_KEY_OF_UPDATE, updateResultBundle);
                        dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onLoading() {

                    }
                }));
            }
        } else {
            hideViewApplicationButton();
            binding.addPostingAction.setOnClickListener(view -> {
                Integer empId = employerHomeViewModel.empIdMutableLiveData.getValue();
                if (empId != null) {
                    mViewModel.addJobPosting(empId);
                }
            });

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, AddJobPostingFragment.listOfCategories);
        binding.categorySpinner.setAdapter(adapter);

        mViewModel.mutableLiveDataOfInsertedId.observe(requireActivity(), integerUIState -> {
            if (integerUIState instanceof UIState.Success) {
                Bundle bundleForResult = new Bundle();
                bundleForResult.putInt(AddJobPostingFragment.bundleOfIntegerId, ((UIState.Success<Integer>) integerUIState).data);
                getParentFragmentManager().setFragmentResult(AddJobPostingFragment.resultKey, bundleForResult);
                clearData();
                dismiss();
            } else if (integerUIState instanceof UIState.Fail) {
                clearData();
                dismiss();
                Toast.makeText(requireContext(), ((UIState.Fail<Integer>) integerUIState).message, Toast.LENGTH_SHORT).show();
            }
        });

        return alertDialog.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void hideViewApplicationButton() {
        binding.viewApplicantsButton.setVisibility(View.GONE);
    }

    public void setUpViewApplicantButton(int jobPostingId) {
        binding.viewApplicantsButton.setVisibility(View.VISIBLE);
        binding.viewApplicantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(JOB_POSTING_REDIRECT_BUNDLE_KEY, jobPostingId);
                getParentFragmentManager().setFragmentResult(REDIRECT_TO_APPLICANTS, bundle);
                dismissNow();
            }
        });

    }

    public void clearData() {
        mViewModel.postingCategory.set("");
        mViewModel.postingName.set("");
        mViewModel.postingDescription.set("");
        mViewModel.postingMaxSalary.set(0.0f);
        mViewModel.postingMinSalary.set(0.0f);
        mViewModel.mutableLiveDataOfInsertedId.setValue(null);
    }

}