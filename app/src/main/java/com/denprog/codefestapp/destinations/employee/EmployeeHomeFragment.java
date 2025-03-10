package com.denprog.codefestapp.destinations.employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.dialog.JobPostingApplicationDialogFragment;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;
import com.denprog.codefestapp.room.entity.JobPosting;

import java.util.List;

public class EmployeeHomeFragment extends Fragment {

    FragmentEmployerHomeBinding binding;
    JobPostingRecyclerViewAdapter adapter;
    EmployeeHomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmployerHomeBinding.inflate(getLayoutInflater(), container, false);
        this.adapter = new JobPostingRecyclerViewAdapter(jobPostingId -> {
            JobPostingApplicationDialogFragment dialogFragment = new JobPostingApplicationDialogFragment();
            dialogFragment.show(requireActivity().getSupportFragmentManager(), "");
        });
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployeeHomeViewModel.class);
        this.viewModel.getAllJobPosting();
        this.viewModel.listMutableLiveData.observe(getViewLifecycleOwner(),
                jobPostingList -> adapter.refreshList(jobPostingList));
    }
}
