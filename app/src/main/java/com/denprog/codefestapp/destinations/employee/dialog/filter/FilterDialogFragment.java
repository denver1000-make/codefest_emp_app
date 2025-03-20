package com.denprog.codefestapp.destinations.employee.dialog.filter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentFilterDialogBinding;
import com.denprog.codefestapp.destinations.employee.EmployeeHomeFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            binding.maxPrice.setValue(maxSalary);
            binding.minPrice.setValue(minSalary);
            String category = argBundle.getString(EmployeeHomeFragment.CATEGORY_ARG_KEY);
            if (category != null) {
                binding.setSelectedCategory(category);
            }
        }

        String[] categories = getResources().getStringArray(R.array.job_categories);
        List<String> categoriesPlusAll = new ArrayList<>(Arrays.asList(categories));
        categoriesPlusAll.add(0, "All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriesPlusAll);
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

            int minPrice = (int) this.binding.minPrice.getValue();
            int maxPrice = (int) this.binding.maxPrice.getValue();
            resultBundle.putInt(MIN_SALARY_BUNDLE_KEY, minPrice);
            resultBundle.putInt(MAX_SALARY_BUNDLE_KEY, maxPrice);
            String selectedCateg = binding.spinner.getSelectedItem().toString();
            resultBundle.putString(CATEGORY_BUNDLE_KEY, selectedCateg.equals("All") ? null : selectedCateg);
            getParentFragmentManager().setFragmentResult(RESULT_KEY, resultBundle);
            dismissNow();
        });

        binding.minPrice.addOnChangeListener((slider, value, fromUser) -> {
            binding.minValueDisplay.setText("Min Salary" + value);
            float maxValue = binding.maxPrice.getValue();
            if (value + 5000 >= maxValue) {
                Toast.makeText(requireContext(), "Illegal Value", Toast.LENGTH_SHORT).show();
                if (maxValue != 0) {
                    slider.setValue(maxValue - 5000);
                } else {
                    slider.setValue(0);
                }
            }

        });

        binding.maxPrice.addOnChangeListener((slider, value, fromUser) -> {
            binding.maxValueDisplay.setText("Max Salary " + value);
            if (value - 5000 <= binding.minPrice.getValue()) {
                slider.setValue(binding.minPrice.getValue() + 5000);
                Toast.makeText(requireContext(), "Illegal Value", Toast.LENGTH_SHORT).show();
            }
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