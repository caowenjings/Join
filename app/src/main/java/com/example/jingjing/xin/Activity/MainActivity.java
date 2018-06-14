package com.example.jingjing.xin.Activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private ViewPager viewPager;
    private List<Fragment> fragmentLists;

    private LinearLayout linearLayout;
    private BottomNavigationBar bottomNavigationBar;
    private BottomNavigationItem bottomNavigationItem;
    private BookingFragment bookingFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final ActionBar actionBar  = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_main);
            viewPager = (ViewPager) findViewById(R.id.view_fragment);
            bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomnavigation);
            linearLayout = (LinearLayout) findViewById(R.id.fragment);
            //添加碎片
            List<Fragment> fragmentLists = new ArrayList<>();
            fragmentLists.add(new BookingFragment());
            fragmentLists.add(new FindFragment());
            fragmentLists.add(new MyFragment());
            //关联适配器

            FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentLists);
            viewPager.setAdapter(fragmentAdapter);
            viewPager.setOffscreenPageLimit(2);
            setBottomNavigationItem(bottomNavigationBar, 6, 60, 50);
            setdefaultFragment();

        }

    //默认
    public void setdefaultFragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        BookingFragment bookingFragment=new BookingFragment();
        transaction.replace(R.id.fragment,bookingFragment);
        transaction.commit();
    }
    public void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar ,int space,int imageLen,int textsize){
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_DEFAULT)
                .setInActiveColor(R.color.colorred)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);


//给Fragment填充内容
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.booking,"预定")).setActiveColor(R.color.colorblue)
                .addItem(new BottomNavigationItem(R.drawable.finding,"发现")).setActiveColor(R.color.colorblue)
                .addItem(new BottomNavigationItem(R.drawable.my,"我的")).setActiveColor(R.color.colorblue)
                .initialise();
        //添加监听事件
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //开启一个事物
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction transaction=fm.beginTransaction();
                //添加碎片在里面
                switch (position){
                    case 0:
                        if(bookingFragment == null){
                            bookingFragment=new BookingFragment();
                        }
                        transaction.replace(R.id.fragment,bookingFragment);
                        getSupportActionBar().setTitle("预定");
                        break;
                    case  1:
                        if(findFragment == null){
                            findFragment=new FindFragment();
                        }
                        transaction.replace(R.id.fragment,findFragment);
                        getSupportActionBar().setTitle("发现");
                        break;
                    case 2:
                        if(myFragment == null){
                            myFragment= new MyFragment();
                        }
                        transaction.replace(R.id.fragment,myFragment);
                        getSupportActionBar().setTitle("我的");
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}