package com.example.kotlindemogithubproject



import androidx.annotation.Nullable
import com.example.kotlindemogithubproject.dao.AuthUser
import com.example.kotlindemogithubproject.mvp.model.User
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess
import java.util.Locale


enum class AppData {
    INSTANCE;

    @AutoAccess(dataName = "appData_loggedUser")
    var loggedUser: User? = null
    @AutoAccess(dataName = "appData_authUser")
    var authUser: AuthUser? = null

    @AutoAccess(dataName = "appData_systemDefaultLocale")
    var systemDefaultLocale: Locale? = null

    val accessToken: String?
        @Nullable get() = if (authUser == null) null else authUser!!.accessToken

    fun getSystemDefaultLocal(): Locale? {
        if (systemDefaultLocale == null) {
            systemDefaultLocale = Locale.getDefault()
        }
        return systemDefaultLocale
    }

}
