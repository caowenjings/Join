package com.example.jingjing.xin.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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

import static com.example.jingjing.xin.constant.Conatant.URL_SELECTUSERBYUSERID;
import static com.example.jingjing.xin.constant.Conatant.URL_UPDATEPASSWORD;

/**
 * Created by jingjing on 2018/5/18.
 */

public class ForgivePassword extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private EditText et_username;
    private EditText et_tel;
    private Button btn_sure;
    private EditText et_new_password;
    private String username,tel,password;
    private User user;
    private String userId;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgive_paaaword1);

        initView();
        initDate();
    }

    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("忘记密码");

        et_username = (EditText)findViewById(R.id.et_username);
        et_tel = (EditText)findViewById(R.id.et_tel);
        et_new_password=(EditText)findViewById(R.id.et_new_password);
        btn_sure = (Button)findViewById(R.id.btn_sure);
    }

    private void  initDate(){
        user = (User) getIntent().getSerializableExtra("user");
        userId = String.valueOf(user.getUserId());

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                userId = String.valueOf(user.getUserId());
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(password)){
                    if(user.getUsername().equals(username)){
                        if(user.getTel().equals(tel)){
                            forgivepassword(userId ,password);
                        }else {
                            Toast.makeText(ForgivePassword.this, "电话号码不正确", Toast.LENGTH_SHORT).show();}
                    }else {
                        Toast.makeText(ForgivePassword.this, "用户名不正确", Toast.LENGTH_SHORT).show();}
                }else{
                    Toast.makeText(ForgivePassword.this, "每项不能为空", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    private void getEditString(){
        username = et_username.getText().toString();
        tel = et_tel.getText().toString();
        password = et_new_password.getText().toString();
    }

    private void forgivepassword(String userId,String password) {
        String loginUrl = URL_SELECTUSERBYUSERID;
        new ForgivePasswordAsyncTask().execute(loginUrl, userId,password);
    }
    private class ForgivePasswordAsyncTask extends AsyncTask<String, Integer, String> {
     public  ForgivePasswordAsyncTask(){

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

                        Toast.makeText(ForgivePassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ForgivePassword.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(ForgivePassword.this, "网络未连接", Toast.LENGTH_LONG).show();

            }
        }
    }
}