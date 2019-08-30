

package com.example.javademogithubpractice.inject.component;

import com.example.javademogithubpractice.inject.FragmentScope;
import com.example.javademogithubpractice.inject.module.FragmentModule;
import com.example.javademogithubpractice.ui.fragment.ProfileInfoFragment;
import com.example.javademogithubpractice.ui.fragment.RepoInfoFragment;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.ui.fragment.UserListFragment;

import dagger.Component;



@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(RepositoriesFragment fragment);
    void inject(RepoInfoFragment repoInfoFragment);
    void inject(ProfileInfoFragment profileInfoFragment);
    void inject(UserListFragment userListFragment);
}
