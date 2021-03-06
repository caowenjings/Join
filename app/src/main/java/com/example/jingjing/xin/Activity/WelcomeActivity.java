package com.example.jingjing.xin.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingjing.xin.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jingjing on 2018/4/2.
 */

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcome;
    private ImageView imageView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();//隐藏标题栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.welcome);

        welcome=(TextView)findViewById(R.id.text_welcome);
        imageView=(ImageView)findViewById(R.id.image_welcome) ;
        yanchi();
    }
   private void yanchi(){
       TimerTask task=new TimerTask() {//定时器延期执行
           @Override
           public void run() {
               Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
               startActivity(intent);
               finish();
           }
       };
       Timer timer=new Timer();
       timer.schedule(task,3000);
   }
}
