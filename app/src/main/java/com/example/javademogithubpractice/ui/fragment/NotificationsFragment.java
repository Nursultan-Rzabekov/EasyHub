

package com.example.javademogithubpractice.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerFragmentComponent;
import com.example.javademogithubpractice.inject.module.FragmentModule;
import com.example.javademogithubpractice.mvp.contract.INotificationsContract;
import com.example.javademogithubpractice.mvp.model.Notification;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.presenter.NotificationsPresenter;
import com.example.javademogithubpractice.ui.adapter.NotificationsAdapter;
import com.example.javademogithubpractice.ui.fragment.baseFragment.ListFragment;
import com.example.javademogithubpractice.util.BundleHelper;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment<NotificationsPresenter, NotificationsAdapter>
        implements INotificationsContract.View, NotificationsAdapter.NotificationAdapterListener {

    public enum NotificationsType{
        Unread, All
    }

    public static Fragment create(NotificationsType type){
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        //setHasOptionsMenu(NotificationsType.Unread.equals(mPresenter.getType()));
        adapter.setListener(this);
    }

    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        adapter.setData(notifications);
        postNotifyDataSetChanged();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadNotifications(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_notifications_tip);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onRepoMarkAsReadClicked(@NonNull Repository repository) {
        //mPresenter.markRepoNotificationsAsRead(repository);
    }

}
