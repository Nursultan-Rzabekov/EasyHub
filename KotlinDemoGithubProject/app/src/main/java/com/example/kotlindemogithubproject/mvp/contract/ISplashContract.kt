package com.example.kotlindemogithubproject.mvp.contract


interface ISplashContract {

    interface View : IBaseContract.View {
        fun showMainPage()
    }

    interface Presenter : IBaseContract.Presenter<View> {

        fun getUser()

        fun saveAccessToken(accessToken: String, scope: String, expireIn: Int)

    }

}
