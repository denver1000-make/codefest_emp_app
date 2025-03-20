package com.denprog.codefestapp.destinations.admin.announcement.announcement_viewer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAnnouncementBinding;
import com.denprog.codefestapp.databinding.FragmentAnnouncementViewerBinding;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.util.List;

public class AnnouncementViewerFragment extends Fragment {

    private AnnouncementViewerViewModel mViewModel;
    private FragmentAnnouncementViewerBinding binding;
    private AnnouncementAttachmentRecyclerViewAdapter adapter;

    public static AnnouncementViewerFragment newInstance() {
        return new AnnouncementViewerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAnnouncementViewerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewModel = new ViewModelProvider(requireActivity()).get(AnnouncementViewerViewModel.class);
        AnnouncementViewerFragmentArgs args = AnnouncementViewerFragmentArgs.fromBundle(getArguments());
        int announcementId = args.getAnnouncementId();
        this.adapter = new AnnouncementAttachmentRecyclerViewAdapter(new AnnouncementAttachmentRecyclerViewAdapter.AttachmentInteraction() {
            @Override
            public void onDownload(String filePath) {
                mViewModel.downloadFileToDownloadsFolder(requireContext(), filePath, new OnOperationSuccessful<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading() {

                    }
                });
            }
        });
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.binding.list.setAdapter(adapter);
        this.mViewModel.getAllAttachmentsOfAnnouncement(announcementId);
        this.mViewModel.announcementAttachmentMutableLiveData.observe(getViewLifecycleOwner(), announcementAttachmentList -> {
            adapter.refreshList(announcementAttachmentList);
        });



    }
}