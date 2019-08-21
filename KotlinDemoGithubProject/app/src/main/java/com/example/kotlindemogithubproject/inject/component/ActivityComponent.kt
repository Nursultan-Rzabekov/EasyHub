package com.example.kotlindemogithubproject.inject.component



import com.example.kotlindemogithubproject.inject.ActivityScope
import com.example.kotlindemogithubproject.inject.module.ActivityModule
import com.example.kotlindemogithubproject.ui.activity.LoginActivity
import com.example.kotlindemogithubproject.ui.activity.SplashActivity
import dagger.Component


@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
}
