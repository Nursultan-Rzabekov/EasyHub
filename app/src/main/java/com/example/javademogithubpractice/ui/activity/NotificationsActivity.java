

package com.example.javademogithubpractice.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.mvp.model.RepositoriesFilter;
import com.example.javademogithubpractice.ui.activity.base.BottomNavigationBehavior;
import com.example.javademogithubpractice.ui.activity.base.PagerActivity;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentPagerModel;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentViewPagerAdapter;
import com.example.javademogithubpractice.ui.fragment.ActivityFragment;
import com.example.javademogithubpractice.ui.fragment.NotificationsFragment;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.ui.fragment.ShareAboutFragment;
import com.example.javademogithubpractice.util.AppOpener;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        setStartDrawerEnable(true);
        setEndDrawerEnable(true);
    }


    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarTitle(getString(R.string.notifications));

        pagerAdapter.setPagerList(FragmentPagerModel.createNotificationsPagerList(getActivity(), getFragments()));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();


        updateStartDrawerContent(R.menu.activity_main_drawer);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        ImageView avatar = navViewStart.getHeaderView(0).findViewById(R.id.avatar);
        TextView name = navViewStart.getHeaderView(0).findViewById(R.id.name);
        TextView mail = navViewStart.getHeaderView(0).findViewById(R.id.mail);

        User loginUser = AppData.INSTANCE.getLoggedUser();
        GlideApp.with(getActivity())
                .load(loginUser.getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(avatar);

        name.setText(StringUtils.isBlank(loginUser.getName()) ? loginUser.getLogin() : loginUser.getName());
        String joinTime = getString(R.string.joined_at).concat(" ").concat(StringUtils.getDateStr(loginUser.getCreatedAt()));
        mail.setText(StringUtils.isBlank(loginUser.getBio()) ? joinTime : loginUser.getBio());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigationMyProfile:
                ProfileActivity.show(getActivity(), AppData.INSTANCE.getLoggedUser().getLogin(),
                        AppData.INSTANCE.getLoggedUser().getAvatarUrl());
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigationHome:
                NotificationsActivity.show(getActivity());
                tabLayout.setVisibility(View.VISIBLE);
                Menu menu1 = bottomNavigationView.getMenu();
                MenuItem menuItem1 = menu1.getItem(1);
                menuItem1.setChecked(true);
                return true;
            case R.id.navigationSearch:
                SearchActivity.show(getActivity());
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigationMenu:
                drawerLayout.openDrawer(GravityCompat.START);
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.getItem(0);
                menuItem.setChecked(true);
                tabLayout.setVisibility(View.GONE);
                return true;
        }
        return false;
    };

    @Override
    protected int getContentView() {
        return R.layout.helper_activity;
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


    //Method from MainActivity
    private final Map<Integer, String> TAG_MAP = new HashMap<>();
    @AutoAccess
    int selectedPage;

    private final List<Integer> FRAGMENT_NAV_ID_LIST = Arrays.asList(
            R.id.nav_repository, R.id.nav_stars,R.id.nav_global_news,R.id.nav_news,R.id.nav_share
    );

    private final List<String> FRAGMENT_TAG_LIST = Arrays.asList(
            RepositoriesFragment.RepositoriesType.OWNED.name(),
            RepositoriesFragment.RepositoriesType.STARRED.name(),
            ActivityFragment.ActivityType.PublicNews.name(),
            ActivityFragment.ActivityType.News.name(),
            ShareAboutFragment.Type.Share.name(),
            ShareAboutFragment.Type.About.name()

    );

    {
        for(int i = 0; i < FRAGMENT_NAV_ID_LIST.size(); i++){
            TAG_MAP.put(FRAGMENT_NAV_ID_LIST.get(i), FRAGMENT_TAG_LIST.get(i));
        }
    }

    private final List<Integer> FRAGMENT_TITLE_LIST = Arrays.asList(
            R.string.my_repos, R.string.starred_repos,R.string.public_news,R.string.my_news,R.string.share,R.string.about
    );


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_sort);
        menuItem.setVisible(selectedPage == R.id.nav_repository || selectedPage == R.id.nav_stars);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        super.onNavItemSelected(item, isStartDrawer);
        if (!isStartDrawer) {
            handlerEndDrawerClick(item);
            return;
        }
        int id = item.getItemId();
        updateFragmentByNavId(id);
    }

    @Override
    protected boolean isEndDrawerMultiSelect() {
        return true;
    }

    @Override
    protected int getEndDrawerToggleMenuItemId() {
        return R.id.nav_sort;
    }

    private void updateFragmentByNavId(int id){
        if(FRAGMENT_NAV_ID_LIST.contains(id)){
            updateTitle(id);
            loadFragment(id);
            updateFilter(id);
            return;
        }
        switch (id) {
            case R.id.nav_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void updateTitle(int itemId) {
        int titleId = FRAGMENT_TITLE_LIST.get(FRAGMENT_NAV_ID_LIST.indexOf(itemId));
        setToolbarTitle(getString(titleId));
    }


    private void handlerEndDrawerClick(MenuItem item) {
        Fragment fragment = getVisibleFragment();
        if (fragment != null && fragment instanceof RepositoriesFragment
                && (selectedPage == R.id.nav_repository || selectedPage == R.id.nav_stars)) {
            ((RepositoriesFragment) fragment).onDrawerSelected(navViewEnd, item);
        }
    }

    private void updateFilter(int itemId) {
        if (itemId == R.id.nav_repository) {
            updateEndDrawerContent(R.menu.menu_repositories_filter);
            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.OWNED);
        } else if (itemId == R.id.nav_stars) {
            updateEndDrawerContent(R.menu.menu_repositories_filter);
            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.STARRED);
        } else {
            removeEndDrawer();
        }
        invalidateOptionsMenu();
    }

    private void loadFragment(int itemId) {
        String fragmentTag = TAG_MAP.get(itemId);

        Fragment showFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        boolean isExist = true;
        if (showFragment == null) {
            isExist = false;
            showFragment = getFragment(itemId);
        }
        if (showFragment.isVisible()) {
            return;
        }

        Fragment visibleFragment = getVisibleFragment();
        if (isExist) {
            showAndHideFragment(showFragment, visibleFragment);
        } else {
            addAndHideFragment(showFragment, visibleFragment, fragmentTag);
        }
    }

    @NonNull
    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.nav_repository:
                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.OWNED,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_stars:
                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_global_news:
                return ActivityFragment.create(ActivityFragment.ActivityType.PublicNews,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_news:
                return ActivityFragment.create(ActivityFragment.ActivityType.News,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_share:
                return ShareAboutFragment.create(ShareAboutFragment.Type.Share);
            case R.id.nav_about:
                return ShareAboutFragment.create(ShareAboutFragment.Type.About);

        }
        return null;
    }


    private void showAndHideFragment(@NonNull Fragment showFragment, @Nullable Fragment hideFragment) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .hide(hideFragment)
                    .commit();
        }

    }


    private void addAndHideFragment(@NonNull Fragment showFragment,
                                    @Nullable Fragment hideFragment, @NonNull String addTag) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout, showFragment, addTag)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout, showFragment, addTag)
                    .hide(hideFragment)
                    .commit();
        }
    }



    private void logout() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.logout_warning)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.logout, (dialog, which) -> {
                    //mPresenter.logout();
                })
                .show();
    }
}
