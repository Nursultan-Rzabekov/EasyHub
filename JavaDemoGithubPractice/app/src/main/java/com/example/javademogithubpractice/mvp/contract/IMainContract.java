

package com.example.javademogithubpractice.mvp.contract;

import android.support.annotation.NonNull;

import com.example.javademogithubpractice.dao.AuthUser;

import java.util.List;

public interface IMainContract {

    interface View extends IBaseContract.View{
        void restartApp();
    }

    interface Presenter extends IBaseContract.Presenter<IMainContract.View>{
        boolean isFirstUseAndNoNewsUser();
        List<AuthUser> getLoggedUserList();
        void toggleAccount(@NonNull String loginId);
        void logout();
    }

}
