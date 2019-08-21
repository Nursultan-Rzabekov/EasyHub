package com.example.kotlindemogithubproject.mvp.contract

import android.content.Intent
import com.example.kotlindemogithubproject.mvp.model.BasicToken


interface ILoginContract {

    interface View : IBaseContract.View {

        fun onGetTokenSuccess(basicToken: BasicToken)

        fun onGetTokenError(errorMsg: String)

        fun onLoginComplete()

    }

    interface Presenter : IBaseContract.Presenter<View> {

        val oAuth2Url: String

        fun getToken(code: String, state: String)

        fun basicLogin(userName: String, password: String)

        fun handleOauth(intent: Intent)

        fun getUserInfo(basicToken: BasicToken)

    }

}
