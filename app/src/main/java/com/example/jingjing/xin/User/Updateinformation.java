package com.example.jingjing.xin.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Activity.MainActivity;
import com.example.jingjing.xin.Activity.RegisterActivity;
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
import static com.example.jingjing.xin.constant.Conatant.URL_UpdateUser;

/**
 * Created by jingjing on 2018/5/14.
 */

public class Updateinformation extends AppCompatActivity {

    private EditText et_username;
    private EditText et_realname;
    private EditText et_tel;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private Button tv_baocun;
    private ImageView tv_back;
    private String sex;
    private String username,realname,tel,lastname;
    private User user;
    private String userId;

    public static final  MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.update_information);

        initView();
        initData();
    }

    private void initView(){

        tv_back=(ImageView)findViewById(R.id.tv_back);
        tv_baocun=(Button) findViewById(R.id.btn_baocun);
        et_username=(EditText) findViewById(R.id.et_username);
        et_realname=(EditText) findViewById(R.id.et_realname);
        et_tel=(EditText) findViewById(R.id.et_tel);
        rg_sex=(RadioGroup) findViewById(R.id.rg_sex);
        rb_male=(RadioButton) findViewById(R.id.rb_male);
        rb_female=(RadioButton) findViewById(R.id.rb_female);

    }

    private void initData(){
        user = (User) getIntent().getSerializableExtra("user");
        userId = String.valueOf(user.getUserId());
        et_username.setText(user.getUsername());
        lastname = user.getUsername();
        et_realname.setText(user.getRealname());
        et_tel.setText(user.getTel());
        if ("男".equals(user.getSex())) {
            rg_sex.check(R.id.rb_male);
            sex = rb_male.getText().toString();
        } else {
            rg_sex.check(R.id.rb_female);
            sex = rb_female.getText().toString();
        }

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {//选择性别
                switch (checkedId){
                    case R.id.rb_male:
                        sex = rb_male.getText().toString();
                        break;
                    case R.id.rb_female:
                        sex = rb_female.getText().toString();
                        break;
                }
            }
        });

        tv_baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();//获取用户输入的信息
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Updateinformation.this, "请输入你的用户名", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(realname)) {
                    Toast.makeText(Updateinformation.this, "请输入你的真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(sex)) {
                    Toast.makeText(Updateinformation.this, "请选择你的性别", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(tel) ) {
                    Toast.makeText(Updateinformation.this, "请输入你的电话号码", Toast.LENGTH_SHORT).show();
                } if(isTelNum(tel)==false){
                    Toast.makeText(Updateinformation.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateinformation(userId,username, lastname, realname, sex, tel);
                }
            }
        });

    }

    public static boolean isTelNum(String tel ){//手机号格式判断是否正确
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    private void getEditString(){//获取用户输入的信息
        username = et_username.getText().toString();
        realname = et_realname.getText().toString();
        tel = et_tel.getText().toString();
    }

    private void updateinformation(String userId, String username,String lastname, String realname, String sex, String tel) {
        String updateUserUrl = URL_UpdateUser;
        new UpdateinformationAsyncTask().execute(updateUserUrl, userId, username, lastname, realname, sex, tel);
    }

    private class UpdateinformationAsyncTask extends AsyncTask<String,Integer ,String>{
        public UpdateinformationAsyncTask(){

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject json = new JSONObject();
            String results = null ;
            Response response = null;
            try{
                json.put("userId", params[1]);
                json.put("username", params[2]);
                json.put("lastname", params[3]);
                json.put("realname", params[4]);
                json.put("sex", params[5]);
                json.put("tel", params[6]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            if(!TextUtils.isEmpty(s)){
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresults = results.getString("result");
                    System.out.println(22);
                    System.out.println(loginresults);
                    final User user = new User();
                    if(loginresults.equals("1")){
                        user.setUserId(results.getInt("userId"));
                        user.setUsername(results.getString("username"));
                        user.setPassword(results.getString("password"));
                        user.setRealname(results.getString("realname"));
                        user.setSex(results.getString("sex"));
                        user.setTel(results.getString("tel"));
                        user.setMyright(results.getString("myRight"));

                        Toast.makeText(Updateinformation.this,"修改成功",Toast.LENGTH_LONG).show();
                        new Handler(new Handler.Callback() {
                            //处理接收到的消息的方法，防止堵塞主线程
                            @Override
                            public boolean handleMessage(Message arg0) {
                                //实现页面跳转
                                Intent intent=new Intent(Updateinformation.this, UserInformationActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("user",user);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 2000);

                    }else {
                        Toast.makeText(Updateinformation.this,"用户名已经有人注册了",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                Toast.makeText(Updateinformation.this,"网络未连接",Toast.LENGTH_LONG).show();
            }
        }
    }
}


