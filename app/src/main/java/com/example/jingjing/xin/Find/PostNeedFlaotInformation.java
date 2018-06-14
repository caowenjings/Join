package com.example.jingjing.xin.Find;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.PostNeedAdapter;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.User;
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

import static com.example.jingjing.xin.constant.Conatant.URL_NEEDINFORMATION;

/**
 * Created by jingjing on 2018/6/1.
 */

public class PostNeedFlaotInformation extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private FrameLayout frameLayout_one;
    private FrameLayout frameLayout_two;
    private FrameLayout frameLayout_three;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab_add_sport;
    private TextView tv_nopostneed;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;



    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mypostneed);
        initView();
        initData();
        super.onCreate(savedInstanceState);

    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("发布的需求");

        frameLayout_one = (FrameLayout) findViewById(R.id.frame_one);
        frameLayout_two = (FrameLayout) findViewById(R.id.frame_two);
        frameLayout_three = (FrameLayout) findViewById(R.id.frame_three);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        fab_add_sport = (FloatingActionButton) findViewById(R.id.fab_add_sport);
        tv_nopostneed = (TextView) findViewById(R.id.no_postneed);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);


        frameLayout_one.removeView(frameLayout_three);
        frameLayout_one.removeView(frameLayout_two);//移除

    }


    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab_add_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostNeedFlaotInformation.this, PostNeedFalot.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("user", user);
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });
        myposrneed(user);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);//刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void refresh(){//刷新
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frameLayout_one.removeView(frameLayout_three);
                        frameLayout_one.removeView(frameLayout_two);//移除布局
                        myposrneed(user);
                    }
                });
            }
        }).start();
    }


    private void myposrneed(User user) {
        String loadingUrl = URL_NEEDINFORMATION;
        new MyPostNeedAsyncTask().execute(loadingUrl,String.valueOf(user.getUserId()));
    }

    private class MyPostNeedAsyncTask extends AsyncTask<String, Integer, String> {

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

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            List<Need> mData = new ArrayList<>();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        Need need = new Need();
                        need.setNeedId(js.getInt("needId"));
                        need.setStadiumname(js.getString("stadiumname"));
                        need.setTime(js.getString("time"));
                        need.setNum(js.getInt("num"));
                        need.setNum_join(js.getInt("num_join"));
                        need.setRemark(js.getString("remark"));
                        mData.add(need);
                    }
                    frameLayout_one.addView(frameLayout_three);//添加布局

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(PostNeedFlaotInformation.this, DividerItemDecoration.VERTICAL));
                    PostNeedAdapter adapter = new PostNeedAdapter(PostNeedFlaotInformation.this, mData);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                frameLayout_one.addView(frameLayout_two);//添加布局
                tv_nopostneed.setText("你还没有约起任何运动，还等什么呢？赶紧约起来吧！");
                List<Need> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(PostNeedFlaotInformation.this, DividerItemDecoration.VERTICAL));
                PostNeedAdapter adapter = new PostNeedAdapter(PostNeedFlaotInformation.this, mData);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(PostNeedFlaotInformation.this, "您还没有发布信息", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
