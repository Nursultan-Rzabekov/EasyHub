

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.mvp.contract.IRepositoryContract;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.room.DaoSessionLocalRepoImpl;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepositoryPresenter extends BasePresenter<IRepositoryContract.View> implements IRepositoryContract.Presenter {

    @AutoAccess(dataName = "repository")
    Repository repository;

    @AutoAccess String owner;
    @AutoAccess String repoName;

    private boolean isStatusChecked = false;
    @AutoAccess boolean isTraceSaved = false;


    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }


    @Inject
    public RepositoryPresenter(DaoSessionLocalRepoImpl daoSessionLocalRepo) {
        super(daoSessionLocalRepo);
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (repository != null) {
            owner = repository.getOwner().getLogin();
            repoName = repository.getName();
            mView.showRepo(repository);
            getRepoInfo(false);
            checkStatus();
        } else {
            getRepoInfo(true);
        }
    }

    private void getRepoInfo(final boolean isShowLoading) {
        if (isShowLoading) mView.showLoading();

        addDisposable(getRepoService().getRepoInfo(true, owner, repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (isShowLoading) mView.hideLoading();
                    repository = response.body();
                    mView.showRepo(repository);
                    checkStatus();
                },error ->{
                    if (isShowLoading) mView.hideLoading();
                    mView.showErrorToast(getErrorTip(error));
                }));
    }


    private void checkStatus(){
        if(isStatusChecked) return;
        isStatusChecked = true;
    }

    public Repository getRepository() {
        return repository;
    }
    public String getRepoName() {
        return repository == null ? repoName : repository.getName();
    }
}
