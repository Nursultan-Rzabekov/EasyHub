package com.example.javademogithubpractice.room.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.javademogithubpractice.room.dao.AuthUserDao;
import com.example.javademogithubpractice.room.dao.LocalRepoDao;
import com.example.javademogithubpractice.room.dao.LocalUserDao;
import com.example.javademogithubpractice.room.model.AuthUser;
import com.example.javademogithubpractice.room.model.LocalRepo;


@Database(entities = {AuthUser.class, LocalRepo.class}, version = 1, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    public abstract AuthUserDao authUserDao();
    public abstract LocalRepoDao localRepoDao();
}
