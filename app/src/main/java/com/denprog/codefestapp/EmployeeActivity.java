package com.denprog.codefestapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityEmployeeBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeeActivity extends AppCompatActivity {
    private ActivityEmployeeBinding binding;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.employeeAppBar.toolbar);
        this.appBarConfiguration = new AppBarConfiguration.Builder(R.id.employeeHomeFragment, R.id.employeeProfileFragment)
                .setOpenableLayout(binding.container)
                .build();

        NavController navController = NavHostFragment.findNavController(binding.employeeAppBar.employeeContent.employeeFragmentContianer.getFragment());
        NavigationUI.setupWithNavController(binding.employeeNavView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = NavHostFragment.findNavController(binding.employeeAppBar.employeeContent.employeeFragmentContianer.getFragment());
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}