package com.example.kotlindemogithubproject.util

import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.kotlindemogithubproject.AppApplication
import com.example.kotlindemogithubproject.R

import java.util.Locale


object AppUtils {

    @NonNull
    fun getLocale(language: String): Locale {
        val locale: Locale
        val array = language.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (array.size > 1) {
            //zh-rCN, zh-rTW", pt-rPT, etc... remove the 'r'
            val country = array[1].replaceFirst("r".toRegex(), "")
            locale = Locale(array[0], country)
        } else {
            locale = Locale(language)
        }
        return locale
    }

    fun copyToClipboard(@NonNull context: Context, @NonNull uri: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.app_github_name), uri)
        clipboard.primaryClip = clip
    }



}
