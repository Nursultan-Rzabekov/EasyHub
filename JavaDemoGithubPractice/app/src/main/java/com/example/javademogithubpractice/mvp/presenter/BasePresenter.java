

package com.example.javademogithubpractice.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.dao.DaoSession;
import com.example.javademogithubpractice.http.LoginService;
import com.example.javademogithubpractice.http.UserService;
import com.example.javademogithubpractice.http.core.AppRetrofit;
import com.example.javademogithubpractice.http.error.HttpError;
import com.example.javademogithubpractice.http.error.UnauthorizedError;
import com.example.javademogithubpractice.mvp.contract.IBaseContract;
import com.example.javademogithubpractice.util.StringUtils;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    private final String TAG = "BasePresenter";

    protected V mView;
    protected DaoSession daoSession;

    private boolean isViewInitialized = false;

    private boolean isAttached = false;


    public BasePresenter(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        DataAutoAccess.saveData(this, outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        if (outState == null) return;
        DataAutoAccess.getData(this, outState);
    }

    @Override
    public void attachView(@NonNull V view) {
        mView = view;

        onViewAttached();
        isAttached = true;
    }

    @Override
    public void onViewInitialized() {
        isViewInitialized = true;
    }

    protected boolean isViewInitialized() {
        return isViewInitialized;
    }


    protected LoginService getLoginService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_BASE_URL, null)
                .create(LoginService.class);
    }

    protected LoginService getLoginService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
                .create(LoginService.class);
    }

    protected UserService getUserService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
                .create(UserService.class);
    }

    protected UserService getUserService() {
        return getUserService(AppData.INSTANCE.getAccessToken());
    }


    private <T> T getServices(Class<T> serviceClass) {
        return getServices(serviceClass, AppConfig.GITHUB_API_BASE_URL, true);
    }

    protected <T> T getServices(Class<T> serviceClass, String baseUrl, boolean isJson) {
        return AppRetrofit.INSTANCE
                .getRetrofit(baseUrl, AppData.INSTANCE.getAccessToken(), isJson)
                .create(serviceClass);
    }

    @NonNull
    @Override
    public Context getContext() {
        if (mView instanceof Context) {
            return (Context) mView;
        } else if (mView instanceof Fragment) {
            return ((Fragment) mView).getContext();
        } else {
            throw new NullPointerException("BasePresenter:mView is't instance of context,can't use getContext() method.");
        }
    }

    @CallSuper
    protected void onViewAttached() {

    }

    private boolean checkIsUnauthorized(Throwable error){
        if(error instanceof UnauthorizedError){
            mView.showErrorToast(error.getMessage());
            daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
            AppData.INSTANCE.setAuthUser(null);
            AppData.INSTANCE.setLoggedUser(null);
            mView.showLoginPage();
            return true;
        }
        return false;
    }

    @NonNull
    protected String getLoadTip() {
        return getContext().getString(R.string.loading).concat("...");
    }

    @NonNull
    protected String getErrorTip(@NonNull Throwable error) {
        String errorTip = null;
        if(error == null){
            return errorTip;
        }
        if(error instanceof UnknownHostException){
            errorTip = getString(R.string.no_network_tip);
        } else if (error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException) {
            errorTip = getString(R.string.load_timeout_tip);
        } else if (error instanceof HttpError) {
            errorTip = error.getMessage();
        } else {
            errorTip = StringUtils.isBlank(error.getMessage()) ? error.toString() : error.getMessage();
        }
        return errorTip;
    }


    @NonNull
    protected String getString(@StringRes int resId) {
        return getContext().getResources().getString(resId);
    }
}
