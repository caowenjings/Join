package com.example.jingjing.xin.User;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Activity.MainActivity;
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

import static com.example.jingjing.xin.constant.Conatant.URL_GETPASSWORD;
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
    private EditText et_reaname;
    private Button btn_sumbit;
    private String musername,mtel,mrealname;

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

    @SuppressLint("WrongViewCast")
    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("找回密码");

        et_username = (EditText)findViewById(R.id.et_username);
        et_tel = (EditText)findViewById(R.id.et_tel);
        et_reaname=(EditText)findViewById(R.id.et_realname);
        btn_sumbit = (Button)findViewById(R.id.btn_tijiao);
    }

    private void  initDate(){

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgivePassword.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if(!TextUtils.isEmpty(musername)&&!TextUtils.isEmpty(mtel)&&!TextUtils.isEmpty(mrealname)){
                    forgivepassword(musername,mrealname,mtel);
                }else {
                    Toast.makeText(ForgivePassword.this, "有选项为空，请填写", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getEditString(){
        musername = et_username.getText().toString();
        mtel = et_tel.getText().toString();
        mrealname = et_reaname.getText().toString();
    }

    private void forgivepassword(String username, String realname, String tel) {
        String url = URL_GETPASSWORD;
        new ForgivePasswordAsyncTask().execute(url, username, realname, tel);
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
                json.put("username", params[1]);
                json.put("realname",params[2]);
                json.put("tel", params[3]);
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
                    System.out.println(loginresult);
                    if (!"0".equals(loginresult)) {
                        final User user = new User();
                        user.setUserId(results.getInt("userId"));
                        user.setPassword(results.getString("password"));//用于修改密码的显示

                        Toast.makeText(ForgivePassword.this, "验证成功", Toast.LENGTH_LONG).show();

                        new Handler(new Handler.Callback() {
                            //处理接收到的消息的方法，防止堵塞主线程
                            @Override
                            public boolean handleMessage(Message arg0) {
                                //实现页面跳转
                                Intent intent=new Intent(ForgivePassword.this, Updatepassword.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("user", user);
                                mBundle.putSerializable("method",1);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                                finish();
                               return false;

                            }
                        }).sendEmptyMessageDelayed(0, 2000);
                    } else {
                        Toast.makeText(ForgivePassword.this, "用户名或手机号码或真实名错误", Toast.LENGTH_SHORT).show();
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
