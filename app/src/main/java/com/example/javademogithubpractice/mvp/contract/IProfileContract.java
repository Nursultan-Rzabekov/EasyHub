

package com.example.javademogithubpractice.mvp.contract;


import com.example.javademogithubpractice.mvp.model.User;

public interface IProfileContract {

    interface View extends IBaseContract.View{
        void showProfileInfo(User user);
        void invalidateOptionsMenu();
        void restartApp();
    }

    interface Presenter extends IBaseContract.Presenter<IProfileContract.View>{
        void logout();
    }
}
