package com.example.jingjing.xin.Stadium;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.GridPictureAdapter;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_LOADINGICON;
import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;

public class StadiumPicture extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private Stadium stadium;
    private GridView gridView;
    private List<String> mData;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.stadium_picture);

        initView();
        initDate();
    }

    private void initView(){

        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("场馆图片");
        gridView = (GridView)findViewById(R.id.gv_stadiumpicture);

    }

    private void initDate(){

        stadium = (Stadium) getIntent().getSerializableExtra("stadium");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stadiumpicture(stadium.getStadiumId());
    }

    private void stadiumpicture(int stadiumId){
        String loginurl = URL_LOADINGICON;
        new StadiumPictureAsyncTask().execute(loginurl,String.valueOf(stadiumId));

    }

    private class StadiumPictureAsyncTask  extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
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
            System.out.println("返回的数据：" + s);
            mData = new ArrayList<>();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        String icon = URL_PICTURE+js.getString("icon");
                        mData.add(icon);
                    }
                    GridPictureAdapter gridIconAdapter = new GridPictureAdapter(StadiumPicture.this,mData);
                    gridView.setAdapter(gridIconAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PictureDialog std = new PictureDialog(mData.get(position));//点击时弹出一个弹框
                            std.show(getFragmentManager(), "adaPicker");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(StadiumPicture.this, "获取失败", Toast.LENGTH_LONG).show();

            }
        }
    }
}
