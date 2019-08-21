package com.example.kotlindemogithubproject.http.core

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.kotlindemogithubproject.AppApplication
import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.AppData
import com.example.kotlindemogithubproject.util.FileUtil
import com.example.kotlindemogithubproject.util.NetHelper
import com.example.kotlindemogithubproject.util.StringUtils
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

enum class AppRetrofit {
    INSTANCE;

    private val TAG = "AppRetrofit"

    private val retrofitMap = HashMap<String, Retrofit>()
    private var token: String? = null

    private fun createRetrofit(@NonNull baseUrl: String, isJson: Boolean) {
        val timeOut = AppConfig.HTTP_TIME_OUT
        val cache = Cache(
            FileUtil.getHttpImageCacheDir(AppApplication.get()),
            AppConfig.HTTP_MAX_CACHE_SIZE.toLong()
        )

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(BaseInterceptor())
            .addNetworkInterceptor(NetworkBaseInterceptor())
            .cache(cache)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)

        if (isJson) {
            builder.addConverterFactory(GsonConverterFactory.create())
        } else {
            builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
        }

        retrofitMap["$baseUrl-$isJson"] = builder.build()
    }

    @JvmOverloads
    fun getRetrofit(@NonNull baseUrl: String, @Nullable token: String?, isJson: Boolean = true): Retrofit? {
        this.token = token
        val key = "$baseUrl-$isJson"
        if (!retrofitMap.containsKey(key)) {
            createRetrofit(baseUrl, isJson)
        }
        return retrofitMap[key]
    }

    private inner class BaseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(@NonNull chain: Interceptor.Chain): Response {
            var request = chain.request()

            //add unique login id in url to differentiate caches
            if (AppData.INSTANCE.loggedUser != null) {
                //                    && !AppConfig.isCommonPageUrl(request.url().toString())){
                val url = request.url().newBuilder()
                    .addQueryParameter(
                        "uniqueLoginId",
                        AppData.INSTANCE.loggedUser!!.login
                    )
                    .build()
                request = request.newBuilder()
                    .url(url)
                    .build()
            }

            //add access token
            if (!StringUtils.isBlank(token)) {
                val auth = if (token!!.startsWith("Basic")) token else "token " + token!!
                request = request.newBuilder()
                    .addHeader("Authorization", auth)
                    .build()
            }
            Log.d(TAG, request.url().toString())

            val forceNetWork = request.header("forceNetWork")
            if (!StringUtils.isBlank(forceNetWork) && !NetHelper.INSTANCE.netEnabled) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            } else if ("true" == forceNetWork) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
            }

            return chain.proceed(request)
        }
    }

    private inner class NetworkBaseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(@NonNull chain: Interceptor.Chain): Response {

            val request = chain.request()
            val originalResponse = chain.proceed(request)

            //            String serverCacheControl = originalResponse.header("Cache-Control");
            var requestCacheControl = request.cacheControl().toString()
            val forceNetWork = request.header("forceNetWork")
            if (!StringUtils.isBlank(forceNetWork)) {
                requestCacheControl = cacheString
            }

            return if (StringUtils.isBlank(requestCacheControl)) {
                originalResponse
            } else {
                originalResponse.newBuilder()
                    .header("Cache-Control", requestCacheControl)
                    //                        .header("Date", getGMTTime())
                    .removeHeader("Pragma")
                    .build()
            }

        }
    }

    companion object {
        private val gmtTime: String
            get() {
                val date = Date()
                val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                format.timeZone = TimeZone.getTimeZone("GMT+8")
                return format.format(date)
            }

        val cacheString: String
            get() = "public, max-age=" + AppConfig.CACHE_MAX_AGE
    }

}
