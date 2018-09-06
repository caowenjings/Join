package com.example.jingjing.xin.Stadium;

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
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.StadiumAdapter;
import com.example.jingjing.xin.Bean.Stadium;
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

import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;
import static com.example.jingjing.xin.constant.Conatant.URL_SEARCHCOLLECTSTADIUM;

/**
 * Created by jingjing on 2018/5/23.
 */
//从数据库里面拿数据
public class StadiumCollection extends AppCompatActivity {

    private TextView tv_title;
    private RelativeLayout tv_back;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView no_join;
    private FrameLayout frame_one;
    private FrameLayout frame_wu;
    private FrameLayout frame_you;
    private TextView tv_text;
    private Stadium stadium;
    private User user;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.stadium_collection);

        initView();
        initData();

    }

    private void  initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("我的收藏");

        recyclerView =(RecyclerView)findViewById(R.id.rv_stadiumcollection);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        frame_one = (FrameLayout)findViewById(R.id.framelayout_one);
        frame_wu = (FrameLayout)findViewById(R.id.framelayout_wu);
        frame_you = (FrameLayout)findViewById(R.id.framelayout_you);
        tv_text = (TextView)findViewById(R.id.tv_text);
        no_join = (ImageView)findViewById(R.id.no_find);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(StadiumCollection.this,DividerItemDecoration.VERTICAL));

        frame_one.removeView(frame_you);//移除
        frame_one.removeView(frame_wu);
    }
    private void initData(){
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        user = (User) getIntent().getSerializableExtra("user");

        tv_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                frame_one.removeView(frame_wu);
                frame_one.removeView(frame_you);
                stadiumcollection(user.getUserId());
                swipeRefreshLayout.setRefreshing(false);//结束
            }
        });

        stadiumcollection(user.getUserId());
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

    private  void stadiumcollection(int userId){
        String SearchUrl = URL_SEARCHCOLLECTSTADIUM;
        new StadiumCollection.StadiumCollectionAsyncTask().execute(SearchUrl,String.valueOf(userId));
    }

    private  class  StadiumCollectionAsyncTask  extends AsyncTask<String ,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("userId",params[1]);//访问服务器的所需参数
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
            List<Stadium> mData = new ArrayList<>();
            if (!"null".equals(s) && s != null){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js= results.getJSONObject(i);
                        Stadium stadium = new Stadium();
                        stadium.setStadiumId(js.getInt("stadiumId"));
                        stadium.setStadiumname(js.getString("stadiumname"));
                        stadium.setStadiumtype(js.getString("stadiumtypename"));
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE+js.optString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setNum(js.getString("num"));
                        stadium.setGrade((float)js.getDouble("grade"));
                        stadium.setIconnum(js.getInt("iconnum"));
                        stadium.setOpentime(js.getString("opentime"));
                        stadium.setClosetime(js.getString("closetime"));
                        stadium.setStadiumtel(js.getString("stadiumtel"));
                        mData.add(stadium);
                    }
                    frame_one.addView(frame_you);//添加布局
                    recyclerView.setLayoutManager(linearLayoutManager);
                    StadiumAdapter adapter = new StadiumAdapter(StadiumCollection.this,mData,user);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);//适配器

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                frame_one.addView(frame_wu);//添加布局
                no_join.setVisibility(View.GONE);
                tv_text.setText("目前你还没有收藏场馆哟！");
                recyclerView.setLayoutManager(linearLayoutManager);//指定布局方式
                StadiumAdapter adapter = new StadiumAdapter(StadiumCollection.this,mData2,user);
                recyclerView.setAdapter(adapter);
                Toast.makeText(StadiumCollection.this,"该场馆没有收藏",Toast.LENGTH_LONG).show();
            }
        }
    }
}