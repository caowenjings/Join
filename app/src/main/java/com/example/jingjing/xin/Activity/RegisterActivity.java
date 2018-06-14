package com.example.jingjing.xin.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_Register;

/**
 * Created by jingjing on 2018/5/9.
 */

public class RegisterActivity extends AppCompatActivity {


    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private EditText et_username;
    private EditText et_password;
    private EditText et_confirmpwd;
    private EditText et_realname;
    private EditText et_tel;
    private Button btn_register;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String sex;
    private String myright;
    private String username,password,confirmmpwd,realname,tel;
    private User user;

    //MediaType使用两部分标识符来确定一个类型，来表明传的东西时什么类型
//提交json数据
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        initView();
        initData();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("注 册");

        et_username = (EditText) findViewById(R.id.et_username_register);
        et_password = (EditText) findViewById(R.id.et_password_register);
        et_confirmpwd= (EditText) findViewById(R.id.et_confirmpwd_register);
        et_realname = (EditText) findViewById(R.id.et_realname_register);
        et_tel = (EditText) findViewById(R.id.et_tel);
        btn_register = (Button) findViewById(R.id.btn_register);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        getWindow().setStatusBarColor(Color.parseColor("#FF029ACC"));
    }

    private void initData() {

        iv_title.setOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//选择性别
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        sex = rb_male.getText().toString();
                        break;
                    case R.id.rb_female:
                        sex = rb_female.getText().toString();
                        break;
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getEditString();//获取用户输入的信息
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "请输入你的用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "请输入你的密码", Toast.LENGTH_SHORT).show();
                    return;
                }if (!password.equals(confirmmpwd)) {
                    Toast.makeText(RegisterActivity.this, "你输入的密码不正确请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(realname)) {
                    Toast.makeText(RegisterActivity.this, "请输入你的真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(sex)) {
                    Toast.makeText(RegisterActivity.this, "请选择你的性别", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(tel) ) {
                    Toast.makeText(RegisterActivity.this, "请输入你的电话号码", Toast.LENGTH_SHORT).show();
                } if(isTelNum(tel)==false){
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Register(username, password, realname, sex, tel, myright);
                }
            }
        });
    }

    public static boolean isTelNum(String tel ){//手机号格式判断是否正确
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    public void getEditString(){//得到用户输入信息
        username=et_username.getText().toString();
        password=et_password.getText().toString();
        tel=et_tel.getText().toString();
        realname=et_realname.getText().toString();
        confirmmpwd=et_confirmpwd.getText().toString();
    }

    private void Register(String username, String password, String realname, String sex, String tel, String myright) {
        String registerUrl = URL_Register;//服务器
        new RegisterAsyncTask().execute(registerUrl, username, password, realname, sex, tel, myright);//执行请求
    }//启动任务

    private class RegisterAsyncTask extends AsyncTask<String, Integer, String> {//异步消息处理
        public RegisterAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {//执行耗时操作，带参post请求（Params是传入的参数）
            Response response = null;
            String results = null;
             user = new User();
            JSONObject json = new JSONObject();//用于添加参数
            try {
                json.put("username", params[1]);
                json.put("password", params[2]);
                json.put("realname", params[3]);
                json.put("sex", params[4]);
                json.put("tel", params[5]);
                json.put("myright", params[6]);
                OkHttpClient okHttpClient = new OkHttpClient();//发送网络请求
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));//用于传递数据
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)//参数传递
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();//返回的类型
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {//后台返回的数据可以作为参数传递到这里方法中，可以利用返回的数据进行一些UI操作
            System.out.println(s);
            if (!TextUtils.isEmpty(s)) {
                try {
                    JSONObject results = new JSONObject(s);//s转化为JsonObject类型
                    String loginresult = results.getString("result");//获取到信息
                    if (loginresult.equals("1")) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                       new Handler(new Handler.Callback() {
                            //处理接收到的消息的方法，防止堵塞主线程
                            @Override
                           public boolean handleMessage(Message arg0) {
                                //实现页面跳转
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                              // Bundle bundle = new Bundle();
                                //bundle.putSerializable("user",user);
                               //intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 2000);
                    } else {
                        Toast.makeText(RegisterActivity.this, "该用户名已注册", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
            }
        }
    }
}

