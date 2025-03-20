package com.denprog.codefestapp.destinations.admin.announcement.dialog;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentAddAnnouncementDialogListBinding;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class AddAnnouncementDialogFragment extends Fragment {
    FragmentAddAnnouncementDialogListBinding binding;
    AddAnnouncementFragmentViewModel viewModel;
    ActivityResultLauncher<Intent> filePicker;
    AnnouncementAttachmentRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddAnnouncementDialogListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = o.getData();
                    if (intent != null) {
                        Uri uri = intent.getData();
                        String filePath = getFileNameFromUri(requireContext(), uri);
                        if (filePath != null) {
                            AnnouncementAttachment announcementAttachment = new AnnouncementAttachment();
                            announcementAttachment.uri = uri;
                            announcementAttachment.fileName = filePath;
                            viewModel.addItem(announcementAttachment);
                            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Please Choose Another File", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        this.viewModel = new ViewModelProvider(requireActivity()).get(AddAnnouncementFragmentViewModel.class);
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.adapter = new AnnouncementAttachmentRecyclerViewAdapter();
        this.binding.list.setAdapter(adapter);

        this.binding.button2.setOnClickListener(view1 -> {
            Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            filePickerIntent.setType("*/*");
            filePicker.launch(filePickerIntent);
        });

        this.viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<AnnouncementAttachment>>() {
            @Override
            public void onChanged(List<AnnouncementAttachment> announcementAttachments) {
                adapter.refreshList(announcementAttachments);
            }
        });

        AddAnnouncementDialogFragmentArgs args = AddAnnouncementDialogFragmentArgs.fromBundle(getArguments());
        NavController navController = Navigation.findNavController(requireActivity(), com.denprog.codefestapp.R.id.adminFragmentContainerView);
        this.binding.publishAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String announcementName = binding.announcementNameField.getText().toString();
                String announcementDescription = binding.announcementDescription.getText().toString();
                viewModel.publishAnnouncement(requireContext(), args.getAdminId(), announcementName, announcementDescription, viewModel.listMutableLiveData.getValue(), new OnOperationSuccessful<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        navController.popBackStack();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading() {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public String getFileNameFromUri(Context context, Uri uri) {
        try (Cursor cursor = context.getContentResolver().query(uri, new String[] {OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}