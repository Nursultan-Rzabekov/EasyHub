

package com.example.javademogithubpractice.inject.module;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;


import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.dao.DBOpenHelper;
import com.example.javademogithubpractice.dao.DaoMaster;
import com.example.javademogithubpractice.dao.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private AppApplication application;

    public AppModule(AppApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public AppApplication provideApplication() {
        return application;
    }

    @NonNull
    @Provides
    @Singleton
    public DaoSession provideDaoSession() {
        DBOpenHelper helper = new DBOpenHelper(application, AppConfig.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }


}
