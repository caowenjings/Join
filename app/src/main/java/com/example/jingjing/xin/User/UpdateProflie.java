package com.example.jingjing.xin.User;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.request.HttpRequest;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

import static com.example.jingjing.xin.constant.Conatant.URL_UPDATEPROFILE;

public class UpdateProflie extends AppCompatActivity  {

    private Button btn;
    private Button btn_xuanze;
    private String path ;
    private User user;
    private TextView tv_progress;
    private ImageView icon_back;
    private ImageView icon_choice;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏actionBar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.update_prodlie);

        initView();
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        btn = (Button) findViewById(R.id.btn_upload);
        btn_xuanze = (Button)findViewById(R.id.btn_select);
       // tv_progress = (TextView) findViewById(R.id.tv_progress);
        icon_back = (ImageView) findViewById(R.id.iv_title);
       // tv_progress.setVisibility(View.GONE);
        icon_choice = (ImageView) findViewById(R.id.iv_prodlie);
       // progressBar = (ProgressBar) findViewById(R.id.progress);

    }

    private void initData() {
       // progressBar.setVisibility(View.GONE);
        user = (User) getIntent().getSerializableExtra("user");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path==null){
                    Toast.makeText(UpdateProflie.this,"请先选择图片",Toast.LENGTH_SHORT).show();
                }else {
                    multiFileUpload();
                }
            }
        });
        btn_xuanze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 Intent intent1 = new Intent(Intent.ACTION_PICK,
                   android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(intent1, 1);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data==null){
            Toast.makeText(UpdateProflie.this,"没有选择图片",Toast.LENGTH_SHORT).show();
        }else{
            if(requestCode==1)
            {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                Bitmap bitmap;
                try
                { bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                    icon_choice.setImageBitmap(bitmap);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("BAD");
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void multiFileUpload()
    {
        String mBaseUrl = URL_UPDATEPROFILE;
        RequestParams params = new RequestParams(mBaseUrl);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", new File(path)));//文件流数据
        list.add(new KeyValue("userId",user.getUserId()));
        org.xutils.http.body.MultipartBody body = new org.xutils.http.body.MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //tv_progress.setVisibility(View.VISIBLE);
               // progressBar.setVisibility(View.VISIBLE);
                //progressBar.setMax((int)total);
               // progressBar.setProgress((int)current);

            }

            @Override
            public void onSuccess(String result) {
             //   progressBar.setVisibility(View.GONE);
              //  tv_progress.setVisibility(View.GONE);
                Toast.makeText(UpdateProflie.this, "上传成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),UserInformationActivity.class);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

}

    /*
    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private Button btn_select;
    private Button btn_upload;
    private ImageView iv_peodlie;
    private String imagePath;
    private String path;
    private User user;

    // private ProgressBar progressBar;
    // private TextView tv_progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.update_prodlie);

        initView();
        initDate();
    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("上传图片");

        btn_select = (Button) findViewById(R.id.btn_select);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        iv_peodlie = (ImageView) findViewById(R.id.iv_prodlie);

        //  progressBar = (ProgressBar) findViewById(R.id.progress);
        // tv_progress.setVisibility(View.GONE);
        // tv_progress = (TextView) findViewById(R.id.tv_progress);
    }

    private void initDate() {
        user = (User) getIntent().getSerializableExtra("user");

        btn_upload.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        iv_peodlie.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                Intent intent = new Intent(UpdateProflie.this, UserInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_select://在这里跳转到手机系统相册里面

              //  Intent intent1 = new Intent(Intent.ACTION_PICK,
                    //    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               // startActivityForResult(intent1, IMAGE);

                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent1, 1);
                break;
            case R.id.btn_upload:
                if (path == null) {
                    Toast.makeText(UpdateProflie.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                } else {
                    ProflieUpload();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data==null){
            Toast.makeText(UpdateProflie.this,"没有选择图片",Toast.LENGTH_SHORT).show();
        }else{
            if(requestCode==1)
            {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                Bitmap bitmap;
                try
                { bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                    iv_peodlie.setImageBitmap(bitmap);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("BAD");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void ProflieUpload(){
        //String mBaseUrl = URL_UPDATEPROFILE;
        RequestParams params = new RequestParams(URL_UPDATEPROFILE);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", new File(path)));//文件流数据
        list.add(new KeyValue("userId",user.getUserId()));
        org.xutils.http.body.MultipartBody body = new org.xutils.http.body.MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
             //   tv_progress.setVisibility(View.VISIBLE);
              //  progressBar.setVisibility(View.VISIBLE);
             //   progressBar.setMax((int)total);
             //   progressBar.setProgress((int)current);

            }

            @Override
            public void onSuccess(String result) {
               // progressBar.setVisibility(View.GONE);
              //  tv_progress.setVisibility(View.GONE);
                Toast.makeText(UpdateProflie.this, "上传成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            iv_peodlie.setImageBitmap(bm);
            c.close();
        }
    }*/
