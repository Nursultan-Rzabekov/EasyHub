

package com.example.javademogithubpractice.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerActivityComponent;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.mvp.contract.ISplashContract;
import com.example.javademogithubpractice.mvp.presenter.SplashPresenter;
import com.example.javademogithubpractice.ui.base.BaseActivity;


public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    private final String TAG = "SplashActivity";

    @Override
    public void showMainPage() {
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            //BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
        }
    }

    @Override
    public void showLoginPage() {
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        mPresenter.getUser();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
