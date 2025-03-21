package com.denprog.codefestapp.destinations.admin.view_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.denprog.codefestapp.R;
import com.denprog.codefestapp.databinding.FragmentViewUserBinding;
import com.denprog.codefestapp.destinations.admin.AdminHomeViewModel;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;

public class ViewUserFragment extends Fragment {

    private ViewUserViewModel mViewModel;
    private FragmentViewUserBinding binding;
    private AdminHomeViewModel adminHomeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentViewUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ViewUserViewModel.class);
        this.adminHomeViewModel = new ViewModelProvider(requireActivity()).get(AdminHomeViewModel.class);
        int userId = ViewUserFragmentArgs.fromBundle(getArguments()).getUserId();
        mViewModel.loadUser(userId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.adminFragmentContainerView);
        mViewModel.userToViewMutableLiveData.observe(getViewLifecycleOwner(), userUIState -> {
            if (userUIState instanceof UIState.Fail) {
                Toast.makeText(requireContext(), ((UIState.Fail<User>) userUIState).message, Toast.LENGTH_SHORT).show();
            } else if (userUIState instanceof UIState.Success) {
                binding.setUser(((UIState.Success<User>) userUIState).data);
            }
        });

        binding.acceptAction.setOnClickListener(view2 -> {
            mViewModel.acceptAnApplication(userId, new ViewUserViewModel.OnOperationSuccessful() {
                @Override
                public void onSuccess() {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    navController.navigate(ViewUserFragmentDirections.actionViewUserFragmentToAdminHomeFragment(userId, true));
                    adminHomeViewModel.userReviewStatusMutableLiveData.setValue(new AdminHomeViewModel.UserReviewStatus(userId, true));
                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.rejectAction.setOnClickListener(view3 -> {
            mViewModel.rejectAnApplication(userId, new ViewUserViewModel.OnOperationSuccessful() {
                @Override
                public void onSuccess() {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    navController.navigate(ViewUserFragmentDirections.actionViewUserFragmentToAdminHomeFragment(userId, false));
                    adminHomeViewModel.userReviewStatusMutableLiveData.setValue(new AdminHomeViewModel.UserReviewStatus(userId, false));

                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.downloadCredentialsAction.setOnClickListener(view1 -> {
            UIState<User> uiStateUser = mViewModel.userToViewMutableLiveData.getValue();
            if (uiStateUser instanceof UIState.Success) {
                User user = ((UIState.Success<User>) uiStateUser).data;
                mViewModel.downloadUserData(view1, user.userId, user.firstName+user.lastName+user.middleName);
            }
        });
    }
}