package com.example.jingjing.xin.Stadium;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;
import static com.example.jingjing.xin.constant.Conatant.URL_SELECTEVALUATION;

public class MyEvaluate extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private TextView tv_stadiumname;
    private ImageView iv_stadiumpicture;
    private EditText et_evaluate;
    private RatingBar rb_ratbar;
    private TextView tv_grade;
    private double grade;
    private User user;
    private Book book;
    //private int myear,mmonth,mday;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_evaluate);

        initView();
        initData();
    }

    private void initView(){

        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("评价");

        rb_ratbar=(RatingBar)findViewById(R.id.rb_ratbar);
        et_evaluate= (EditText) findViewById(R.id.et_evaluate);
        tv_stadiumname= (TextView) findViewById(R.id.tv_stadiumname);
        iv_stadiumpicture= (ImageView) findViewById(R.id.iv_stadiumpicture);
        tv_grade= (TextView) findViewById(R.id.tv_grade);
    }

    private void initData(){
        book = (Book) getIntent().getSerializableExtra("book");
        tv_stadiumname.setText(book.getStadiumname());

        System.out.println(book.getStadiumpicture());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(100)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(URL_PICTURE+book.getStadiumpicture(),iv_stadiumpicture,options);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myevaluate(book.getBookingId());
    }

    private  void  myevaluate(int bookingId){
        String loginurl = URL_SELECTEVALUATION;
        new MyEvaluateAsyncTask().execute(loginurl,String.valueOf(bookingId));

    }

    private class MyEvaluateAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("bookingId",params[1]);
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
                    if (!loginresult.equals("0")) {
                        float grade = (float)results.getDouble("grade");
                        String content = results.getString("content");
                        rb_ratbar.setRating(grade);
                        tv_grade.setText("评价分数:"+grade);
                        et_evaluate.setText(content);
                    } else {
                        Toast.makeText(MyEvaluate.this, "获取失败,请重试", Toast.LENGTH_SHORT).show();
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
