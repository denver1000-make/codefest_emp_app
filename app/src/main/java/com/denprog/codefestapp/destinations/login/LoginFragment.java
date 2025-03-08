package com.denprog.codefestapp.destinations.login;

import static com.denprog.codefestapp.HomeActivityViewModel.ADMIN_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLER_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;
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

import com.denprog.codefestapp.EmployeeActivity;
import com.denprog.codefestapp.HomeActivity;
import com.denprog.codefestapp.R;
import com.denprog.codefestapp.activities.EmployerActivity;
import com.denprog.codefestapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        mViewModel.loginResultState.observe(getViewLifecycleOwner(), roleState -> {
            if (roleState instanceof LoginViewModel.RoleState.Fail) {
                Toast.makeText(requireContext(), ((LoginViewModel.RoleState.Fail) roleState).message, Toast.LENGTH_SHORT).show();
            } else if (roleState instanceof LoginViewModel.RoleState.EmployerState) {
                mViewModel.checkIfUserIsUnderReview(((LoginViewModel.RoleState.EmployerState) roleState).user.userId, new LoginViewModel.OnUserReviewStatus() {
                    @Override
                    public void onUserIsNotUnderReview() {
                        Intent intent = new Intent(requireActivity(), EmployerActivity.class);
                        intent.putExtra(USER_ID_BUNDLE_KEY, ((LoginViewModel.RoleState.EmployerState) roleState).user.userId);
                        intent.putExtra(EMPLOYER_ID_BUNDLE_KEY, ((LoginViewModel.RoleState.EmployerState) roleState).employerId);
                        startActivity(intent);
                    }

                    @Override
                    public void onUserIsUnderReview() {
                        Toast.makeText(requireContext(), "User is under review", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (roleState instanceof LoginViewModel.RoleState.EmployeeState) {
                mViewModel.checkIfUserIsUnderReview(((LoginViewModel.RoleState.EmployeeState) roleState).user.userId, new LoginViewModel.OnUserReviewStatus() {
                    @Override
                    public void onUserIsNotUnderReview() {
                        Intent intent = new Intent(requireActivity(), EmployeeActivity.class);
                        intent.putExtra(USER_ID_BUNDLE_KEY, ((LoginViewModel.RoleState.EmployeeState) roleState).user.userId);
                        intent.putExtra(EMPLOYEE_ID_BUNDLER_KEY, ((LoginViewModel.RoleState.EmployeeState) roleState).employeeId);
                        startActivity(intent);
                    }

                    @Override
                    public void onUserIsUnderReview() {
                        Toast.makeText(requireContext(), "User is under review", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (roleState instanceof LoginViewModel.RoleState.AdminState) {
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                intent.putExtra(USER_ID_BUNDLE_KEY, ((LoginViewModel.RoleState.AdminState) roleState).user.userId);
                intent.putExtra(ADMIN_ID_BUNDLE_KEY, ((LoginViewModel.RoleState.AdminState) roleState).adminId);
                startActivity(intent);
            }
        });
        this.binding.redirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_adminRegister);
            }
        });
        this.binding.setViewModel(mViewModel);
    }

}