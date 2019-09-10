

package com.example.javademogithubpractice.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.R;

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

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_github_name), uri);
        clipboard.setPrimaryClip(clip);
        Toast.makeText((AppApplication.get()), context.getString(R.string.success_copied),Toast.LENGTH_SHORT).show();
    }
}
