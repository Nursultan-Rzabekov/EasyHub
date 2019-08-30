

package com.example.javademogithubpractice.ui.adapter.baseAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.R;
import com.example.javademogithubpractice.mvp.model.Repository;
import com.example.javademogithubpractice.mvp.model.SearchModel;
import com.example.javademogithubpractice.mvp.model.User;
import com.example.javademogithubpractice.ui.fragment.ProfileInfoFragment;
import com.example.javademogithubpractice.ui.fragment.RepoInfoFragment;
import com.example.javademogithubpractice.ui.fragment.RepositoriesFragment;
import com.example.javademogithubpractice.ui.fragment.UserListFragment;
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
        ));
    }

    private static List<FragmentPagerModel> setPagerFragmentFlag(List<FragmentPagerModel> list) {
        for (FragmentPagerModel model : list) {
            model.getFragment().setPagerFragment(true);
        }
        return list;
    }


    public static List<FragmentPagerModel> createProfilePagerList(Context context, final User user
            , @NonNull ArrayList<Fragment> fragments) {

        List<FragmentPagerModel> list = new ArrayList<>();
        list.add(new FragmentPagerModel(context.getString(R.string.info), getFragment(fragments, 0, () -> ProfileInfoFragment.create(user))));

//        list.add(new FragmentPagerModel(context.getString(R.string.activity),
//                getFragment(fragments, 1, () -> ActivityFragment.create(ActivityFragment.ActivityType.User, user.getLogin(), null))));

        if (user.isUser()) {
            list.add(new FragmentPagerModel(context.getString(R.string.starred),
                    getFragment(fragments, 2, () -> RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED, user.getLogin()))));
        }
        return setPagerFragmentFlag(list);
    }


    public static List<FragmentPagerModel> createSearchPagerList(@NonNull Context context
            , @NonNull final ArrayList<SearchModel> searchModels, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.repositories),
                        getFragment(fragments, 0, () -> RepositoriesFragment.createForSearch(searchModels.get(0)))),
                new FragmentPagerModel(context.getString(R.string.users),
                        getFragment(fragments, 1, () -> UserListFragment.createForSearch(searchModels.get(1))))
        ));
    }

    private static BaseFragment getFragment(ArrayList<Fragment> fragments, int position, FragmentCreator fragmentCreator){
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
