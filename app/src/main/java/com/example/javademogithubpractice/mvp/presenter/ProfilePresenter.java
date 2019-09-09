

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.mvp.contract.IProfileContract;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProfilePresenter extends BasePresenter<IProfileContract.View> implements IProfileContract.Presenter{

    @AutoAccess String loginId;
    @AutoAccess String userAvatar;
    private User user;

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Inject
    public ProfilePresenter(AuthSessionRepository daoSession) {
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
        getProfileInfo();
    }

    private void getProfileInfo(){
        mView.showLoading();
        addDisposable(getUserRepositoryImpl().getUser(true, loginId)
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
        mView.showProfileInfo(user);
    }

    public String getUserAvatar() {
        return user != null ? user.getAvatarUrl() : userAvatar;
    }

    public User getUser() {
        return user;
    }


    public boolean isUser(){
        return user != null && user.isUser();
    }

    public boolean isMe(){
        return user != null && user.getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin());
    }
}
