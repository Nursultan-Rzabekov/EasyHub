

package com.example.javademogithubpractice.ui.adapter.baseAdapter;

import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import javax.inject.Inject;


public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentPagerModel> mPagerList;

    private Fragment curFragment;

    @Inject
    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPagerList(List<FragmentPagerModel> pagerList) {
        mPagerList = pagerList;
    }

    @Override
    public Fragment getItem(int position) {
        return mPagerList.get(position).getFragment();
    }


    @Override
    public int getCount() {
        return mPagerList == null ? 0 : mPagerList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPagerList.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(curFragment != null && curFragment.equals(object)){
            curFragment = null;
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        curFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurFragment() {
        return curFragment;
    }

    public List<FragmentPagerModel> getPagerList() {
        return mPagerList;
    }

}
