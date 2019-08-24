package com.example.javademogithubpractice.room.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.javademogithubpractice.room.model.AuthUser;
import com.example.javademogithubpractice.room.model.LocalRepo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface LocalRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertLocalRepo(LocalRepo localRepo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateLocalRepo(LocalRepo localRepo);

}

