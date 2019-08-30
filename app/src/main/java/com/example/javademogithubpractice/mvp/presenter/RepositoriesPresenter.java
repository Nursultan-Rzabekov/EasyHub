package com.example.javademogithubpractice.mvp.presenter;
import com.example.javademogithubpractice.mvp.contract.IRepositoriesContract;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.SearchModel;
import com.example.javademogithubpractice.network.error.HttpPageNoFoundError;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.util.StringUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import java.util.ArrayList;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class RepositoriesPresenter extends BasePagerPresenter<IRepositoriesContract.View> implements IRepositoriesContract.Presenter {

    private ArrayList<Repository> repos;

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @AutoAccess RepositoriesFragment.RepositoriesType type;
    @AutoAccess String user;
    @AutoAccess String repo;
    @AutoAccess RepositoriesFilter filter;
    @AutoAccess SearchModel searchModel;

    @Inject
    public RepositoriesPresenter(DaoSessionImpl daoSession) {
        super(daoSession);
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
    }

    @Override
    protected void loadData() {
        if (RepositoriesFragment.RepositoriesType.SEARCH.equals(type)) {
            if (searchModel != null) searchRepos(1);
            return;
        }
        loadRepositories(false, 1);
    }

    private void searchRepos(final int page) {
        mView.showLoading();

        addDisposable(getSearchService().searchRepos(searchModel.getQuery(),"asc", page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    mView.hideLoading();
                    if (repos == null || page == 1) {
                        repos = response.body().getItems();
                    } else {
                        repos.addAll(response.body().getItems());
                    }
                    if (response.body().getItems().size() == 0 && repos.size() != 0) {
                        mView.setCanLoadMore(false);
                    } else {
                        mView.showRepositories(repos);
                    }
                },this::errorLoadSearch));

    }

    private void errorLoadSearch(Throwable throwable) {
        mView.hideLoading();
        handleError(throwable);
    }

    @Override
    public void loadRepositories(final boolean isReLoad, final int page) {
        filter = getFilter();
        mView.showLoading();
        final boolean readCacheFirst = !isReLoad && page == 1;

        addDisposable(getObservable(true,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mView.hideLoading();
                    if (isReLoad || readCacheFirst || repos == null || page == 1) {
                        repos = response.body();
                    } else {
                        repos.addAll(response.body());
                    }
                    if (response.body().size() == 0 && repos.size() != 0) {
                        mView.setCanLoadMore(false);
                    } else {
                        mView.showRepositories(repos);
                    }
                },this::errorLoadRepo));

    }

    @Override
    public void loadRepositories(RepositoriesFilter filter) {
        this.filter = filter;
        loadRepositories(false, 1);
    }

    private void errorLoadRepo(Throwable error) {
        mView.hideLoading();
        handleError(error);
    }

    private Observable<Response<ArrayList<Repository>>> getObservable(boolean forceNetWork, int page) {
        switch (type) {
            case OWNED:
                return getRepoService().getUserRepos(forceNetWork, page, filter.getType(),
                        filter.getSort(), filter.getSortDirection());
            case PUBLIC:
                return getRepoService().getUserPublicRepos(forceNetWork, user, page,
                        filter.getType(), filter.getSort(), filter.getSortDirection());
            case STARRED:
                return getRepoService().getStarredRepos(forceNetWork, user, page,
                        filter.getSort(), filter.getSortDirection());
            case FORKS:
                return getRepoService().getForks(forceNetWork, user, repo, page);
            default:
                return null;
        }
    }

    private void handleError(Throwable error) {
        if (!StringUtils.isBlankList(repos)) {
            mView.showErrorToast(getErrorTip(error));
        } else if (error instanceof HttpPageNoFoundError) {
            mView.showRepositories(new ArrayList<>());
        } else {
            mView.showLoadError(getErrorTip(error));
        }
    }

    public String getUser() {
        return user;
    }

    public RepositoriesFragment.RepositoriesType getType() {
        return type;
    }

    public RepositoriesFilter getFilter() {
        if (filter == null) {
            filter = RepositoriesFragment.RepositoriesType.STARRED.equals(type) ?
                    RepositoriesFilter.DEFAULT_STARRED_REPO : RepositoriesFilter.DEFAULT;
        }
        return filter;
    }
}
