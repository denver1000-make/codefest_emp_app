package com.denprog.codefestapp.destinations.admin.announcement;

import static com.denprog.codefestapp.HomeActivityViewModel.ADMIN_ID_BUNDLE_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAnnouncementListBinding;
import com.denprog.codefestapp.destinations.admin.announcement.dialog.AddAnnouncementDialogFragment;
import com.denprog.codefestapp.destinations.admin.announcement.placeholder.PlaceholderContent;
public class AnnouncementFragment extends Fragment {
    FragmentAnnouncementListBinding binding;
    AnnouncementFragmentViewModel viewModel;
    AddAnnouncementDialogFragment fragment;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentAnnouncementListBinding.inflate(inflater, container, false);
        fragment = new AddAnnouncementDialogFragment();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(AnnouncementFragmentViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.adminFragmentContainerView);
        Intent intent = requireActivity().getIntent();
        int adminId = intent.getIntExtra(ADMIN_ID_BUNDLE_KEY, -1);
        if (adminId != -1) {
            this.binding.addAnnouncement.setOnClickListener(view1 -> {
                navController.navigate(AnnouncementFragmentDirections.actionAnnouncementFragmentToAddAnnouncementDialogFragment(adminId));
            });
        } else {
            requireActivity().finish();
        }
    }

}