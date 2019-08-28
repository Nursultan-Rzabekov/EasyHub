

package com.example.javademogithubpractice.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.AppEventBus;
import com.example.javademogithubpractice.common.Event;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerActivityComponent;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.mvp.contract.ISearchContract;
import com.example.javademogithubpractice.mvp.model.SearchModel;
import com.example.javademogithubpractice.mvp.presenter.SearchPresenter;
import com.example.javademogithubpractice.ui.activity.base.PagerActivity;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentPagerModel;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.util.StringUtils;
import com.example.javademogithubpractice.util.ViewUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import java.util.ArrayList;

public class SearchActivity extends PagerActivity<SearchPresenter> implements ISearchContract.View,
        MenuItemCompat.OnActionExpandListener,
        SearchView.OnQueryTextListener {

    public static void show(@NonNull Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }


    @AutoAccess boolean isInputMode = true;
    @AutoAccess String[] sortInfos;

    @Override
    protected void initActivity() {
        super.initActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setBackgroundColor(getResources().getColor(R.color.material_red_200));
        searchView.setQuery(mPresenter.getSearchModels().get(0).getQuery(), false);
        if (isInputMode) {
            MenuItemCompat.expandActionView(searchItem);
        } else {
            MenuItemCompat.collapseActionView(searchItem);
        }
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        AutoCompleteTextView autoCompleteTextView = searchView.findViewById(R.id.search_src_text);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, R.layout.layout_item_simple_list, mPresenter.getSearchRecordList()));
        autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(ViewUtils.getWindowBackground(getActivity())));
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            onQueryTextSubmit(parent.getAdapter().getItem(position).toString());
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isInputMode) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            searchView.setQuery(mPresenter.getSearchModels().get(0).getQuery(), false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        isInputMode = true;
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        isInputMode = false;
        invalidateOptionsMenu();
        return true;
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.search));
        if(sortInfos == null)
        sortInfos = new String[]{
                getString(R.string.best_match), getString(R.string.best_match)
        };
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (StringUtils.isBlank(query)) {
            showWarningToast(getString(R.string.invalid_query));
            return true;
        }
        isInputMode = false;
        invalidateOptionsMenu();
        search(query);
        setSubTitle(viewPager.getCurrentItem());
        mPresenter.addSearchRecord(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void search(String query) {
        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel.createSearchPagerList(getActivity(), mPresenter.getQueryModels(query), getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();
        } else {
            for (SearchModel searchModel : mPresenter.getQueryModels(query)) {
                postSearchEvent(searchModel);
            }
        }
    }

    private void postSearchEvent(SearchModel searchModel) {
        AppEventBus.INSTANCE.getEventBus().post(new Event.SearchEvent(searchModel));
    }

    private void setSubTitle(int page) {
        setToolbarSubTitle(mPresenter.getSearchModels().get(0).getQuery() + "/" + sortInfos[page]);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        setSubTitle(position);
    }

    @Override
    public int getPagerSize() {
        return 2;
    }

    @Override
    public void showSearches(ArrayList<SearchModel> searchModels) {
        search(searchModels.get(0).getQuery());
        setSubTitle(0);
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof RepositoriesFragment){
            return 0;
//        }else if(fragment instanceof UserListFragment){
//            return 1;
        }else
            return -1;
    }


}
