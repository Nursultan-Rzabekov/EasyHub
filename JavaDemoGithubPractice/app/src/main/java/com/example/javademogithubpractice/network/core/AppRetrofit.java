

package com.example.javademogithubpractice.network.core;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.javademogithubpractice.AppApplication;
import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.util.FileUtil;
import com.example.javademogithubpractice.util.NetHelper;
import com.example.javademogithubpractice.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public enum  AppRetrofit {
    INSTANCE;

    private final String TAG = "AppRetrofit";

    private HashMap<String, Retrofit> retrofitMap = new HashMap<>();
    private String token;

    private void createRetrofit(@NonNull String baseUrl, boolean isJson) {
        int timeOut = AppConfig.HTTP_TIME_OUT;
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(AppApplication.get()), AppConfig.HTTP_MAX_CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .addNetworkInterceptor(new NetworkBaseInterceptor())
                .cache(cache)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);

        if(isJson){
            builder.addConverterFactory(GsonConverterFactory.create());
        } else {
            builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict());
        }

        retrofitMap.put(baseUrl + "-" + isJson, builder.build());
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token, boolean isJson) {
        this.token = token;
        String key = baseUrl + "-" + isJson;
        if (!retrofitMap.containsKey(key)) {
            createRetrofit(baseUrl, isJson);
        }
        return retrofitMap.get(key);
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token) {
        return getRetrofit(baseUrl, token, true);
    }

    private class BaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

    //        add unique login id in url to differentiate caches
            if(AppData.INSTANCE.getLoggedUser() != null){
                   /// && !AppConfig.isCommonPageUrl(request.url().toString())){
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("uniqueLoginId",
                                AppData.INSTANCE.getLoggedUser().getLogin())
                        .build();
                request = request.newBuilder()
                        .url(url)
                        .build();
            }

            //add access token

            if(!StringUtils.isBlank(token)){
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .build();
            }
            Log.d(TAG, request.url().toString());

            String forceNetWork = request.header("forceNetWork");
            if (!StringUtils.isBlank(forceNetWork) && !NetHelper.INSTANCE.getNetEnabled()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            } else if("true".equals(forceNetWork)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }

            return chain.proceed(request);
        }
    }

    private class NetworkBaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            Response originalResponse = chain.proceed(request);

//            String serverCacheControl = originalResponse.header("Cache-Control");
            String requestCacheControl = request.cacheControl().toString();
            String forceNetWork = request.header("forceNetWork");
            if(!StringUtils.isBlank(forceNetWork)){
                requestCacheControl = getCacheString();
            }

            if (StringUtils.isBlank(requestCacheControl)) {
                return originalResponse;
            }
            else {
                Response res = originalResponse.newBuilder()
                        .header("Cache-Control", requestCacheControl)
//                        .header("Date", getGMTTime())
                        .removeHeader("Pragma")
                        .build();
                return res;
            }

        }
    }

    private static String getGMTTime(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String gmtTime = format.format(date);
        return gmtTime;
    }

    public static String getCacheString(){
        return "public, max-age=" + AppConfig.CACHE_MAX_AGE;
    }

}
