package com.example.javademogithubpractice.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.mvp.contract.IBaseContract;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;


public abstract class BaseDrawerActivity<P extends IBaseContract.Presenter> extends BaseActivity<P> implements IBaseContract.View {

    @BindView(R.id.nav_view) @Nullable protected NavigationView navViewStart;
    @BindView(R.id.nav_view_end) @Nullable protected NavigationView navViewEnd;
    @BindView(R.id.drawer_layout) @Nullable protected DrawerLayout drawerLayout;

    private boolean startDrawerEnable = false;
    private boolean endDrawerEnable = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (drawerLayout == null) return;
        initStartDrawerView();
        initEndDrawerView();
    }

    private void initEndDrawerView() {
        if (navViewEnd == null) return;
        if (endDrawerEnable) {
            navViewEnd.setNavigationItemSelectedListener(item -> BaseDrawerActivity.this.onNavigationItemSelected(item, false));
        } else {
            drawerLayout.removeView(navViewEnd);
        }
    }

    private void initStartDrawerView() {
        if (navViewStart == null) return;
        if (startDrawerEnable) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                    null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.syncState();
            navViewStart.setNavigationItemSelectedListener(item -> BaseDrawerActivity.this.onNavigationItemSelected(item, true));
        } else {
            drawerLayout.removeView(navViewStart);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && (drawerLayout.isDrawerOpen(GravityCompat.START)
                || drawerLayout.isDrawerOpen(GravityCompat.END))) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout != null && item.getItemId() == getEndDrawerToggleMenuItemId()) {
            openDrawer(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onNavigationItemSelected(@NonNull final MenuItem item, final boolean isStartDrawer) {
        NavigationView navView = getNavigationView(isStartDrawer);
        boolean isMultiSelect = isStartDrawer ? isStartDrawerMultiSelect() : isEndDrawerMultiSelect();
        if (navView == null) return true;
        closeDrawer();
        new Handler().postDelayed(() -> onNavItemSelected(item, isStartDrawer), 250);
        return !isMultiSelect;
    }

    protected final void openDrawer(boolean isStartDrawer) {
        if (drawerLayout != null)
            drawerLayout.openDrawer(isStartDrawer ? GravityCompat.START : GravityCompat.END);
    }

    protected final void closeDrawer() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END);
        }

    }

    private NavigationView getNavigationView(boolean isStartDrawer) {
        return isStartDrawer ? navViewStart : navViewEnd;
    }

    protected void removeStartDrawer() {
        removeDrawer(navViewStart);
    }
    protected void removeEndDrawer() {
        removeDrawer(navViewEnd);
    }

    protected void updateStartDrawerContent(int menuId) {
        updateDrawerContent(navViewStart, menuId);
    }

    protected void updateEndDrawerContent(int menuId) {
        updateDrawerContent(navViewEnd, menuId);
    }

    private void removeDrawer(NavigationView navView) {
        if (drawerLayout != null && navView != null) drawerLayout.removeView(navView);
    }

    private void updateDrawerContent(NavigationView navView, int menuId) {
        if (drawerLayout != null && navView != null) {
            navView.getMenu().clear();
            navView.inflateMenu(menuId);
            if (drawerLayout.indexOfChild(navView) == -1) drawerLayout.addView(navView);
        }
    }

    public void setStartDrawerEnable(boolean startDrawerEnable) {
        this.startDrawerEnable = startDrawerEnable;
    }

    public void setEndDrawerEnable(boolean endDrawerEnable) {
        this.endDrawerEnable = endDrawerEnable;
    }

    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {

    }

    protected boolean isStartDrawerMultiSelect() {
        return false;
    }

    protected boolean isEndDrawerMultiSelect() {
        return false;
    }

    protected int getEndDrawerToggleMenuItemId() {
        return -1;
    }

}
