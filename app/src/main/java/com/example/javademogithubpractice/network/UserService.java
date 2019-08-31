

package com.example.javademogithubpractice.network;




import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.mvp.model.User;
import java.util.ArrayList;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserService {
    @NonNull
    @GET("user")
    Observable<Response<User>> getPersonInfo(
            @Header("forceNetWork") boolean forceNetWork
    );

    @NonNull @GET("users/{user}")
    Observable<Response<User>> getUser(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );


    @NonNull @GET("user/following/{user}")
    Observable<Response<ResponseBody>> checkFollowing(
            @Path("user") String user
    );

    /**
     * Check if one user follows another
     */

    @NonNull @GET("users/{user}/followers")
    Observable<Response<ArrayList<User>>> getFollowers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/following")
    Observable<Response<ArrayList<User>>> getFollowing(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    /**
     * List github public events
     */

    @NonNull @GET("events")
    Observable<Response<ArrayList<Event>>> getPublicEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/received_events")
    Observable<Response<ArrayList<Event>>> getNewsEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull @GET("orgs/{org}/members")
    Observable<Response<ArrayList<User>>> getOrgMembers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("org") String org,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/orgs")
    Observable<Response<ArrayList<User>>> getUserOrgs(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );
}
