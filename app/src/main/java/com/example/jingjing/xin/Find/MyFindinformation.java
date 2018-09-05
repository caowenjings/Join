package com.example.jingjing.xin.Find;

import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jingjing.xin.Adapter.JoinUserAdapter;
import com.example.jingjing.xin.Bean.Book;
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

import static com.example.jingjing.xin.constant.Conatant.URL_JOINEDUSERINFORMATION;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

public class MyFindinformation extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView tv_nobooking;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView no_book;
    private FrameLayout frame_one;
    private FrameLayout frame_wu;
    private FrameLayout frame_you;
    private Need need;
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
        setContentView(R.layout.userorder_information);
        initView();
        initData();
    }

    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("消 息");


        tv_nobooking = (TextView)findViewById(R.id.tv_nobooking);
        no_book = (ImageView) findViewById(R.id.no_book);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        frame_one=(FrameLayout)findViewById(R.id.frame_one);
        frame_wu=(FrameLayout)findViewById(R.id.frame_wu);
        frame_you=(FrameLayout)findViewById(R.id.frame_you);
        layoutManager = new LinearLayoutManager(this);

        frame_one.removeView(frame_wu);
        frame_one.removeView(frame_you);//移
    }
    private void  initData(){
        user = (User)getIntent().getSerializableExtra("user");
        need = (Need) getIntent().getSerializableExtra("need");

        tv_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

        myfindinformation(need.getNeedId());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                frame_one.removeView(frame_wu);
                frame_one.removeView(frame_you);
                myfindinformation(need.getNeedId());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    private void myfindinformation(int needId){
        String url = URL_JOINEDUSERINFORMATION;
        new MyfindinformaionAsyncTask().execute(url, String.valueOf(needId));

    }

    private class MyfindinformaionAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("needId", params[1]);
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
            List<User> mData = new ArrayList<>();
            if (!"null".equals(s)){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=results.length()-1;i>=0;i--){
                        JSONObject js= results.getJSONObject(i);
                        User user = new User();
                        user.setUsername(js.getString("username"));
                        user.setSex(js.getString("sex"));
                        user.setTel(js.getString("tel"));
                        user.setProflie(URL_PROFLIE+js.optString("userproflie"));
                        mData.add(user);
                    }
                    frame_one.addView(frame_you);//添加布局
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(MyFindinformation.this,DividerItemDecoration.VERTICAL));
                    JoinUserAdapter adapter = new JoinUserAdapter(MyFindinformation.this,mData);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<User> mData2 = new ArrayList<>();
                frame_one.addView(frame_wu);//添加布局
                no_book.setVisibility(View.GONE);
                tv_nobooking.setText("目前没有动友加入你哟！");
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(MyFindinformation.this,DividerItemDecoration.VERTICAL));
                JoinUserAdapter adapter = new JoinUserAdapter(MyFindinformation.this,mData2);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
