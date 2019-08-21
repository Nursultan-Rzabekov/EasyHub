package com.example.kotlindemogithubproject.mvp.presenter

import androidx.annotation.NonNull
import com.example.kotlindemogithubproject.AppData
import com.example.kotlindemogithubproject.dao.AuthUser
import com.example.kotlindemogithubproject.dao.AuthUserDao
import com.example.kotlindemogithubproject.dao.DaoSession
import com.example.kotlindemogithubproject.http.core.HttpObserver
import com.example.kotlindemogithubproject.http.core.HttpResponse
import com.example.kotlindemogithubproject.mvp.contract.ISplashContract
import com.example.kotlindemogithubproject.mvp.model.User

import javax.inject.Inject
import java.util.Date


class SplashPresenter @Inject constructor(daoSession: DaoSession) : BasePresenter<ISplashContract.View>(daoSession), ISplashContract.Presenter {


    private val TAG = "SplashPresenter"

    private var authUser: AuthUser? = null
    private var isMainPageShowwed = false

    override fun getUser() {
        val authUserDao = daoSession.authUserDao

        val users = authUserDao.queryBuilder()
            .where(AuthUserDao.Properties.Selected.eq(true))
            .limit(1)
            .list()

        var selectedUser: AuthUser? = if (users != null && users!!.size > 0) users!!.get(0) else null

        //if none selected, choose first account
        if (selectedUser == null) {
            val firstAccount = authUserDao.queryBuilder()
                .limit(1)
                .list()
            selectedUser = if (firstAccount != null && firstAccount!!.size > 0) firstAccount!!.get(0) else null
        }

        if (selectedUser != null && isExpired(selectedUser!!)) {
            authUserDao.delete(selectedUser)
            selectedUser = null
        }

        if (selectedUser != null) {
            AppData.INSTANCE.authUser = selectedUser
            getUserInfo(selectedUser.accessToken!!)
        } else {
            mView!!.showLoginPage()
        }

    }

    override fun saveAccessToken(accessToken: String, scope: String, expireIn: Int) {
        val authUser = AuthUser()
        authUser.selected = true
        authUser.scope = scope
        authUser.expireIn = expireIn
        authUser.authTime = Date()
        authUser.accessToken = accessToken
        daoSession.authUserDao.insert(authUser)
        this.authUser = authUser
    }

    private fun getUserInfo(accessToken: String) {
        val httpObserver = object : HttpObserver<User> {
            override fun onError(@NonNull error: Throwable) {
                daoSession.authUserDao.delete(AppData.INSTANCE.authUser)
                AppData.INSTANCE.authUser = null
                getErrorTip(error)?.let { mView!!.showErrorToast(it) }
                mView!!.showLoginPage()
            }

            override fun onSuccess(@NonNull response: HttpResponse<User>) {
                AppData.INSTANCE.loggedUser = response.body()
                if (authUser != null) {
                    authUser!!.loginId =response.body()?.login
                    daoSession.authUserDao.update(authUser)
                }
                if (!isMainPageShowwed) {
                    isMainPageShowwed = true
                    mView!!.showMainPage()
                }
            }
        }

//        generalRxHttpExecute(
//            IObservableCreator<Any> { forceNetWork -> userService.getPersonInfo(forceNetWork) },
//            httpObserver,
//            true
//        )

    }

    private fun isExpired(selectedUser: AuthUser): Boolean {
        return selectedUser.authTime!!.getTime() + selectedUser.expireIn * 1000 < System.currentTimeMillis()
    }

}
