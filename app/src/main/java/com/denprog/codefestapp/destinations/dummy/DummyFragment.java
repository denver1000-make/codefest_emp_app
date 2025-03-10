package com.denprog.codefestapp.destinations.dummy;

import static com.denprog.codefestapp.HomeActivityViewModel.ADMIN_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLER_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denprog.codefestapp.EmployeeActivity;
import com.denprog.codefestapp.HomeActivity;
import com.denprog.codefestapp.R;
import com.denprog.codefestapp.activities.EmployerActivity;
import com.denprog.codefestapp.destinations.login.LoginViewModel;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DummyFragment extends Fragment {

    private DummyViewModel mViewModel;

    public static DummyFragment newInstance() {
        return new DummyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DummyViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        mViewModel.checkIfThereIsSavedLogin(new OnOperationSuccessful<>() {
            @Override
            public void onSuccess(User data) {
                mViewModel.performRoleBasedRedirect(data, new DummyViewModel.OnUserRoleLoaded() {
                    @Override
                    public void employee(User user, int employeeId) {
                        Intent intent = new Intent(requireActivity(), EmployeeActivity.class);
                        intent.putExtra(USER_ID_BUNDLE_KEY, user.userId);
                        intent.putExtra(EMPLOYEE_ID_BUNDLER_KEY, employeeId);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void employer(User user, int employerId) {
                        Intent intent = new Intent(requireActivity(), EmployerActivity.class);
                        intent.putExtra(USER_ID_BUNDLE_KEY, user.userId);
                        intent.putExtra(EMPLOYER_ID_BUNDLE_KEY, employerId);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void admin(User user, int adminId) {
                        Intent intent = new Intent(requireActivity(), HomeActivity.class);
                        intent.putExtra(USER_ID_BUNDLE_KEY, user.userId);
                        intent.putExtra(ADMIN_ID_BUNDLE_KEY, adminId);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void noRoleAttached() {
                        navController.navigate(DummyFragmentDirections.actionDummyFragmentToAdminRegister());
                    }
                });
            }


            @Override
            public void onError(String message) {
                navController.navigate(R.id.action_dummyFragment_to_adminRegister);
            }
        });
    }
}