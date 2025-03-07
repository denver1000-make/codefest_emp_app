package com.denprog.codefestapp.hilt.module;

import android.content.Context;

import androidx.room.Room;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {

        return Room.databaseBuilder(context, AppDatabase.class, "AppDatabase").fallbackToDestructiveMigration().build();
    }

}
