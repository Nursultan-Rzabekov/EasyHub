

package com.example.javademogithubpractice.ui.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerActivityComponent;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.mvp.contract.ILoginContract;
import com.example.javademogithubpractice.mvp.model.BasicToken;
import com.example.javademogithubpractice.mvp.presenter.LoginPresenter;
import com.example.javademogithubpractice.ui.activity.base.BaseActivity;
import com.example.javademogithubpractice.util.StringUtils;
import com.example.javademogithubpractice.util.ViewUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.user_name_et)
    TextInputEditText userNameEt;
    @BindView(R.id.user_name_layout)
    TextInputLayout userNameLayout;
    @BindView(R.id.password_et)
    TextInputEditText passwordEt;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_bn)
    SubmitButton loginBn;


    private String userName;
    private String password;


    @Override
    public void onGetTokenSuccess(BasicToken basicToken) {
        loginBn.doResult(true);
        mPresenter.getUserInfo(basicToken);
    }

    @Override
    public void onGetTokenError(String errorMsg) {
        loginBn.doResult(false);

        new Handler().postDelayed(() -> {
            loginBn.reset();
            loginBn.setEnabled(true);
        }, 1000);

        Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginComplete() {
        delayFinish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        loginBn.setOnResultEndListener(() -> {
        });

        passwordEt.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getAction() == KeyEvent.ACTION_DOWN)){
                if(loginBn.isEnabled())
                    ViewUtils.virtualClick(loginBn);
            }
            return false;
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.handleOauth(intent);
        setIntent(null);
    }

    @OnClick(R.id.login_bn)
    public void onLoginClick(){
        if(loginCheck()){
            loginBn.setEnabled(false);
            mPresenter.basicLogin(userName, password);
        }else{
            loginBn.reset();
        }
    }

    private boolean loginCheck(){
        boolean valid = true;
        userName = userNameEt.getText().toString();
        password = passwordEt.getText().toString();
        if(StringUtils.isBlank(userName)){
            valid = false;
            userNameLayout.setError(getString(R.string.user_name_warning));
        }else{
            userNameLayout.setErrorEnabled(false);
        }
        if(StringUtils.isBlank(password)){
            valid = false;
            passwordLayout.setError(getString(R.string.password_warning));
        }else{
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }
}
