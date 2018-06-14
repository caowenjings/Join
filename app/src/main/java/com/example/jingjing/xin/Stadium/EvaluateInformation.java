package com.example.jingjing.xin.Stadium;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jingjing.xin.Adapter.MyFragmentAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Fragment.EvaluateFragment;
import com.example.jingjing.xin.Fragment.NoevaluateFragment;
import com.example.jingjing.xin.Fragment.NouserorderFragment;
import com.example.jingjing.xin.Fragment.UserorderFragment;
import com.example.jingjing.xin.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class EvaluateInformation extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private RadioGroup rg_userorder;
    private RadioButton rb_userorder;
    private RadioButton rb_passuser;
    private ViewPager viewPager;

    private BaseFragment fragment1; //定义Fragment
    private BaseFragment fragment2;
    private List<BaseFragment> mFragment;
    private android.support.v4.app.FragmentManager fragmentManager;//定义FragmentManager

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.elavuate_information);

        initView();
        initDate();
        initViewPager();

    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("我的评价");

        rg_userorder = (RadioGroup)findViewById(R.id.rg_userorder);
        rb_userorder = (RadioButton) findViewById(R.id.rb_userorder);
        rb_passuser = (RadioButton) findViewById(R.id.rb_pastuser);
        viewPager = (ViewPager)findViewById(R.id.view_fragment);
    }

    private void initDate(){

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager.addOnPageChangeListener(this);//ViewPager页面切换监听
        rg_userorder.setOnCheckedChangeListener(this);//RadioGroup状态改变监听
    }

    private void initViewPager(){
        fragment1 = new EvaluateFragment();
        fragment2 = new NoevaluateFragment();

        mFragment = new ArrayList<BaseFragment>();
        mFragment.add(fragment1);
        mFragment.add(fragment2);

        fragmentManager = getSupportFragmentManager(); //获取FragmentManager对象
        MyFragmentAdapter adapter = new MyFragmentAdapter(fragmentManager,mFragment);//获取FragmentPageAdapter对象
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0); //设置ViewPager默认显示第一个View
        rb_userorder.setChecked(true);//设置第一个RadioButton为默认选中状态

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {//ViewPager切换Fragment时，RadioGroup做相应的监听
        switch (position){
            case 0:
                rg_userorder.check(R.id.rb_userorder);
                break;
            case 1:
                rg_userorder.check(R.id.rb_pastuser);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_userorder:
                viewPager.setCurrentItem(0,false);//显示第一个Fragment并关闭动画效果
                break;
            case R.id.rb_pastuser:
                viewPager.setCurrentItem(1,false);
                break;
        }
    }

}
