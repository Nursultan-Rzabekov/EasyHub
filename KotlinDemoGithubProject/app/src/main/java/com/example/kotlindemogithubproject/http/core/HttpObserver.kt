package com.example.kotlindemogithubproject.http.core


interface HttpObserver<T : Any> {
    fun onError(error: Throwable)
    fun onSuccess(response: HttpResponse<T>)
}
