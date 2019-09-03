package com.example.javademogithubpractice.repository;

import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.SearchResult;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.network.SearchService;

import io.reactivex.Observable;
import retrofit2.Response;

public class SearchRepositpryImpl {
    private SearchService searchService;

    public SearchRepositpryImpl(SearchService searchService) {
        this.searchService = searchService;
    }


    public Observable<Response<SearchResult<Repository>>> searchRepos(String query,String order,int page){
        return searchService.searchRepos(query,order,page);
    }

    public Observable<Response<SearchResult<User>>> searchUsers(String query,String order,int page){
        return searchService.searchUsers(query,order,page);
    }
}
