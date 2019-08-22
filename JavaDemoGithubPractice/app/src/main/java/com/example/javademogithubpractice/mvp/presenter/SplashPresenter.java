

package com.example.javademogithubpractice.mvp.presenter;

import android.widget.Toast;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.mvp.contract.ISplashContract;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.example.javademogithubpractice.room.model.AuthUser;

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

    //private final String TAG = "SplashPresenter";

    private AuthUser authUser;
    private boolean isMainPageShowwed = false;

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Inject
    SplashPresenter(DaoSessionImpl daoSession) {
        super(daoSession);
    }

    @Override
    public void getUser() {
        Observable<List<AuthUser>> observable = daoSession.loadAllAuthUser();
        addDisposable(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessAuthUser,this::onErrorUser));
    }


    private void onSuccessAuthUser(List<AuthUser> users) {
        System.out.println("######################GOOOOOOOOOOOOOOOODDD" + users);
        AuthUser selectedUser = users != null && users.size() > 0 ? users.get(0) : null;

        if (selectedUser != null && isExpired(selectedUser)) {
            //authUserDao.delete(selectedUser);
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
        daoSession.storeAuthUser(authUser);
        this.authUser = authUser;
    }


    private void getUserInfo(final String accessToken) {
        Observable<Response<User>> observable = getUserService(accessToken).getPersonInfo(true);
        addDisposable(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessUser,this::onErrorUser));
    }

    private void onSuccessUser(Response<User> response) {
        AppData.INSTANCE.setLoggedUser(response.body());
        if (authUser != null) {
            assert response.body() != null;
            authUser.setLoginId(response.body().getLogin());
            addDisposable(daoSession.storeAuthUser(authUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::successStore,this::errorStore));
        }
        if(!isMainPageShowwed) {
            isMainPageShowwed = true;
            mView.showMainPage();
        }
    }

    private void errorStore(Throwable throwable) {
        Toast.makeText(getContext(),"Error update" + throwable,Toast.LENGTH_SHORT).show();
    }

    private void successStore() {
        Toast.makeText(getContext(),"Success update",Toast.LENGTH_SHORT).show();
    }

    private void onErrorUser(Throwable throwable) {
        //daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
        AppData.INSTANCE.setAuthUser(null);
        mView.showErrorToast(getErrorTip(throwable));
        mView.showLoginPage();
    }

    private boolean isExpired(AuthUser selectedUser){
        return selectedUser.getAuthTime().getTime() + selectedUser.getExpireIn() * 1000 < System.currentTimeMillis();
    }

    @Override
    public void detachView() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        mView = null;
    }
}
