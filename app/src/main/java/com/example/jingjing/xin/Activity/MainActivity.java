package com.example.jingjing.xin.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.jingjing.xin.Adapter.FragmentAdapter;
import com.example.jingjing.xin.Fragment.BookingFragment;
import com.example.jingjing.xin.Fragment.FindFragment;
import com.example.jingjing.xin.Fragment.MyFragment;
import com.example.jingjing.xin.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private LinearLayout linearLayout;
    private BottomNavigationBar bottomNavigationBar;
    private BottomNavigationItem bottomNavigationItem;
    private BookingFragment bookingFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private Fragment mFragment;//当前显示的Fragment


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.activity_main);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomnavigation);
        linearLayout = (LinearLayout) findViewById(R.id.fragment);
        initBottomNavigationItem();
        initFragment();


    }

    private void initFragment() {
        bookingFragment = new BookingFragment();
        findFragment = new FindFragment();
        myFragment = new MyFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, bookingFragment)
                .commit();
        mFragment = bookingFragment;
    }


    public void initBottomNavigationItem() {
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_DEFAULT)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
                .setActiveColor(R.color.colorblue) //设置选中的颜色
                .setInActiveColor(R.color.colorhui);//未选中颜色;
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.booking, "预定"))
                .addItem(new BottomNavigationItem(R.drawable.finding, "发现"))
                .addItem(new BottomNavigationItem(R.drawable.my, "我的"))
                .initialise();
        //添加监听事件
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中到选中
                switch (position) {
                    case 0:
                        switchFragment(bookingFragment);
                        break;
                    case 1:
                        switchFragment(findFragment);
                        break;
                    case 2:
                        switchFragment(myFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {//选中到未选中
            }

            @Override
            public void onTabReselected(int position) {//选中到选中
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.fragment, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }
}
