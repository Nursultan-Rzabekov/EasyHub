package com.example.kotlindemogithubproject.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.greenrobot.greendao.database.Database

import java.util.ArrayList
import java.util.UUID

/**
 * Created by ThirtyDegreesRay on 2017/11/13 10:40:22
 */

class DBOpenHelper : DaoMaster.DevOpenHelper {

    constructor(context: Context, name: String) : super(context, name) {}

    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory) : super(
        context,
        name,
        factory
    ) {
    }

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 2 && newVersion == 3) {
            //create new table, keep ori
        } else if (oldVersion == 3 && newVersion == 4) {
            //create new table
            LocalUserDao.createTable(db!!, false)

            //transfer data from ori
            transferBookmarksAndTraceData(db)

            //drop old tables
        } else if (oldVersion == 4 && newVersion == 5) {
        } else {
            super.onUpgrade(db, oldVersion, newVersion)
        }
    }

    private fun transferBookmarksAndTraceData(db: Database) {
        val daoSession = DaoMaster(db).newSession()

        daoSession.clear()
    }

}
