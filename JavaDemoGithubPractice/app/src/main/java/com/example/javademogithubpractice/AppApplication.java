

package com.example.javademogithubpractice;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerAppComponent;
import com.example.javademogithubpractice.inject.module.AppModule;
import com.example.javademogithubpractice.service.NetBroadcastReceiver;
import com.example.javademogithubpractice.util.NetHelper;
import com.orhanobut.logger.Logger;


public class AppApplication extends Application {

    private final String TAG = AppApplication.class.getSimpleName();

    private static AppApplication application;
    private AppComponent mAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        long startTime = System.currentTimeMillis();
        AppData.INSTANCE.getSystemDefaultLocal();

        Logger.t(TAG).i("startTime:" + startTime);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        initNetwork();

        startTime = System.currentTimeMillis();
        Logger.t(TAG).i("application ok:" + (System.currentTimeMillis() - startTime));


    }

    private void initNetwork(){
        NetBroadcastReceiver receiver = new NetBroadcastReceiver();
        IntentFilter filter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        } else {
            filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        registerReceiver(receiver, filter);
        NetHelper.INSTANCE.init(this);
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }


}
