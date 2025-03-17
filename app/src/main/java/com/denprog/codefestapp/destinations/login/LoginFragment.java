package com.denprog.codefestapp.destinations.login;

import static com.denprog.codefestapp.HomeActivityViewModel.ADMIN_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.app.AlertDialog;
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
import com.denprog.codefestapp.destinations.dummy.DummyViewModel;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.OnOperationSuccessful;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;
    private AlertDialog alertDialog;

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
        LoginFragmentArgs args = LoginFragmentArgs.fromBundle
                (getArguments());

        if (args.getPerformAutoLogin()) {
            mViewModel.loginResultState.setValue(new LoginViewModel.RoleState.AutoLoginAfterRegistration());
        }

        mViewModel.loginResultState.observe(getViewLifecycleOwner(), roleState -> {
            if (roleState instanceof LoginViewModel.RoleState.Fail) {
                Toast.makeText(requireContext(), ((LoginViewModel.RoleState.Fail) roleState).message, Toast.LENGTH_SHORT).show();
            } else if (roleState instanceof LoginViewModel.RoleState.AttemptRedirect) {
                binding.loginAction.setEnabled(false);
                mViewModel.performRoleBasedRedirect(roleState.user, new DummyViewModel.OnUserRoleLoaded() {
                    @Override
                    public void employee(User user, int employeeId) {
                        redirect(new Intent(requireActivity(), EmployeeActivity.class), user.userId, EMPLOYEE_ID_BUNDLE_KEY, employeeId);
                        binding.loginAction.setEnabled(true);
                    }

                    @Override
                    public void employer(User user, int employerId) {
                        redirect(new Intent(requireActivity(), EmployerActivity.class), user.userId, EMPLOYER_ID_BUNDLE_KEY, employerId);
                        binding.loginAction.setEnabled(true);
                    }

                    @Override
                    public void admin(User user, int adminId) {
                        redirect(new Intent(requireActivity(), HomeActivity.class), user.userId, ADMIN_ID_BUNDLE_KEY, adminId);
                        binding.loginAction.setEnabled(true);
                    }

                    @Override
                    public void noRoleAttached() {
                        mViewModel.loginResultState.setValue(new LoginViewModel.RoleState.Fail("No Roles Attached"));
                        binding.loginAction.setEnabled(true);
                    }
                });
            } else if (roleState instanceof LoginViewModel.RoleState.AutoLoginAfterRegistration) {
                String emailFromNav = args.getEmail();
                String passwordFromNav = args.getPassword();
                if (emailFromNav != null && passwordFromNav != null) {
                    mViewModel.emailField.set(emailFromNav);
                    mViewModel.passwordField.set(passwordFromNav);
                    mViewModel.login();
                    binding.loginAction.setEnabled(false);
                }
            } else if (roleState instanceof LoginViewModel.RoleState.PromptSaveUser) {
               buildAndShowAlertDialog(roleState.user, () -> mViewModel.loginResultState.setValue(new LoginViewModel.RoleState.AttemptRedirect(roleState.user)));
            } else if (roleState instanceof LoginViewModel.RoleState.Loading) {
                binding.loginAction.setEnabled(false);
            } else {
                binding.loginAction.setEnabled(true);
            }
        });

        binding.redirectToRegister.setOnClickListener(view1 -> navController.navigate(R.id.action_loginFragment_to_adminRegister));
        this.binding.setViewModel(mViewModel);
    }

    private void redirect(Intent intent, int userId, String roleIdNameKey, int roleTableId) {
        intent.putExtra(USER_ID_BUNDLE_KEY, userId);
        intent.putExtra(roleIdNameKey, roleTableId);
        startActivity(intent);
        requireActivity().finish();
    }

    public void buildAndShowAlertDialog(User user, Runnable runnable) {
        this.alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Save Login Credentials?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    mViewModel.saveUserCredential(user.userId, new OnOperationSuccessful<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(requireContext(), "Error In Saving", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoading() {

                        }
                    });
                    dialogInterface.dismiss();
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).setOnDismissListener(dialogInterface -> {
                    runnable.run();
                }).create();
        this.alertDialog.show();
    }

}