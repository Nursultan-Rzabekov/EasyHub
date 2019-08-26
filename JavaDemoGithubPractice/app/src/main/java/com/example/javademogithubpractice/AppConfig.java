

package com.example.javademogithubpractice;

public class AppConfig {

    public final static String GITHUB_BASE_URL = "https://github.com/";

    public final static String GITHUB_API_BASE_URL = "https://api.github.com/";

    public final static int HTTP_TIME_OUT = 32 * 1000;

    public final static int HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60;

    public final static String DB_NAME = "EasyHub.db";


    public final static String DEMOGITHUB_CLIENT_ID = "a11dc36f787d8cb01f97";

    public final static String DEMOGITHUB_CLIENT_SECRET = "4f9a62c9fd413382ff677f3b23feab49e31af579";

    public final static String OAUTH2_SCOPE = "user,repo,gist,notifications";


    public final static String OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize";

    public final static String AUTH_HOME = GITHUB_BASE_URL + "Nursultan-Rzabekov";

    public final static String DEMOGITHUB_HOME = AUTH_HOME + "/EasyHub";

    public final static String REDIRECT_URL = "demogithub://login";

    public final static String DEMOGITHUB_RELEASE_SIGN_MD5 = "C0:99:37:D9:6A:36:FB:B7:AB:4C:5E:3D:42:96:FA:AF";


}
