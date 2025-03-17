package com.denprog.codefestapp.destinations.employee.inbox;

import static com.denprog.codefestapp.HomeActivityViewModel.EMAIL_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.EMPLOYEE_ID_BUNDLE_KEY;
import static com.denprog.codefestapp.HomeActivityViewModel.USER_ID_BUNDLE_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentChatThreadListBinding;
import com.denprog.codefestapp.destinations.employee.inbox.placeholder.PlaceholderContent;
import com.denprog.codefestapp.room.view.ChatThreadWithEmployeeName;
import com.denprog.codefestapp.util.UIState;

import java.util.List;

public class InboxFragment extends Fragment {

    InboxFragmentViewModel viewModel;
    FragmentChatThreadListBinding binding;
    ChatThreadRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatThreadListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(InboxFragmentViewModel.class);
        Intent intent = requireActivity().getIntent();
        Bundle bundle = intent.getExtras();
        NavController navController = NavHostFragment.findNavController(requireParentFragment());
        if (bundle != null) {
            int employeeId = bundle.getInt(EMPLOYEE_ID_BUNDLE_KEY, -1);
            int userId = bundle.getInt(USER_ID_BUNDLE_KEY, -1);
            String email = bundle.getString(EMAIL_ID_BUNDLE_KEY, null);
            if (employeeId != -1 && userId != -1 && email != null) {
                this.viewModel.getAllChatThread(employeeId);
                this.adapter =
                        new ChatThreadRecyclerViewAdapter(
                                (employerId, threadId) ->
                                        navController.navigate(
                                                InboxFragmentDirections
                                                        .actionEmployeeInboxToChatEmployerFragment(
                                                                employeeId,
                                                                employerId,
                                                                threadId,
                                                                email)));
                this.viewModel.chatThreadLiveData.observe(getViewLifecycleOwner(), chatThreadWithEmployeeNames -> adapter.refreshList(chatThreadWithEmployeeNames));
            }
        }
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.binding.list.setAdapter(adapter);


    }



}