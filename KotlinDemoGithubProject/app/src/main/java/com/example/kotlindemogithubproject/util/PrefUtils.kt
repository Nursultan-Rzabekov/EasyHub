package com.example.kotlindemogithubproject.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.NonNull
import com.example.kotlindemogithubproject.AppApplication

object PrefUtils {

    val LIGHT_INDIGO = "Light indigo"
    val DARK = "Dark"
    val LIGHT_TEAL = "Light teal"
    val AMOLED_DARK = "AMOLED dark"

    val LIGHT_BLUE = 0
    val BLUE = 1
    val INDIGO = 2
    val ORANGE = 3

    val YELLOW = 4
    val AMBER = 5
    val GREY = 6
    val BROWN = 7

    val CYAN = 8
    val TEAL = 9
    val LIME = 10
    val GREEN = 11

    val PINK = 12
    val RED = 13
    val PURPLE = 14
    val DEEP_PURPLE = 15

    val FIRST_USE = "firstUse"

    val THEME = "appTheme"
    val ACCENT_COLOR = "accentColor"
    val START_PAGE = "startPage"
    val CACHE_FIRST_ENABLE = "cacheFirstEnable"
    val SYSTEM_DOWNLOADER = "systemDownloader"
    val LANGUAGE = "language"
    val LOGOUT = "logout"
    val CODE_WRAP = "codeWrap"
    val CUSTOM_TABS_ENABLE = "customTabsEnable"


    val POP_TIMES = "popTimes"
    val POP_VERSION_TIME = "popVersionTime"
    val LAST_POP_TIME = "lastPopTime"

    val NEW_YEAR_WISHES_TIP_ENABLE = "newYearWishesTipEnable"
    val STAR_WISHES_TIP_TIMES = "starWishesTipFlag"
    val LAST_STAR_WISHES_TIP_TIME = "lastStarWishesTipTime"

    val DOUBLE_CLICK_TITLE_TIP_ABLE = "doubleClickTitleTipAble"
    val ACTIVITY_LONG_CLICK_TIP_ABLE = "activityLongClickTipAble"
    val RELEASES_LONG_CLICK_TIP_ABLE = "releasesLongClickTipAble"
    val LANGUAGES_EDITOR_TIP_ABLE = "languagesEditorTipAble"
    val COLLECTIONS_TIP_ABLE = "collectionsTipAble"
    val BOOKMARKS_TIP_ABLE = "bookmarksTipAble"
    val CUSTOM_TABS_TIPS_ENABLE = "customTabsTipsEnable"
    val TOPICS_TIP_ABLE = "topicsTipAble"
    val DISABLE_LOADING_IMAGE = "disableLoadingImage"


    val SEARCH_RECORDS = "searchRecords"

    val defaultSp: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(AppApplication.get())

    val theme: String?
        get() = getDefaultSp(AppApplication.get()).getString(THEME, LIGHT_TEAL)

    val language: String?
        get() = getDefaultSp(AppApplication.get()).getString(LANGUAGE, "en")

    val startPage: String?
        get() = getDefaultSp(AppApplication.get()).getString(START_PAGE, "news")

    val accentColor: Int
        get() = getDefaultSp(AppApplication.get()).getInt(ACCENT_COLOR, 11)

    val isCacheFirstEnable: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(CACHE_FIRST_ENABLE, true)

    val isCodeWrap: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(CODE_WRAP, false)

    val isDoubleClickTitleTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(DOUBLE_CLICK_TITLE_TIP_ABLE, true)

    val isActivityLongClickTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(ACTIVITY_LONG_CLICK_TIP_ABLE, true)

    val isReleasesLongClickTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(RELEASES_LONG_CLICK_TIP_ABLE, true)

    val isLanguagesEditorTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(LANGUAGES_EDITOR_TIP_ABLE, true)

    val popTimes: Int
        get() = getDefaultSp(AppApplication.get()).getInt(POP_TIMES, 0)

    val popVersionTime: Long
        get() = getDefaultSp(AppApplication.get()).getLong(POP_VERSION_TIME, 1)

    val lastPopTime: Long
        get() = getDefaultSp(AppApplication.get()).getLong(LAST_POP_TIME, 0)

    val starWishesTipTimes: Int
        get() = getDefaultSp(AppApplication.get()).getInt(STAR_WISHES_TIP_TIMES, 0)

    val lastStarWishesTipTime: Long
        get() = getDefaultSp(AppApplication.get()).getLong(LAST_STAR_WISHES_TIP_TIME, 0)

    val isSystemDownloader: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(SYSTEM_DOWNLOADER, true)

    val isCustomTabsEnable: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(CUSTOM_TABS_ENABLE, true)

    val searchRecords: String?
        get() = getDefaultSp(AppApplication.get()).getString(SEARCH_RECORDS, null)

    val isFirstUse: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(FIRST_USE, true)

    val isCollectionsTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(COLLECTIONS_TIP_ABLE, true)

    val isBookmarksTipAble: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(BOOKMARKS_TIP_ABLE, true)

    val isCustomTabsTipsEnable: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(CUSTOM_TABS_TIPS_ENABLE, true)

    val isTopicsTipEnable: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(TOPICS_TIP_ABLE, true)

    val isDisableLoadingImage: Boolean
        get() = getDefaultSp(AppApplication.get()).getBoolean(DISABLE_LOADING_IMAGE, false)

    val isLoadImageEnable: Boolean
        get() = NetHelper.INSTANCE.netStatus == NetHelper.TYPE_WIFI || !PrefUtils.isDisableLoadingImage

    operator fun set(@NonNull key: String, @NonNull value: Any?) {
        if (StringUtils.isBlank(key) || value == null) {
            throw NullPointerException(String.format("Key and value not be null key=%s, value=%s", key, value))
        }
        val edit = defaultSp.edit()
        if (value is String) {
            edit.putString(key, value as String?)
        } else if (value is Int) {
            edit.putInt(key, (value as Int?)!!)
        } else if (value is Long) {
            edit.putLong(key, (value as Long?)!!)
        } else if (value is Boolean) {
            edit.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Float) {
            edit.putFloat(key, (value as Float?)!!)
        } else if (value is Set<*>) {
            edit.putStringSet(key, value as Set<String>?)
        } else {
            throw IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value))
        }
        edit.apply()
    }

    fun clearKey(@NonNull key: String) {
        getDefaultSp(AppApplication.get()).edit().remove(key).apply()
    }

    fun isnewYearWishesTipEnable(): Boolean {
        return getDefaultSp(AppApplication.get()).getBoolean(NEW_YEAR_WISHES_TIP_ENABLE, true)
    }

    fun getDefaultSp(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getSP(context: Context, spName: String): SharedPreferences {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

}
