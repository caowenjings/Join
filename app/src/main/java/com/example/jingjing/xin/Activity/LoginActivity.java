package com.example.jingjing.xin.Activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.User.ForgivePassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.PreferenceChangeEvent;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.example.jingjing.xin.constant.Conatant.URL_LOGIN;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

/**
 * Created by jingjing on 2018/5/9.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_password;
    private TextView btn_register;
    private TextView btn_forgetpwd;
    private Button btn_login;
    private String username,password;
    private CheckBox rememberpass;
    private TextView btn_forgive;
    private ImageView lv_delete;
    private ImageView lv_delete_one;
    private User user;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();//隐藏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.login);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_APN_SETTINGS
        }, MY_PERMISSION_REQUEST_CODE);

        initView();
        initData();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_forgetpwd=(TextView)findViewById(R.id.btn_forgive);
        rememberpass=(CheckBox)findViewById(R.id.remember_pwd);
        btn_forgive = (TextView) findViewById(R.id.btn_forgive);
        lv_delete = (ImageView)findViewById(R.id.iv_delete);
        lv_delete_one = (ImageView)findViewById(R.id.iv_delete_one);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);//获取SharedPreferences对象
        boolean isRemember =preferences.getBoolean("remember_password",false);//获取这个键对应的值，默认flase，当选中时时ture
        if(isRemember){
            String username = preferences.getString("username","");//读取值
            String password = preferences.getString("password","");
            et_username.setText(username);//将账号和密码都设置到文本框中
            et_password.setText(password);
            rememberpass.setChecked(true);
        }
    }

    private void initData() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forgive.setOnClickListener(this);
        lv_delete.setOnClickListener(this);
        lv_delete_one.setOnClickListener(this);

        getEditString();
        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {//推断是否有焦点
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!username.equals("")){
                        lv_delete.setVisibility(VISIBLE);
                        et_username.addTextChangedListener(new EditChangedListener());
                    }else {
                        lv_delete.setVisibility(View.GONE);
                        et_username.addTextChangedListener(new EditChangedListener());
                    }
                }else {
                    lv_delete.setVisibility(View.GONE);//隐藏
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {//推断是否有焦点
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!password.equals("")){
                        lv_delete_one.setVisibility(View.VISIBLE);
                        et_password.addTextChangedListener(new EditChangedListener_one());
                    }else {
                        lv_delete_one.setVisibility(View.GONE);//隐藏
                        et_password.addTextChangedListener(new EditChangedListener_one());
                    }
                }else {
                    lv_delete_one.setVisibility(View.GONE);//隐藏
                }
            }
        });
    }

    public void getEditString(){//获取用户输入信息
        username=et_username.getText().toString();
        password=et_password.getText().toString();
    }

   private class EditChangedListener implements TextWatcher{//文本监视器监听用户输入状态
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {//文本框改变之前
       }
       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
           if(!"".equals(s.toString())){
               lv_delete.setVisibility(View.VISIBLE);
           }else {
               lv_delete.setVisibility(View.GONE);//隐藏
           }
       }
       @Override
       public void afterTextChanged(Editable s) {
       }
   }


   private class  EditChangedListener_one implements TextWatcher{
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
           if(s.length()!= 0){
               lv_delete_one.setVisibility(View.VISIBLE);

           }else {
               lv_delete_one.setVisibility(View.GONE);//隐藏
           }
       }

       @Override
       public void afterTextChanged(Editable s) {

       }
   }
    @Override
    public void onClick(View v) {
        getEditString();
        switch (v.getId()) {
            case R.id.btn_login:
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    Login(username,password);
                } else {
                    Toast.makeText(this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forgive:
                showDialog();
                break;
            case R.id.iv_delete:
                et_username.setText("");
                lv_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_delete_one:
                et_password.setText("");
                lv_delete_one.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private void Login(String username, String password) {
        String loginUrl = URL_LOGIN;
        new LoginAsyncTask().execute(loginUrl, username, password);////启动任务
    }

    private class LoginAsyncTask extends AsyncTask<String, Integer, String> {
        public LoginAsyncTask() {
        }
        @Override
        protected String doInBackground(String... params) {//后台执行具体的下载逻辑
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("username", params[1]);
                json.put("password", params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();//发送网络请求
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
                    JSONObject results = new JSONObject(s);//解析json数据,获取返回的值
                    String loginresult = results.getString("result");
                    System.out.println(loginresult);
                    final User user = new User();
                    if (!"0".equals(loginresult)) {
                        editor = preferences.edit();//获取一个sharedPreFerences.Editord对象
                        if(rememberpass.isChecked()){//检查复选框是否被选中了
                            editor.putBoolean("remember_password",true);//将flase改为true
                            editor.putString("username",username);
                            editor.putString("password",password);
                        }else {
                            editor.clear();
                        }
                        editor.apply();//提交

                        user.setUserId(results.getInt("userId"));
                        user.setUsername(results.getString("username"));
                        user.setPassword(results.getString("password"));
                        user.setRealname(results.getString("realname"));
                        user.setSex(results.getString("sex"));
                        user.setTel(results.getString("tel"));
                        user.setMyright(results.getString("myRight"));
                        user.setProflie(URL_PROFLIE+results.optString("proflie"));
                        Toast.makeText(LoginActivity.this, "正在登陆，请稍后", Toast.LENGTH_LONG).show();
                        new Handler(new Handler.Callback() {
                            //处理接收到的消息的方法，防止堵塞主线程
                            @Override
                            public boolean handleMessage(Message arg0) {
                                //实现页面跳转
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("user", user);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 1000);
                    } else {
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void showDialog() {//忘记密码
    user = (User) getIntent().getSerializableExtra("user");

    final AlertDialog mDialog = new AlertDialog.Builder(this).create();
    mDialog.show();//显示对话框
    View view = View.inflate(this, R.layout.forgive_password, null);//布局
    final TextView find_password = (TextView) view.findViewById(R.id.tv_find_password);
    final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
    final LinearLayout ll_photo = (LinearLayout)view.findViewById(R.id.ll_photot);
    ll_photo.setVisibility(View.GONE);
    find_password.setText("找回密码");

    find_password.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, ForgivePassword.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();//关闭对话框
        }
    });

    Window window = mDialog.getWindow();//获取当前所在窗体，然后从底部弹出
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.popupAnimation);//设置窗体样式
    window.setContentView(view);
    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    mDialog.setCanceledOnTouchOutside(false);//点击区域外不消失
    mDialog.setCancelable(false);//
    }
}