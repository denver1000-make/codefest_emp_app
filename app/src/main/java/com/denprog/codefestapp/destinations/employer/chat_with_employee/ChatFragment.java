package com.denprog.codefestapp.destinations.employer.chat_with_employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.databinding.FragmentChatListBinding;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.TimeUtil;

import java.util.List;

public class ChatFragment extends Fragment {

    ChatFragmentViewModel viewModel;
    private FragmentChatListBinding binding;
    private ChatRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentChatListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(ChatFragmentViewModel.class);
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());

        this.adapter = new ChatRecyclerViewAdapter(args.getSenderEmail());
        this.binding.list.setAdapter(adapter);
        this.binding.setViewModel(this.viewModel);
        this.binding.sendChatAction.setOnClickListener(view2 -> {
            String chatContent = viewModel.chatContent.get();
            long epochSecond = TimeUtil.getCurrentTimeSeconds();
            String formatted = TimeUtil.getFormattedTime(epochSecond);
            viewModel.sendChat(epochSecond, formatted, chatContent, args.getSenderEmail(), args.getEmployerId(), args.getThreadId(), new OnOperationSuccessful<Void>() {
                @Override
                public void onSuccess(Void data) {
                    viewModel.chatContent.set("");
                    refreshChatList(args.getThreadId());
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading() {
                    Toast.makeText(requireContext(), "Sending....", Toast.LENGTH_SHORT).show();
                }
            });
        });

        viewModel.mutableChatThread.observe(getViewLifecycleOwner(), new Observer<List<PrivateChatItemText>>() {
            @Override
            public void onChanged(List<PrivateChatItemText> privateChatItemTexts) {
                adapter.refreshList(privateChatItemTexts);
            }
        });
    }

    public void refreshChatList(int threadId) {
        viewModel.getAllChat(threadId, new OnOperationSuccessful<List<PrivateChatItemText>>() {
            @Override
            public void onSuccess(List<PrivateChatItemText> data) {
                adapter.refreshList(data);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onLoading() {

            }
        });
    }

}