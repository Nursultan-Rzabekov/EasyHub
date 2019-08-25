package com.example.javademogithubpractice.room;

import com.example.javademogithubpractice.room.dao.AuthUserDao;
import com.example.javademogithubpractice.room.dao.LocalRepoDao;
import com.example.javademogithubpractice.room.dao.LocalUserDao;
import com.example.javademogithubpractice.room.model.AuthUser;
import com.example.javademogithubpractice.room.model.LocalRepo;

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

    public Completable deleteAuthUser(AuthUser authUser){
        return authUserDao.deleteAuthUser(authUser);
    }


}
