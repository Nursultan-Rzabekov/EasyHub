

package com.example.javademogithubpractice.util;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.Locale;


public class AppUtils {
    public static void updateAppLanguage(@NonNull Context context) {
        String lang = PrefUtils.getLanguage();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) updateResources(context, lang);
        updateResourcesLegacy(context, lang);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static void updateResourcesLegacy(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @NonNull
    public static Locale getLocale(String language) {
        Locale locale;
        String[] array = language.split("-");
        if (array.length > 1) {
            //zh-rCN, zh-rTW", pt-rPT, etc... remove the 'r'
            String country =  array[1].replaceFirst("r", "");
            locale = new Locale(array[0], country);
        } else {
            locale = new Locale(language);
        }
        return locale;
    }

}
