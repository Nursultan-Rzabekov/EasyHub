

package com.example.javademogithubpractice.mvp.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface IBaseContract {

    interface View {
        void showProgressDialog(String content);
        void dismissProgressDialog();
        ProgressDialog getProgressDialog(String content);
        void showTipDialog(String content);
        void showConfirmDialog(String msn, String title, String confirmText, DialogInterface.OnClickListener confirmListener);
        void showToast(String message);
        void showInfoToast(String message);
        void showSuccessToast(String message);
        void showErrorToast(String message);
        void showWarningToast(String message);
        default void showLoading() {}
        default void hideLoading() {}
        void showLoginPage();
    }

    interface Presenter<V extends IBaseContract.View>{
        void onSaveInstanceState(Bundle outState);
        void onRestoreInstanceState(Bundle outState);
        void attachView(@NonNull V view);
        void detachView();
        void onViewInitialized();
        @Nullable
        Context getContext();
    }
}
