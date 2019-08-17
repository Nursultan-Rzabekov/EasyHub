

package com.example.javademogithubpractice.http.core;

import android.app.AlertDialog;
import android.support.annotation.NonNull;

public class HttpProgressSubscriber<T> extends HttpSubscriber<T> {

    private AlertDialog mDialog;

    public HttpProgressSubscriber(@NonNull AlertDialog dialog, @NonNull HttpObserver<T> observer) {
        super(observer);
        mDialog = dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUnsubscribed())
            mDialog.show();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        mDialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        mDialog.dismiss();
    }
}
