

package com.example.kotlindemogithubproject.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import com.example.kotlindemogithubproject.util.NetHelper;


public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, @NonNull Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            int preNetStatus = NetHelper.INSTANCE.getNetStatus();
            NetHelper.INSTANCE.checkNet();
            int curNetStatus = NetHelper.INSTANCE.getNetStatus();
        }
    }

}
