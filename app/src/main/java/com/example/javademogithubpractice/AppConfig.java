

package com.example.javademogithubpractice;

public class AppConfig {

    public final static String GITHUB_BASE_URL = "https://github.com/";

    public final static String GITHUB_API_BASE_URL = "https://api.github.com/";

    public final static int HTTP_TIME_OUT = 32 * 1000;

    public final static int HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60;

    public final static String DB_NAME = "EasyHub.db";


    public final static String DEMOGITHUB_CLIENT_ID = "5395cda474cd53926595";

    public final static String DEMOGITHUB_CLIENT_SECRET = "cf16b36f512c250a3c9f48ecdc8e814e7732380c";

    public final static String OAUTH2_SCOPE = "user,repo,gist,notifications,admin:org,admin:org_hook";


    public final static String OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize";

    public final static String REDIRECT_URL = "easyhub://redirecturi";
}
