package com.example.javademogithubpractice.repository;


import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.network.RepoService;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;


public class RepoRepositoryImpl {
    private RepoService repoService;

    public RepoRepositoryImpl(RepoService repoService) {
        this.repoService = repoService;
    }

    public Observable<Response<ArrayList<Repository>>> getStarredRepos(
            boolean forceNetWork, String user,
            int page, String sort, String direction){
        return repoService.getStarredRepos(forceNetWork,user,page,sort,direction);
    }

    public Observable<Response<ArrayList<Repository>>> getUserRepos(
            boolean forceNetWork,int page,String type,String sort, String direction){
        return repoService.getUserRepos(forceNetWork,page,type,sort,direction);
    }

    public Observable<Response<ArrayList<Repository>>> getUserPublicRepos(
            boolean forceNetWork, String user,
            int page, String type,
            String sort, String direction){
        return repoService.getUserPublicRepos(forceNetWork,user,page,type,sort,direction);
    }

    public Observable<Response<Repository>> getRepoInfo(
            boolean forceNetWork, String owner,
            String repo
    ){
        return repoService.getRepoInfo(forceNetWork,owner,repo);
    }

}
