

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.common.Event;
import com.example.javademogithubpractice.mvp.contract.IUserListContract;
import com.example.javademogithubpractice.mvp.model.SearchModel;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.network.error.HttpPageNoFoundError;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.example.javademogithubpractice.ui.fragment.UserListFragment;
import com.example.javademogithubpractice.util.StringUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class UserListPresenter extends BasePagerPresenter<IUserListContract.View>
        implements IUserListContract.Presenter {

    @AutoAccess UserListFragment.UserListType type;
    @AutoAccess String user;
    @AutoAccess String repo;
    @AutoAccess SearchModel searchModel;

    private ArrayList<User> users;

    @Inject
    public UserListPresenter(AuthSessionRepository daoSession) {
        super(daoSession);
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (type.equals(UserListFragment.UserListType.SEARCH)) {
            //setEventSubscriber(true);
        }
    }

    @Override
    protected void loadData() {
        if(UserListFragment.UserListType.SEARCH.equals(type)){
            if(searchModel != null) searchUsers(1);
        } else {
            loadUsers(1, false);
        }
    }

    @Override
    public void loadUsers(final int page, final boolean isReload) {
        if (type.equals(UserListFragment.UserListType.SEARCH)) {
            searchUsers(page);
            return;
        }
        mView.showLoading();
        final boolean readCacheFirst = page == 1 && !isReload;

        addDisposable(createObservable(true,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    mView.hideLoading();
                    if (isReload || users == null || readCacheFirst) {
                        users = response.body();
                    } else {
                        users.addAll(response.body());
                    }
                    if(response.body().size() == 0 && users.size() != 0){
                        mView.setCanLoadMore(false);
                    } else {
                        mView.showUsers(users);
                    }

                },this::handleError));

    }

    private Observable<Response<ArrayList<User>>> createObservable(boolean forceNetWork,int page) {
        if (type.equals(UserListFragment.UserListType.FOLLOWERS)) {
            return getUserRepositoryImpl().getFollowers(forceNetWork, user, page);
        } else if (type.equals(UserListFragment.UserListType.FOLLOWING)) {
            return getUserRepositoryImpl().getFollowing(forceNetWork, user, page);
        } else if (type.equals(UserListFragment.UserListType.ORG_MEMBERS)) {
            return getUserRepositoryImpl().getOrgMembers(forceNetWork, user, page);
        } else {
            throw new IllegalArgumentException(type.name());
        }
    }

    private void searchUsers(final int page) {
        mView.showLoading();
        addDisposable(getSearchRepositoryImpl()
                .searchUsers(
                searchModel.getQuery(),
                "asc", page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mView.hideLoading();
                    if (users == null || page == 1) {
                        users = response.body().getItems();
                    } else {
                        users.addAll(response.body().getItems());
                    }
                    if(response.body().getItems().size() == 0 && users.size() != 0){
                        mView.setCanLoadMore(false);
                    } else {
                        mView.showUsers(users);
                    }
                },this::errorSearchUser));
    }

    private void errorSearchUser(Throwable error) {
        mView.hideLoading();
        handleError(error);
    }

    @Subscribe
    public void onSearchEvent(Event.SearchEvent searchEvent) {
        if (!searchEvent.searchModel.getType().equals(SearchModel.SearchType.User)) return;
        setLoaded(false);
        this.searchModel = searchEvent.searchModel;
        prepareLoadData();
    }

    private void handleError(Throwable error){
        mView.hideLoading();
        if(!StringUtils.isBlankList(users)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showUsers(new ArrayList<User>());
        } else {
            mView.showLoadError(getErrorTip(error));
        }
    }

}
