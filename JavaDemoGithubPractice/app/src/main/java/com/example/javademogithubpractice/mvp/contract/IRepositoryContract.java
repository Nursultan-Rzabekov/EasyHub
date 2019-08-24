

package com.example.javademogithubpractice.mvp.contract;


import com.example.javademogithubpractice.mvp.model.Repository;

public interface IRepositoryContract {

    interface View extends IBaseContract.View{
        void showRepo(Repository repo);
    }

    interface Presenter extends IBaseContract.Presenter<IRepositoryContract.View>{
    }

}
