package com.example.javademogithubpractice.mvp.presenter;
import com.example.javademogithubpractice.mvp.contract.IRepoInfoContract;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import javax.inject.Inject;



public class RepoInfoPresenter extends BasePagerPresenter<IRepoInfoContract.View> implements IRepoInfoContract.Presenter{

    @AutoAccess Repository repository;

    @Inject
    public RepoInfoPresenter(DaoSessionImpl daoSession) {
        super(daoSession);
    }


    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        mView.showRepoInfo(repository);
    }


    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
