package com.example.jingjing.xin.Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Adapter.FindAdapter;
import com.example.jingjing.xin.Adapter.SearchSelectAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.Notice;
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
import static com.example.jingjing.xin.constant.Conatant.URL_NOTICE;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

/**
 * Created by jingjing on 2018/4/24.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener{

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
    private List<String> mCity;


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

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//更新
            @Override
            public void onRefresh() {
                initData();
                swipeRefresh.setRefreshing(false);
            }
        });

        return  view;
    }
    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        need = (Need) getActivity().getIntent().getSerializableExtra("need");

        add_sport.setOnClickListener(this);
        find_soprt.setOnClickListener(this);
        find_information.setOnClickListener(this);

        findInformation(user,BookingFragment.city);//根据首页选择的城市来展示相应的动态
        new MyWeather().execute(API + BookingFragment.city);//调用天气
        LoadingGongGao();//获取城市
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_sport://发布需求
                Intent intent1 = new Intent(getContext(), PostNeedFalot.class);
                Bundle mbundle1 = new Bundle();
                mbundle1.putSerializable("user",user);
                intent1.putExtras(mbundle1);
                startActivity(intent1);
                break;
            case R.id.find_sport://运动圈
                Intent intent = new Intent(getContext(), FindSport.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("user",user);
                mbundle.putSerializable("city", "成都市");
                intent.putExtras(mbundle);
                startActivity(intent);
                break;
            case R.id.find_information://天气
                showPasswordSetDailog();
                break;
        }
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
            if (!"null".equals(s) && s != null){
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

    class MyWeather extends AsyncTask<String, String, String> {//天气
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

    //天气的弹框
    private void showPasswordSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getContext(), R.layout.tianqi_dialog, null);  // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0

        final Button btn_sure =(Button) view.findViewById(R.id.btn_tianqi);
        final ImageView iv_back = (ImageView)view.findViewById(R.id.iv_title);
        final TextView iv_title = (TextView) view.findViewById(R.id.tv_title);
        final EditText et_tianqi = (EditText)view.findViewById(R.id.et_tianqi);
        final FrameLayout frame_one = (FrameLayout)view.findViewById(R.id.framee_one);
        final FrameLayout frame_xuan = (FrameLayout)view.findViewById(R.id.frame_xuan);
        final FrameLayout frame_sou = (FrameLayout)view.findViewById(R.id.frame_sou);
        final Button btn_sou = (Button)view.findViewById(R.id.btn_sou);
        final ListView list_city = (ListView) view.findViewById(R.id.list_city);
        iv_title.setText("请输入城市名");

        frame_one.removeView(frame_sou);

        btn_sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_one.removeView(frame_xuan);
                frame_one.addView(frame_sou);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                String city_tianqi = et_tianqi.getText().toString();
                new MyWeather().execute(API + city_tianqi);
                dialog.dismiss();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mCity);
        list_city.setAdapter(adapter);
        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String content = textView.getText().toString();
                    new MyWeather().execute(API + content);
                    dialog.dismiss();
                }
            }
        });
    }



    private void LoadingGongGao(){//获取城市显示出来
        String gonggaoUrl = URL_NOTICE;
        new cityAsyncTask().execute(gonggaoUrl);
    }
    private class cityAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(params[0])
                        .get()
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
                //判断请求是否成功
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            mCity = new ArrayList();
            if (!TextUtils.isEmpty(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    JSONArray cityresults = results.getJSONArray("city");
                    for (int i = 0; i < cityresults.length(); i++) {
                        JSONObject js = cityresults.getJSONObject(i);
                        String city = js.getString("cityname");
                        mCity.add(city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(getContext(), "目前没有城市", Toast.LENGTH_LONG).show();
            }
        }
    }
}
