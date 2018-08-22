package com.example.jingjing.xin.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;
import static com.example.jingjing.xin.constant.Conatant.URL_SELECTUSERBYUSERID;


public class UserInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_username;
    private TextView tv_realname;
    private TextView tv_sex;
    private TextView tv_tel;
    private ImageView tv_back;
    private ImageView iv_touxiang;

    private User user;
    private ImageView icon_back;
    private ImageView btn_update;
    private String userId;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏actionBar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.userinformatoin);
        initView();
        initData();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_back = (ImageView) findViewById(R.id.tv_back);
        iv_touxiang =(ImageView) findViewById(R.id.iv_touxiang);

        btn_update = (ImageView) findViewById(R.id.update_information);
        getWindow().setStatusBarColor(Color.parseColor("#FF029ACC"));
    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        userId = String.valueOf(user.getUserId());
        RefrshUser(userId);

        tv_back.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_information:
                Intent intent = new Intent(UserInformationActivity.this, Updateinformation.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", user);
                intent.putExtras(mBundle);
                startActivity(intent);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_touxiang:
               showDialog();
                break;
            default:
                break;
        }
    }


    private void RefrshUser(String userId) {
        String loginUrl = URL_SELECTUSERBYUSERID;
        new RefrshUserAsyncTask().execute(loginUrl, userId);
    }

    private class RefrshUserAsyncTask extends AsyncTask<String, Integer, String> {
        public RefrshUserAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
                //判断请求是否成功
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            if (s != null) {
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    System.out.println("22");
                    System.out.println(loginresult);
                    if (!"0".equals(loginresult)) {
                        user.setUserId(results.getInt("userId"));
                        user.setUsername(results.getString("username"));
                        user.setPassword(results.getString("password"));
                        user.setRealname(results.getString("realname"));
                        user.setSex(results.getString("sex"));
                        user.setTel(results.getString("tel"));
                        user.setMyright(results.getString("myRight"));
                        user.setProflie(URL_PROFLIE+results.getString("proflie"));
                        tv_username.setText(user.getUsername());
                        tv_realname.setText(user.getRealname());
                        tv_sex.setText(user.getSex());
                        tv_tel.setText(user.getTel());
                        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(UserInformationActivity.this);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        ImageLoader.getInstance().init(configuration);
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                                .showImageOnLoading(R.drawable.loading)
                                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                                .delayBeforeLoading(0)  // 下载前的延迟时间
                                .build();
                        ImageLoader.getInstance().displayImage(user.getProflie(),iv_touxiang, options);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(UserInformationActivity.this, "网络未连接", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void showDialog() {//选择图片
        user = (User) getIntent().getSerializableExtra("user");

        final android.support.v7.app.AlertDialog mDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.popupAnimation);

        View view = View.inflate(this, R.layout.forgive_password, null);
        final TextView find_password = (TextView) view.findViewById(R.id.tv_find_password);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        find_password.setText("选择图片");

        find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this,UpdateProflie.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();//关闭对话框
            }
        });
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
    }
}