package com.example.jingjing.xin.Stadium;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_EVALUATESTADIUM;
import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;

public class EvaluateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_fabu;
    private ImageView tv_back;
    private TextView tv_stadiumname;
    private ImageView iv_stadiumpicture;
    private EditText et_evaluate;
    private RatingBar rb_ratbar;
    private double grade;
    private User user;
    private Book book;
    private int myear,mmonth,mday;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.evaluate);

        initView();
        initData();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        tv_fabu=(TextView)findViewById(R.id.tv_fabu);
        tv_back=(ImageView)findViewById(R.id.iv_back);
        rb_ratbar=(RatingBar)findViewById(R.id.rb_ratbar);
        et_evaluate= (EditText) findViewById(R.id.et_evaluate);
        tv_stadiumname= (TextView) findViewById(R.id.tv_stadiumname);
        iv_stadiumpicture= (ImageView) findViewById(R.id.iv_picture);

    }

    private void initData() {
        tv_back.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);

        user = (User) getIntent().getSerializableExtra("user");
        book= (Book) getIntent().getSerializableExtra("book");
        tv_stadiumname.setText(book.getStadiumname());

        System.out.println(book.getStadiumpicture());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(100)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(URL_PICTURE+book.getStadiumpicture(),iv_stadiumpicture,options);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_fabu:
                getCalender();
                grade = rb_ratbar.getRating();
                String content = et_evaluate.getText().toString();
                String evaluatetime = myear+"年"+mmonth+"月"+mday+"日";
                evaluate(book.getStadiumId(),grade,book.getBookingId(),content,book.getUserId(),evaluatetime);

                break;
                default:
                    break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCalender() {//获取当前的日期
        java.util.Calendar cal = java.util.Calendar.getInstance();
        myear = cal.get(java.util.Calendar.YEAR);
        mmonth = cal.get(java.util.Calendar.MONTH) + 1;//calendar是以0开始的
        mday = cal.get(java.util.Calendar.DAY_OF_MONTH);//当月多少天
    }

    private void evaluate(int stadiumId, double grade ,int bookingid,String content,int userId,String time){
        String orderURL = URL_EVALUATESTADIUM;
        new EvaluateAsyncTask().execute(orderURL,String.valueOf(stadiumId), String.valueOf(grade), String.valueOf(bookingid),content,String.valueOf(userId),time);
    }

    private class EvaluateAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String results = null;
            Response response = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                json.put("grade",params[2]);
                json.put("bookingId",params[3]);
                json.put("content",params[4]);
                json.put("userId",params[5]);
                json.put("evaluatetime",params[6]);
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
            if(!s.equals("")){
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if(loginresult.equals("1")){
                        Toast.makeText(EvaluateActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(EvaluateActivity.this, "评价失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                System.out.println("网络未连接，结果为空");
            }
        }
    }
}
/*
    private ImageView icon_stadium;
    private TextView tv_stadiumname;
    private EditText et_content;
    private RatingBar ratingBar;
    private Button btn_submit;
    private Book book;
    private double grade;
    private ImageView icon_back;



    private void initView() {
        icon_stadium = findViewById(R.id.icon_stadium);
        tv_stadiumname = findViewById(R.id.tv_stadiumname);
        ratingBar = findViewById(R.id.ratbar);
        btn_submit = findViewById(R.id.btn_submit);
        et_content = findViewById(R.id.et_content);
        icon_back = findViewById(R.id.icon_back);
        getWindow().setStatusBarColor(Color.parseColor("#FF029ACC"));
    }

    private void initData() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade = (double) ratingBar.getRating();
                String content = et_content.getText().toString();

                EvaluateStadium(book.getStadiumId(),grade,book.getBookingId(),content,book.getUserId(),evaluatetime);

            }
        });
    }

    private void EvaluateStadium(int stadiumId, double grade ,int bookingid,String content,int userId,String time) {
        String orderURL = URL_EVALUATESTADIUM;
        new EvaluateStadiumAsyncTask().execute(orderURL,String.valueOf(stadiumId), String.valueOf(grade), String.valueOf(bookingid),content,String.valueOf(userId),time);
    }




 */