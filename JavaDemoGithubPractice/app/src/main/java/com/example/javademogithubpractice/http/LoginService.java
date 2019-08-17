

package com.example.javademogithubpractice.http;

import android.support.annotation.NonNull;


import com.example.javademogithubpractice.http.model.AuthRequestModel;
import com.example.javademogithubpractice.mvp.model.BasicToken;
import com.example.javademogithubpractice.mvp.model.OauthToken;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface LoginService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    Observable<Response<BasicToken>> authorizations(
            @NonNull @Body AuthRequestModel authRequestModel
    );

    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    Observable<Response<OauthToken>> getAccessToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String code,
            @Query("state") String state
    );

}
