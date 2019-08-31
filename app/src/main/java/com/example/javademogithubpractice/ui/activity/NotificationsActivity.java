

package com.example.javademogithubpractice.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.ui.activity.base.BottomNavigationBehavior;
import com.example.javademogithubpractice.ui.activity.base.PagerActivity;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentPagerModel;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentViewPagerAdapter;
import com.example.javademogithubpractice.ui.fragment.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class NotificationsActivity extends PagerActivity {

    public static void show(@NonNull Activity activity){
        Intent intent = new Intent(activity, NotificationsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initActivity() {
        super.initActivity();
        pagerAdapter =  new FragmentViewPagerAdapter(getSupportFragmentManager());
    }


    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.notifications));

        pagerAdapter.setPagerList(FragmentPagerModel.createNotificationsPagerList(getActivity(), getFragments()));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigationMyProfile:
                ProfileActivity.show(getActivity(), AppData.INSTANCE.getLoggedUser().getLogin(),
                        AppData.INSTANCE.getLoggedUser().getAvatarUrl());
                break;
            case R.id.navigationHome:
                break;
            case R.id.navigationSearch:
                SearchActivity.show(getActivity());
                break;
            case R.id.navigationMenu:
                //drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return false;
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_view_pager;
    }

    @Override
    public int getPagerSize() {
        return 2;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof NotificationsFragment){
            NotificationsFragment.NotificationsType type = (NotificationsFragment.NotificationsType) fragment.getArguments().get("type");
            if(NotificationsFragment.NotificationsType.Unread.equals(type)){
                return 0;
            } else if(NotificationsFragment.NotificationsType.All.equals(type)){
                return 1;
            } else
                return -1;
        } else
            return -1;
    }

}
