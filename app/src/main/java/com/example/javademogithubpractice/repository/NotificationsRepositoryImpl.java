package com.example.javademogithubpractice.repository;

import com.example.javademogithubpractice.mvp.model.Notification;
import com.example.javademogithubpractice.network.NotificationsService;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;

public class NotificationsRepositoryImpl {
    private NotificationsService notificationsService;

    public NotificationsRepositoryImpl(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }


    public Observable<Response<ArrayList<Notification>>> getMyNotifications(boolean forceNetWork,
            boolean all, boolean participating){
        return notificationsService.getMyNotifications(forceNetWork,all,participating);
    }
}
