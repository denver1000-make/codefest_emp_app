package com.denprog.codefestapp.destinations.employee;

import static androidx.core.app.NavUtils.getParentActivityIntent;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLER_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.dialog.JobPostingApplicationDialogFragment;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;
import com.denprog.codefestapp.util.UIState;


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
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_employee);
        this.mainViewModel.mutableLiveData.observe(getViewLifecycleOwner(), employeeIdUIState -> {
            if (employeeIdUIState instanceof UIState.Success) {
                EmployeeActivityViewModel.EmployeeId employeeIdCredentials = ((UIState.Success<EmployeeActivityViewModel.EmployeeId>) employeeIdUIState).data;
                        adapter = new JobPostingRecyclerViewAdapter(jobPostingId -> {
                    navController.navigate(EmployeeHomeFragmentDirections.actionEmployeeHomeFragmentToJobPostingApplicationDialogFragment(employeeIdCredentials.employeeId, jobPostingId));

                });
                viewModel.getAllJobPosting();
                viewModel.listMutableLiveData.observe(getViewLifecycleOwner(),
                        jobPostingList -> adapter.refreshList(jobPostingList));
                binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.list.setAdapter(adapter);
            } else if (employeeIdUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<EmployeeActivityViewModel.EmployeeId>) employeeIdUIState).message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
