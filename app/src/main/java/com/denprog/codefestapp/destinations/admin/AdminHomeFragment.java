package com.denprog.codefestapp.destinations.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentApplicationListBinding;
import com.denprog.codefestapp.destinations.admin.placeholder.PlaceholderContent;
import com.denprog.codefestapp.room.entity.User;

import java.util.Collections;
import java.util.List;

public class AdminHomeFragment extends Fragment {
    ApplicationRecyclerViewAdapter adapter;
    FragmentApplicationListBinding binding;
    AdminHomeViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplicationListBinding.inflate(inflater, container, false);
        RecyclerView rcv = binding.getRoot();
        rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_home);
        this.adapter = new ApplicationRecyclerViewAdapter((int userId) -> {
            navController.navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToViewUserFragment(userId));
        });
        rcv.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(AdminHomeViewModel.class);
        viewModel.getAccountsToReview();
        viewModel.credentialsList.observe(getViewLifecycleOwner(), users -> {
            adapter.refreshList(users);
        });
    }
}