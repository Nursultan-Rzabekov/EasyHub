package com.example.kotlindemogithubproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.kotlindemogithubproject.R
import com.example.kotlindemogithubproject.inject.component.AppComponent
import com.example.kotlindemogithubproject.mvp.contract.ILoginContract
import com.example.kotlindemogithubproject.mvp.model.BasicToken
import com.example.kotlindemogithubproject.mvp.presenter.LoginPresenter
import com.example.kotlindemogithubproject.ui.base.BaseActivity
import com.example.kotlindemogithubproject.util.StringUtils
import com.example.kotlindemogithubproject.util.ViewUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unstoppable.submitbuttonview.SubmitButton
import kotlinx.android.synthetic.main.login_activity.*


class LoginActivity : BaseActivity<LoginPresenter>(), ILoginContract.View {

    private val TAG = LoginActivity::class.java.simpleName

    private var userName: String? = null
    private var password: String? = null


    override fun getContentView(): Int {
        return R.layout.login_activity
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mPresenter!!.handleOauth(intent)
        setIntent(null)
    }

    override fun onGetTokenSuccess(basicToken: BasicToken) {
        //login_bn!!.doResult(true)
        mPresenter!!.getUserInfo(basicToken)
    }

    override fun onGetTokenError(errorMsg: String) {
        //login_bn!!.doResult(false)
        Handler().postDelayed({
            //login_bn!!.reset()
            login_bn!!.isEnabled = true
        }, 1000)

        Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginComplete() {
        //delayFinish()
        //startActivity(new Intent(getActivity(), MainActivity.class));
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        //login_bn!!.setOnResultEndListener { }


        password_et!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER &&
                event.action == KeyEvent.ACTION_DOWN
            ) {
                if (login_bn!!.isEnabled)
                    ViewUtils.virtualClick(login_bn!!)
            }
            false
        }

    }


    fun onOauthLoginClick() {
        //AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getOAuth2Url());
    }

    fun onLoginClick() {
        if (loginCheck()) {
            login_bn!!.isEnabled = false
            mPresenter!!.basicLogin(userName!!, password!!)
        } else {
            //login_bn!!.()
        }
    }

    private fun loginCheck(): Boolean {
        var valid = true
        userName = user_name_et!!.text!!.toString()
        password = password_et!!.text!!.toString()
        if (StringUtils.isBlank(userName)) {
            valid = false
            user_name_layout!!.error = getString(R.string.user_name_warning)
        } else {
            user_name_layout!!.isErrorEnabled = false
        }
        if (StringUtils.isBlank(password)) {
            valid = false
            password_layout!!.error = getString(R.string.password_warning)
        } else {
            password_layout!!.isErrorEnabled = false
        }
        return valid
    }
}
