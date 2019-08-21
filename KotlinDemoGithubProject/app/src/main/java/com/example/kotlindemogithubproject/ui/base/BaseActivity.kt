package com.example.kotlindemogithubproject.ui.base


import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlindemogithubproject.AppApplication
import com.example.kotlindemogithubproject.AppData
import com.example.kotlindemogithubproject.dao.DaoSession
import com.example.kotlindemogithubproject.inject.component.AppComponent
import com.example.kotlindemogithubproject.mvp.contract.IBaseContract
import com.example.kotlindemogithubproject.ui.activity.LoginActivity
import com.example.kotlindemogithubproject.ui.activity.SplashActivity
import com.example.kotlindemogithubproject.util.WindowUtil
import com.thirtydegreesray.dataautoaccess.DataAutoAccess
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess
import javax.inject.Inject

abstract class BaseActivity<P:IBaseContract.Presenter<*>> : AppCompatActivity(), IBaseContract.View {

    @Inject
    protected var mPresenter: P? = null

    private var mProgressDialog: ProgressDialog? = null

    var isAlive = true


    protected val activity: BaseActivity<*>
        @NonNull
        get() = this

    protected val appApplication: AppApplication
        @NonNull
        get() = application as AppApplication

    protected val appComponent: AppComponent
        get() = appApplication.appComponent!!

    protected val daoSession: DaoSession
        get() = appComponent.daoSession


    val appVersionName: String
        get() {
            var versionName = ""
            try {
                versionName = packageManager.getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionName
        }

    @AutoAccess
    internal var isFullScreen = false

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        if ((AppData.INSTANCE.authUser == null || AppData.INSTANCE.loggedUser == null)
            && this.javaClass != SplashActivity::class.java
            && this.javaClass != LoginActivity::class.java
        ) {
            super.onCreate(savedInstanceState)
            finishAffinity()
            startActivity(Intent(activity, SplashActivity::class.java))
            return
        }

        super.onCreate(savedInstanceState)
        isAlive = true
        setupActivityComponent(appComponent)
        DataAutoAccess.getData(this, savedInstanceState)

        if (mPresenter != null) {
            mPresenter!!.onRestoreInstanceState(savedInstanceState ?: intent.extras)
            mPresenter!!.attachView(this)
        }

        if (savedInstanceState != null && AppData.INSTANCE.authUser == null) {
            DataAutoAccess.getData(AppData.INSTANCE, savedInstanceState)
        }

        getScreenSize()
        if (getContentView() != 0) {
            setContentView(getContentView())
        }

        initActivity()
        initView(savedInstanceState)
        if (mPresenter != null) mPresenter!!.onViewInitialized()
        if (isFullScreen) {
            intoFullScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        DataAutoAccess.saveData(this, outState)
        if (mPresenter != null) mPresenter!!.onSaveInstanceState(outState)
        if (curActivity == this) {
            DataAutoAccess.saveData(AppData.INSTANCE, outState)
        }
    }

    protected abstract fun setupActivityComponent(appComponent: AppComponent)

    @LayoutRes
    protected abstract fun getContentView(): Int

    @CallSuper
    protected open fun initActivity() {

    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        curActivity = activity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) mPresenter!!.detachView()
        if (this == curActivity) curActivity = null
        isAlive = false
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.with(this).onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.with(this).onLowMemory()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun finishActivity() {
        finish()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showProgressDialog(content: String) {
        getProgressDialog(content)
        mProgressDialog!!.show()
    }

    override fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        } else {
            throw NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!")
        }
    }

    override fun getProgressDialog(content: String): ProgressDialog {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(activity)
            mProgressDialog!!.setCancelable(false)
        }
        mProgressDialog!!.setMessage(content)
        return mProgressDialog as ProgressDialog
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showInfoToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showWarningToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showTipDialog(content: String) {
        AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle("qweqwe")
            .setMessage(content)
            .setCancelable(true)
            .setPositiveButton("wqewq", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            .show()
    }

    override fun showConfirmDialog(
        msn: String,
        title: String,
        confirmText: String,
        confirmListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle(title)
            .setMessage(msn)
            .setCancelable(true)
            .setPositiveButton(confirmText, confirmListener)
            .setNegativeButton("sadasd") { dialog, which -> dialog.cancel() }
            .show()
    }

    private fun getScreenSize() {
        if (WindowUtil.screenHeight === 0 || WindowUtil.screenWidth === 0) {
            val display = windowManager.defaultDisplay
            WindowUtil.screenWidth = display.width
            WindowUtil.screenHeight = display.height
        }
    }

    override fun showLoginPage() {
        activity.finishAffinity()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    protected fun exitFullScreen() {
        showStatusBar()
        isFullScreen = false
    }

    protected fun intoFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        isFullScreen = true
    }

    private fun showStatusBar() {
        val attrs = window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onBackPressed() {
        if (isFullScreen) {
            exitFullScreen()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        var curActivity: BaseActivity<*>? = null
            private set
    }
}
