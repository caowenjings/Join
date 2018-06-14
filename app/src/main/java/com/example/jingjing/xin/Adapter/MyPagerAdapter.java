package com.example.jingjing.xin.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jingjing on 2018/5/3.
 */
//viewpager的自定义适配器
public class MyPagerAdapter extends PagerAdapter {
    private List<View> mviewList;

    public MyPagerAdapter(List<View>  mViewList) {
        this.mviewList =   mViewList;
    }

    @Override
    public int getCount() {
        return mviewList != null ? mviewList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 将当前的View添加到ViewGroup容器中
     * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPage上
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mviewList.get(position));
        return mviewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
