

package com.example.javademogithubpractice.mvp.contract;


import com.example.javademogithubpractice.mvp.model.User;

import java.util.ArrayList;

public interface IUserListContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showUsers(ArrayList<User> users);
    }

    interface Presenter extends IBasePagerContract.Presenter<IUserListContract.View>{
        void loadUsers(int page, boolean isReload);
    }

}
