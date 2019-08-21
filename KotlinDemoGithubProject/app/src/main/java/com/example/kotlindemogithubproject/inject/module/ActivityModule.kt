package com.example.kotlindemogithubproject.inject.module

import android.content.Context
import com.example.kotlindemogithubproject.inject.ActivityScope
import com.example.kotlindemogithubproject.ui.base.BaseActivity
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(private val mActivity: BaseActivity<*>) {

    @Provides
    @ActivityScope
    fun provideActivity(): BaseActivity<*> {
        return mActivity
    }

    @Provides
    @ActivityScope
    fun provideContext(): Context {
        return mActivity
    }
}
