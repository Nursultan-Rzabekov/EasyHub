

package com.example.javademogithubpractice.mvp.contract;


import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.RepositoriesFilter;

import java.util.ArrayList;

public interface IRepositoriesContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showRepositories(ArrayList<Repository> repositoryList);

    }

    interface Presenter extends IBasePagerContract.Presenter<IRepositoriesContract.View> {
        void loadRepositories(boolean isReLoad, int page);
        void loadRepositories(RepositoriesFilter filter);
    }

}
