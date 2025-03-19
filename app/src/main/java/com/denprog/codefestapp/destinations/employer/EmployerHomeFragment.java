package com.denprog.codefestapp.destinations.employer;

import static com.denprog.codefestapp.destinations.employee.EmployeeHomeFragment.CATEGORY_ARG_KEY;
import static com.denprog.codefestapp.destinations.employee.EmployeeHomeFragment.MAX_SALARY_ARG_KEY;
import static com.denprog.codefestapp.destinations.employee.EmployeeHomeFragment.MIN_SALARY_ARG_KEY;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.EmployeeHomeViewModel;
import com.denprog.codefestapp.destinations.employee.dialog.filter.FilterDialogFragment;
import com.denprog.codefestapp.destinations.employer.addJobPositng.AddJobPostingFragment;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;
import java.util.List;
public class EmployerHomeFragment extends Fragment {
    JobPostingRecyclerViewAdapter adapter;
    FragmentEmployerHomeBinding binding;
    EmployerHomeViewModel viewModel;
    AddJobPostingFragment addJobPostingFragment = new AddJobPostingFragment();
    FilterDialogFragment dialogFragment;
    public static final String POSTING_ID_BUNDLE_KEY = "posting_id_bundle_key";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentEmployerHomeBinding.inflate(inflater);
        adapter = new JobPostingRecyclerViewAdapter((jobPostingId) -> {
            if (!addJobPostingFragment.isVisible() && !addJobPostingFragment.isAdded()) {
                Bundle bundle = new Bundle();
                bundle.putInt(POSTING_ID_BUNDLE_KEY, jobPostingId);
                addJobPostingFragment.setArguments(bundle);
                addJobPostingFragment.show(getParentFragmentManager(), "ADD_JOB_POSTING_DIALOG");
            }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogFragment = new FilterDialogFragment();
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployerHomeViewModel.class);
        viewModel.empIdMutableLiveData.observe(getViewLifecycleOwner(), integer -> {
            viewModel.getAllJobPosting(integer);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.employerFragmentContainer);
            binding.addJobpostingAction.setOnClickListener(view1 -> {
                addJobPostingFragment.setArguments(null);
                addJobPostingFragment.show(getParentFragmentManager(), "ADD_JOB_POSTING_DIALOG");
            });
            getParentFragmentManager().setFragmentResultListener(AddJobPostingFragment.resultKey, requireActivity(), (requestKey, result) -> {
                viewModel.getAllJobPosting(integer);
            });
            setupSearchView();
            setupFilter();

            getParentFragmentManager().setFragmentResultListener(AddJobPostingFragment.REDIRECT_TO_APPLICANTS, requireActivity(), (requestKey, result) -> {
                int jobPostingId = result.getInt(AddJobPostingFragment.JOB_POSTING_REDIRECT_BUNDLE_KEY, -1);
                if (jobPostingId != -1) {
                    navController.navigate(EmployerHomeFragmentDirections.actionEmployerHomeFragmentToJobPostingApplicationFragment(jobPostingId));
                }
            });
        });

        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), jobPostingList -> {
            adapter.refreshList(jobPostingList);
        });
    }

    private void setupSearchView() {
        this.viewModel.searchStateMutableLiveData.observe(getViewLifecycleOwner(), searchState -> {
            if (searchState instanceof EmployeeHomeViewModel.SearchState.OnSearch) {
                EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList = ((EmployeeHomeViewModel.SearchState.OnSearch) searchState).searchQueryFilterAndList;
                if (searchQueryFilterAndList.minSalary == 0 && searchQueryFilterAndList.maxSalary == 0) {
                    viewModel.filterByCategoryAndSearchQ(searchQueryFilterAndList.category, searchQueryFilterAndList.searchQuery);
                }
                viewModel.filterByAll(searchQueryFilterAndList.minSalary, searchQueryFilterAndList.maxSalary, searchQueryFilterAndList.category, searchQueryFilterAndList.searchQuery);
            }
        });
        this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                EmployeeHomeViewModel.SearchState searchState = viewModel.searchStateMutableLiveData.getValue();
                EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList;

                if (searchState instanceof EmployeeHomeViewModel.SearchState.OnSearch) {
                    searchQueryFilterAndList = ((EmployeeHomeViewModel.SearchState.OnSearch) searchState).searchQueryFilterAndList;
                    searchQueryFilterAndList.searchQuery = s;
                } else {
                    searchQueryFilterAndList = new EmployeeHomeViewModel.SearchQueryFilterAndList();
                }

                if (s.isBlank() || s.isEmpty()) {
                    searchQueryFilterAndList.searchQuery = null;
                } else {
                    searchQueryFilterAndList.searchQuery = s;
                }

                viewModel.searchStateMutableLiveData.setValue(new EmployeeHomeViewModel.SearchState.OnSearch(searchQueryFilterAndList));
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                EmployeeHomeViewModel.SearchState searchState = viewModel.searchStateMutableLiveData.getValue();
                EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList;

                if (searchState instanceof EmployeeHomeViewModel.SearchState.OnSearch) {
                    searchQueryFilterAndList = ((EmployeeHomeViewModel.SearchState.OnSearch) searchState).searchQueryFilterAndList;
                    searchQueryFilterAndList.searchQuery = s;
                } else {
                    searchQueryFilterAndList = new EmployeeHomeViewModel.SearchQueryFilterAndList();
                }

                if (s.isBlank() || s.isEmpty()) {
                    searchQueryFilterAndList.searchQuery = null;
                } else {
                    searchQueryFilterAndList.searchQuery = s;
                }

                viewModel.searchStateMutableLiveData.setValue(new EmployeeHomeViewModel.SearchState.OnSearch(searchQueryFilterAndList));
                return true;
            }
        });
    }

    private EmployeeHomeViewModel.SearchQueryFilterAndList parseFilterFragmentResult(Bundle result) {
        int minValue = result.getInt(FilterDialogFragment.MIN_SALARY_BUNDLE_KEY, 0);
        int maxValue = result.getInt(FilterDialogFragment.MAX_SALARY_BUNDLE_KEY, 0);
        String category = result.getString(FilterDialogFragment.CATEGORY_BUNDLE_KEY, null);
        EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList;
        if (viewModel.searchStateMutableLiveData.getValue() instanceof EmployeeHomeViewModel.SearchState.OnSearch) {
            EmployeeHomeViewModel.SearchQueryFilterAndList searchQuery = ((EmployeeHomeViewModel.SearchState.OnSearch) viewModel.searchStateMutableLiveData.getValue()).searchQueryFilterAndList;
            searchQuery.maxSalary = maxValue;
            searchQuery.minSalary = minValue;
            searchQuery.category = category;
            searchQueryFilterAndList = searchQuery;
        } else {
            searchQueryFilterAndList = new EmployeeHomeViewModel.SearchQueryFilterAndList();
            searchQueryFilterAndList.searchQuery = null;
            searchQueryFilterAndList.category = category;
            searchQueryFilterAndList.maxSalary = maxValue;
            searchQueryFilterAndList.minSalary = minValue;
        }
        return searchQueryFilterAndList;
    }

    private void setupFilter () {
        binding.applyAdditionalFilter.setOnClickListener(view1 -> {
            if (!dialogFragment.isVisible() && !dialogFragment.isAdded()) {
                dialogFragment.setArguments(getFilterArgs(viewModel.searchStateMutableLiveData.getValue()));
                dialogFragment.show(getParentFragmentManager(), "FilterDialog");
                getParentFragmentManager().setFragmentResultListener(
                        FilterDialogFragment.RESULT_KEY,
                        getViewLifecycleOwner(),
                        (requestKey, result) -> {
                            EmployeeHomeViewModel.SearchQueryFilterAndList searchQ = parseFilterFragmentResult(result);
                            viewModel.searchStateMutableLiveData.setValue(new EmployeeHomeViewModel.SearchState.OnSearch(searchQ));
                        });
            }
        });
    }

    private Bundle getFilterArgs(EmployeeHomeViewModel.SearchState searchQueryFilterAndList) {
        if (searchQueryFilterAndList instanceof EmployeeHomeViewModel.SearchState.OnSearch) {
            EmployeeHomeViewModel.SearchQueryFilterAndList query = ((EmployeeHomeViewModel.SearchState.OnSearch) searchQueryFilterAndList).searchQueryFilterAndList;
            Bundle bundle = new Bundle();
            bundle.putInt(MIN_SALARY_ARG_KEY, query.minSalary);
            bundle.putInt(MAX_SALARY_ARG_KEY, query.maxSalary);
            bundle.putString(CATEGORY_ARG_KEY, query.category);
            return bundle;
        } else {
            return null;
        }
    }



}