

package com.example.javademogithubpractice.network.model;

import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.BuildConfig;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;



public class AuthRequestModel {

    private List<String> scopes;
    private String note;
    private String noteUrl;
    @SerializedName("client_id") private String clientId;
    @SerializedName("client_secret") private String clientSecret;

    public static AuthRequestModel generate(){
        AuthRequestModel model = new AuthRequestModel();
        model.scopes = Arrays.asList("user", "repo", "gist", "notifications");
        model.note = BuildConfig.APPLICATION_ID;
        model.clientId = AppConfig.DEMOGITHUB_CLIENT_ID;
        model.clientSecret = AppConfig.DEMOGITHUB_CLIENT_SECRET;
        model.noteUrl = AppConfig.REDIRECT_URL;
        return model;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getNote() {
        return note;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
