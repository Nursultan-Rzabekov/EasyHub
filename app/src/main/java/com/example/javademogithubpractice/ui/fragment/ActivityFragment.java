

package com.example.javademogithubpractice.ui.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerFragmentComponent;
import com.example.javademogithubpractice.inject.module.FragmentModule;
import com.example.javademogithubpractice.mvp.contract.IActivityContract;
import com.example.javademogithubpractice.mvp.model.Event;
import com.example.javademogithubpractice.mvp.presenter.ActivityPresenter;
import com.example.javademogithubpractice.ui.activity.ProfileActivity;
import com.example.javademogithubpractice.ui.adapter.ActivitiesAdapter;
import com.example.javademogithubpractice.ui.fragment.baseFragment.ListFragment;
import com.example.javademogithubpractice.util.BundleHelper;

import java.util.ArrayList;

public class ActivityFragment extends ListFragment<ActivityPresenter, ActivitiesAdapter>
        implements IActivityContract.View {

    public enum ActivityType {
        News,PublicNews
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user) {
        return create(type, user, null);
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user,
                                          @Nullable String repo) {
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).put("repo", repo).build());
        return fragment;
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
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadEvents(true, 1);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_activity);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        Event event = adapter.getData().get(position);
        if (event.getRepo() == null) {
            ProfileActivity.show(getActivity(), null, event.getActor().getLogin(), event.getActor().getAvatarUrl());
            return;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getIntent() != null) {
            startActivity(item.getIntent());
        }
        return true;
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadEvents(false, page);
    }

    @Override
    public void showEvents(ArrayList<Event> events) {
        adapter.setData(events);
        postNotifyDataSetChanged();
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if (mPresenter != null) mPresenter.prepareLoadData();
    }

}
