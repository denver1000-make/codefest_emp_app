package com.denprog.codefestapp.destinations.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentApplicationListBinding;

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