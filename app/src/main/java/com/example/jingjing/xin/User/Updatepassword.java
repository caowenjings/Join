package com.example.jingjing.xin.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_UPDATEPASSWORD;

/**
 * Created by jingjing on 2018/5/12.
 */

public class Updatepassword extends AppCompatActivity  {


        private TextView tv_title;
        private ImageView iv_title;
        private RelativeLayout tv_back;

        private EditText et_old_password;
        private EditText et_new_password;
        private EditText et_comfirm_password;
        private Button baocun;
        private LinearLayout ll_old_password;
        private User user;
        private String userId;
        private String oldpassword,newpassword,confrimpassword;
        private int method=0;

        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            android.support.v7.app.ActionBar actionBar =getSupportActionBar();
            actionBar.hide();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.BLACK);//设置颜色
            setContentView(R.layout.update_password);

            initView();
            initData();
        }
        @SuppressLint("WrongViewCast")
        private void initView(){
            tv_title=(TextView)findViewById(R.id.tv_title);
            iv_title=(ImageView)findViewById(R.id.iv_title);
            tv_back=(RelativeLayout)findViewById(R.id.tv_back);
            tv_title.setText("修改密码");

            et_old_password=(EditText)findViewById(R.id.et_old_password);
            et_new_password=(EditText)findViewById(R.id.et_new_password);
            et_comfirm_password=(EditText)findViewById(R.id.et_comfirm_password);
            ll_old_password = (LinearLayout)findViewById(R.id.ll_old_password);
            baocun=(Button)findViewById(R.id.btn_sure);

            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

        private void initData() {
            user = (User) getIntent().getSerializableExtra("user");
            method = (int) getIntent().getSerializableExtra("method");

            if(method == 1){//忘记密码所要用到的
                et_old_password.setText(user.getPassword());
                ll_old_password.setVisibility(View.GONE);//隐藏
                tv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Updatepassword.this,ForgivePassword.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            baocun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getEditString();
                    userId = String.valueOf(user.getUserId());
                    if (!TextUtils.isEmpty(oldpassword) && !TextUtils.isEmpty(newpassword) && !TextUtils.isEmpty(confrimpassword) ) {
                        if (user.getPassword().equals(oldpassword)){
                            if (newpassword.equals(confrimpassword)) {
                                updatepassword(userId, newpassword);
                            } else {
                                et_comfirm_password.setText("");
                                Toast.makeText(Updatepassword.this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            et_old_password.setText("");
                            Toast.makeText(Updatepassword.this, "旧密码输入错误.请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Updatepassword.this, "每项不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    private void getEditString(){
            oldpassword = et_old_password.getText().toString();
            newpassword = et_new_password.getText().toString();
           confrimpassword= et_comfirm_password.getText().toString();
    }

    private void updatepassword(String userId, String password) {
        String loginUrl = URL_UPDATEPASSWORD;
        new UpdatePasswordAsyncTask().execute(loginUrl,userId, password);
    }
    private class UpdatePasswordAsyncTask extends AsyncTask<String, Integer, String> {
        public UpdatePasswordAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                json.put("password", params[2]);
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
                        Toast.makeText(Updatepassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                        new Handler(new Handler.Callback() {
                            //处理接收到的消息的方法，防止堵塞主线程
                            @Override
                            public boolean handleMessage(Message arg0) {
                                //实现页面跳转
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user",user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Updatepassword.this, "身份过期了，请重新输入密码登录", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 2000);
                    } else {
                        Toast.makeText(Updatepassword.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(Updatepassword.this, "网络未连接", Toast.LENGTH_LONG).show();
            }
        }
    }
}

