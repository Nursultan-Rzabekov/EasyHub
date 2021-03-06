

package com.example.javademogithubpractice.mvp.presenter;



import android.widget.Toast;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.mvp.contract.IMainContract;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.example.javademogithubpractice.util.PrefUtils;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<IMainContract.View> implements IMainContract.Presenter{

    @Inject
    public MainPresenter(AuthSessionRepository daoSession) {
        super(daoSession);
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void logout() {
        addDisposable(daoSession.deleteAuthUser(AppData.INSTANCE.getAuthUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successDeleteUser,this::errorDeleteUser));

        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

    private void errorDeleteUser(Throwable throwable) {
        Toast.makeText(getContext(),"Error delete user" + throwable,Toast.LENGTH_SHORT).show();
    }

    private void successDeleteUser() {
        //Toast.makeText(getContext(),"Success delete user",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void detachView() {
        mView =  null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
