package com.denprog.codefestapp.destinations.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        mViewModel.loginResultState.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.RoleState>() {
            @Override
            public void onChanged(LoginViewModel.RoleState roleState) {
                if (roleState instanceof LoginViewModel.RoleState.Fail) {
                    Toast.makeText(requireContext(), ((LoginViewModel.RoleState.Fail) roleState).message, Toast.LENGTH_SHORT).show();
                } else if (roleState instanceof LoginViewModel.RoleState.EmployerState) {
                    Log.d("Test", "");
                } else if (roleState instanceof LoginViewModel.RoleState.EmployeeState) {
                    Log.d("Test", "");
                } else if (roleState instanceof LoginViewModel.RoleState.AdminState) {
                    Log.d("Test", "");
                }
            }
        });
        this.binding.setViewModel(mViewModel);
    }

}