package com.denprog.codefestapp.destinations.employee.inbox.chat_with_employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denprog.codefestapp.databinding.FragmentChatListBinding;
import com.denprog.codefestapp.destinations.employer.chat_with_employee.ChatRecyclerViewAdapter;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;

import java.util.List;
import java.util.Objects;

public class ChatEmployerFragment extends Fragment {

    private FragmentChatListBinding binding;
    private ChatWithEmployerViewModel viewModel;
    ChatRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentChatListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(ChatWithEmployerViewModel.class);
        ChatEmployerFragmentArgs args = ChatEmployerFragmentArgs.fromBundle(getArguments());
        this.adapter = new ChatRecyclerViewAdapter(args.getEmail());
        this.viewModel.privateChatItemTextLiveData.observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<PrivateChatItemText> privateChatItemTexts) {
                adapter.refreshList(privateChatItemTexts);
            }
        });
        this.binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.binding.list.setAdapter(adapter);

        this.binding.sendChatAction.setOnClickListener(view1 -> {
            String content = Objects.requireNonNull(binding.chatContent.getText()).toString();
            viewModel.sendChat(args.getEmployeeId(), args.getThreadId(), args.getEmail(), content);
        });

        this.viewModel.getChat(args.getThreadId());
    }
}
