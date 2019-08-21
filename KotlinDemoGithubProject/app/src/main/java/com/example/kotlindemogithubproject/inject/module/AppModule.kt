package com.example.kotlindemogithubproject.inject.module

import com.example.kotlindemogithubproject.AppApplication
import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.dao.DBOpenHelper
import com.example.kotlindemogithubproject.dao.DaoMaster
import com.example.kotlindemogithubproject.dao.DaoSession
import dagger.Module
import dagger.Provides

import javax.inject.Singleton


@Suppress("UNREACHABLE_CODE")
@Module
class AppModule(private val application: AppApplication) {

    @Provides
    @Singleton
    fun provideApplication(): AppApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideDaoSession(): DaoSession {
        val helper = DBOpenHelper(application, AppConfig.DB_NAME, null!!)
        val db = helper.writableDatabase
        val daoMaster = DaoMaster(db)
        return daoMaster.newSession()
    }


}
