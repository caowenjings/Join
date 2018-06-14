package com.example.jingjing.xin.Find;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.RegisterActivity;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.StadiumOrder;

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

import static com.example.jingjing.xin.constant.Conatant.URL_INSERTNEED;

/**
 * Created by jingjing on 2018/5/29.
 */

public class PostNeedFalot extends AppCompatActivity implements View.OnClickListener,SetNumDialog.SetNumListener,SetStadiumDialog.SetStadiumListener {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    public Button btn_stadiumname;
    public Button btn_date;
    public Button btn_time;
    public Button btn_num;
    public Button btn_sumbit;
    public TextView tv_stadiumname;
    public TextView tv_date;
    public TextView tv_time;
    public TextView tv_num;
    public EditText et_remark;
    private Stadium set_stadium;
    private User user;
    private String time_xuanze,set_num;

    private java.util.Calendar mCalendar = java.util.Calendar.getInstance(Locale.CHINA);
    private int myear, mmonth, mday, mhour, mminute;
    private String stadiumname, dateone, timeone, numpeople, remark;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postneedfalot);

        initView();
        initDate();
    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("约运动");

        btn_stadiumname = (Button) findViewById(R.id.btn_stadiumname);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_num = (Button) findViewById(R.id.btn_num);
        tv_stadiumname = (TextView) findViewById(R.id.tv_stadiumname);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_num = (TextView) findViewById(R.id.tv_num);
        et_remark = (EditText) findViewById(R.id.et_remark);
        btn_sumbit = (Button) findViewById(R.id.btn_sumbit);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDate() {
        getCalender();
        user = (User) getIntent().getSerializableExtra("user");
        tv_back.setOnClickListener(this);
        btn_stadiumname.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_num.setOnClickListener(this);
        btn_sumbit.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
            case R.id.btn_stadiumname:
                setPlaceClick(v);
                break;
            case R.id.btn_date:
                showDataDialog();
                break;
            case R.id.btn_time:
                showTimeDialog();
                break;
            case R.id.btn_num:
                setNumClick(v);
                break;
            case R.id.btn_sumbit:
                getEditString();
                getCalender();
                if (!TextUtils.isEmpty(stadiumname) && !TextUtils.isEmpty(dateone)
                        && !TextUtils.isEmpty(timeone) && !TextUtils.isEmpty(numpeople)) {

                    time_xuanze = dateone + timeone;
                    String thistime = myear + "年" + mmonth + "月" + mday + "日";
                    String releasetime =  myear + "年" + mmonth + "月" + mday + "日";
                    InsertNeed(user.getUserId(),set_stadium.getStadiumId(),
                            time_xuanze,set_num,set_stadium.getStadiumtype(),remark,releasetime);

                } else {
                    Toast.makeText(PostNeedFalot.this, "有选项没有选哟！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void getEditString() {
        user = (User) getIntent().getSerializableExtra("user");
        stadiumname = tv_stadiumname.getText().toString();
        dateone = tv_date.getText().toString();
        timeone = tv_time.getText().toString();
        numpeople = tv_num.getText().toString();
        remark = et_remark.getText().toString();
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

    @Override
    public void onSetNumComplete(int num) {//选择人数
          set_num = String.valueOf(num);
        tv_num.setText(String.valueOf(num) + "位");

    }


    public void setNumClick(View v) {
        SetNumDialog std = new SetNumDialog();
        std.show(getFragmentManager(), "numPicker");
    }

    @Override
    public void onSetStadiumComplete(Stadium stadium) {//实现接口，选择场馆
        if (stadium == null) {
            Toast.makeText(PostNeedFalot.this, "没有选场馆", Toast.LENGTH_SHORT).show();
        } else {
            set_stadium = stadium;//传值
            tv_stadiumname.setText(stadium.getStadiumname().toString());
        }
    }

    public void setPlaceClick(View v) {
        SetStadiumDialog std = new SetStadiumDialog();
        std.show(getSupportFragmentManager(), "adaPicker");
    }

    private void showDataDialog() {// 显示日期对话框
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 根据对话框的调整，设置日历
                mCalendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置时间格式
                tv_date.setText(dateFormat.format(mCalendar.getTime()));// 获取系统当前时间，显示在textview上
            }
        };
        // 创建对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(PostNeedFalot.this, dateSetListener,
                myear, mmonth, mday);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());//选定的最小时间,new Date()为获取当前系统时间
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);//最大时间
        datePickerDialog.show();
    }


    private void showTimeDialog() {//显示时间对话框
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                mCalendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay); // 根据时间对话框的调整，设置时和分
                mCalendar.set(java.util.Calendar.MINUTE, minute);
                tv_time.setText(hourOfDay + ":" + minute);
            }
        };
        // 根据Calendar对象获取到的时、分创建对话框，true表示24小时
        TimePickerDialog timePickerDialog = new TimePickerDialog(PostNeedFalot.this, timeSetListener,
                mhour, mminute, true);
        timePickerDialog.show();
    }


    private void InsertNeed(int userId, int stadiumId, String time, String num, String stadiumtype, String remark,String releasetime) {
        String orderURL = URL_INSERTNEED;
        new InsertNeedAsyncTask().execute(orderURL, String.valueOf(userId), String.valueOf(stadiumId), time, num, stadiumtype, remark,releasetime);
    }


    private class InsertNeedAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                json.put("stadiumId", params[2]);
                json.put("time", params[3]);
                json.put("num", params[4]);
                json.put("stadiumtype", params[5]);
                json.put("remark", params[6]);
                json.put("releasetime", params[7]);
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
                        Toast.makeText(PostNeedFalot.this, "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PostNeedFalot.this, "发布失败，请重试", Toast.LENGTH_SHORT).show();
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