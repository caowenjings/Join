package com.example.jingjing.xin.Find;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.example.jingjing.xin.Adapter.FindAdapter;
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

import static com.example.jingjing.xin.constant.Conatant.URL_FINDINFORMATION;
import static com.example.jingjing.xin.constant.Conatant.URL_NEEDINFORMATION;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

/**
 * Created by jingjing on 2018/6/3.
 */

public class JoinNeedInformation extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private TextView  tv_nofind;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.joinneedinformation);

        initView();
        initDate();
    }

    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("加入需求");

        tv_nofind=(TextView)findViewById(R.id.tv_nofind);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(JoinNeedInformation.this,DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                joinedneed(user.getUserId());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initDate(){

        user = (User) getIntent().getSerializableExtra("user");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        joinedneed(user.getUserId());
        setSwipeRefreshLayout(swipeRefreshLayout);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {//解决刷新冲突问题
        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                if (recyclerView == null) {
                    return false;
                }
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0;
            }
        });
    }

    private void joinedneed(int userId) {
        String SearchUrl = URL_FINDINFORMATION;
        new joinedneedAsyncTask().execute(SearchUrl, String.valueOf(userId));
    }

    private class joinedneedAsyncTask extends AsyncTask<String, Integer, String> {
        public joinedneedAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            int method = 2;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                json.put("method",method);
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
            System.out.println("返回的数据："+s);
            List<Need> mData = new ArrayList<>();
            if (!"null".equals(s) && s != null){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js= results.getJSONObject(i);
                        Need need = new Need();
                        need.setNeedId(js.getInt("needId"));
                        need.setUserId(js.getInt("userId"));
                        need.setUsername(js.getString("username"));
                        need.setStadiumname(js.getString("stadiumname"));
                        need.setTime(js.getString("time"));
                        need.setNum(js.getInt("num"));
                        need.setNum_join(js.getInt("num_join"));
                        need.setProflie(URL_PROFLIE+js.optString("userproflie"));
                        need.setRemark(js.getString("remark"));
                        mData.add(need);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    FindAdapter adapter = new FindAdapter(JoinNeedInformation.this,mData,user,false);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                tv_nofind.setText("你还没有参加任何运动，还等什么呢？赶紧加入我们吧！");
                List<Need> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(layoutManager);
                FindAdapter adapter = new FindAdapter(JoinNeedInformation.this,mData2,user,false);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}