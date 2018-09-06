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

    //判断是否要生成新子视图，相等就不产生了
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /*为给定的位置创建相应的View。创建View之后,需要在该方法中自行添加到container中。
     * return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPage上
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
