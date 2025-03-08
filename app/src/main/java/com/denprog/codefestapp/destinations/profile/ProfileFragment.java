package com.denprog.codefestapp.destinations.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentProfileBinding;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.mutableUserState.observe(getViewLifecycleOwner(), userUIState -> {
            if (userUIState instanceof UIState.Success) {
                binding.setUser(((UIState.Success<User>) userUIState).data);
                mViewModel.determineRole(((UIState.Success<User>) userUIState).data.userId, data -> binding.textView7.setText(data));
            } else if (userUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<User>) userUIState).message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}