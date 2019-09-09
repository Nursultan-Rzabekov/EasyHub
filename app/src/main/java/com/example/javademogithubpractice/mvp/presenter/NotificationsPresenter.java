

package com.example.javademogithubpractice.mvp.presenter;



import android.widget.Toast;
import com.example.javademogithubpractice.mvp.contract.INotificationsContract;
import com.example.javademogithubpractice.mvp.model.Notification;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.room.AuthSessionRepository;
import com.example.javademogithubpractice.ui.fragment.NotificationsFragment;
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

public class NotificationsPresenter extends BasePagerPresenter<INotificationsContract.View>
        implements INotificationsContract.Presenter {

    @AutoAccess NotificationsFragment.NotificationsType type;
    private ArrayList<Notification> notifications;

    @Inject
    public NotificationsPresenter(AuthSessionRepository daoSession) {
        super(daoSession);
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void loadData() {
        loadNotifications(1, false);
    }

    @Override
    public void loadNotifications(final int page, boolean isReload) {
        mView.showLoading();

        addDisposable(createObservable(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    mView.hideLoading();
                    if (notifications == null || page == 1) {
                        notifications = response.body();

                    } else {
                        notifications.addAll(response.body());
                    }
                    if (response.body().size() == 0 && notifications.size() != 0) {
                        mView.setCanLoadMore(false);
                    } else {
                        Toast.makeText(getContext(),"Else",Toast.LENGTH_SHORT).show();
                        mView.showNotifications(notifications);
                    }
                },this::errorLoadNotifications));
    }

    private void errorLoadNotifications(Throwable error) {
        mView.hideLoading();
        if (!StringUtils.isBlankList(notifications)) {
            mView.showErrorToast(getErrorTip(error));
        } else {
            mView.showLoadError(getErrorTip(error));
        }
    }


    public Observable<Response<ArrayList<Notification>>> createObservable(boolean forceNetWork) {
        if (NotificationsFragment.NotificationsType.Unread.equals(type)) {
            return getNotificationRepositoryImpl().getMyNotifications(forceNetWork, false, false);
        } else if (NotificationsFragment.NotificationsType.All.equals(type)) {
            return getNotificationRepositoryImpl().getMyNotifications(forceNetWork, true, false);
        } else {
            return null;
        }
    }

    public NotificationsFragment.NotificationsType getType() {
        return type;
    }

    @Override
    public void detachView() {
        mView = null;
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
