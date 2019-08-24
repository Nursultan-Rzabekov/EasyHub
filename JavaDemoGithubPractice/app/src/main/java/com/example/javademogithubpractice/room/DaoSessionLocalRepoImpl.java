package com.example.javademogithubpractice.room;


import com.example.javademogithubpractice.room.dao.LocalRepoDao;
import com.example.javademogithubpractice.room.model.LocalRepo;


import io.reactivex.Completable;

public class DaoSessionLocalRepoImpl {

    private LocalRepoDao localRepoDao;

    public DaoSessionLocalRepoImpl(LocalRepoDao localRepoDao) {
        this.localRepoDao = localRepoDao;
    }

    public Completable storeLocalRepo(LocalRepo localRepo){
        return localRepoDao.insertLocalRepo(localRepo);
    }

    public Completable updateLocalRepo(LocalRepo localRepo){
        return localRepoDao.updateLocalRepo(localRepo);
    }

}
