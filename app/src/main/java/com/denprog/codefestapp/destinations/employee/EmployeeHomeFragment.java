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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentEmployerHomeBinding;
import com.denprog.codefestapp.destinations.employee.dialog.JobPostingApplicationDialogFragment;
import com.denprog.codefestapp.destinations.employer.JobPostingRecyclerViewAdapter;
import com.denprog.codefestapp.util.UIState;


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



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(EmployeeHomeViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_employee);
        Intent intent = requireActivity().getIntent();

        if (intent != null) {
            int userId = intent.getIntExtra(USER_ID_BUNDLE_KEY, -1);
            int employeeId = intent.getIntExtra(EMPLOYEE_ID_BUNDLER_KEY, -1);
            if (userId != -1 && employeeId != -1) {
                viewModel.employeeCredentialsUIState.setValue(new UIState.Success<>(new EmployeeHomeViewModel.EmployeeCredentials(employeeId, userId)));
            } else {
                viewModel.employeeCredentialsUIState.setValue(new UIState.Fail<>("No EmployeeId Was Loaded"));
            }
        } else {
            viewModel.employeeCredentialsUIState.setValue(new UIState.Fail<>("Intent was null"));
        }

        viewModel.employeeCredentialsUIState.observe(getViewLifecycleOwner(), employeeCredentialsUIState -> {
            if (employeeCredentialsUIState instanceof UIState.Success) {
                this.adapter = new JobPostingRecyclerViewAdapter(jobPostingId -> {
                    EmployeeHomeViewModel.EmployeeCredentials employeeCredentials = ((UIState.Success<EmployeeHomeViewModel.EmployeeCredentials>) employeeCredentialsUIState).data;
                    navController.navigate(EmployeeHomeFragmentDirections.actionEmployeeHomeFragmentToJobPostingApplicationDialogFragment(employeeCredentials.employeeId, jobPostingId));

                });

                this.viewModel.getAllJobPosting();
                this.viewModel.listMutableLiveData.observe(getViewLifecycleOwner(),
                        jobPostingList -> adapter.refreshList(jobPostingList));
                binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.list.setAdapter(adapter);
            } else if (employeeCredentialsUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<EmployeeHomeViewModel.EmployeeCredentials>) employeeCredentialsUIState).message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
