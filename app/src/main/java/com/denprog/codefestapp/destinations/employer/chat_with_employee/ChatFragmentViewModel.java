package com.denprog.codefestapp.destinations.employer.chat_with_employee;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;
import com.denprog.codefestapp.room.entity.PrivateChatThread;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatFragmentViewModel extends ViewModel {
    private AppDao appDao;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    public ObservableField<String> chatContent = new ObservableField<>("");
    public MutableLiveData<List<PrivateChatItemText>> mutableChatThread = new MutableLiveData<>(new ArrayList<>());
    @Inject
    public ChatFragmentViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllChat(int threadId, OnOperationSuccessful<List<PrivateChatItemText>> onOperationSuccessful) {
        CompletableFuture<List<PrivateChatItemText>> chatItemTextList = CompletableFuture.supplyAsync(new Supplier<List<PrivateChatItemText>>() {
            @Override
            public List<PrivateChatItemText> get() {
                return appDao.getAllChatItem(threadId);
            }
        });

        chatItemTextList.thenAcceptAsync(new Consumer<>() {
            @Override
            public void accept(List<PrivateChatItemText> privateChatItemTexts) {
                mutableChatThread.postValue(privateChatItemTexts);
            }
        });

        chatItemTextList.exceptionally(new Function<Throwable, List<PrivateChatItemText>>() {
            @Override
            public List<PrivateChatItemText> apply(Throwable throwable) {
                onOperationSuccessful.onError(throwable.getLocalizedMessage());
                return Collections.emptyList();
            }
        });
    }

    public void sendChat(long timeInSecond, String timeStamp, String content, int senderId, int threadId, OnOperationSuccessful<Void> onOperationSuccessful) {
        CompletableFuture<Void> sendingChat = CompletableFuture.supplyAsync(new Supplier<Void>() {
            @Override
            public Void get() {
                appDao.insertChat(new PrivateChatItemText(timeInSecond, timeStamp, senderId, threadId, chatContent.get()));
                return null;
            }
        });

        sendingChat.thenAcceptAsync(new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onOperationSuccessful.onSuccess(null);
                    }
                });
            }
        });

        sendingChat.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onOperationSuccessful.onError(throwable.getLocalizedMessage());
                    }
                });
                return null;
            }
        });
    }



}
