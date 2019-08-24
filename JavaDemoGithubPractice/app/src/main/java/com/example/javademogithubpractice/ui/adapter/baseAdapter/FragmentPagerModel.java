

package com.example.javademogithubpractice.ui.adapter.baseAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.ui.fragment.RepoInfoFragment;
import com.example.javademogithubpractice.ui.fragment.baseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FragmentPagerModel {

    private String title;
    private BaseFragment fragment;

    public FragmentPagerModel(String title, BaseFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public static List<FragmentPagerModel> createRepoPagerList(@NonNull Context context
            , @NonNull final Repository repository, @NonNull ArrayList<Fragment> fragments) {

        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.info),
                        getFragment(fragments, 0, () -> RepoInfoFragment.create(repository)))
//                new FragmentPagerModel(context.getString(R.string.files),
//                        getFragment(fragments, 1, () -> RepoFilesFragment.create(repository))),
//                new FragmentPagerModel(context.getString(R.string.commits),
//                        getFragment(fragments, 2,
//                                () -> CommitsFragment.createForRepo(repository.getOwner().getLogin(),
//                                repository.getName(), repository.getDefaultBranch()))),
//                new FragmentPagerModel(context.getString(R.string.activity),
//                        getFragment(fragments, 3,
//                                () -> ActivityFragment.create(ActivityFragment.ActivityType.Repository,
//                                repository.getOwner().getLogin(), repository.getName())

        ));
    }

    private static List<FragmentPagerModel> setPagerFragmentFlag(List<FragmentPagerModel> list) {
        for (FragmentPagerModel model : list) {
            model.getFragment().setPagerFragment(true);
        }
        return list;
    }

    private static BaseFragment getFragment(ArrayList<Fragment> fragments
            , int position, FragmentCreator fragmentCreator){
        Fragment fragment  = fragments.get(position);
        if(fragment == null){
            fragment = fragmentCreator.createFragment();
        }else{
        }
        return (BaseFragment) fragment;
    }

    interface FragmentCreator<F extends Fragment>{
        F createFragment();
    }

}
