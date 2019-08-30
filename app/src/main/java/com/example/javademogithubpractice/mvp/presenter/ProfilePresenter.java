

package com.example.javademogithubpractice.mvp.presenter;

import android.os.Handler;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.mvp.contract.IProfileContract;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ProfilePresenter extends BasePresenter<IProfileContract.View> implements IProfileContract.Presenter{

    @AutoAccess String loginId;
    @AutoAccess String userAvatar;
    private User user;
    private boolean following = false;

    private boolean isTransitionComplete = false;
    private boolean isWaitForTransition = false;


    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Inject
    public ProfilePresenter(DaoSessionImpl daoSession) {
        super(daoSession);
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        new Handler().postDelayed(() -> {
            if(mView == null) return;
            isTransitionComplete = true;
            if(isWaitForTransition) mView.showProfileInfo(user);
            isWaitForTransition = false;
            getProfileInfo();
        }, 500);
    }

    private void getProfileInfo(){
        mView.showLoading();

        addDisposable(getUserService().getUser(true, loginId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successProfile,this::errorProfile));
    }

    private void errorProfile(Throwable throwable) {
        mView.showErrorToast(getErrorTip(throwable));
        mView.hideLoading();
    }

    private void successProfile(Response<User> response) {
        user = response.body();
        mView.hideLoading();
        if(isTransitionComplete){
            mView.showProfileInfo(user);
        } else {
            isWaitForTransition = true;
        }
        //saveTrace();

    }

    public String getLoginId() {
        return loginId;
    }

    public String getUserAvatar() {
        return user != null ? user.getAvatarUrl() : userAvatar;
    }

    public User getUser() {
        return user;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isUser(){
        return user != null && user.isUser();
    }

    public boolean isMe(){
        return user != null && user.getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin());
    }

    @Override
    public void followUser(boolean follow) {
        following = follow;

        if(follow){
            addDisposable((getUserService().followUser(loginId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::successFollow,this::errorFollow)));
        }
        else {
            addDisposable((getUserService().unfollowUser(loginId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::successFollow,this::errorFollow)));
        }

    }

    private void errorFollow(Throwable throwable) {
        mView.showErrorToast(getErrorTip(throwable));
    }

    private void successFollow(Response<ResponseBody> responseBodyResponse) {

    }


}