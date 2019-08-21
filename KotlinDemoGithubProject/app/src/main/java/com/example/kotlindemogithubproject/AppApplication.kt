package com.example.kotlindemogithubproject

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import com.example.kotlindemogithubproject.inject.component.AppComponent
import com.example.kotlindemogithubproject.inject.component.DaggerAppComponent
import com.example.kotlindemogithubproject.inject.module.AppModule
import com.example.kotlindemogithubproject.service.NetBroadcastReceiver
import com.example.kotlindemogithubproject.util.NetHelper
import com.orhanobut.logger.Logger

class AppApplication : Application() {

    private val TAG = AppApplication::class.java.simpleName
    var appComponent: AppComponent? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        //init application
        var startTime = System.currentTimeMillis()
        AppData.INSTANCE.getSystemDefaultLocal()
        Logger.t(TAG).i("startTime:$startTime")
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        initNetwork()

        startTime = System.currentTimeMillis()
        Logger.t(TAG).i("application ok:" + (System.currentTimeMillis() - startTime))


    }
    private fun initNetwork() {
        val receiver = NetBroadcastReceiver()
        val filter: IntentFilter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        } else {
            filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        }
        registerReceiver(receiver, filter)

        NetHelper.INSTANCE.init(this)
    }

    companion object {
        private lateinit var application: AppApplication
        fun get(): AppApplication{
            return application
        }
    }


}
