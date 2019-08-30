

package com.example.javademogithubpractice.mvp.contract;

public interface IMainContract {

    interface View extends IBaseContract.View{
        void restartApp();
    }

    interface Presenter extends IBaseContract.Presenter<IMainContract.View>{
        boolean isFirstUseAndNoNewsUser();
        //List<AuthUser> getLoggedUserList();
        void logout();
    }

}
