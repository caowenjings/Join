package com.example.jingjing.xin.User;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jingjing.xin.R;

/**
 * Created by jingjing on 2018/5/17.
 */

public class About_join extends AppCompatActivity {


    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;
    private TextView tv_about;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.setting_about_join);

        initView();
        initDate();
    }

    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);

        tv_title.setText("关于Join");

        tv_about=(TextView)findViewById(R.id.tv_about);
    }

    private void initDate(){
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_about.setText("Join是一个运动场地的预约系统，是个全部信息在线开放式的互动平台。");
    }
}
