package com.denprog.codefestapp.destinations.employee.dialog.filter;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentFilterDialogBinding;
import com.denprog.codefestapp.destinations.employee.EmployeeHomeFragment;

public class FilterDialogFragment extends DialogFragment {
    FragmentFilterDialogBinding binding;
    public static final String CATEGORY_BUNDLE_KEY = "categ_filter";
    public static final String MIN_SALARY_BUNDLE_KEY = "min_salary_filter";
    public static final String MAX_SALARY_BUNDLE_KEY = "max_salary_filter";
    public static final String RESULT_KEY = "FILTER_DIALOG_RESULT";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        binding = FragmentFilterDialogBinding.inflate(getLayoutInflater());
        alertDialog.setView(binding.getRoot());

        Bundle argBundle = getArguments();
        if (argBundle != null) {
            int maxSalary = argBundle.getInt(EmployeeHomeFragment.MAX_SALARY_ARG_KEY);
            int minSalary = argBundle.getInt(EmployeeHomeFragment.MIN_SALARY_ARG_KEY);
            binding.maxPrice.setProgress(maxSalary);
            binding.minPrice.setProgress(minSalary);
            String category = argBundle.getString(EmployeeHomeFragment.CATEGORY_ARG_KEY);
            if (category != null) {
                binding.setSelectedCategory(category);
            }
        }

        String[] categories = getResources().getStringArray(R.array.job_categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categories);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        this.binding.spinner.setAdapter(arrayAdapter);
        alertDialog.setTitle("Filters");
        alertDialog.setPositiveButton("Search", (dialogInterface, i) -> {
            Bundle resultBundle = new Bundle();

            int minPrice = this.binding.minPrice.getProgress();
            int maxPrice = this.binding.maxPrice.getProgress();
            resultBundle.putInt(MIN_SALARY_BUNDLE_KEY, minPrice);
            resultBundle.putInt(MAX_SALARY_BUNDLE_KEY, maxPrice == 0 ? -1 : maxPrice);
            resultBundle.putString(CATEGORY_BUNDLE_KEY, binding.spinner.getSelectedItem().toString());
            getParentFragmentManager().setFragmentResult(RESULT_KEY, resultBundle);
            dismissNow();
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismissAllowingStateLoss();
            }
        });


        return alertDialog.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}