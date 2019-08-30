

package com.example.javademogithubpractice.mvp.presenter;

import com.example.javademogithubpractice.mvp.contract.IActivityContract;
import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.network.error.HttpPageNoFoundError;
import com.example.javademogithubpractice.room.DaoSessionImpl;
import com.example.javademogithubpractice.ui.fragment.ActivityFragment;
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


public class ActivityPresenter extends BasePagerPresenter<IActivityContract.View>
        implements IActivityContract.Presenter{

    @AutoAccess ActivityFragment.ActivityType type ;
    @AutoAccess String user ;

    ArrayList<Event> events;

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Inject
    public ActivityPresenter(DaoSessionImpl daoSession) {
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
        if(events != null){
            mView.showEvents(events);
            mView.hideLoading();
            return;
        }
        loadEvents(false, 1);
    }

    @Override
    public void loadEvents(final boolean isReload, final int page) {
        mView.showLoading();
        final boolean readCacheFirst = !isReload && page == 1;
        addDisposable(getObservable(true, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    mView.hideLoading();
                    if(events == null || isReload || readCacheFirst){
                        events = response.body();
                    } else {
                        events.addAll(response.body());
                    }
                    if(response.body().size() == 0 && events.size() != 0){
                        mView.setCanLoadMore(false);
                    } else {
                        mView.showEvents(events);
                    }

                },this::errorLoadEvent));
    }

    private void errorLoadEvent(Throwable error) {
        mView.hideLoading();
        if(!StringUtils.isBlankList(events)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showEvents(new ArrayList<>());
        }else{
            mView.showLoadError(getErrorTip(error));
        }
    }

    private Observable<Response<ArrayList<Event>>> getObservable(boolean forceNetWork, int page){
        if(type.equals(ActivityFragment.ActivityType.News)){
            return getUserService().getNewsEvent(forceNetWork, user, page);
        } else if(type.equals(ActivityFragment.ActivityType.PublicNews)){
            return getUserService().getPublicEvent(forceNetWork, page);
        } else {
            return null;
        }
    }
}

