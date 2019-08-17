

package com.example.javademogithubpractice.inject.component;


import com.example.javademogithubpractice.inject.ActivityScope;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.ui.activity.LoginActivity;
import com.example.javademogithubpractice.ui.activity.SplashActivity;

import dagger.Component;


@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
}
