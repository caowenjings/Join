package com.example.jingjing.xin.Stadium;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.Place;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_ORDERSTADIUM;

/**
 * Created by jingjing on 2018/5/24.
 */

public class StadiumOrder extends AppCompatActivity implements View.OnClickListener, SetPlaceDialog.SetPlaceListener,SetOrderTimeDialog.SetOrdertime {


    private TextView tv_title;
    private RelativeLayout tv_back;

    private Button btn_date;
    private Button btn_time;
    private Button btn_place;
    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_place;
    private TextView tv_userId;
    private Button btn_sure;
    private User user;
    private Stadium stadium;
    private String time_order;
    private java.util.Calendar mCalendar = java.util.Calendar.getInstance(Locale.CHINA);
    private int myear, mmonth, mday, mhour, mminute;
    private String date, time, place_set;
    private Place place;
    private String thistime;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();//隐藏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.stadium_order);
        initView();
        initData();
        getCalender();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("预定场地");

        btn_date = (Button) findViewById(R.id.btn_date);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_place = (Button) findViewById(R.id.btn_place);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_userId = (TextView) findViewById(R.id.tv_userId);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_place = (TextView) findViewById(R.id.tv_place);
        btn_sure = (Button) findViewById(R.id.btn_sure);

    }

    private void initData() {

        user = (User) getIntent().getSerializableExtra("user");
        tv_userId.setText(String.valueOf(user.getUserId()));
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");

        tv_back.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_place.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onSetPlaceComplete(Place place_set) {//调用接口
        if (place_set == null) {
            Toast.makeText(StadiumOrder.this, "没有选场地", Toast.LENGTH_SHORT).show();
        } else {
            place = place_set;
            tv_place.setText(place_set.getPlacename());
        }
    }

    public void getordertime(String time) {//调用接口
        tv_time.setText(time);
        time_order = tv_date.getText().toString() + tv_time.getText().toString();
    }


    public void setPlaceClick(View v) {
        SetPlaceDialog std = new SetPlaceDialog(stadium, time_order);
        std.show(getSupportFragmentManager(), "placePicker");
    }


    public void setTimeClick(View v) {
        SetOrderTimeDialog timed = new SetOrderTimeDialog(stadium , tv_date.getText().toString());
        timed.show(getFragmentManager(),"timePicker");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_date:
                showDataDialog();
                break;
            case R.id.btn_time:
                gerEditString();
                getCalender();
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(StadiumOrder.this, "请先选择日期", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    String this_day = myear + "年" + mmonth + "月" + mday + "日";
                    if (this_day.equals(date)){//判断是否今天
                        int time_this = calendar.get(Calendar.HOUR_OF_DAY);
                        if ((time_this+1)>= Integer.parseInt(stadium.getClosetime())) {//不能选择了
                            Toast.makeText(StadiumOrder.this, "该场馆今日已休息，请选择其他日期", Toast.LENGTH_SHORT).show();
                        } else {
                           setTimeClick(v);
                        }
                    } else {
                        setTimeClick(v);
                    }
                }
                break;
            case R.id.btn_place:
                setPlaceClick(v);
                break;
            case R.id.btn_sure:
                gerEditString();
                if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(place_set)) {

                    thistime = myear + "年" + mmonth + "月" + mday + "日";
                    time_order = date + time;
                    OrderStadium(user.getUserId(), stadium.getStadiumId(), thistime, time_order,String.valueOf(place.getPlaceId()), user.getTel());

                } else {
                    Toast.makeText(StadiumOrder.this, "您有未输入的内容", Toast.LENGTH_LONG).show();
                }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCalender() {//获取当前的日期
        java.util.Calendar cal = java.util.Calendar.getInstance();
        myear = cal.get(java.util.Calendar.YEAR);
        mmonth = cal.get(java.util.Calendar.MONTH) + 1;//calendar是以0开始的
        mday = cal.get(java.util.Calendar.DAY_OF_MONTH);//当月多少天
        mhour = cal.get(java.util.Calendar.HOUR_OF_DAY);//当天多少时
        mminute = cal.get(java.util.Calendar.MINUTE);
        setTitle(myear + "_" + mmonth + "_" + mday + "_" + mhour + ":" + mminute);
    }

    private void gerEditString() {
        date = tv_date.getText().toString();
        time = tv_time.getText().toString();
        place_set = tv_place.getText().toString();
    }


    private void showDataDialog() {// 显示日期对话框
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                tv_date.setText(year + "年" + (month + 1) + "月" + day + "日");
            }
        };
        // 创建对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(StadiumOrder.this,R.style.MyDatePickerDialogTheme,dateSetListener ,
                year, month, day);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());//选定的最小时间,new Date()为获取当前系统时间
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);//最大时间
        datePickerDialog.show();

    }


    private void OrderStadium(int userId, int stadiumId, String time, String time_order, String placeId, String tel) {
        String orderURL = URL_ORDERSTADIUM;
        new OrderStadiumAsyncTask().execute(orderURL, String.valueOf(userId), String.valueOf(stadiumId), time, time_order, placeId, tel);
    }

    private class OrderStadiumAsyncTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                json.put("stadiumId", params[2]);
                json.put("time", params[3]);
                json.put("time_order", params[4]);
                json.put("placeId", params[5]);
                json.put("tel", params[6]);
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
            if (!"".equals(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if (loginresult.equals("1")) {
                        Toast.makeText(StadiumOrder.this, "预约成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StadiumOrder.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
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