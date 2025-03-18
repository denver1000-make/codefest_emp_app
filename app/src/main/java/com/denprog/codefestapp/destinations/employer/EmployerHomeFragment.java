package com.denprog.codefestapp.destinations.employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.EmployeeHomeViewModel;
import com.denprog.codefestapp.destinations.employer.addJobPositng.AddJobPostingFragment;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;
import java.util.List;

public class EmployerHomeFragment extends Fragment {

    JobPostingRecyclerViewAdapter adapter;
    FragmentEmployerHomeBinding binding;
    EmployerHomeViewModel viewModel;

    public static final String POSTING_ID_BUNDLE_KEY = "posting_id_bundle_key";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentEmployerHomeBinding.inflate(inflater);
        adapter = new JobPostingRecyclerViewAdapter((jobPostingId) -> {
            AddJobPostingFragment addJobPostingFragment = new AddJobPostingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSTING_ID_BUNDLE_KEY, jobPostingId);
            addJobPostingFragment.setArguments(bundle);
            addJobPostingFragment.show(getParentFragmentManager(), "ADD_JOB_POSTING_DIALOG");
        });
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        viewModel.empIdMutableLiveData.observe(getViewLifecycleOwner(), integer -> {
            viewModel.getAllJobPosting(integer);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.employerFragmentContainer);
            binding.addJobpostingAction.setOnClickListener(view1 -> {
                AddJobPostingFragment addJobPostingFragment = new AddJobPostingFragment();
                addJobPostingFragment.show(getParentFragmentManager(), "ADD_JOB_POSTING_DIALOG");
            });
            getParentFragmentManager().setFragmentResultListener(AddJobPostingFragment.resultKey, getViewLifecycleOwner(), (requestKey, result) -> {
                viewModel.getAllJobPosting(integer);
            });
            setupSearchView();
            getParentFragmentManager().setFragmentResultListener(AddJobPostingFragment.REDIRECT_TO_APPLICANTS, getViewLifecycleOwner(), (requestKey, result) -> {
                int jobPostingId = result.getInt(AddJobPostingFragment.JOB_POSTING_REDIRECT_BUNDLE_KEY, -1);
                if (jobPostingId != -1) {
                    navController.navigate(EmployerHomeFragmentDirections.actionEmployerHomeFragmentToJobPostingApplicationFragment(jobPostingId));
                }
            });
        });
    }

    private void setupSearchView() {
        this.viewModel.searchUiStateMutableLiveData.observe(getViewLifecycleOwner(), searchQueryAndListUIState -> {
            if (searchQueryAndListUIState instanceof UIState.Success) {
                EmployeeHomeViewModel.SearchQueryAndList data = ((UIState.Success<EmployeeHomeViewModel.SearchQueryAndList>) searchQueryAndListUIState).data;
                if (data.searchQuery != null && !data.searchQuery.isEmpty() && !data.searchQuery.isBlank()) {
                    List<JobPosting> matchingJobPosting = new ArrayList<>();
                    data.jobPostingList.forEach(jobPosting -> {
                        if (jobPosting.postingName.toLowerCase().contains(data.searchQuery.toLowerCase())) {
                            matchingJobPosting.add(jobPosting);
                        }
                    });
                    adapter.refreshList(matchingJobPosting);
                } else if (data.searchQuery == null || data.searchQuery.isBlank() || data.searchQuery.isEmpty()) {
                    adapter.refreshList(data.jobPostingList);
                }
            }
        });
        this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                UIState<EmployeeHomeViewModel.SearchQueryAndList> data = viewModel.searchUiStateMutableLiveData.getValue();
                if (data instanceof UIState.Success) {
                    EmployeeHomeViewModel.SearchQueryAndList queryAndList = ((UIState.Success<EmployeeHomeViewModel.SearchQueryAndList>) data).data;
                    EmployeeHomeViewModel.SearchQueryAndList searchQueryAndList = new EmployeeHomeViewModel.SearchQueryAndList(queryAndList.jobPostingList, s);
                    UIState.Success<EmployeeHomeViewModel.SearchQueryAndList> uiState = new UIState.Success<>(searchQueryAndList);
                    viewModel.searchUiStateMutableLiveData.setValue(uiState);
                }
                return true;
            }
        });
    }

}