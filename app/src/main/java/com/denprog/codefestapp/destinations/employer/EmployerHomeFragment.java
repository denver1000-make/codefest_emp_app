package com.denprog.codefestapp.destinations.employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employer.addJobPositng.AddJobPostingFragment;

public class EmployerHomeFragment extends Fragment {

    JobPostingRecyclerViewAdapter adapter;
    FragmentEmployerHomeBinding binding;
    EmployerHomeViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentEmployerHomeBinding.inflate(inflater);
        adapter = new JobPostingRecyclerViewAdapter();
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        viewModel.jobPostingMutableLiveData.observe(getViewLifecycleOwner(), jobPostingList -> {
            adapter.refreshList(jobPostingList);
        });
        viewModel.getAllJobPosting();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_employer);
        binding.addJobpostingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddJobPostingFragment addJobPostingFragment = new AddJobPostingFragment();
                addJobPostingFragment.show(getParentFragmentManager(), "ADD_JOB_POSTING_DIALOG");
            }
        });
        getParentFragmentManager().setFragmentResultListener(AddJobPostingFragment.resultKey, getViewLifecycleOwner(), (requestKey, result) -> {
            viewModel.getAllJobPosting();
        });
    }


}