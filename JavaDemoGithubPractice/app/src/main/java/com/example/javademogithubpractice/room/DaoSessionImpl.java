package com.example.javademogithubpractice.room;

import com.example.javademogithubpractice.room.dao.AuthUserDao;
import com.example.javademogithubpractice.room.model.AuthUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class DaoSessionImpl {
    private AuthUserDao authUserDao;

    public DaoSessionImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    public Completable storeAuthUser(AuthUser authUser){
        return authUserDao.insertAuthUser(authUser);
    }

    public Completable updataAuthUser(AuthUser authUser){
        return authUserDao.updateAuthUser(authUser);
    }

    public Observable<List<AuthUser>> loadAllAuthUser(){
        return authUserDao.loadAll();
    }

    public Completable deleteAllAuthUser(){
        return authUserDao.deleteAllAuthUser();
    }

    public Observable<AuthUser> getFirstAuthUser(){
        return authUserDao.getFirstUser();
    }
}
