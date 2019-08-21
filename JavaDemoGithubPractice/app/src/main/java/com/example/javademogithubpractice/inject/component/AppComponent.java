

package com.example.javademogithubpractice.inject.component;



import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.dao.DaoSession;
import com.example.javademogithubpractice.inject.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    AppApplication getApplication();
    DaoSession getDaoSession();

}
