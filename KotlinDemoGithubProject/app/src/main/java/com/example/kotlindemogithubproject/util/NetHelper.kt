package com.example.kotlindemogithubproject.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.annotation.NonNull

enum class NetHelper {
    INSTANCE;


    var netStatus: Int = 0
        private set
    private var mContext: Context? = null

    val netEnabled: Boolean
        @NonNull
        get() = netStatus == TYPE_MOBILE || netStatus == TYPE_WIFI

    val isMobileStatus: Boolean
        @NonNull
        get() = netStatus == TYPE_MOBILE

    fun init(context: Context) {
        mContext = context
        checkNet()
    }

    fun checkNet() {
        try {
            val connectivity = mContext!!
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.activeNetworkInfo
                if (info != null && info.isAvailable) {
                    if (info.state == NetworkInfo.State.CONNECTED) {
                        if (info.type == ConnectivityManager.TYPE_WIFI)
                            netStatus = TYPE_WIFI
                        if (info.type == ConnectivityManager.TYPE_MOBILE)
                            netStatus = TYPE_MOBILE
                    }
                } else {
                    netStatus = TYPE_DISCONNECT
                }
            }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            e.printStackTrace()
            netStatus = TYPE_DISCONNECT
        }

    }

    companion object {

        val TYPE_DISCONNECT = 0
        val TYPE_WIFI = 1
        val TYPE_MOBILE = 2
    }
}
