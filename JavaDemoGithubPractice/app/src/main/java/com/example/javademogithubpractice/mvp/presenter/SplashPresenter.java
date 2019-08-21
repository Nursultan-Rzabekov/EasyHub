

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.dao.AuthUser;
import com.example.javademogithubpractice.dao.AuthUserDao;
import com.example.javademogithubpractice.dao.DaoSession;
import com.example.javademogithubpractice.mvp.contract.ISplashContract;
import com.example.javademogithubpractice.mvp.model.User;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class SplashPresenter extends BasePresenter<ISplashContract.View> implements ISplashContract.Presenter{

    private final String TAG = "SplashPresenter";

    private AuthUser authUser;
    private boolean isMainPageShowwed = false;


    @Inject
    public SplashPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getUser() {
        AuthUserDao authUserDao = daoSession.getAuthUserDao();

        List<AuthUser> users = authUserDao.queryBuilder()
                .where(AuthUserDao.Properties.Selected.eq(true))
                .limit(1)
                .list();

        AuthUser selectedUser = users != null && users.size() > 0 ? users.get(0) : null;

        //if none selected, choose first account
        if(selectedUser == null){
            List<AuthUser> firstAccount = authUserDao.queryBuilder()
                    .limit(1)
                    .list();
            selectedUser = firstAccount != null && firstAccount.size() > 0 ? firstAccount.get(0) : null;
        }

        if (selectedUser != null && isExpired(selectedUser)) {
            authUserDao.delete(selectedUser);
            selectedUser = null;
        }

        if (selectedUser != null) {
            AppData.INSTANCE.setAuthUser(selectedUser);
            getUserInfo(selectedUser.getAccessToken());
        } else {

            mView.showLoginPage();
        }
    }

    @Override
    public void saveAccessToken(String accessToken, String scope, int expireIn) {
        AuthUser authUser = new AuthUser();
        authUser.setSelected(true);
        authUser.setScope(scope);
        authUser.setExpireIn(expireIn);
        authUser.setAuthTime(new Date());
        authUser.setAccessToken(accessToken);
        daoSession.getAuthUserDao().insert(authUser);
        this.authUser = authUser;
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void getUserInfo(final String accessToken) {
        Observable<Response<User>> observable = getUserService(accessToken).getPersonInfo(true);
        addDisposable(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessUser,this::onErrorUser));
    }

    private void onSuccessUser(Response<User> response) {
        AppData.INSTANCE.setLoggedUser(response.body());
        if (authUser != null) {
            authUser.setLoginId(response.body().getLogin());
            daoSession.getAuthUserDao().update(authUser);
        }
        if(!isMainPageShowwed) {
            isMainPageShowwed = true;
            mView.showMainPage();
        }
    }

    private void onErrorUser(Throwable throwable) {
        daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
        AppData.INSTANCE.setAuthUser(null);
        mView.showErrorToast(getErrorTip(throwable));
        mView.showLoginPage();
    }

    private boolean isExpired(AuthUser selectedUser){
        return selectedUser.getAuthTime().getTime() + selectedUser.getExpireIn() * 1000 < System.currentTimeMillis();
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
