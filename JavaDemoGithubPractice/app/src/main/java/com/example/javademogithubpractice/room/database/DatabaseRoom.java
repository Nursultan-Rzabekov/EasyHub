package com.example.javademogithubpractice.room.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.javademogithubpractice.room.dao.AuthUserDao;
import com.example.javademogithubpractice.room.model.AuthUser;


@Database(entities = {AuthUser.class}, version = 1, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    public abstract AuthUserDao authUserDao();
}
