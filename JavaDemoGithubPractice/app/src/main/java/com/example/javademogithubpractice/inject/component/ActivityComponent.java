

package com.example.javademogithubpractice.inject.component;


import com.example.javademogithubpractice.inject.ActivityScope;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.ui.activity.LoginActivity;
import com.example.javademogithubpractice.ui.activity.MainActivity;
import com.example.javademogithubpractice.ui.activity.ProfileActivity;
import com.example.javademogithubpractice.ui.activity.RepositoryActivity;
import com.example.javademogithubpractice.ui.activity.SplashActivity;
import com.example.javademogithubpractice.ui.activity.SearchActivity;

import dagger.Component;


@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(RepositoryActivity activity);
    void inject(ProfileActivity activity);
    void inject(SearchActivity activity);
}
