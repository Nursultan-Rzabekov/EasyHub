

package com.example.javademogithubpractice.mvp.contract;


public interface ISplashContract {

    interface View extends IBaseContract.View{
        void showMainPage();
    }

    interface Presenter extends IBaseContract.Presenter<ISplashContract.View>{
        void getUser();
        void saveAccessToken(String accessToken, String scope, int expireIn);
    }

}
