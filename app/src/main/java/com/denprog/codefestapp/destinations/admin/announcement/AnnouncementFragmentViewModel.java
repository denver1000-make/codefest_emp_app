package com.denprog.codefestapp.destinations.admin.announcement;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.Announcement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnnouncementFragmentViewModel extends ViewModel {

    AppDao appDao;
    public MutableLiveData<List<Announcement>> announcementsLiveData = new MutableLiveData<>(new ArrayList<>());

    @Inject
    public AnnouncementFragmentViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllAnnouncements() {
        CompletableFuture.supplyAsync(new Supplier<List<Announcement>>() {
            @Override
            public List<Announcement> get() {
                return appDao.getAllAnnouncements();
            }
        }).thenAcceptAsync(new Consumer<List<Announcement>>() {
            @Override
            public void accept(List<Announcement> announcements) {
                announcementsLiveData.postValue(announcements);
            }
        });
    }



}
