package com.example.kotlindemogithubproject.inject.component


import com.example.kotlindemogithubproject.AppApplication
import com.example.kotlindemogithubproject.dao.DaoSession
import com.example.kotlindemogithubproject.inject.module.AppModule
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    val application: AppApplication
    val daoSession: DaoSession

}
