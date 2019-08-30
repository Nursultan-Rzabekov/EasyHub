

package com.example.javademogithubpractice.mvp.contract;

import com.example.javademogithubpractice.mvp.model.User;

import java.util.ArrayList;

public interface IProfileInfoContract {

    interface View extends IBaseContract.View, IBasePagerContract.View{
        void showProfileInfo(User user);
        void showUserOrgs(ArrayList<User> orgs);
    }

    interface Presenter extends IBasePagerContract.Presenter<IProfileInfoContract.View>{

    }

}
