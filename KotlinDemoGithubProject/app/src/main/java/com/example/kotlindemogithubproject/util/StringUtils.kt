package com.example.kotlindemogithubproject.util

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.kotlindemogithubproject.R

import java.text.SimpleDateFormat
import java.util.*

object StringUtils {

    private val DATE_REGEX_MAP = HashMap<Locale, String>()

    val todayDate: Date
        @NonNull get() = getDateByTime(Date())

    fun isBlank(@Nullable str: String?): Boolean {
        return str == null || str.trim { it <= ' ' } == ""
    }

    fun isBlankList(@Nullable list: List<*>?): Boolean {
        return list == null || list.size == 0
    }

    fun stringToList(@NonNull str: String, @NonNull separator: String): List<String>? {
        var list: List<String>? = null
        if (!str.contains(separator)) {
            return list
        }
        val strs = str.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        list = Arrays.asList(*strs)
        return list
    }

    fun listToString(@NonNull list: List<String>, @NonNull separator: String): String {
        val stringBuilder = StringBuilder("")
        if (list.size == 0 || isBlank(separator)) {
            return stringBuilder.toString()
        }
        for (i in list.indices) {
            stringBuilder.append(list[i])
            if (i != list.size - 1) {
                stringBuilder.append(separator)
            }
        }
        return stringBuilder.toString()
    }

    fun getSizeString(size: Long): String? {
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", size)
        } else if (size < 1024 * 1024) {
            val sizeK = size / 1024f
            return String.format(Locale.getDefault(), "%.2f KB", sizeK)
        } else if (size < 1024 * 1024 * 1024) {
            val sizeM = size / (1024f * 1024f)
            return String.format(Locale.getDefault(), "%.2f MB", sizeM)
        } else if (size / 1024 < 1024 * 1024 * 1024) {
            val sizeG = size / (1024f * 1024f * 1024f)
            return String.format(Locale.getDefault(), "%.2f GB", sizeG)
        }
        return null
    }

    init {
        DATE_REGEX_MAP[Locale.CHINA] = "yyyy-MM-dd"
        DATE_REGEX_MAP[Locale.TAIWAN] = "yyyy-MM-dd"
        DATE_REGEX_MAP[Locale.ENGLISH] = "MMM d, yyyy"
        DATE_REGEX_MAP[Locale.GERMAN] = "dd.MM.yyyy"
        DATE_REGEX_MAP[Locale.GERMANY] = "dd.MM.yyyy"
    }

    fun getDateStr(@NonNull date: Date): String {
        val locale = AppUtils.getLocale(PrefUtils.language!!)
        val regex = if (DATE_REGEX_MAP.containsKey(locale)) DATE_REGEX_MAP[locale] else "yyyy-MM-dd"
        val format = SimpleDateFormat(regex, locale)
        return format.format(date)
    }

    fun getNewsTimeStr(@NonNull context: Context, @NonNull date: Date): String {
        val subTime = System.currentTimeMillis() - date.time
        val MILLIS_LIMIT = 1000.0
        val SECONDS_LIMIT = 60 * MILLIS_LIMIT
        val MINUTES_LIMIT = 60 * SECONDS_LIMIT
        val HOURS_LIMIT = 24 * MINUTES_LIMIT
        val DAYS_LIMIT = 30 * HOURS_LIMIT
        return if (subTime < MILLIS_LIMIT) {
            context.getString(R.string.just_now)
        } else if (subTime < SECONDS_LIMIT) {
            Math.round(subTime / MILLIS_LIMIT).toString() + " " + context.getString(R.string.seconds_ago)
        } else if (subTime < MINUTES_LIMIT) {
            Math.round(subTime / SECONDS_LIMIT).toString() + " " + context.getString(R.string.minutes_ago)
        } else if (subTime < HOURS_LIMIT) {
            Math.round(subTime / MINUTES_LIMIT).toString() + " " + context.getString(R.string.hours_ago)
        } else if (subTime < DAYS_LIMIT) {
            Math.round(subTime / HOURS_LIMIT).toString() + " " + context.getString(R.string.days_ago)
        } else
            getDateStr(date)
    }

    fun upCaseFirstChar(str: String): String? {
        return if (isBlank(str)) null else str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    @NonNull
    fun getDateByTime(@NonNull time: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

}
