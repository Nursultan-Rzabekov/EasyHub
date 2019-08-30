package com.example.javademogithubpractice.room.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.javademogithubpractice.room.model.AuthUser;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface AuthUserDao {

    @Insert
    Completable insertAuthUser(AuthUser authUser);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAuthUser(AuthUser authUser);

    @Query("SELECT * FROM authUser_database")
    Observable<List<AuthUser>> loadAll();
//
//    @Query("DELETE FROM authUser_database WHERE accessToken = :accessAuthUser")
//    Completable deleteAuthUser(String accessAuthUser);

    @Delete
    Completable deleteAuthUser(AuthUser authUser);

    @Query("DELETE FROM authUser_database")
    Completable deleteAllAuthUser();




}

