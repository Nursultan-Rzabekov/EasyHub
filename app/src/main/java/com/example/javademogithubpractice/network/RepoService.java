

package com.example.javademogithubpractice.network;


import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.User;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RepoService {

    /**
     * List repositories being starred
     */
    @NonNull
    @GET("users/{user}/starred")
    Observable<Response<ArrayList<Repository>>> getStarredRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    @NonNull @GET("user/repos")
    Observable<Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    /**
     * List user repositories
     */
    @NonNull @GET("users/{user}/repos")
    Observable<Response<ArrayList<Repository>>> getUserPublicRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );


    @NonNull @GET("repos/{owner}/{repo}")
    Observable<Response<Repository>> getRepoInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * List public events for a network of repositories
     */

    @NonNull @GET("networks/{owner}/{repo}/events")
    Observable<Response<ArrayList<Event>>> getRepoEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

}
