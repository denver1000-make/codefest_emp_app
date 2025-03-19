package com.denprog.codefestapp.destinations.employee;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.content.Intent;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.dialog.filter.FilterDialogFragment;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;


public class EmployeeHomeFragment extends Fragment {
    FragmentEmployerHomeBinding binding;
    JobPostingRecyclerViewAdapter adapter;
    EmployeeHomeViewModel viewModel;
    FilterDialogFragment dialogFragment;
    public static final String MAX_SALARY_ARG_KEY = "arg_max_salary";
    public static final String MIN_SALARY_ARG_KEY = "arg_min_salary";
    public static final String CATEGORY_ARG_KEY = "arg_category_key";

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
        dialogFragment = new FilterDialogFragment();
        NavController navController = NavHostFragment.findNavController(requireParentFragment());
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int employeeId = bundle.getInt(EMPLOYEE_ID_BUNDLE_KEY, -1);
            int userId = bundle.getInt(USER_ID_BUNDLE_KEY, -1);
            if (employeeId != -1 && userId != -1) {
                viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), jobPostingList -> {
                    adapter.refreshList(jobPostingList);
                });
                viewModel.getAllJobPosting();
                setupRcvAdapter(navController, employeeId);
                setupSearchView();
                setupFilter();
            }
        }
    }

    private void setupRcvAdapter (NavController navController, int employeeId) {
        adapter = new JobPostingRecyclerViewAdapter(jobPostingId -> {
            navController.navigate(EmployeeHomeFragmentDirections.actionEmployeeHomeFragmentToJobPostingApplicationDialogFragment(employeeId, jobPostingId));
        });
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list.setAdapter(adapter);
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
