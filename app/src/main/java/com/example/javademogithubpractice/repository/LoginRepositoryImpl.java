package com.example.javademogithubpractice.repository;



import com.example.javademogithubpractice.mvp.model.BasicToken;
import com.example.javademogithubpractice.mvp.model.OauthToken;
import com.example.javademogithubpractice.network.LoginService;
import com.example.javademogithubpractice.network.model.AuthRequestModel;

import io.reactivex.Observable;
import retrofit2.Response;


public class LoginRepositoryImpl {
    private LoginService loginService;

    public LoginRepositoryImpl(LoginService loginService) {
        this.loginService = loginService;
    }

    public Observable<Response<BasicToken>> authorizations(
            AuthRequestModel authRequestModel){
        return loginService.authorizations(authRequestModel);
    }

    public Observable<Response<OauthToken>> getAccessToken(
            String clientId, String clientSecret, String code){
        return loginService.getAccessToken(clientId,clientSecret,code);
    }
}
