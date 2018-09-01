package com.example.jingjing.xin.User;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.jingjing.xin.constant.Conatant.URL_UPDATEPROFILE;

public class UpdateProflieone extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_back;

    private Button btn_photo;
    private Button btn_upload;
    private ImageView iv_prodlie;
    private String imagepath;
    private User user;
    private LinearLayout ll_progress;
    private TextView tv_progress;
    private ProgressBar progressBar;

    private static String photoPath = "/sdcard/AnBo/";
    private static String photoName = photoPath + "laolisb.jpg";
    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));//第二个参数是临时文件，在后面将会被修改
    private String uploadFile = "/sdcard/AnBo/laolisb.jpg";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_title);
        tv_title.setText("上传图片");

        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_photo = (Button) findViewById(R.id.btn_select);
        btn_photo.setText("拍照");
        iv_prodlie = (ImageView) findViewById(R.id.iv_prodlie);
        ll_progress = (LinearLayout) findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.GONE);//隐藏
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        iv_back.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                multiFileUpload();
                break;
            case R.id.btn_select:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(photoPath);
                if (!file.exists()) { // 检查图片存放的文件夹是否存在
                    file.mkdir(); // 不存在的话 创建文件夹
                }
                File photo = new File(photoName);
                imageUri = Uri.fromFile(photo);
                // 这样就将文件的存储方式和uri指定到了Camera应用中
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();
        switch (requestCode) {
            case 1:
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i("内存卡错误", "请检查您的内存卡");
                } else {
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    // 设置图片的大小
                    Bitmap bitMap = BitmapFactory.decodeFile(photoName);
                    int width = bitMap.getWidth();
                    int height = bitMap.getHeight();
                    // 设置想要的大小
                    int newWidth = 480;
                    int newHeight = 640;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
                    // canvas.drawBitmap(bitMap, 0, 0, paint)
                    // 防止内存溢出
                    op.inSampleSize = 4; // 这个数字越大,图片大小越小.
                    Bitmap pic = null;
                    pic = BitmapFactory.decodeFile(photoName, op);
                    iv_prodlie.setImageBitmap(pic); // 这个ImageView是拍照完成后显示图片
                    FileOutputStream b = null;
                    try {
                        b = new FileOutputStream(photoName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (pic != null) {
                        pic.compress(Bitmap.CompressFormat.JPEG, 50, b);
                    }
                }
                break;
            default:
                return;
        }
    }
    public void multiFileUpload() {
        String mBaseUrl = URL_UPDATEPROFILE;
        RequestParams params = new RequestParams(mBaseUrl);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", new File(uploadFile)));
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
                Toast.makeText(UpdateProflieone.this, "上传成功", Toast.LENGTH_SHORT).show();
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
