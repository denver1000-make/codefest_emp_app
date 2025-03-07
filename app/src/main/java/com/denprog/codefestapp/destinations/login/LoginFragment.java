package com.denprog.codefestapp.destinations.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denprog.codefestapp.EmployeeActivity;
import com.denprog.codefestapp.HomeActivity;
import com.denprog.codefestapp.R;
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
                        startActivity(intent);
                    }

                    @Override
                    public void onUserIsUnderReview() {
                        Toast.makeText(requireContext(), "User is under review", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (roleState instanceof LoginViewModel.RoleState.AdminState) {
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        this.binding.setViewModel(mViewModel);
    }

}