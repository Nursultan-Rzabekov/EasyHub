package com.example.kotlindemogithubproject.mvp.presenter

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.AppData
import com.example.kotlindemogithubproject.R
import com.example.kotlindemogithubproject.dao.DaoSession
import com.example.kotlindemogithubproject.http.LoginService
import com.example.kotlindemogithubproject.http.UserService
import com.example.kotlindemogithubproject.http.core.*
import com.example.kotlindemogithubproject.http.error.HttpError
import com.example.kotlindemogithubproject.http.error.UnauthorizedError
import com.example.kotlindemogithubproject.mvp.contract.IBaseContract
import com.example.kotlindemogithubproject.util.StringUtils
import com.thirtydegreesray.dataautoaccess.DataAutoAccess
import org.apache.http.conn.ConnectTimeoutException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.HashMap


abstract class BasePresenter<V : IBaseContract.View>(protected var daoSession: DaoSession) : IBaseContract.Presenter<V>  {

    private val TAG = "BasePresenter"

    protected var mView: V? = null

    private var isEventSubscriber = false
    protected var isViewInitialized = false

    private var isAttached = false


    protected val loginService: LoginService
        get() = AppRetrofit.INSTANCE
            .getRetrofit(AppConfig.GITHUB_BASE_URL, null)?.create(LoginService::class.java)!!

    protected val userService: UserService
        get() = getUserService(AppData.INSTANCE.accessToken!!)

    override val context: Context
        @NonNull
        get() = (if (mView is Context) {
            mView as Context?
        } else if (mView is Fragment) {
            (mView as Fragment).getContext()
        } else {
            throw NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.")
        })!!

    private val requestTimesMap = HashMap<String, Int>()


    protected val loadTip: String
        @NonNull
        get() = context?.getString(R.string.loading) + "..."

//    init {
//        subscribers = ArrayList<Subscriber<*>>()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        DataAutoAccess.saveData(this, outState)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        DataAutoAccess.getData(this, outState)
    }

    override fun attachView(@NonNull view: V) {
        mView = view
        //if (isEventSubscriber) AppEventBus.INSTANCE.eventBus?.register(this)
        onViewAttached()
        isAttached = true
    }

    override fun detachView() {
        mView = null
//        for (subscriber in subscribers) {
//            if (subscriber != null && !subscriber!!.isUnsubscribed()) {
//                subscriber!!.unsubscribe()
//                //Logger.d(TAG, "unsubscribe:" + subscriber.toString());
//            }
//        }
//        if (isEventSubscriber) AppEventBus.INSTANCE.eventBus.unregister(this)
    }

    override fun onViewInitialized() {
        isViewInitialized = true
    }

    protected fun getLoginService(token: String): LoginService {
        return AppRetrofit.INSTANCE
            .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
            ?.create(LoginService::class.java)!!
    }

    private fun getUserService(token: String): UserService {
        return AppRetrofit.INSTANCE
            .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
            ?.create(UserService::class.java)!!
    }

    private fun <T> getServices(serviceClass: Class<T>): T {
        return getServices(serviceClass, AppConfig.GITHUB_API_BASE_URL, true)
    }

    private fun <T> getServices(serviceClass: Class<T>, baseUrl: String, isJson: Boolean): T {
        return AppRetrofit.INSTANCE
            .getRetrofit(baseUrl, AppData.INSTANCE.accessToken, isJson)
            ?.create(serviceClass)!!
    }

    @CallSuper
    protected fun onViewAttached() {

    }

//    protected interface IObservableCreator<T> {
//        fun createObservable(forceNetWork: Boolean): Observable<Response<T>>
//    }


//    protected fun <T> generalRxHttpExecute(
//        @NonNull observable: Observable<Response<T>>, @Nullable subscriber: HttpSubscriber<T>?
//    ) {
//
//        if (subscriber != null) {
//            subscribers.add(subscriber)
//            observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber)
//        } else {
//            observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(HttpSubscriber<T>())
//        }
//    }

//    protected fun <T> generalRxHttpExecute(@NonNull observableCreator: IObservableCreator<T>, @NonNull httpObserver: HttpObserver<T>) {
//        generalRxHttpExecute(observableCreator, httpObserver, false)
//    }

//    protected fun <T> generalRxHttpExecute(
//        @NonNull observableCreator: IObservableCreator<T>, @NonNull httpObserver: HttpObserver<T>, readCacheFirst: Boolean
//    ) {
//        generalRxHttpExecute(observableCreator, httpObserver, readCacheFirst, null)
//    }

//    protected fun <T> generalRxHttpExecute(
//        @NonNull observableCreator: IObservableCreator<T>, @NonNull httpObserver: HttpObserver<T>, readCacheFirst: Boolean, @Nullable progressDialog: ProgressDialog?
//    ) {
//        requestTimesMap[observableCreator.toString()] = 1
//
//        val tempObserver = object : HttpObserver<T>() {
//            override fun onError(error: Throwable) {
//                if (!checkIsUnauthorized(error)) {
//                    httpObserver.onError(error)
//                }
//            }
//
//            override fun onSuccess(@NonNull response: HttpResponse<T>) {
//                if (response.isSuccessful) {
//                    if (readCacheFirst && response.isFromCache
//                        && NetHelper.INSTANCE.netEnabled
//                        && requestTimesMap[observableCreator.toString()] < 2
//                    ) {
//                        requestTimesMap[observableCreator.toString()] = 2
//                        generalRxHttpExecute<Any>(
//                            observableCreator.createObservable(true),
//                            getHttpSubscriber(this, progressDialog)
//                        )
//                    }
//                    httpObserver.onSuccess(response)
//                } else if (response.oriResponse.code() === 404) {
//                    onError(HttpPageNoFoundError())
//                } else if (response.oriResponse.code() === 504) {
//                    onError(HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK))
//                } else if (response.oriResponse.code() === 401) {
//                    onError(UnauthorizedError())
//                } else {
//                    onError(Error(response.oriResponse.message()))
//                }
//
//            }
//        }
//
//        val cacheFirstEnable = PrefUtils.isCacheFirstEnable
//        //        cacheFirstEnable = cacheFirstEnable || !NetHelper.INSTANCE.getNetEnabled();
//        generalRxHttpExecute<Any>(
//            observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
//            getHttpSubscriber<Any>(tempObserver, progressDialog)
//        )
//    }

//    private fun <T> getHttpSubscriber(
//        httpObserver: HttpObserver<T>,
//        progressDialog: ProgressDialog?
//    ): HttpSubscriber<T> {
//        return progressDialog?.let { HttpProgressSubscriber(it, httpObserver) } ?: HttpSubscriber(httpObserver)
//    }

    private fun checkIsUnauthorized(error: Throwable): Boolean {
        if (error is UnauthorizedError) {
            error.message?.let { mView!!.showErrorToast(it) }
            daoSession.authUserDao.delete(AppData.INSTANCE.authUser)
            AppData.INSTANCE.authUser = null
            AppData.INSTANCE.loggedUser = null
            mView!!.showLoginPage()
            return true
        }
        return false
    }

//    fun rxDBExecute(@NonNull runnable: Runnable) {
//        daoSession.rxTx().run(runnable).subscribe()
//    }

//    protected fun isLastResponse(@NonNull response: HttpResponse): Boolean {
//        return response.isFromNetWork() || !NetHelper.INSTANCE.netEnabled
//    }

    @NonNull
    protected fun getErrorTip(@NonNull error: Throwable?): String? {
        var errorTip: String? = null
        if (error == null) {
            return errorTip
        }
        if (error is UnknownHostException) {
            errorTip = getString(R.string.no_network_tip)
        } else if (error is SocketTimeoutException || error is ConnectTimeoutException) {
            errorTip = getString(R.string.load_timeout_tip)
        } else if (error is HttpError) {
            errorTip = error.message
        } else {
            errorTip = if (StringUtils.isBlank(error.message)) error.toString() else error.message
        }
        return errorTip
    }


    @NonNull
    protected fun getString(@StringRes resId: Int): String {
        return context?.resources.getString(resId)
    }

//    fun setEventSubscriber(eventSubscriber: Boolean) {
//        isEventSubscriber = eventSubscriber
//        if (isEventSubscriber && isAttached && !AppEventBus.INSTANCE.eventBus?.isRegistered(this)!!) {
//            AppEventBus.INSTANCE.eventBus?.register(this)
//        }
//    }

//    protected fun executeSimpleRequest(@NonNull observable: Observable<Response<ResponseBody>>) {
//        val httpObserver = object : HttpObserver<ResponseBody>() {
//            override fun onError(error: Throwable) {
//                getErrorTip(error)?.let { mView!!.showErrorToast(it) }
//            }
//
//            override fun onSuccess(response: HttpResponse<ResponseBody>) {}
//        }
//        generalRxHttpExecute<ResponseBody>(object : IObservableCreator<ResponseBody> {
//            override fun createObservable(forceNetWork: Boolean): Observable<Response<ResponseBody>> {
//                return observable
//            }
//        }, httpObserver)
//    }

//    protected fun checkStatus(
//        @NonNull observable: Observable<Response<ResponseBody>>,
//        @NonNull callback: CheckStatusCallback
//    ) {
//        val httpSubscriber = HttpSubscriber(
//            object : HttpObserver<ResponseBody>() {
//                override fun onError(error: Throwable) {}
//
//                override fun onSuccess(response: HttpResponse<ResponseBody>) {
//                    callback.onChecked(response.isSuccessful)
//                }
//            }
//        )
//
//        generalRxHttpExecute<Any>(observable, httpSubscriber)
//    }

//    protected interface CheckStatusCallback {
//        fun onChecked(status: Boolean)
//    }
}
