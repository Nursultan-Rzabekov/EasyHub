package com.example.kotlindemogithubproject.dao

import android.database.Cursor
import android.database.sqlite.SQLiteStatement
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.Property
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.database.DatabaseStatement
import org.greenrobot.greendao.internal.DaoConfig
import java.util.*

class AuthUserDao : AbstractDao<AuthUser, String> {

    object Properties {
        val AccessToken = Property(0, String::class.java, "accessToken", true, "ACCESS_TOKEN")
        val AuthTime = Property(1, java.util.Date::class.java, "authTime", false, "AUTH_TIME")
        val ExpireIn = Property(2, Int::class.javaPrimitiveType, "expireIn", false, "EXPIRE_IN")
        val Scope = Property(3, String::class.java, "scope", false, "SCOPE")
        val Selected = Property(4, Boolean::class.javaPrimitiveType, "selected", false, "SELECTED")
        val LoginId = Property(5, String::class.java, "loginId", false, "LOGIN_ID")
        val Name = Property(6, String::class.java, "name", false, "NAME")
        val Avatar = Property(7, String::class.java, "avatar", false, "AVATAR")
    }


    constructor(config: DaoConfig) : super(config) {}

    constructor(config: DaoConfig, daoSession: DaoSession) : super(config, daoSession) {}

    override fun bindValues(stmt: DatabaseStatement, entity: AuthUser) {
        stmt.clearBindings()
        stmt.bindString(1, entity.accessToken)
        stmt.bindLong(2, entity.authTime!!.time)
        stmt.bindLong(3, entity.expireIn.toLong())
        stmt.bindString(4, entity.scope)
        stmt.bindLong(5, if (entity.selected) 1L else 0L)
        stmt.bindString(6, entity.loginId)

        val name = entity.name
        if (name != null) {
            stmt.bindString(7, name)
        }

        val avatar = entity.avatar
        if (avatar != null) {
            stmt.bindString(8, avatar)
        }
    }

    override fun bindValues(stmt: SQLiteStatement, entity: AuthUser) {
        stmt.clearBindings()
        stmt.bindString(1, entity.accessToken)
        stmt.bindLong(2, entity.authTime!!.time)
        stmt.bindLong(3, entity.expireIn.toLong())
        stmt.bindString(4, entity.scope)
        stmt.bindLong(5, if (entity.selected) 1L else 0L)
        stmt.bindString(6, entity.loginId)

        val name = entity.name
        if (name != null) {
            stmt.bindString(7, name)
        }

        val avatar = entity.avatar
        if (avatar != null) {
            stmt.bindString(8, avatar)
        }
    }

    public override fun readKey(cursor: Cursor, offset: Int): String {
        return cursor.getString(offset + 0)
    }

    public override fun readEntity(cursor: Cursor, offset: Int): AuthUser {
        return AuthUser( //
            cursor.getString(offset + 0), // accessToken
            Date(cursor.getLong(offset + 1)), // authTime
            cursor.getInt(offset + 2), // expireIn
            cursor.getString(offset + 3), // scope
            cursor.getShort(offset + 4).toInt() != 0, // selected
            cursor.getString(offset + 5), // loginId
            if (cursor.isNull(offset + 6)) null else cursor.getString(offset + 6), // name
            if (cursor.isNull(offset + 7)) null else cursor.getString(offset + 7) // avatar
        )
    }

    public override fun readEntity(cursor: Cursor, entity: AuthUser, offset: Int) {
        entity.accessToken = cursor.getString(offset + 0)
        entity.authTime = java.util.Date(cursor.getLong(offset + 1))
        entity.expireIn = cursor.getInt(offset + 2)
        entity.scope = cursor.getString(offset + 3)
        entity.selected = cursor.getShort(offset + 4).toInt() != 0
        entity.loginId = cursor.getString(offset + 5)
        entity.name = if (cursor.isNull(offset + 6)) null else cursor.getString(offset + 6)
        entity.avatar = if (cursor.isNull(offset + 7)) null else cursor.getString(offset + 7)
    }

    override fun updateKeyAfterInsert(entity: AuthUser, rowId: Long): String? {
        return entity.accessToken
    }

    public override fun getKey(entity: AuthUser?): String? {
        return entity?.accessToken
    }

    public override fun hasKey(entity: AuthUser): Boolean {
        throw UnsupportedOperationException("Unsupported for entities with a non-null key")
    }

    override fun isEntityUpdateable(): Boolean {
        return true
    }

    companion object {

        val TABLENAME = "AUTH_USER"

        /** Creates the underlying database table.  */
        fun createTable(db: Database, ifNotExists: Boolean) {
            val constraint = if (ifNotExists) "IF NOT EXISTS " else ""
            db.execSQL(
                "CREATE TABLE " + constraint + "\"AUTH_USER\" (" + //

                        "\"ACCESS_TOKEN\" TEXT PRIMARY KEY NOT NULL ," + // 0: accessToken

                        "\"AUTH_TIME\" INTEGER NOT NULL ," + // 1: authTime

                        "\"EXPIRE_IN\" INTEGER NOT NULL ," + // 2: expireIn

                        "\"SCOPE\" TEXT NOT NULL ," + // 3: scope

                        "\"SELECTED\" INTEGER NOT NULL ," + // 4: selected

                        "\"LOGIN_ID\" TEXT NOT NULL ," + // 5: loginId

                        "\"NAME\" TEXT," + // 6: name

                        "\"AVATAR\" TEXT);"
            ) // 7: avatar
        }

        /** Drops the underlying database table.  */
        fun dropTable(db: Database, ifExists: Boolean) {
            val sql = "DROP TABLE " + (if (ifExists) "IF EXISTS " else "") + "\"AUTH_USER\""
            db.execSQL(sql)
        }
    }

}
