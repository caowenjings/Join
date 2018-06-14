package com.example.jingjing.xin.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.jingjing.xin.Adapter.GridViewAdapter;
import com.example.jingjing.xin.Adapter.MyPagerAdapter;
import com.example.jingjing.xin.Adapter.StadiumAdapter;
import com.example.jingjing.xin.Banner.MyLoader;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.App;
import com.example.jingjing.xin.Bean.AppGrid;
import com.example.jingjing.xin.Bean.Notice;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.SearchStadium;
import com.example.jingjing.xin.Stadium.SerachSelectDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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

import static com.example.jingjing.xin.constant.Conatant.URL_CITY;
import static com.example.jingjing.xin.constant.Conatant.URL_LOADINGORDER;
import static com.example.jingjing.xin.constant.Conatant.URL_NOTICE;
import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;
import static com.example.jingjing.xin.constant.Conatant.URL_SPORTS;
import static com.example.jingjing.xin.constant.Conatant.URL_SPORTSTYPE;

/**
 * Created by jingjing on 2018/4/24.
 */
public class  BookingFragment extends BaseFragment implements OnBannerListener{

    private Banner banner;
    private ArrayList bannerLists= new ArrayList<>();;

    private ViewPager viewPager;
    private List<View> mViewList;
    private List<App> mDatas;//数据源
    private LinearLayout mDots;//装小圆点的
    private LayoutInflater inflater;//布局服务器：用来找Layout布局的，并且实例化
    private int pageCount;//总的页数
    private int pageSize = 8;//每一页显示的个数
    private int curIndex = 0;//当前显示的是第几页
    private ProgressDialog progressDialog;//提醒弹出框

    private ViewFlipper flipper;//公告
    private List<Notice> testList;
    private int count;
    private TextView tv_city;//城市选择
    private ImageView iv_city;
    private LinearLayout btn_searchstadium;
    private List<String> mCity;
    private SwipeRefreshLayout swipeRefreshLayout;//刷新
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;//用于指定布局方式
    private User user;
    public static String city;//public不同的Fragment也可以使用

    private LocationClient mLocationClient;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.bookingfrgment, null);
        banner = (Banner) view.findViewById(R.id.banner);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mDots = (LinearLayout) view.findViewById(R.id.dots);
        flipper = (ViewFlipper) view. findViewById(R.id.flipper);
        tv_city = (TextView)view.findViewById(R.id.tv_city);
        iv_city = (ImageView)view.findViewById(R.id.iv_city);
        btn_searchstadium = (LinearLayout)view.findViewById(R.id.tv_search);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.sr_booking);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_stadium);
        linearLayoutManager=new LinearLayoutManager(mContext);
        return view;
    }

    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        setBanner();//轮播图
        LoadingGongGao();//下载公告
        LoadingCitys();//获取选择处的城市
        LoadingSportsApp();//获取spotrs图标
        //  requestLocation();//定位
        // mLocationClient = new LocationClient(getContext());
        // mLocationClient.registerLocationListener(new MyLocationListener());


        tv_city.setOnClickListener(new View.OnClickListener() {//选择城市
            @Override
            public void onClick(View v) {
                doSelect(v);

            }
        });
        iv_city.setOnClickListener(new View.OnClickListener() {//选择城市按钮
            @Override
            public void onClick(View v) {
                doSelect(v);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorYellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Loading(tv_city.getText().toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btn_searchstadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SearchStadium.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user",user);
                mBundle.putSerializable("city",tv_city.getText().toString());
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
    }

    //banner设置轮播图
    private void setBanner() {
        bannerLists = new ArrayList<>();
        bannerLists.add(R.drawable.tu_one);
       bannerLists.add(R.drawable.tu_two);
       bannerLists.add(R.drawable.tu_three);

        banner.setDelayTime(3000);//图片间隔时间
        banner.setImages(bannerLists);//加载图片集合
        banner.setImageLoader(new MyLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置格式
        banner.isAutoPlay(true);//自动加载
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器的位置，小点点，左中右
        banner.start(); //开始
        banner.setOnBannerListener(this);
    }
    @Override
    public void OnBannerClick(int position) {
    }


    public void setDots() {
        for (int i = 0; i < pageCount; i++) {
            mDots.addView(inflater.inflate(R.layout.dots, null));//加载布局
        }
        mDots.getChildAt(0).findViewById(R.id.v_dot)  // 默认显示第一页
                .setBackgroundResource(R.drawable.selecet);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {//viewpager的点击事件
            public void onPageSelected(int position) {

                mDots.getChildAt(curIndex)  // 取消圆点选中
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.unselecet);

                mDots.getChildAt(position) // 圆点选中
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.selecet);
                curIndex = position;
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    private void LoadingCitys() {//获取城市
        String url = URL_CITY;
        new CitysAsyncTask().execute(url);

    }
    private class CitysAsyncTask extends AsyncTask<String, Integer, String> {
        public CitysAsyncTask() {
        }
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
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
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            mCity = new ArrayList();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        String city = js.getString("cityname");
                        mCity.add(city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(mContext, "获取城市失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void LoadingSportsApp(){//获取运动图标
        String url = URL_SPORTS;
        new SportsAsyncTask().execute(url);
    }

    @SuppressLint("StaticFieldLeak")
    private class SportsAsyncTask extends AsyncTask<String, Integer, String> {
        public SportsAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
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
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            mDatas = new ArrayList<>();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        App app = new App();
                        app.setName(js.getString("sportsname"));
                        app.setIcon(URL_SPORTSTYPE + js.getString("sportsicon"));
                        mDatas.add(app);
                    }
                    pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
                    mViewList = new ArrayList<>();
                    for (int a = 0; a < pageCount; a++) {
                        final GridView gridView = (GridView) View.inflate(mContext, R.layout.grid_view, null);
                        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//去掉GridView的选择与被选择的默认样式

                        GridViewAdapter gridViewAdapter=new GridViewAdapter(getContext(),mDatas,a,pageSize);
                        gridView.setAdapter(gridViewAdapter);//使用GridView作为每个ViewPager的页面，也就是说每个ViewPager的页面都是inflate出一个GridView新实例

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object obj = gridView.getItemAtPosition(position);
                                Intent intent = new Intent(getActivity(), SearchStadium.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("city", tv_city.getText().toString());
                                mBundle.putSerializable("type", ((App) obj).getName());
                                mBundle.putSerializable("user", user);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                        });
                        mViewList.add(gridView);
                    }
                    MyPagerAdapter pagerAdapter=new MyPagerAdapter(mViewList); //设置适配器
                    viewPager.setAdapter(pagerAdapter);

                    for (int i = 0; i < pageCount; i++) {
                        mDots.addView(LayoutInflater.from(getContext()).inflate(R.layout.dots,null));//加载布局
                    }
                    mDots.getChildAt(0).findViewById(R.id.v_dot)  // 默认显示第一页
                            .setBackgroundResource(R.drawable.selecet);

                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {//viewpager的点击事件
                        public void onPageSelected(int position) {

                            mDots.getChildAt(curIndex)  // 取消圆点选中
                                    .findViewById(R.id.v_dot)
                                    .setBackgroundResource(R.drawable.unselecet);

                            mDots.getChildAt(position) // 圆点选中
                                    .findViewById(R.id.v_dot)
                                    .setBackgroundResource(R.drawable.selecet);
                            curIndex = position;
                        }
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                        }
                        public void onPageScrollStateChanged(int arg0) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(mContext, "获取失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void LoadingGongGao(){//装载公告
        String gonggaoUrl = URL_NOTICE;
        new GongGaoAsyncTask().execute(gonggaoUrl);

    }
    private class GongGaoAsyncTask extends AsyncTask<String, Integer, String> {
        public GongGaoAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("gonggao", 1);
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
            List<Notice> testList = new ArrayList<>();
            if (!TextUtils.isEmpty(s)) {
                try {
                    final JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        Notice notice = new Notice();
                        notice.setContent(js.getString("content"));
                        notice.setTime(js.getString("time"));
                        testList.add(notice);
                        count = testList.size();

                        final View content = View.inflate(getContext(), R.layout.gonggaolan, null);
                        TextView tv_gonggao = (TextView) content.findViewById(R.id.tv_gonggao);
                        ImageView iv_cancel = (ImageView) content.findViewById(R.id.iv_cancel);
                        tv_gonggao.setText(testList.get(i).getContent());//添加公告
                        iv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //对当前显示的视图进行移除
                                flipper.removeView(content);
                                count--;
                                //当删除后仅剩 一条 新闻时，则取消滚动
                                if (count == 1) {
                                    flipper.stopFlipping();
                                }
                            }
                        });
                        flipper.addView(content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                Toast.makeText(getContext(), "目前没有公告", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void Loading(String tv_city) {//根据城市出现场馆
        String loadingUrl = URL_LOADINGORDER;
        new LoadingAsyncTask().execute(loadingUrl, tv_city);
    }

    private class LoadingAsyncTask extends AsyncTask<String, Integer, String> {
        public LoadingAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("city", params[1]);
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
            if (!"null".equals(s)){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js = results.getJSONObject(i);
                        Stadium stadium = new Stadium();
                        stadium.setStadiumId(js.getInt("stadiumId"));
                        stadium.setStadiumname(js.getString("stadiumname"));
                        stadium.setStadiumtype(js.getString("stadiumtypename"));
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE + js.getString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setNum(js.getString("num"));
                        stadium.setOpentime(js.getString("opentime"));
                        stadium.setClosetime(js.getString("closetime"));
                        stadium.setGrade((float) js.getDouble("grade"));
                        stadium.setIconnum(js.getInt("iconnum"));
                        mData.add(stadium);
                    }

                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
                    StadiumAdapter adapter = new StadiumAdapter(mContext,mData,user);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);//适配器

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(linearLayoutManager);//指定布局方式
                StadiumAdapter adapter = new StadiumAdapter(mContext,mData2,user);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(mContext,"该城市上没有体育场所加入",Toast.LENGTH_LONG).show();

            }
        }
    }
    public void doSelect(View view){//搜索列表选项
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(mContext);
        alert.setListData(mCity);
        alert.setTitle("请选择城市");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                tv_city.setText(info);//显示选择的城市
                city=tv_city.getText().toString();//得到选择的城市
                swipeRefreshLayout.post(new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);//更新
                        Loading(tv_city.getText().toString());
                        swipeRefreshLayout.setRefreshing(true);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9,0.9,getActivity());
    }

/*
    private void requestLocation(){//定位
        initLocation();
     mLocationClient.start();

 }

   private void initLocation(){
       LocationClientOption option = new LocationClientOption();
       option.setIsNeedAddress(true);
       option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
       option.setScanSpan(5000);//设置更新的间隔,5秒更新一下当前的位置
       mLocationClient.setLocOption(option);
       //mLocationClient.setLocOption(localLocationClientOption);
   }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//停止定位
    }

    private class MyLocationListener implements BDLocationListener
    {
        private MyLocationListener() {}

        public void onReceiveLocation(final BDLocation BDLocation)
        {

            getActivity().runOnUiThread(new Runnable()
            {
                public void run()
                {
                    System.out.println("12" + BDLocation.getCity());
                    if (BDLocation.getCity().equals("")) {
                        tv_city.setText("城市名");
                    }
                    for (;;)
                    {
                        tv_city.setText(BDLocation.getCity());
                        Loading(tv_city.getText().toString());
                        mLocationClient.stop();

                        return;
                    }
                }
            });
        }
    }*/

}
