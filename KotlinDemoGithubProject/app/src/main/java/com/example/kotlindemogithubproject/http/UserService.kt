package com.example.kotlindemogithubproject.http


import androidx.annotation.NonNull
import com.example.kotlindemogithubproject.common.Event
import com.example.kotlindemogithubproject.mvp.model.User
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


import java.util.ArrayList

interface UserService {
    @NonNull
    @GET("user")
    fun getPersonInfo(
        @Header("forceNetWork") forceNetWork: Boolean
    ): Observable<Response<User>>

    @NonNull
    @GET("users/{user}")
    fun getUser(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String
    ): Observable<Response<User>>


    @NonNull
    @GET("user/following/{user}")
    fun checkFollowing(
        @Path("user") user: String
    ): Observable<Response<ResponseBody>>

    /**
     * Check if one user follows another
     */
    @NonNull
    @GET("users/{user}/following/{targetUser}")
    fun checkFollowing(
        @Path("user") user: String,
        @Path("targetUser") targetUser: String
    ): Observable<Response<ResponseBody>>

    @NonNull
    @PUT("user/following/{user}")
    fun followUser(
        @Path("user") user: String
    ): Observable<Response<ResponseBody>>

    @NonNull
    @DELETE("user/following/{user}")
    fun unfollowUser(
        @Path("user") user: String
    ): Observable<Response<ResponseBody>>

    @NonNull
    @GET("users/{user}/followers")
    fun getFollowers(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<User>>>

    @NonNull
    @GET("users/{user}/following")
    fun getFollowing(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<User>>>

    /**
     * List events performed by a user
     */
    @NonNull
    @GET("users/{user}/events")
    fun getUserEvents(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<Event>>>

    /**
     * List github public events
     */
    @NonNull
    @GET("events")
    fun getPublicEvent(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<Event>>>

    @NonNull
    @GET("users/{user}/received_events")
    fun getNewsEvent(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<Event>>>

    @NonNull
    @GET("orgs/{org}/members")
    fun getOrgMembers(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("org") org: String,
        @Query("page") page: Int
    ): Observable<Response<ArrayList<User>>>

    @NonNull
    @GET("users/{user}/orgs")
    fun getUserOrgs(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("user") user: String
    ): Observable<Response<ArrayList<User>>>
}
