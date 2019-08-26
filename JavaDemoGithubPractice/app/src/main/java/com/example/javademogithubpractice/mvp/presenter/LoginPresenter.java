

package com.example.javademogithubpractice.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.javademogithubpractice.AppConfig;
import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.network.model.AuthRequestModel;
import com.example.javademogithubpractice.mvp.contract.ILoginContract;
import com.example.javademogithubpractice.mvp.model.BasicToken;
import com.example.javademogithubpractice.mvp.model.OauthToken;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.example.javademogithubpractice.room.model.AuthUser;
import com.example.javademogithubpractice.util.StringUtils;
import java.util.Date;
import java.util.UUID;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<ILoginContract.View> implements ILoginContract.Presenter{

    @Inject
    LoginPresenter(DaoSessionImpl daoSession) {
        super(daoSession);
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void getToken(String code, String state) {
        Observable<Response<OauthToken>> observable = getLoginService().getAccessToken(
                AppConfig.DEMOGITHUB_CLIENT_ID,
                AppConfig.DEMOGITHUB_CLIENT_SECRET, code, state);

        addDisposable(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::success,this::handleError));

        mView.showProgressDialog(getLoadTip());
    }

    private void handleError(Throwable throwable) {
        mView.dismissProgressDialog();
        mView.showErrorToast(getErrorTip(throwable));
    }

    private void success(Response<OauthToken> response) {
        OauthToken token = response.body();
        if (token != null) {
            mView.onGetTokenSuccess(BasicToken.generateFromOauthToken(token));
        } else {
            mView.onGetTokenError("Error check please");
        }
    }

    @NonNull
    @Override
    public String getOAuth2Url() {
        String randomState = UUID.randomUUID().toString();
        return AppConfig.OAUTH2_URL +
                "?client_id=" + AppConfig.DEMOGITHUB_CLIENT_ID +
//                "&scope=" + AppConfig.OAUTH2_SCOPE +
                "&redirect_uri=" + AppConfig.REDIRECT_URL;
//                "&state=" + randomState;
    }


    @Override
    public void basicLogin(String userName, String password) {
        AuthRequestModel authRequestModel = AuthRequestModel.generate();
        String token = Credentials.basic(userName, password);

        Observable<Response<BasicToken>> observable = getLoginService(token).authorizations(authRequestModel);

        addDisposable(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::successAuth, this::handleErrorAuth));

    }

    private void handleErrorAuth(Throwable throwable) {
        mView.onGetTokenError(getErrorTip(throwable));
    }

    private void successAuth(Response<BasicToken> response) {
        BasicToken token = response.body();
        if (token != null) {
            mView.onGetTokenSuccess(token);
        } else {
            mView.onGetTokenError("Error " + response.message());
        }
    }

    @Override
    public void handleOauth(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            getToken(code, state);
        }
    }

    @Override
    public void getUserInfo(final BasicToken basicToken) {
        Observable<Response<User>> observable = getUserService(basicToken.getToken()).getPersonInfo(true);

        addDisposable(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> {
            assert response.body() != null;
            saveAuthUser(basicToken, response.body());
                        mView.onLoginComplete();
                    }, this::onErrorUser));
        mView.showProgressDialog(getLoadTip());

    }

    private void onErrorUser(Throwable throwable) {
        mView.dismissProgressDialog();
        mView.showErrorToast(getErrorTip(throwable));
    }

    private void saveAuthUser(BasicToken basicToken, User userInfo) {
        AuthUser authUser = new AuthUser();
        String scope = StringUtils.listToString(basicToken.getScopes(), ",");
        Date date = new Date();
        authUser.setAccessToken(basicToken.getToken());
        authUser.setScope(scope);
        authUser.setAuthTime(date);
        authUser.setExpireIn(360 * 24 * 60 * 60);
        authUser.setSelected(true);
        authUser.setLoginId(userInfo.getLogin());
        authUser.setName(userInfo.getName());
        authUser.setAvatar(userInfo.getAvatarUrl());

        addDisposable(daoSession.storeAuthUser(authUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successStore,this::errorStore));

        AppData.INSTANCE.setAuthUser(authUser);
        AppData.INSTANCE.setLoggedUser(userInfo);
    }

    private void errorStore(Throwable throwable) {
        Toast.makeText(getContext(),"Error store" + throwable,Toast.LENGTH_SHORT).show();
    }

    private void successStore() {
        Toast.makeText(getContext(),"Success store",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
