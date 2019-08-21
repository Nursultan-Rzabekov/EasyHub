package com.example.kotlindemogithubproject

object AppConfig {

    val GITHUB_BASE_URL = "https://github.com/"

    val GITHUB_API_BASE_URL = "https://api.github.com/"

    val HTTP_TIME_OUT = 32 * 1000

    val HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024

    val IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024

    val CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60

    val DB_NAME = "KotlinGithub.db"

    val OPENHUB_CLIENT_ID = "30a575738bda118edd84"

    val OPENHUB_CLIENT_SECRET = "fa8c360c73010fa1ff91a047b09dd2410081e188"

    val OAUTH2_SCOPE = "user,repo,gist,notifications"


    val OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize"

    val AUTH_HOME = GITHUB_BASE_URL + "Nursultan-Rzabekov"

    val OPENHUB_HOME = "$AUTH_HOME/OpenHub"

    val REDIRECT_URL = "https://github.com/Nursultan-Rzabekov/CallBackk"

    val OPENHUB_RELEASE_SIGN_MD5 = "C0:99:37:D9:6A:36:FB:B7:AB:4C:5E:3D:42:96:FA:AF"


}
