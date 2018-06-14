package com.example.jingjing.xin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jingjing.xin.Base.BaseFragment;

import java.util.List;

/**
 * Created by jingjing on 2018/4/24.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mlist;//Fragment集合

    public  FragmentAdapter(FragmentManager fm , List<Fragment> fragmentList){
        super(fm);
        this.mlist=fragmentList;
    }
    @Override
    public Fragment getItem(int position) {  //当前显示的是第几个
        return mlist.get(position);
    }

    @Override
    public int getCount() { //计算需要几个item
        return mlist !=null ? mlist.size():0 ;
    }
}
