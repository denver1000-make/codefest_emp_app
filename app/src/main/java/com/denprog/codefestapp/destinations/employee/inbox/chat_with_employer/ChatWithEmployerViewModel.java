package com.denprog.codefestapp.destinations.employee.inbox.chat_with_employer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;
import com.denprog.codefestapp.util.TimeUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatWithEmployerViewModel extends ViewModel {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    public MutableLiveData<List<PrivateChatItemText>> privateChatItemTextLiveData = new MutableLiveData<>(new ArrayList<>());
    private AppDao appDao;

    @Inject
    public ChatWithEmployerViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getChat(int threadId) {
        executorService.submit(() -> {
            List<PrivateChatItemText> privateChatItemTextList = appDao.getAllChatItem(threadId);
            privateChatItemTextLiveData.postValue(privateChatItemTextList);
        });
    }

    public void sendChat(int senderId, int threadId, String senderEmail, String content) {
        long timeStamp = TimeUtil.getCurrentTimeSeconds();
        String formattedTime = TimeUtil.getFormattedTime(timeStamp);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                appDao.insertChat(new PrivateChatItemText(timeStamp, formattedTime, senderId, senderEmail, threadId, content));
                getChat(threadId);
            }
        });
    }


}
