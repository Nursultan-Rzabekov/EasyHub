package com.example.javademogithubpractice.repository;

import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.network.UserService;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;

public class UserRepositoryImpl {
    private UserService userService;

    public UserRepositoryImpl(UserService userService) {
        this.userService = userService;
    }


    public Observable<Response<User>> getPersonInfo(
            boolean forceNetWork){
        return userService.getPersonInfo(forceNetWork);
    }

    public Observable<Response<User>> getUser(
           boolean forceNetWork, String user){
        return userService.getUser(forceNetWork,user);
    }

    public Observable<Response<ArrayList<User>>> getFollowers(
            boolean forceNetWork,
            String user, int page){
        return userService.getFollowers(forceNetWork,user,page);
    }

    public Observable<Response<ArrayList<User>>> getFollowing(
            boolean forceNetWork, String user,
            int page){
        return userService.getFollowing(forceNetWork,user,page);
    }

    public Observable<Response<ArrayList<Event>>> getPublicEvent(
            boolean forceNetWork, int page){
        return userService.getPublicEvent(forceNetWork,page);
    }

    public Observable<Response<ArrayList<Event>>> getNewsEvent(
            boolean forceNetWork, String user, int page){
        return userService.getNewsEvent(forceNetWork,user,page);
    }

    public Observable<Response<ArrayList<User>>> getOrgMembers(
            boolean forceNetWork, String org, int page){
        return userService.getOrgMembers(forceNetWork,org,page);
    }

    public Observable<Response<ArrayList<User>>> getUserOrgs(
            boolean forceNetWork, String user){
        return userService.getUserOrgs(forceNetWork,user);
    }



}
