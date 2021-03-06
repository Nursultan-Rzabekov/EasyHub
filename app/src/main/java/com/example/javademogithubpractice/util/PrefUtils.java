

package com.example.javademogithubpractice.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.example.javademogithubpractice.AppApplication;

import java.util.Set;

public class PrefUtils {

    public final static String LIGHT_TEAL = "Light teal";


    public final static String FIRST_USE = "firstUse";

    public final static String THEME = "appTheme";
    public final static String LANGUAGE = "language";


    public final static String DOUBLE_CLICK_TITLE_TIP_ABLE = "doubleClickTitleTipAble";
    public final static String DISABLE_LOADING_IMAGE = "disableLoadingImage";


    public final static String SEARCH_RECORDS = "searchRecords";

    public static SharedPreferences getDefaultSp(){
        return PreferenceManager.getDefaultSharedPreferences(AppApplication.get());
    }

    public static void set(@NonNull String key, @NonNull Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            throw new NullPointerException(String.format("Key and value not be null key=%s, value=%s", key, value));
        }
        SharedPreferences.Editor edit = getDefaultSp().edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if(value instanceof Set){
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        edit.apply();
    }



    public static String getTheme(){
        return getDefaultSp(AppApplication.get()).getString(THEME, LIGHT_TEAL);
    }

    public static String getLanguage(){
        return getDefaultSp(AppApplication.get()).getString(LANGUAGE, "en");
    }

    public static boolean isDoubleClickTitleTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(DOUBLE_CLICK_TITLE_TIP_ABLE, true);
    }


    public static String getSearchRecords(){
        return getDefaultSp(AppApplication.get()).getString(SEARCH_RECORDS, null);
    }

    public static boolean isFirstUse(){
        return getDefaultSp(AppApplication.get()).getBoolean(FIRST_USE, true);
    }


    public static boolean isDisableLoadingImage(){
        return getDefaultSp(AppApplication.get()).getBoolean(DISABLE_LOADING_IMAGE, false);
    }

    public static boolean isLoadImageEnable(){
        return NetHelper.INSTANCE.getNetStatus() == NetHelper.TYPE_WIFI || !PrefUtils.isDisableLoadingImage();
    }

    public static SharedPreferences getDefaultSp(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
