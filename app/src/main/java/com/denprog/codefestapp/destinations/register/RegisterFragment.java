package com.denprog.codefestapp.destinations.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentRegisterBinding;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.FileUtil;
import com.denprog.codefestapp.util.SelectedFile;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private FragmentRegisterBinding binding;
    FilesRecyclerViewAdapter adapter;
    AlertDialog alertDialog;
    NavController navController;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                if (o.getData() != null && o.getData().getData() != null) {
                    Uri uri = o.getData().getData();
                    String extension = FileUtil.getFilesExtension(requireContext(), uri);
                    mViewModel
                            .fileActionStateMutableLiveData
                            .setValue(new RegisterViewModel.FileActionState.AddFile(
                            new SelectedFile(uri, FileUtil.getFileName(requireContext(), uri))));
                }
            }
        }
    });

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentRegisterBinding.inflate(inflater);
        this.alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Do you want to login your account.")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    UIState<User> uiState = mViewModel.userMutableLiveData.getValue();
                    if (uiState instanceof UIState.Success) {
                        RegisterFragmentDirections.ActionAdminRegisterToLoginFragment fragmentDirections = RegisterFragmentDirections.actionAdminRegisterToLoginFragment(((UIState.Success<User>) uiState).data.email, ((UIState.Success<User>) uiState).data.password);
                        fragmentDirections.setPerformAutoLogin(true);
                        navController.navigate(fragmentDirections);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    navController.navigate(RegisterFragmentDirections.actionAdminRegisterToLoginFragment(null, null));
                    dialogInterface.dismiss();
                }).setOnDismissListener(dialogInterface -> {
                    mViewModel.userMutableLiveData.setValue(null);
                    mViewModel.passwordField.set("");
                    mViewModel.emailField.set("");
                    mViewModel.confirmPasswordField.set("");
                    mViewModel.firstNameField.set("");
                    mViewModel.lastNameField.set("");
                    mViewModel.middleNameField.set("");
                }).create();
        return this.binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        this.adapter = new FilesRecyclerViewAdapter(index -> {
            mViewModel.fileActionStateMutableLiveData.setValue(new RegisterViewModel.FileActionState.RemoveFile(index));
        }, new ArrayList<>());

        this.navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        this.binding.setViewModel(mViewModel);
        this.binding.redirectLogin.setOnClickListener(view1 -> {
            navController.navigate(RegisterFragmentDirections.actionAdminRegisterToLoginFragment(null, null));
        });


        binding.registerAction.setOnClickListener(view_ -> {
            mViewModel.register(view_, adapter.getSelectedFiles());
        });

        ArrayList<String> roles = new ArrayList<>();
        roles.add("Admin");
        roles.add("Employee");
        roles.add("Employer");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, roles);

        this.binding.roleSpinner.setAdapter(arrayAdapter);
        this.binding.roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedRole = (String) adapterView.getSelectedItem();
                mViewModel.roleMutableLiveData.setValue(selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.binding.filesLoadedList.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.binding.filesLoadedList.setAdapter(adapter);

        this.binding.addFile.setOnClickListener(view_ -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            launcher.launch(intent);
        });

        mViewModel.roleMutableLiveData.observe(getViewLifecycleOwner(), s -> {
            if (s.equals("Admin")) {
                this.binding.addFile.setVisibility(View.GONE);
                this.binding.filesLoadedList.setVisibility(View.GONE);
            } else if (s.equals("Employee")) {
                this.binding.addFile.setVisibility(View.VISIBLE);
                this.binding.filesLoadedList.setVisibility(View.VISIBLE);
            } else if (s.equals("Employer")) {
                this.binding.addFile.setVisibility(View.VISIBLE);
                this.binding.filesLoadedList.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), userUIState -> {
            if (userUIState instanceof UIState.Success) {
                this.alertDialog.show();
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (userUIState instanceof UIState.Loading) {
                Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show();
            } else if (userUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<User>) userUIState).message, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.fileActionStateMutableLiveData.observe(getViewLifecycleOwner(), fileActionState -> {
            if (fileActionState instanceof RegisterViewModel.FileActionState.AddFile) {
                adapter.addFile(((RegisterViewModel.FileActionState.AddFile) fileActionState).data);
            } else if (fileActionState instanceof RegisterViewModel.FileActionState.RemoveFile) {
                adapter.fileDeleted(((RegisterViewModel.FileActionState.RemoveFile) fileActionState).data);
            }
        });
    }

    public void resetState() {
        mViewModel.userMutableLiveData.setValue(null);
        mViewModel.roleMutableLiveData.setValue(null);
    }
}