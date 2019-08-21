package com.example.kotlindemogithubproject.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.example.kotlindemogithubproject.R
import com.example.kotlindemogithubproject.inject.component.AppComponent
import com.example.kotlindemogithubproject.mvp.contract.ISplashContract
import com.example.kotlindemogithubproject.mvp.presenter.SplashPresenter
import com.example.kotlindemogithubproject.ui.base.BaseActivity


class SplashActivity : BaseActivity<SplashPresenter>(), ISplashContract.View {

    private val TAG = "SplashActivity"

    override fun getContentView(): Int {
        return 0
    }

    override fun showMainPage() {
        //delayFinish()
//        val dataUri = intent.data
//        if (dataUri == null) {
//            Toast.makeText(applicationContext, "YEEEEEESSS", Toast.LENGTH_SHORT).show()
//            //startActivity(new Intent(getActivity(), MainActivity.class));
//        } else {
//            //BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
//        }
    }

    override fun showLoginPage() {
        //delayFinish()
        startActivity(Intent(activity, LoginActivity::class.java))
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }

    override fun initActivity() {
        super.initActivity()
        mPresenter!!.getUser()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            //            case REQUEST_ACCESS_TOKEN:
//            //                if(resultCode == RESULT_OK){
//            //                    showMainPage();
//            //                }
//            //                break;
//            else -> {
//            }
//        }
//    }

}
