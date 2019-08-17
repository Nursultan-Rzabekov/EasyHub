

package com.example.javademogithubpractice.http.core;


public interface HttpObserver<T> {
    void onError(Throwable error);
    void onSuccess(HttpResponse<T> response);
}
