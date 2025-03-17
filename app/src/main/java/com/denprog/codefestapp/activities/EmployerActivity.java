package com.denprog.codefestapp.activities;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.ActivityEmployerBinding;
import com.denprog.codefestapp.destinations.employer.EmployerHomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployerActivity extends AppCompatActivity {

    private ActivityEmployerBinding binding;
    private EmployerHomeViewModel mainViewModel;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployerBinding.inflate(getLayoutInflater());
        this.mainViewModel = new ViewModelProvider(this).get(EmployerHomeViewModel.class);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        setSupportActionBar(binding.employerAppBar.toolbar);
        int employerId = intent.getIntExtra(EMPLOYER_ID_BUNDLE_KEY, -1);
        if (employerId != -1) {
            mainViewModel.empIdMutableLiveData.setValue(employerId);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.employerHomeFragment, R.id.employerProfile)
                    .setOpenableLayout(binding.container)
                    .build();

            NavController navController = NavHostFragment.findNavController(binding.employerAppBar.employerContent.employerFragmentContainer.getFragment());
            NavigationUI.setupWithNavController(binding.employerNavigation, navController);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        } else {
            Toast.makeText(this, "Error In Loading the Employer", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =NavHostFragment.findNavController(binding.employerAppBar.employerContent.employerFragmentContainer.getFragment());
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}