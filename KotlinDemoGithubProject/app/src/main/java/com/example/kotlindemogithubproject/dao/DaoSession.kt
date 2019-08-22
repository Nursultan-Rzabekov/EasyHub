package com.example.kotlindemogithubproject.dao

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.AbstractDaoSession
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.greenrobot.greendao.internal.DaoConfig

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

class DaoSession(db: Database, type: IdentityScopeType, daoConfigMap: Map<Class<out AbstractDao<*, *>>, DaoConfig>) :
    AbstractDaoSession(db) {

    private val authUserDaoConfig: DaoConfig
    private val localUserDaoConfig: DaoConfig

    val authUserDao: AuthUserDao
    val localUserDao: LocalUserDao


    init {
        authUserDaoConfig = daoConfigMap[AuthUserDao::class.java]!!.clone()
        authUserDaoConfig.initIdentityScope(type)
        localUserDaoConfig = daoConfigMap[LocalUserDao::class.java]!!.clone()
        localUserDaoConfig.initIdentityScope(type)

        authUserDao = AuthUserDao(authUserDaoConfig, this)
        localUserDao = LocalUserDao(localUserDaoConfig, this)
        registerDao(AuthUser::class.java, authUserDao)
        registerDao(LocalUser::class.java, localUserDao)
    }

    fun clear() {
        authUserDaoConfig.clearIdentityScope()
        localUserDaoConfig.clearIdentityScope()
    }

}