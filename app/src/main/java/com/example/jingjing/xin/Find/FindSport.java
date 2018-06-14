package com.example.jingjing.xin.Find;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jingjing.xin.Adapter.FindAdapter;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Fragment.BookingFragment;
import com.example.jingjing.xin.Fragment.FindFragment;
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

/**
 * Created by jingjing on 2018/6/4.
 */

public class FindSport extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tv_nofind;
    private User user;
    private String city;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.find_sport);

        initView();
        initData();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_back = (RelativeLayout) findViewById(R.id.tv_back);
        tv_title.setText("运动圈");

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe);
        tv_nofind=(TextView)findViewById(R.id.tv_nofind);
        layoutManager=new LinearLayoutManager(this);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//更新
            @Override
            public void onRefresh() {
                initData();
                swipeRefresh.setRefreshing(false);
            }
        });
    }
    private void initData(){
        user= (User) getIntent().getSerializableExtra("user");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findInformation(user,BookingFragment.city);//根据选择的城市来展示相应的动态
    }


    private void findInformation(User user,String city) {//服务器
        String loadingUrl = URL_FINDINFORMATION;
        new findInformationAsyncTask().execute(loadingUrl,String.valueOf(user.getUserId()),city);
    }

    private class findInformationAsyncTask extends AsyncTask<String, Integer, String> {
        public findInformationAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            int method = 1;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("userId",params[1]);
                json.put("method",method);
                json.put("city",params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response=okHttpClient.newCall(request).execute();
                results=response.body().string();
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
            if (!"null".equals(s)){
                try {
                    JSONArray results = new JSONArray(s);
                    for( int i=results.length()-1;i>=0;i--){
                        JSONObject js= results.getJSONObject(i);
                        Need need = new Need();
                        need.setNeedId(js.getInt("needId"));
                        need.setUserId(js.getInt("userId"));
                        need.setUsername(js.getString("username"));
                        need.setStadiumname(js.getString("stadiumname"));
                        need.setTime(js.getString("time"));
                        need.setNum(js.getInt("num"));
                        need.setNum_join(js.getInt("num_join"));
                        //need.setProflie(URL_PROFLIE+js.optString("userproflie"));
                        need.setRemark(js.getString("remark"));
                        need.setReleasetime(js.optString("releasetime"));
                        mData.add(need);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(FindSport.this,DividerItemDecoration.VERTICAL));
                    FindAdapter adapter = new FindAdapter(FindSport.this,mData,user,true);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                tv_nofind.setText("当前没有任何召集信息");
                List<Need> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(FindSport.this,DividerItemDecoration.VERTICAL));
                FindAdapter adapter = new FindAdapter(FindSport.this,mData,user,true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}


