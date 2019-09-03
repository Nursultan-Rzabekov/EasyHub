

package com.example.javademogithubpractice.ui.activity.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.mvp.contract.IBaseContract;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.example.javademogithubpractice.ui.activity.LoginActivity;
import com.example.javademogithubpractice.ui.activity.SplashActivity;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.WindowUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity<P extends IBaseContract.Presenter> extends AppCompatActivity implements IBaseContract.View{

    @Inject
    protected P mPresenter;
    private ProgressDialog mProgressDialog;
    private static BaseActivity curActivity;


    @BindView(R.id.toolbar) @Nullable protected Toolbar toolbar;

    protected boolean isAlive = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if((AppData.INSTANCE.getAuthUser() == null || AppData.INSTANCE.getLoggedUser() == null)
                && !this.getClass().equals(SplashActivity.class)
                && !this.getClass().equals(LoginActivity.class)){
            super.onCreate(savedInstanceState);
            finishAffinity();
            startActivity(new Intent(getActivity(), SplashActivity.class));
            return;
        }

        super.onCreate(savedInstanceState);
        isAlive = true;
        setupActivityComponent(getAppComponent());
        DataAutoAccess.getData(this, savedInstanceState);
        if(mPresenter != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);
            mPresenter.attachView(this);
        }
        if(savedInstanceState != null && AppData.INSTANCE.getAuthUser() == null){
            DataAutoAccess.getData(AppData.INSTANCE, savedInstanceState);
        }

        getScreenSize();

        if(getContentView() != 0){
            setContentView(getContentView());
            ButterKnife.bind(getActivity());
        }

        initActivity();
        initView(savedInstanceState);
        if(mPresenter != null) mPresenter.onViewInitialized();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DataAutoAccess.saveData(this, outState);
        if(mPresenter != null) mPresenter.onSaveInstanceState(outState);
        if(curActivity.equals(this)){
            DataAutoAccess.saveData(AppData.INSTANCE, outState);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    protected abstract void setupActivityComponent(AppComponent appComponent);

    @LayoutRes
    protected abstract int getContentView();

    @CallSuper
    protected void initActivity(){

    }

    @CallSuper
    protected void initView(Bundle savedInstanceState){
        if(toolbar != null){
            setSupportActionBar(toolbar);
//            DoubleClickHandler.setDoubleClickListener(toolbar, new DoubleClickHandler.DoubleClickListener() {
//                @Override
//                public void onDoubleClick(View view) {
//                    onToolbarDoubleClick();
//                }
//            });
        }
    }

    protected void onToolbarDoubleClick(){
        PrefUtils.set(PrefUtils.DOUBLE_CLICK_TITLE_TIP_ABLE, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        curActivity = getActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) mPresenter.detachView();
        if(this.equals(curActivity)) curActivity = null;
        isAlive = false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    protected void setToolbarBackEnable() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setToolbarTitle(String title, String subTitle) {
        setToolbarTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setToolbarSubTitle(String subTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setToolbarScrollAble(boolean scrollAble) {
        if(toolbar == null) return;
        int flags = scrollAble ? (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP) : 0;
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setScrollFlags(flags);
        toolbar.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishActivity(){
        finish();
    }

    protected Fragment getVisibleFragment(){
        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if(fragmentList != null ){
            for(Fragment fragment : fragmentList){
                if(fragment != null && fragment.isVisible()){
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void showProgressDialog(String content) {
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }else{
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInfoToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWarningToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTipDialog(String content) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("qweqwe")
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton("wqewq", (dialog, which) -> dialog.cancel())
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton("sadasd", (dialog, which) -> dialog.cancel())
                .show();
    }

    protected void postDelayFinish(int time){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, time);
    }

    public static BaseActivity getCurActivity() {
        return curActivity;
    }


    @NonNull
    protected BaseActivity getActivity() {
        return this;
    }

    @NonNull
    protected AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    protected AppComponent getAppComponent(){
        return getAppApplication().getAppComponent();
    }

    protected AuthSessionRepository getDaoSession(){
        return getAppComponent().getDaoSession();
    }


    private void getScreenSize(){
        if(WindowUtil.screenHeight == 0 ||
                WindowUtil.screenWidth == 0){
            Display display = getWindowManager().getDefaultDisplay();
            WindowUtil.screenWidth = display.getWidth();
            WindowUtil.screenHeight = display.getHeight();
        }
    }

    public String getAppVersionName() {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    protected void delayFinish(){
        delayFinish(1000);
    }

    protected void delayFinish(int mills){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, mills);
    }

    protected void setTransparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    public void showLoginPage() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() { super.onBackPressed();

    }
}
