package com.example.jingjing.xin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jingjing.xin.Base.BaseFragment;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mlist;//Fragment集合

    public MyFragmentAdapter(FragmentManager fm,List<BaseFragment> fragmentList) {
        super(fm);
        this.mlist = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist !=null ? mlist.size():0 ;
    }
}
