package com.example.jingjing.xin.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Adapter.FindAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Find.FindSport;
import com.example.jingjing.xin.Find.MyFindinformation;
import com.example.jingjing.xin.Find.PostNeedFalot;
import com.example.jingjing.xin.Find.SetNumDialog;
import com.example.jingjing.xin.R;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

public class FindFragment extends BaseFragment{

    private LinearLayout add_sport;
    private LinearLayout find_soprt;
    private LinearLayout find_information;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tv_nofind;
    private User user;
    private Need need;
    private String city;

    private TextView tv_tianqi;
    private TextView tv_wendu;
    private TextView tv_fengli;
    private TextView tv_city;
    private TextView tv_pm;


    //利用和风天气
    private String API = "https://free-api.heweather.com/s6/weather/now?key=11e895a6b3854f0fb49508eea65df6ca&location=";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.findfragment, null);
        tv_city =(TextView) view.findViewById(R.id.mian_city);
        tv_tianqi =(TextView) view.findViewById(R.id.main_tianqi);
        tv_fengli = (TextView)view.findViewById(R.id.main_fengli);
        tv_wendu =(TextView) view.findViewById(R.id.main_wendu);
        add_sport = (LinearLayout) view.findViewById(R.id.add_sport);
        find_soprt=(LinearLayout)view.findViewById(R.id.find_sport);
        find_information=(LinearLayout)view.findViewById(R.id.find_information);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        tv_nofind=(TextView)view.findViewById(R.id.tv_nofind);
        layoutManager=new LinearLayoutManager(getContext());
        return  view;
    }
    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        need = (Need) getActivity().getIntent().getSerializableExtra("need");

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

        find_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyWeather().execute(API + BookingFragment.city);
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
                        need.setProflie(URL_PROFLIE+js.optString("userproflie"));
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

    class MyWeather extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;
                if (httpURLConnection.getResponseCode() == 200) {
                    inputStream = httpURLConnection.getInputStream();//检测网络异常
                } else {
                    return "11";
                }
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                stringBuffer = new StringBuffer();
                String timp = null;
                while ((timp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(timp);
                }
                inputStream.close();
                reader.close();
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("11")) {
                try {
                    JSONObject object = new JSONObject(s);//解析得到数据
                    JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);//取出
                    JSONObject basic = object1.getJSONObject("basic");
                    JSONObject now = object1.getJSONObject("now");
                    String city = basic.getString("location");//根据对应的键，得到对应的值
                    String tianqi = now.getString("cond_txt");
                    String wendu = now.getString("tmp");
                    String fengli = now.getString("wind_dir");
                    String qiangdu = now.getString("wind_sc");
                    //String pm = now.getString("pm25");
                    tv_city.setText(city);
                    tv_tianqi.setText("今天是" + "“" + tianqi + "”" + "哦，主人");
                    tv_wendu.setText(wendu + "℃");
                    tv_fengli.setText(fengli + qiangdu + "级");
                    //tv_pm.setText("PM:"+pm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
