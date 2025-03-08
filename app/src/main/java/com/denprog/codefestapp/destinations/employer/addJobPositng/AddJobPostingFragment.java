package com.denprog.codefestapp.destinations.employer.addJobPositng;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denprog.codefestapp.databinding.FragmentAddJobPostingBinding;
import com.denprog.codefestapp.destinations.employer.EmployerHomeViewModel;
import com.denprog.codefestapp.util.UIState;

public class AddJobPostingFragment extends DialogFragment {


    public static final String[] listOfCategories = new String[]{"Finance", "IT", "Art"};
    public static final String resultKey = "ID_OF_JOB_POSTING";
    public static final String bundleOfIntegerId = "ID_OF_ADDED_JOB_POSTING";
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
        alertDialog.setView(binding.getRoot());
        this.mViewModel = new ViewModelProvider(requireActivity()).get(AddJobPostingViewModel.class);
        this.employerHomeViewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        binding.setViewModel(mViewModel);

        binding.addPostingAction.setOnClickListener(view -> {
            Integer empId = employerHomeViewModel.empIdMutableLiveData.getValue();
            if (empId != null) {
                mViewModel.addJobPosting(empId);
            }
        });

        mViewModel.mutableLiveDataOfInsertedId.observe(getViewLifecycleOwner(), new Observer<UIState<Integer>>() {
            @Override
            public void onChanged(UIState<Integer> integerUIState) {
                if (integerUIState instanceof UIState.Success) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(AddJobPostingFragment.bundleOfIntegerId, ((UIState.Success<Integer>) integerUIState).data);
                    getParentFragmentManager().setFragmentResult(AddJobPostingFragment.resultKey, bundle);
                } else if (integerUIState instanceof UIState.Fail) {
                    Toast.makeText(requireContext(), ((UIState.Fail<Integer>) integerUIState).message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return alertDialog.create();
    }

}