

package com.example.javademogithubpractice.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.inject.component.AppComponent;
import com.example.javademogithubpractice.inject.component.DaggerFragmentComponent;
import com.example.javademogithubpractice.inject.module.FragmentModule;
import com.example.javademogithubpractice.mvp.contract.IRepoInfoContract;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.presenter.RepoInfoPresenter;
import com.example.javademogithubpractice.ui.activity.RepositoryActivity;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;
import com.example.javademogithubpractice.util.BundleHelper;
import com.example.javademogithubpractice.util.StringUtils;

import butterknife.BindView;


public class RepoInfoFragment extends BaseFragment<RepoInfoPresenter> implements IRepoInfoContract.View,
        RepositoryActivity.RepositoryListener {

    public static RepoInfoFragment create(Repository repository) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setArguments(BundleHelper.builder().put("repository", repository).build());
        return fragment;
    }

    @BindView(R.id.scroll_view) NestedScrollView nestedScrollView;
    @BindView(R.id.repo_title_text) TextView repoTitleText;
    @BindView(R.id.fork_info_text) TextView forkInfoText;
    @BindView(R.id.repo_created_info_text) TextView repoCreatedInfoText;
    @BindView(R.id.issues_num_text) TextView issuesNumText;
    @BindView(R.id.issues_lay) View issueLay;
    @BindView(R.id.stargazers_num_text) TextView stargazersNumText;
    @BindView(R.id.forks_num_text) TextView forksNumText;
    @BindView(R.id.watchers_num_text) TextView watchersNumText;
    @BindView(R.id.readme_title) TextView readmeTitle;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repo_info;
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
    }

    @Override
    public void showRepoInfo(Repository repository) {
        issueLay.setVisibility(repository.isHasIssues() ? View.VISIBLE :View.GONE);
        issuesNumText.setText(String.valueOf(repository.getOpenIssuesCount()));
        stargazersNumText.setText(String.valueOf(repository.getStargazersCount()));
        forksNumText.setText(String.valueOf(repository.getForksCount()));
        watchersNumText.setText(String.valueOf(repository.getSubscribersCount()));

        String createStr = (repository.isFork() ? getString(R.string.forked_at)
                : getString(R.string.created_at)) + " " + StringUtils.getDateStr(repository.getCreatedAt());
        if(repository.getPushedAt() != null){
            String updateStr = getString(R.string.latest_commit) + " ";
            repoCreatedInfoText.setText(String.format("%s, %s", createStr, updateStr));
        } else {
            repoCreatedInfoText.setText(createStr);
        }

        if (repository.isFork() && repository.getParent() != null) {
            forkInfoText.setVisibility(View.VISIBLE);
            forkInfoText.setText(getString(R.string.forked_from)
                    .concat(" ").concat(repository.getParent().getFullName()));
        } else {
            forkInfoText.setVisibility(View.GONE);
        }

        String fullName = repository.getFullName();
        SpannableStringBuilder spannable = new SpannableStringBuilder(fullName);
//        spannable.setSpan(new ForegroundColorSpan(ViewUtils.getAccentColor(getContext())),
//                0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                ProfileActivity.show(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
//                        mPresenter.getRepository().getOwner().getAvatarUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {

            }
        }, 0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        repoTitleText.setMovementMethod(LinkMovementMethod.getInstance());
        repoTitleText.setText(spannable);
    }

//    @OnClick({R.id.issues_lay, R.id.stargazers_lay, R.id.froks_lay, R.id.watchers_lay,
//            R.id.fork_info_text})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.issues_lay:
//                IssuesActivity.showForRepo(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
//                        mPresenter.getRepository().getName());
////                showInfoToast(getString(R.string.developing));
//                break;
//            case R.id.stargazers_lay:
//                if(mPresenter.getRepository().getStargazersCount() == 0) return;
//                UserListActivity.show(getActivity(), UserListFragment.UserListType.STARGAZERS,
//                        mPresenter.getRepository().getOwner().getLogin(),
//                        mPresenter.getRepository().getName());
//                break;
//            case R.id.froks_lay:
//                if(mPresenter.getRepository().getForksCount() == 0) return;
//                RepoListActivity.showForks(getContext(),
//                        mPresenter.getRepository().getOwner().getLogin(),
//                        mPresenter.getRepository().getName());
//                break;
//            case R.id.watchers_lay:
//                if(mPresenter.getRepository().getSubscribersCount() == 0) return;
//                UserListActivity.show(getActivity(), UserListFragment.UserListType.WATCHERS,
//                        mPresenter.getRepository().getOwner().getLogin(),
//                        mPresenter.getRepository().getName());
//                break;
//            case R.id.fork_info_text:
//                RepositoryActivity.show(getActivity(), mPresenter.getRepository().getParent());
//                break;
//        }
//    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onRepositoryInfoUpdated(Repository repository) {
        if(mPresenter == null){
            getArguments().putParcelable("repository", repository);
        }else{
            mPresenter.setRepository(repository);
            mPresenter.setLoaded(false);
            mPresenter.prepareLoadData();
        }
    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        nestedScrollView.scrollTo(0, 0);
    }
}
