package com.example.jingjing.xin.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Activity.MainActivity;
import com.example.jingjing.xin.Adapter.FindAdapter;
import com.example.jingjing.xin.Adapter.PostNeedAdapter;
import com.example.jingjing.xin.Banner.MyLoader;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Find.FindSport;
import com.example.jingjing.xin.Find.PostNeed;
import com.example.jingjing.xin.Find.PostNeedFalot;
import com.example.jingjing.xin.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_FINDINFORMATION;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

/**
 * Created by jingjing on 2018/4/24.
 */

public class FindFragment extends BaseFragment  implements OnBannerListener{

    private Banner find_banner;
    private ArrayList findlists;
    private LinearLayout add_sport;
    private LinearLayout find_soprt;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tv_nofind;
    private User user;
    private String city;
    private LocationClient mLocationClient;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.findfragment, null);
        find_banner = (Banner) view.findViewById(R.id.baner_find);
        add_sport = (LinearLayout) view.findViewById(R.id.add_sport);
        find_soprt=(LinearLayout)view.findViewById(R.id.find_sport);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        tv_nofind=(TextView)view.findViewById(R.id.tv_nofind);
        layoutManager=new LinearLayoutManager(getContext());
        return  view;
    }

    @Override
    protected void initData() {
        setfindBanner();//轮播图

        user = (User) getActivity().getIntent().getSerializableExtra("user");


        add_sport.setOnClickListener(new View.OnClickListener() {//发布需求
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostNeedFalot.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("user",user);
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });

        find_soprt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FindSport.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("user",user);
                mbundle.putSerializable("city", "成都市");
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });

        findInformation(user,BookingFragment.city);//根据选择的城市来展示相应的动态

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//更新
            @Override
            public void onRefresh() {
                initData();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    //banner轮播图
    private void setfindBanner() {
        findlists = new ArrayList<>();
        findlists.add(R.drawable.find_one);
        findlists.add(R.drawable.find_two);
        findlists.add(R.drawable.find_three);

        find_banner.setDelayTime(3000);//图片间隔时间
        find_banner.setImages(findlists);//加载图片集合
        find_banner.setImageLoader(new MyLoader());
        find_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置格式
        find_banner.isAutoPlay(true);//自动加载
        find_banner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR);//设置指示器的位置，小点点，左中右
        find_banner.start(); //开始
        find_banner.setOnBannerListener(this);
    }
    @Override
    public void OnBannerClick(int position) {
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
                    for(int i=results.length()-1;i>=0;i--){
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
                    recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
                    FindAdapter adapter = new FindAdapter(mContext,mData,user,true);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空111");
                List<Need> mData2 = new ArrayList<>();
                tv_nofind.setText("暂无动态");
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
                FindAdapter adapter = new FindAdapter(mContext,mData2,user,true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
