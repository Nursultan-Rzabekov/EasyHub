package com.example.javademogithubpractice.inject.module;


import android.app.Application;
import androidx.room.Room;
import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.example.javademogithubpractice.room.dao.AuthUserDao;
import com.example.javademogithubpractice.room.database.DatabaseRoom;
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


    private volatile DatabaseRoom INSTANCE;

    @Provides
    @Singleton
    public DatabaseRoom getDatabase(Application application) {
        if(INSTANCE == null){
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(application, DatabaseRoom.class, AppConfig.DB_NAME).build();
            }
        }
        return INSTANCE;
    }

    @Provides
    @Singleton
    public AuthUserDao provideAuthUserDao(DatabaseRoom database){
        return database.authUserDao();
    }



    @Provides
    @Singleton
    public DaoSessionImpl provideDaoSession() {
        AuthUserDao authUserDao = getDatabase(application).authUserDao();
        return new DaoSessionImpl(authUserDao);
    }


}
