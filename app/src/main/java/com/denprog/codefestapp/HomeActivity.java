package com.denprog.codefestapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denprog.codefestapp.databinding.ActivityHomeBinding;
import com.denprog.codefestapp.databinding.HeaderLayoutBinding;
import com.denprog.codefestapp.room.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.function.Consumer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private HomeActivityViewModel viewModel;
    AppBarConfiguration appBarConfiguration;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        this.viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        setContentView(binding.getRoot());

        NavigationView navigationView = binding.navView;
        DrawerLayout drawerLayout = binding.adminDrawer;
        Toolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int userId = intent.getIntExtra(HomeActivityViewModel.USER_ID_BUNDLE_KEY, -1);

        if (userId != -1) {
            this.viewModel.setUserIdMutableLiveData(userId);
            this.viewModel.fetchUser(userId).thenAcceptAsync(user -> {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        HeaderLayoutBinding headerLayoutBinding = HeaderLayoutBinding.bind(binding.navView.getHeaderView(0));
                        headerLayoutBinding.headerEmail.setText(user.email);
                        String fullName = user.firstName + " " + user.middleName + " " + user.lastName;
                        headerLayoutBinding.headerUserName.setText(fullName);
                    }
                });
            });
        } else {
            Toast.makeText(this, "Invalid Admin Account", Toast.LENGTH_SHORT).show();
            finish();
        }
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.adminHomeFragment, R.id.adminProfile)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = NavHostFragment.findNavController(binding.appBarMain.content.adminFragmentContainerView.getFragment());
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = NavHostFragment.findNavController(binding.appBarMain.content.adminFragmentContainerView.getFragment());
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}