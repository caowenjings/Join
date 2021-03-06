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
import android.widget.LinearLayout;
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

public class UpdateProflie extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView iv_back;
    private RelativeLayout tv_back;

    private Button btn_select;
    private Button btn_upload;
    private ImageView iv_prodlie;
    private String imagepath ;
    private User user;
    private LinearLayout ll_progress;
    private TextView tv_progress;
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
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("上传图片");

        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_select = (Button)findViewById(R.id.btn_select);
        btn_select.setText("选择图片");
        iv_prodlie = (ImageView) findViewById(R.id.iv_prodlie);
        ll_progress = (LinearLayout)findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.GONE);//隐藏
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        iv_back.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                if(imagepath==null){
                    Toast.makeText(UpdateProflie.this,"请先选择图片",Toast.LENGTH_SHORT).show();
                }else {
                    multiFileUpload();
                }
                break;
            case R.id.btn_select:
                /*
                 Intent intent1 = new Intent(Intent.ACTION_PICK,
                   android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(intent1, 1);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);//启动
                break;
            case R.id.iv_title:
                Intent intent1 = new Intent(getApplicationContext(),UserInformationActivity.class);
                startActivity(intent1);
                finish();
                break;
                default:
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//（获得结果）
        if(data==null){
            Toast.makeText(UpdateProflie.this,"没有选择图片",Toast.LENGTH_SHORT).show();
        }else{
            if(requestCode==1)//请求码
            {
                Uri uri = data.getData(); //获得图片的uri
                ContentResolver cr = this.getContentResolver();
                Bitmap bitmap;
                try
                {
                    Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();//将光标移至开头，防止引起越界
                    imagepath = cursor.getString(column_index);//最后根据索引值获取图片路径
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));//从SD卡中找头像，转换成Bitmap
                    iv_prodlie.setImageBitmap(bitmap);

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
        list.add(new KeyValue("file", new File(imagepath)));//文件流数据
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
               ll_progress.setVisibility(View.VISIBLE);
//                progressBar.setMax((int)total);
                progressBar.setProgress((int)current);
            }
            @Override
            public void onSuccess(String result) {
                ll_progress.setVisibility(View.GONE);
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
