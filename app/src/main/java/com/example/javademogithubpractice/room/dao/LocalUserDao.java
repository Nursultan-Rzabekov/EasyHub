package com.example.javademogithubpractice.room.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.javademogithubpractice.room.model.AuthUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface LocalUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAuthUser(AuthUser authUser);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAuthUser(AuthUser authUser);

    @Query("SELECT * FROM authUser_database")
    Observable<List<AuthUser>> loadAll();

    @Query("DELETE FROM authUser_database")
    Completable deleteAllAuthUser();


    @Query("SELECT * FROM authUser_database LIMIT 1")
    Observable<AuthUser> getFirstUser();
}

