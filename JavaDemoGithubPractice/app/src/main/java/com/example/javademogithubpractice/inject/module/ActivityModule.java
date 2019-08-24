

package com.example.javademogithubpractice.inject.module;

import android.content.Context;


import androidx.fragment.app.FragmentManager;

import com.example.javademogithubpractice.inject.ActivityScope;
import com.example.javademogithubpractice.ui.base.BaseActivity;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {

    private BaseActivity mActivity;
    public ActivityModule(BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    public BaseActivity provideActivity(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    public Context provideContext(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    public FragmentManager provideFragmentManager(){
        return mActivity.getSupportFragmentManager();
    }

}
