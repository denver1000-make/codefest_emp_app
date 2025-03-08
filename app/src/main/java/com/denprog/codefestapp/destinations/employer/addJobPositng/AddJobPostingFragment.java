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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAddJobPostingBinding;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
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
        this.binding = FragmentAddJobPostingBinding.inflate(getLayoutInflater());
        alertDialog.setView(binding.getRoot());
        this.mViewModel = new ViewModelProvider(requireActivity()).get(AddJobPostingViewModel.class);
        this.employerHomeViewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        binding.setViewModel(mViewModel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, AddJobPostingFragment.listOfCategories);
        binding.categorySpinner.setAdapter(adapter);

        binding.addPostingAction.setOnClickListener(view -> {
            Integer empId = employerHomeViewModel.empIdMutableLiveData.getValue();
            if (empId != null) {
                mViewModel.addJobPosting(empId);
            }
        });

        mViewModel.mutableLiveDataOfInsertedId.observe(requireActivity(), integerUIState -> {
            if (integerUIState instanceof UIState.Success) {
                Bundle bundle = new Bundle();
                bundle.putInt(AddJobPostingFragment.bundleOfIntegerId, ((UIState.Success<Integer>) integerUIState).data);
                getParentFragmentManager().setFragmentResult(AddJobPostingFragment.resultKey, bundle);
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

    public void clearData() {
        mViewModel.postingCategory.set("");
        mViewModel.postingName.set("");
        mViewModel.postingDescription.set("");
        mViewModel.postingMaxSalary.set(0.0f);
        mViewModel.postingMinSalary.set(0.0f);
        mViewModel.mutableLiveDataOfInsertedId.setValue(null);
    }

}