

package com.example.javademogithubpractice.inject.module;

import android.content.Context;

import com.example.javademogithubpractice.inject.FragmentScope;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;

import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {

    private BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @FragmentScope
    public BaseFragment provideFragment(){
        return mFragment;
    }

    @Provides
    @FragmentScope
    public Context provideContext(){
        return mFragment.getActivity();
    }
}
