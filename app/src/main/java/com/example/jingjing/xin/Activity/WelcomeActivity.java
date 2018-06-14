package com.example.jingjing.xin.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
       welcome=(TextView)findViewById(R.id.text_welcome);
        imageView=(ImageView)findViewById(R.id.image_welcome) ;
        yanchi();
    }
   private void yanchi(){
       Timer timer=new Timer();
       TimerTask task=new TimerTask() {//定时器延期执行
           @Override
           public void run() {
               Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
               startActivity(intent);
               finish();
           }
       };
       timer.schedule(task,3000);

   }
}
