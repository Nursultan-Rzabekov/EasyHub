

package com.example.javademogithubpractice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

public enum NetHelper {
	INSTANCE;

	public static final int TYPE_DISCONNECT = 0;
	public static final int TYPE_WIFI = 1;
	public static final int TYPE_MOBILE = 2;


	private int mCurNetStatus;
	private Context mContext;

	public void init(Context context){
		mContext = context;
		checkNet();
	}

	public void checkNet(){
		try {
			ConnectivityManager connectivity = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null&& info.isAvailable()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						if(info.getType() == ConnectivityManager.TYPE_WIFI)
							mCurNetStatus =  TYPE_WIFI;
						if(info.getType() == ConnectivityManager.TYPE_MOBILE)
							mCurNetStatus =  TYPE_MOBILE;
					}
				} else{
					mCurNetStatus = TYPE_DISCONNECT;
				}
			}
		} catch (Exception e) {
			Log.v("error",e.toString());
			e.printStackTrace();
			mCurNetStatus = TYPE_DISCONNECT;
		}
	}

	@NonNull
    public Boolean getNetEnabled(){
		return mCurNetStatus == TYPE_MOBILE || mCurNetStatus == TYPE_WIFI;
	}

	@NonNull
    public Boolean isMobileStatus(){
		return mCurNetStatus == TYPE_MOBILE;
	}

	public int getNetStatus() {
		return mCurNetStatus;
	}
}
