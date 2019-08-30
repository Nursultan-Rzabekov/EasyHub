

package com.example.javademogithubpractice.mvp.contract;


import com.example.javademogithubpractice.mvp.model.Repository;

public interface IRepoInfoContract {

    interface View extends IBaseContract.View, IBasePagerContract.View{
        void showRepoInfo(Repository repository);
    }

    interface Presenter extends IBasePagerContract.Presenter<IRepoInfoContract.View>{
    }

}
