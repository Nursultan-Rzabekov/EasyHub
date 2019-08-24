

package com.example.javademogithubpractice.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.common.GlideApp;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerActivityComponent;
import com.example.javademogithubpractice.inject.module.ActivityModule;
import com.example.javademogithubpractice.mvp.contract.IRepositoriesContract;
import com.example.javademogithubpractice.mvp.contract.IRepositoryContract;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.presenter.RepositoryPresenter;
import com.example.javademogithubpractice.ui.adapter.baseAdapter.FragmentPagerModel;
import com.example.javademogithubpractice.ui.base.PagerActivity;
import com.example.javademogithubpractice.ui.fragment.RepoInfoFragment;
import com.example.javademogithubpractice.util.AppOpener;
import com.example.javademogithubpractice.util.BundleHelper;
import com.example.javademogithubpractice.util.PrefUtils;
import com.example.javademogithubpractice.util.StringUtils;

import java.util.ArrayList;
import java.util.Locale;
import butterknife.BindView;

public class RepositoryActivity extends PagerActivity<RepositoryPresenter> implements IRepositoryContract.View {

    public static void show(@NonNull Context activity, @NonNull String owner,
                            @NonNull String repoName) {
        Intent intent = createIntent(activity, owner, repoName);
        activity.startActivity(intent);
    }

    public static void show(@NonNull Context activity, @NonNull Repository repository) {
        Intent intent = new Intent(activity, RepositoryActivity.class);
        intent.putExtra("repository", repository);
        activity.startActivity(intent);
    }

    public static Intent createIntent(@NonNull Context activity, @NonNull String owner,
                            @NonNull String repoName) {
        return new Intent(activity, RepositoryActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("owner", owner)
                        .put("repoName", repoName)
                        .build());
    }

    @BindView(R.id.user_avatar_bg) ImageView userImageViewBg;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.desc) TextView desc;
    @BindView(R.id.info) TextView info;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_repository;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if(getMPresenter().getRepository() != null){
//            getMenuInflater().inflate(R.menu.menu_repository, menu);
//            MenuItem starItem = menu.findItem(R.id.action_star);
//            MenuItem bookmark = menu.findItem(R.id.action_bookmark);
//            starItem.setTitle(getMPresenter().isStarred() ? R.string.unstar : R.string.star);
//            starItem.setIcon(getMPresenter().isStarred() ?
//                    R.drawable.ic_star_title : R.drawable.ic_un_star_title);
//            menu.findItem(R.id.action_watch).setTitle(getMPresenter().isWatched() ?
//                    R.string.unwatch : R.string.watch);
//            menu.findItem(R.id.action_fork).setTitle(getMPresenter().isFork() ?
//                    R.string.forked : R.string.fork);
//            menu.findItem(R.id.action_fork).setVisible(getMPresenter().isForkEnable());
//            bookmark.setTitle(getMPresenter().isBookmarked() ?
//                    getString(R.string.remove_bookmark) : getString(R.string.bookmark));
//            menu.findItem(R.id.action_wiki).setVisible(getMPresenter().getRepository().isHasWiki());
//        }
//        return true;
//    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTransparentStatusBar();
//        getToolbar().setTitleTextAppearance(getActivity(), R.style.Toolbar_TitleText);
//        getToolbar().setSubtitleTextAppearance(getActivity(), R.style.Toolbar_Subtitle);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getRepoName());
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_share:
//                AppOpener.shareText(getActivity(), mPresenter.getRepository().getHtmlUrl());
//                return true;
//            case R.id.action_open_in_browser:
//                AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getRepository().getHtmlUrl());
//                return true;
//            case R.id.action_copy_url:
//                AppUtils.copyToClipboard(getActivity(), mPresenter.getRepository().getHtmlUrl());
//                return true;
//            case R.id.action_wiki:
//                WikiActivity.show(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
//                        getMPresenter().getRepository().getName());
//                return true;
//            case R.id.action_download_source_zip:
//                AppOpener.startDownload(getActivity(), mPresenter.getZipSourceUrl(),
//                        getMPresenter().getZipSourceName());
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


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
    public void showRepo(Repository repo) {
        //        setToolbarTitle(repo.getFullName(), repo.getDefaultBranch());
        desc.setText(repo.getDescription());
        String language = StringUtils.isBlank(repo.getLanguage()) ?
                getString(R.string.unknown) : repo.getLanguage();
        info.setText(String.format(Locale.getDefault(), "Language %s, size %s",
                language, StringUtils.getSizeString(repo.getSize() * 1024)));

        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel.createRepoPagerList(getActivity(), repo, getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();

            GlideApp.with(getActivity())
                    .load(repo.getOwner().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(userImageViewBg);
        } else {
            //noticeRepositoryUpdated(repo);
        }
        invalidateOptionsMenu();

    }

    @Override
    public void showStarWishes() {

    }

    private void noticeRepositoryUpdated(Repository repository){
        for(FragmentPagerModel pagerModel : pagerAdapter.getPagerList()){
            if(pagerModel.getFragment() instanceof RepositoryListener){
                ((RepositoryListener)pagerModel.getFragment()).onRepositoryInfoUpdated(repository);
            }
        }
    }

    public interface RepositoryListener{
        void onRepositoryInfoUpdated(Repository repository);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public int getPagerSize() {
        return 4;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof RepoInfoFragment){
            return 0;
//        }else if(fragment instanceof RepoFilesFragment){
//            return 1;
//        }else if(fragment instanceof CommitsFragment){
//            return 2;
//        }else if(fragment instanceof ActivityFragment){
//            return 3;
        }else
            return -1;
    }

}
