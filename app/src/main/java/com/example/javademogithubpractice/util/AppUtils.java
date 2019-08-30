

package com.example.javademogithubpractice.util;

import androidx.annotation.NonNull;

import java.util.Locale;


public class AppUtils {
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
