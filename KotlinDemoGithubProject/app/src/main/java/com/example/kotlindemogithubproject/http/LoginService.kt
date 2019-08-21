package com.example.kotlindemogithubproject.http


import androidx.annotation.NonNull
import com.example.kotlindemogithubproject.http.model.AuthRequestModel
import com.example.kotlindemogithubproject.mvp.model.BasicToken
import com.example.kotlindemogithubproject.mvp.model.OauthToken
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface LoginService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
        @NonNull @Body authRequestModel: AuthRequestModel
    ): Observable<Response<BasicToken>>

    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("state") state: String
    ): Observable<Response<OauthToken>>

}
