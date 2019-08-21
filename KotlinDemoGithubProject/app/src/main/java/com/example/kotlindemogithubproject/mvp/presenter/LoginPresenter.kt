package com.example.kotlindemogithubproject.mvp.presenter

import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.AppData
import com.example.kotlindemogithubproject.dao.AuthUser
import com.example.kotlindemogithubproject.dao.AuthUserDao
import com.example.kotlindemogithubproject.dao.DaoSession
import com.example.kotlindemogithubproject.http.model.AuthRequestModel
import com.example.kotlindemogithubproject.mvp.contract.ILoginContract
import com.example.kotlindemogithubproject.mvp.model.BasicToken
import com.example.kotlindemogithubproject.mvp.model.User
import com.example.kotlindemogithubproject.util.StringUtils
import okhttp3.Credentials

import javax.inject.Inject
import java.util.Date
import java.util.UUID

class LoginPresenter @Inject constructor(daoSession: DaoSession) :
    BasePresenter<ILoginContract.View>(daoSession), ILoginContract.Presenter {


    override val oAuth2Url: String
        @NonNull
        get() {
            val randomState = UUID.randomUUID().toString()
            return AppConfig.OAUTH2_URL +
                    "?client_id=" + AppConfig.OPENHUB_CLIENT_ID +
                    "&scope=" + AppConfig.OAUTH2_SCOPE +
                    "&state=" + randomState
        }

    override fun getToken(code: String, state: String) {
        val observable = loginService.getAccessToken(
            AppConfig.OPENHUB_CLIENT_ID,
            AppConfig.OPENHUB_CLIENT_SECRET, code, state
        )

//        val subscriber = HttpSubscriber(
//            object : HttpObserver<OauthToken>() {
//                override fun onError(@NonNull error: Throwable) {
//                    mView!!.dismissProgressDialog()
//                    mView!!.showErrorToast(getErrorTip(error))
//                }
//
//                override fun onSuccess(@NonNull response: HttpResponse<OauthToken>) {
//                    val token = response.body()
//                    if (token != null) {
//                        mView!!.onGetTokenSuccess(BasicToken.generateFromOauthToken(token))
//                    } else {
//                        mView!!.onGetTokenError(response.oriResponse.message())
//                    }
//                }
//            }
//        )
//        generalRxHttpExecute<Any>(observable, subscriber)
        mView!!.showProgressDialog(loadTip)
    }

    override fun basicLogin(userName: String, password: String) {
        val authRequestModel = AuthRequestModel.generate()
        val token = Credentials.basic(userName, password)
        val observable = getLoginService(token).authorizations(authRequestModel)
//        val subscriber = HttpSubscriber(
//            object : HttpObserver<BasicToken>() {
//                fun onError(@NonNull error: Throwable) {
//                    mView!!.onGetTokenError(getErrorTip(error))
//                }
//
//                fun onSuccess(@NonNull response: HttpResponse<BasicToken>) {
//                    val token = response.body()
//                    if (token != null) {
//                        mView!!.onGetTokenSuccess(token)
//                    } else {
//                        mView!!.onGetTokenError(response.oriResponse.message())
//                    }
//
//                }
//            }
//        )
//        generalRxHttpExecute<Any>(observable, subscriber)
    }

    override fun handleOauth(intent: Intent) {
        val uri = intent.data
        if (uri != null) {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            getToken(code, state)
        }
    }

    override fun getUserInfo(basicToken: BasicToken) {
//        val subscriber = HttpSubscriber(
//            object : HttpObserver<User>() {
//                fun onError(error: Throwable) {
//                    mView!!.dismissProgressDialog()
//                    mView!!.showErrorToast(getErrorTip(error))
//                }
//
//                fun onSuccess(response: HttpResponse<User>) {
//                    //                        mView.dismissProgressDialog();
//                    saveAuthUser(basicToken, response.body())
//                    mView!!.onLoginComplete()
//                }
//            }
//        )
//        val observable = getUserService(basicToken.token!!).getPersonInfo(true)
//        generalRxHttpExecute<Any>(observable, subscriber)
        mView!!.showProgressDialog(loadTip)

    }

    private fun saveAuthUser(basicToken: BasicToken, userInfo: User) {
        val updateSql = ("UPDATE " + daoSession.authUserDao.getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0")
        daoSession.authUserDao.getDatabase().execSQL(updateSql)

        val deleteExistsSql = ("DELETE FROM " + daoSession.authUserDao.getTablename()
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " = '" + userInfo.login + "'")
        daoSession.authUserDao.getDatabase().execSQL(deleteExistsSql)

        val authUser = AuthUser()
        val scope = basicToken.scopes?.let { StringUtils.listToString(it, ",") }
        val date = Date()
        authUser.accessToken = basicToken.token
        authUser.scope = scope
        authUser.authTime = date
        authUser.expireIn = 360 * 24 * 60 * 60
        authUser.selected = true
        authUser.loginId = userInfo.login
        authUser.name = userInfo.name
        authUser.avatar = userInfo.avatarUrl
        daoSession.authUserDao.insert(authUser)

        AppData.INSTANCE.authUser = authUser
        AppData.INSTANCE.loggedUser = userInfo
    }
}
