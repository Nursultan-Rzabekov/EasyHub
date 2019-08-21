package com.example.javademogithubpractice.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ThirtyDegreesRay on 2017/11/13 10:40:22
 */

public class DBOpenHelper extends DaoMaster.DevOpenHelper {

    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion == 2 && newVersion == 3){
            //create new table, keep ori
        } else if(oldVersion == 3 && newVersion == 4){
            //create new table

            //transfer data from ori
            transferBookmarksAndTraceData(db);

        }  else {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }

    private void transferBookmarksAndTraceData(Database db){
        DaoSession daoSession = new DaoMaster(db).newSession();
        daoSession.clear();
    }

}
