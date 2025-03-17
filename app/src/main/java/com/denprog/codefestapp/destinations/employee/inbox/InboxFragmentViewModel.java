package com.denprog.codefestapp.destinations.employee.inbox;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.view.ChatThreadWithEmployeeName;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InboxFragmentViewModel extends ViewModel {

    public MutableLiveData<List<ChatThreadWithEmployeeName>> chatThreadLiveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<UIState<CredentialsForMessage>> credeUiStateMutableLiveData = new MediatorLiveData<>(null);
    private AppDao appDao;

    @Inject
    public InboxFragmentViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllChatThread(int employeeId) {
        CompletableFuture.supplyAsync(new Supplier<Void>() {
            @Override
            public Void get() {
                List<ChatThreadWithEmployeeName> chatThreadWithEmployeeId = appDao.getAllChatThreadWithEmployeeId(employeeId);
                chatThreadLiveData.postValue(chatThreadWithEmployeeId);
                return null;
            }
        }).exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {

                return null;
            }
        });
    }

    public static class CredentialsForMessage {
        public int threadId;
        public int senderId;
        public String email;

        public CredentialsForMessage(int threadId, int senderId, String email) {
            this.threadId = threadId;
            this.senderId = senderId;
            this.email = email;
        }
    }

}
