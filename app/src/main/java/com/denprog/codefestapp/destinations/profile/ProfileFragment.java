package com.denprog.codefestapp.destinations.profile;

import android.content.Intent;
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

import com.denprog.codefestapp.MainActivity;
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
        mViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        mViewModel.mutableUserState.observe(getViewLifecycleOwner(), userUIState -> {
            if (userUIState instanceof UIState.Success) {
                binding.setUser(((UIState.Success<User>) userUIState).data);
                mViewModel.determineRole(((UIState.Success<User>) userUIState).data.userId, data -> binding.textView7.setText(data));
            } else if (userUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<User>) userUIState).message, Toast.LENGTH_SHORT).show();
            }
        });

        this.mViewModel.logoutState.observe(getViewLifecycleOwner(), new Observer<UIState<Void>>() {
            @Override
            public void onChanged(UIState<Void> voidUIState) {
                if (voidUIState instanceof UIState.Loading) {
                    binding.logoutAction.setEnabled(false);
                } else if (voidUIState instanceof UIState.Fail) {
                    binding.logoutAction.setEnabled(true);
                    Toast.makeText(requireContext(), ((UIState.Fail<Void>) voidUIState).message, Toast.LENGTH_SHORT).show();
                } else if (voidUIState instanceof UIState.Success) {
                    binding.logoutAction.setEnabled(false);
                    Toast.makeText(requireContext(), "Cleared Saved Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
        });

        this.binding.logoutAction.setOnClickListener(view -> {
            mViewModel.clearSavedLogin();
        });
    }

}