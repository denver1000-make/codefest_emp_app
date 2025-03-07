package com.denprog.codefestapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityHomeBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private HomeActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        int userId = intent.getIntExtra(HomeActivityViewModel.USER_ID_BUNDLE_KEY, -1);
        if (userId != -1) {
            this.viewModel.setUserIdMutableLiveData(userId);
        }


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.adminHomeFragment)
                .build();
        NavController navController = NavHostFragment.findNavController(binding.navHostFragmentActivityHome.getFragment());
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}