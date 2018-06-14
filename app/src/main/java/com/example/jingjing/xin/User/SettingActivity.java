package com.example.jingjing.xin.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by jingjing on 2018/5/16.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private ImageView update_password;
    private ImageView about_join;
    private User user;
    private String userId;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.setting);

        initView();
        initData();
    }

    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("设 置");

        update_password=(ImageView)findViewById(R.id.update_password);
        about_join=(ImageView)findViewById(R.id.about_join);
    }

    private void  initData(){

        user = (User) getIntent().getSerializableExtra("user");
        userId = String.valueOf(user.getUserId());
        tv_back.setOnClickListener(this);
        update_password.setOnClickListener(this);
        about_join.setOnClickListener(this);

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
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(SettingActivity.this, "网络未连接", Toast.LENGTH_LONG).show();

            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back :
                finish();
                break;
            case R.id.update_password :
                Intent intent = new Intent(SettingActivity.this,Updatepassword.class);
                Bundle mBundle1 = new Bundle();
                mBundle1.putSerializable("user", user);
                intent.putExtras(mBundle1);
                startActivity(intent);
                break;
            case R.id.about_join:
                Intent intent1 = new Intent(SettingActivity.this,About_join.class);
                startActivity(intent1);
                break;

            default:
                break;

        }

    }
}
