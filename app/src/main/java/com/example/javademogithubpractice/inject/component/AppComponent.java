

package com.example.javademogithubpractice.inject.component;



import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.inject.module.AppModule;
import com.example.javademogithubpractice.room.AuthSessionRepository;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    AppApplication getApplication();
    AuthSessionRepository getDaoSession();
}
