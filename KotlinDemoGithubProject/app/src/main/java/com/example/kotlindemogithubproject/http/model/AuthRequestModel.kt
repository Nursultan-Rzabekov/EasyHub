package com.example.kotlindemogithubproject.http.model

import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.BuildConfig
import com.google.gson.annotations.SerializedName

import java.util.Arrays


class AuthRequestModel {
    var scopes: List<String>? = null
        private set
    var note: String? = null
        private set
    var noteUrl: String? = null
        private set
    @SerializedName("client_id")
    var clientId: String? = null
        private set
    @SerializedName("client_secret")
    var clientSecret: String? = null
        private set

    companion object {
        fun generate(): AuthRequestModel {
            val model = AuthRequestModel()
            model.scopes = Arrays.asList("user", "repo", "gist", "notifications")
            model.note = BuildConfig.APPLICATION_ID
            model.clientId = AppConfig.OPENHUB_CLIENT_ID
            model.clientSecret = AppConfig.OPENHUB_CLIENT_SECRET
            model.noteUrl = AppConfig.REDIRECT_URL
            return model
        }
    }
}
