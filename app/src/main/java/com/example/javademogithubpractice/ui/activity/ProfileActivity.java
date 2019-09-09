

package com.example.javademogithubpractice.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerActivityComponent;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.mvp.contract.IProfileContract;
import com.example.javademogithubpractice.mvp.model.RepositoriesFilter;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.mvp.presenter.ProfilePresenter;
import com.example.javademogithubpractice.ui.activity.base.BottomNavigationBehavior;
import com.example.javademogithubpractice.ui.activity.base.PagerActivity;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentPagerModel;
import com.example.javademogithubpractice.ui.fragment.ActivityFragment;
import com.example.javademogithubpractice.ui.fragment.ProfileInfoFragment;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.ui.fragment.ShareAboutFragment;
import com.example.javademogithubpractice.util.AppOpener;
import com.example.javademogithubpractice.util.BundleHelper;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class ProfileActivity extends PagerActivity<ProfilePresenter> implements IProfileContract.View {

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    public static void show(@NonNull Activity activity, @NonNull String loginId) {
        show(activity, loginId, null);
    }

    public static void show(@NonNull Activity activity,
                            @NonNull String loginId, @Nullable String userAvatar) {
        show(activity, null, loginId, userAvatar);
    }

    public static void show(@NonNull Activity activity, @Nullable View userAvatarView,
                            @NonNull String loginId, @Nullable String userAvatar) {
        Intent intent = createIntent(activity, loginId, userAvatar);
        activity.startActivity(intent);
    }

    public static Intent createIntent(@NonNull Activity activity, @NonNull String loginId,
                                      @Nullable String userAvatar) {
        return new Intent(activity, ProfileActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("loginId", loginId)
                        .put("userAvatar", userAvatar)
                        .build());
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @BindView(R.id.user_avatar_bg) ImageView userImageViewBg;
    @BindView(R.id.loader) ProgressBar loader;

    @Override
    protected void initActivity() {
        super.initActivity();
        setStartDrawerEnable(true);
        setEndDrawerEnable(true);
    }

    @Override
    public void restartApp() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_profile_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTransparentStatusBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setUserAvatar();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
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

    @Override
    public void showProfileInfo(User user) {
        invalidateOptionsMenu();
        setUserAvatar();
        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel.createProfilePagerList(getActivity(), user, getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();
        } else {
            notifyUserInfoUpdated(user);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigationMyProfile:
                ProfileActivity.show(getActivity(), AppData.INSTANCE.getLoggedUser().getLogin(),
                        AppData.INSTANCE.getLoggedUser().getAvatarUrl());
                return true;
            case R.id.navigationHome:
                NotificationsActivity.show(getActivity());
                return true;
            case  R.id.navigationSearch:
                SearchActivity.show(getActivity());
                return true;
            case  R.id.navigationMenu:
                drawerLayout.openDrawer(GravityCompat.START);
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.getItem(0);
                menuItem.setChecked(true);
                return true;
        }
        return false;
    };


    private void notifyUserInfoUpdated(User user){
        for(Fragment fragment : getFragments()){
            if(fragment != null && fragment instanceof ProfileInfoFragment){
                //Toast.makeText(getActivity(),"Yes",Toast.LENGTH_SHORT).show();
                ((ProfileInfoFragment)fragment).updateProfileInfo(user);
            }
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loader.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        supportFinishAfterTransition();
    }

    private void setUserAvatar() {
        GlideApp.with(getActivity())
                .load(mPresenter.getUserAvatar())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userImageViewBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getPagerSize() {
        return 3;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if (fragment instanceof ProfileInfoFragment) {
            return 0;
        } else if (fragment instanceof RepositoriesFragment) {
            return 1;
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
                    .add(R.id.frame_layout_profile_to, showFragment, addTag)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_profile_to, showFragment, addTag)
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
