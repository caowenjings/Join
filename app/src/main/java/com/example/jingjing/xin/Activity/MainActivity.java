package com.example.jingjing.xin.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
        private ViewPager viewPager;
        private List<Fragment> fragmentList;

        private LinearLayout linearLayout;
        private BottomNavigationBar bottomNavigationBar;
        private BottomNavigationItem bottomNavigationItem;
        private BookingFragment bookingFragment;
        private FindFragment findFragment;
        private MyFragment myFragment;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            android.support.v7.app.ActionBar actionBar =getSupportActionBar();
            actionBar.hide();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.BLACK);//设置颜色
            setContentView(R.layout.activity_main);

            viewPager = (ViewPager) findViewById(R.id.view_fragment);
            bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomnavigation);
            linearLayout = (LinearLayout) findViewById(R.id.fragment);
            initBottomNavigationItem();
            initFragment();

        }

      public void initFragment(){
          //数据源添加碎片
          fragmentList = new ArrayList<>();
          fragmentList.add(new BookingFragment());
          fragmentList.add(new FindFragment());
          fragmentList.add(new MyFragment());
          //关联适配器
          FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
          viewPager.setAdapter(fragmentAdapter);
          viewPager.setCurrentItem(0);//用来制定初始化的页面
          viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {//点击上面的tab直接setCurrentItem）
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              }
              @Override
              public void onPageSelected(int position) {//代表哪个页面被选中
                  bottomNavigationBar.selectTab(position);
              }
              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });
      }

        public void initBottomNavigationItem(){
            bottomNavigationBar
                    .setMode(BottomNavigationBar.MODE_DEFAULT)
                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
                    .setActiveColor(R.color.colorblue) //设置选中的颜色
                    .setInActiveColor(R.color.colorhui);//未选中颜色;
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.booking,"预定"))
                    .addItem(new BottomNavigationItem(R.drawable.finding,"发现"))
                    .addItem(new BottomNavigationItem(R.drawable.my,"我的"))
                    .setFirstSelectedPosition(0)//默认位置是0
                    .initialise();
            //添加监听事件
            bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position) {//未选中到选中
                    viewPager.setCurrentItem(position);
                }
                @Override
                public void onTabUnselected(int position) {//选中到未选中
                }
                @Override
                public void onTabReselected(int position) {//选中到选中

                }
            });
        }
    }
