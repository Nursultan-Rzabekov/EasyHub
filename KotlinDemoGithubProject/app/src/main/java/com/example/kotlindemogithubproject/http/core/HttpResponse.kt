package com.example.kotlindemogithubproject.http.core

import androidx.annotation.Nullable
import retrofit2.Response


class HttpResponse<T : Any>(val oriResponse: Response<T>) {

    val isSuccessful: Boolean
        get() = oriResponse.isSuccessful

    val isFromCache: Boolean
        get() = isResponseEnable(oriResponse.raw().cacheResponse())

    val isFromNetWork: Boolean
        get() = isResponseEnable(oriResponse.raw().networkResponse())

    private fun isResponseEnable(@Nullable response: okhttp3.Response?): Boolean {
        return response != null && response.code() == 200
    }

    fun body(): T? {
        return oriResponse.body()
    }
}
