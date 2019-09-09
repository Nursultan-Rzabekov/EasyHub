

package com.example.javademogithubpractice.network;


import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.Notification;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationsService {

    @NonNull
    @GET("notifications")
    Observable<Response<ArrayList<Notification>>> getMyNotifications(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("all") boolean all,
            @Query("participating") boolean participating
    );

    @NonNull @PATCH("notifications/threads/{threadId}")
    Observable<Response<ResponseBody>> markNotificationAsRead(
            @Path("threadId") String threadId
    );


}
