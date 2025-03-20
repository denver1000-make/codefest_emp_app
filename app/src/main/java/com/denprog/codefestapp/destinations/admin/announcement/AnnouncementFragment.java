package com.denprog.codefestapp.destinations.admin.announcement;

import static com.denprog.codefestapp.HomeActivityViewModel.ADMIN_ID_BUNDLE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAnnouncementListBinding;
import com.denprog.codefestapp.destinations.admin.announcement.dialog.AddAnnouncementDialogFragment;
import com.denprog.codefestapp.room.entity.Announcement;

import java.util.List;

public class AnnouncementFragment extends Fragment {

    FragmentAnnouncementListBinding binding;
    AnnouncementFragmentViewModel viewModel;
    AnnouncementFragmentRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentAnnouncementListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(AnnouncementFragmentViewModel.class);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.adminFragmentContainerView);
            Intent intent = requireActivity().getIntent();
            int adminId = intent.getIntExtra(ADMIN_ID_BUNDLE_KEY, -1);
            adapter = new AnnouncementFragmentRecyclerViewAdapter(new AnnouncementFragmentRecyclerViewAdapter.AnnouncementInteraction() {
                @Override
            public void onOpen(int announcementId) {
                    navController.navigate(AnnouncementFragmentDirections.actionAdminAnnouncementToAnnouncementViewerFragment(announcementId));
            }
        });
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.binding.list.setAdapter(adapter);
        if (adminId != -1) {
            this.binding.addAnnouncement.setOnClickListener(view1 -> {
                navController.navigate(AnnouncementFragmentDirections.actionAnnouncementFragmentToAddAnnouncementDialogFragment(adminId));
            });
        } else {
            requireActivity().finish();
        }



        this.viewModel.getAllAnnouncements();

        this.viewModel.announcementsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                adapter.refreshAdapter(announcements);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}