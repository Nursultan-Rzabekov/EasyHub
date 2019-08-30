

package com.example.javademogithubpractice.network;

import androidx.annotation.NonNull;

import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.SearchResult;
import com.example.javademogithubpractice.mvp.model.User;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;




public interface SearchService {

//    https://api.github.com/search/users?q=Ray&sort=followers&order=desc
    @NonNull
    @GET("search/users")
    Observable<Response<SearchResult<User>>> searchUsers(
            @Query(value = "q", encoded = true) String query,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull @GET("search/repositories")
    Observable<Response<SearchResult<Repository>>> searchRepos(
            @Query(value = "q", encoded = true) String query,
            @Query("order") String order,
            @Query("page") int page
    );
}
