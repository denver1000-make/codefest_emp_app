package com.denprog.codefestapp.activities;

import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYER_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.denprog.codefestapp.MainActivity;
import com.denprog.codefestapp.R;
import com.denprog.codefestapp.destinations.employer.EmployerHomeViewModel;
import com.denprog.codefestapp.destinations.login.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityEmployerBinding;

public class EmployerActivity extends AppCompatActivity {

    private ActivityEmployerBinding binding;
    private EmployerHomeViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployerBinding.inflate(getLayoutInflater());
        this.mainViewModel = new ViewModelProvider(this).get(EmployerHomeViewModel.class);
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        int employerId = intent.getIntExtra(EMPLOYER_ID_BUNDLE_KEY, -1);
        if (employerId != -1) {
            mainViewModel.empIdMutableLiveData.setValue(employerId);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.employerHomeFragment)
                    .build();

            NavController navController =NavHostFragment.findNavController(binding.navHostFragmentActivityEmployer.getFragment());
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            Toast.makeText(this, "Error In Loading the Employer", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}