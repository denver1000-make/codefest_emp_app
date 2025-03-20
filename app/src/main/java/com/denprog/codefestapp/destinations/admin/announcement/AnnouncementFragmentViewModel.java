package com.denprog.codefestapp.destinations.admin.announcement;

import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnnouncementFragmentViewModel extends ViewModel {

    AppDao appDao;

    @Inject
    public AnnouncementFragmentViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }
}
