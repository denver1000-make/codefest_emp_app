package com.denprog.codefestapp;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLER_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityEmployeeBinding;
import com.denprog.codefestapp.util.UIState;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeeActivity extends AppCompatActivity {
    private ActivityEmployeeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = NavHostFragment.findNavController(binding.navHostFragmentActivityEmployee.getFragment());
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}