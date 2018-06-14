package com.example.jingjing.xin.User;

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
        private User user;
        private String userId;
        private String oldpassword,newpassword,confrimpassword;

        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            android.support.v7.app.ActionBar actionBar =getSupportActionBar();
            actionBar.hide();
            setContentView(R.layout.update_password);

            initView();
            initData();
        }
        private void initView(){
            tv_title=(TextView)findViewById(R.id.tv_title);
            iv_title=(ImageView)findViewById(R.id.iv_title);
            tv_back=(RelativeLayout)findViewById(R.id.tv_back);
            tv_title.setText("修改密码");

            et_old_password=(EditText)findViewById(R.id.et_old_password);
            et_new_password=(EditText)findViewById(R.id.et_new_password);
            et_comfirm_password=(EditText)findViewById(R.id.et_comfirm_password);

            baocun=(Button)findViewById(R.id.btn_baocun);

        }

        private void initData() {
            user = (User) getIntent().getSerializableExtra("user");

            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }


            });

            baocun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getEditString();
                    userId = String.valueOf(user.getUserId());
                    if (!TextUtils.isEmpty(oldpassword) && !TextUtils.isEmpty(newpassword) && !TextUtils.isEmpty(confrimpassword) ) {
                        if (user.getPassword().equals(oldpassword)){
                            if (newpassword.equals(confrimpassword)) {
                                updatepassword(userId, newpassword);
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
                                et_comfirm_password.setText("");
                                Toast.makeText(Updatepassword.this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        } else {
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
        new UpdatePasswordAsyncTask().execute(loginUrl, userId, password);
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
/* if(TextUtils.isEmpty(oldpassword)){
                        Toast.makeText(Updatepassword.this, "请输入原来的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }if(!oldpassword.equals(user.getPassword())){
                        Toast.makeText(Updatepassword.this, "对不起，你输入的密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }if(TextUtils.isEmpty(newpassword)){
                        Toast.makeText(Updatepassword.this, "请输入新的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }if(TextUtils.isEmpty(confrimpassword)){
                        Toast.makeText(Updatepassword.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                        return;
                    }if(!newpassword.equals(newpassword)){
                        Toast.makeText(Updatepassword.this, "两次密码输入不一样，请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                      updatepassword(userId,newpassword);
*/

/*
 private EditText et_password_old;
    private EditText et_password_new;
    private EditText et_confire_password_new;
    private LinearLayout lout_passwordold;
    private Button btn_submit;
    private ImageView icon_back;
    private User user;
    private int method=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        initView();
        if(FindFragment.isWiFi(this) || FindFragment.isMobile(this)){
            initData();
        }else {
            Toast.makeText(this,"网络未连接,请检查网络",Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        lout_passwordold = findViewById(R.id.lout_password);
        et_password_old = findViewById(R.id.et_password_old);
        et_password_new = findViewById(R.id.et_password_new);
        et_confire_password_new = findViewById(R.id.et_confire_password_new);
        btn_submit = findViewById(R.id.btn_submit);
        icon_back = findViewById(R.id.icon_back);

        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        method = (int) getIntent().getSerializableExtra("method");
        if(method==1){
         et_password_old.setText(user.getPassword());
         lout_passwordold.setVisibility(View.GONE);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(et_password_old.getText().toString().equals(""))
                        && !(et_password_new.getText().toString().equals(""))
                        && !(et_confire_password_new.getText().toString().equals(""))) {
                    if (user.getPassword().equals(et_password_old.getText().toString())) {
                        if (et_password_new.getText().toString().equals(et_confire_password_new.getText().toString())) {
                            updatePassword(user.getUserId(), et_password_new.getText().toString());
                        } else {
                            et_password_new.setText("");
                            et_confire_password_new.setText("");
                            Toast.makeText(UpdatePassword.this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        et_password_old.setText("");
                        Toast.makeText(UpdatePassword.this, "旧密码输入错误.请重新输入", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UpdatePassword.this, "每项不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void updatePassword(int userId, String password) {
        String loginUrl = URL_UPDATEPASSWORD;
        new UpdatePasswordAsyncTask().execute(loginUrl, String.valueOf(userId), password);
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
                        Toast.makeText(UpdatePassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdatePassword.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(UpdatePassword.this, "网络未连接", Toast.LENGTH_LONG).show();

            }
        }
    }
}

 */