package com.denprog.codefestapp.destinations.employee;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;
import java.util.List;


public class EmployeeHomeFragment extends Fragment {

    FragmentEmployerHomeBinding binding;
    JobPostingRecyclerViewAdapter adapter;
    EmployeeHomeViewModel viewModel;
    EmployeeActivityViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmployerHomeBinding.inflate(getLayoutInflater(), container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployeeHomeViewModel.class);
        this.mainViewModel = new ViewModelProvider(requireActivity()).get(EmployeeActivityViewModel.class);

        NavController navController = NavHostFragment.findNavController(requireParentFragment());
        this.viewModel.employeStateMutableLiveData.observe(getViewLifecycleOwner(), employeeIdUIState -> {
            if (employeeIdUIState instanceof UIState.Success) {
                EmployeeActivityViewModel.EmployeeId employeeIdCredentials = ((UIState.Success<EmployeeActivityViewModel.EmployeeId>) employeeIdUIState).data;
                setupRcvAdapter(navController, employeeIdCredentials);
                setupSearchView();

            } else if (employeeIdUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<EmployeeActivityViewModel.EmployeeId>) employeeIdUIState).message, Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int employeeId = bundle.getInt(EMPLOYEE_ID_BUNDLE_KEY, -1);
            int userId = bundle.getInt(USER_ID_BUNDLE_KEY, -1);
            if (employeeId != -1 && userId != -1) {
                this.viewModel.employeStateMutableLiveData.setValue(new UIState.Success<>(new EmployeeActivityViewModel.EmployeeId(employeeId, userId)));
            }
        }
    }

    private void setupRcvAdapter (NavController navController, EmployeeActivityViewModel.EmployeeId employeeCredentials) {
        adapter = new JobPostingRecyclerViewAdapter(jobPostingId -> {
            navController.navigate(EmployeeHomeFragmentDirections.actionEmployeeHomeFragmentToJobPostingApplicationDialogFragment(employeeCredentials.employeeId, jobPostingId));
        });
        viewModel.getAllJobPosting();
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(),
                jobPostingList -> adapter.refreshList(jobPostingList));
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
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
