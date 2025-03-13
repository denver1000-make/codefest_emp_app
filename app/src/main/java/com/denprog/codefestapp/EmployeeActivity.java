package com.denprog.codefestapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityEmployeeBinding;

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