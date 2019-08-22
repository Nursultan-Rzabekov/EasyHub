

package com.example.javademogithubpractice.util;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils {

    public static boolean isBlank(@Nullable String str) {
        return str == null || str.trim().equals("");
    }

    public static String listToString(@NonNull List<String> list, @NonNull String separator){
        StringBuilder stringBuilder = new StringBuilder("");
        if(list.size() == 0 || isBlank(separator)){
            return stringBuilder.toString();
        }
        for(int i = 0; i < list.size(); i++){
            stringBuilder.append(list.get(i));
            if(i != list.size() - 1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    private final static Map<Locale, String> DATE_REGEX_MAP = new HashMap<>();
    static {
        DATE_REGEX_MAP.put(Locale.CHINA, "yyyy-MM-dd");
        DATE_REGEX_MAP.put(Locale.TAIWAN, "yyyy-MM-dd");
        DATE_REGEX_MAP.put(Locale.ENGLISH, "MMM d, yyyy");
        DATE_REGEX_MAP.put(Locale.GERMAN, "dd.MM.yyyy");
        DATE_REGEX_MAP.put(Locale.GERMANY, "dd.MM.yyyy");
    }


    @NonNull public static Date getDateByTime(@NonNull Date time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @NonNull public static Date getTodayDate(){
        return getDateByTime(new Date());
    }


    public static String getDateStr(@NonNull Date date){
        Locale locale = AppUtils.getLocale(PrefUtils.getLanguage());
        String regex = DATE_REGEX_MAP.containsKey(locale) ? DATE_REGEX_MAP.get(locale) : "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(regex, locale);
        return format.format(date);
    }

}
