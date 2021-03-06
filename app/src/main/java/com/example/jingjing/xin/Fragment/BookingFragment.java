package com.example.jingjing.xin.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.bumptech.glide.Glide;
import com.example.jingjing.xin.Adapter.GridViewAdapter;
import com.example.jingjing.xin.Adapter.MyPagerAdapter;
import com.example.jingjing.xin.Adapter.StadiumAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.App;
import com.example.jingjing.xin.Bean.Notice;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.SearchStadium;
import com.example.jingjing.xin.Stadium.SerachSelectDialog;
import com.example.jingjing.xin.Stadium.StadiumActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.bingoogolapple.bgabanner.BGABanner;
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
public class  BookingFragment extends BaseFragment {

    private BGABanner  mContentBanner;

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
        mContentBanner = (BGABanner)view.findViewById(R.id.bgabanner);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mDots = (LinearLayout) view.findViewById(R.id.dots);
        flipper = (ViewFlipper) view. findViewById(R.id.flipper);
        tv_city = (TextView)view.findViewById(R.id.tv_city);
        iv_city = (ImageView)view.findViewById(R.id.iv_city);
        btn_searchstadium = (LinearLayout)view.findViewById(R.id.tv_search);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.sr_booking);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_stadium);
        linearLayoutManager=new LinearLayoutManager(mContext);
        mLocationClient = new LocationClient(mContext);//接收全局的参数Context
        return view;
    }

    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        setBGAbanner();//轮播图
        LoadingGongGao();//下载公告和获取选择处的城市
        LoadingSportsApp();//获取spotrs图标
        requestLocation();//定位
        mLocationClient.registerLocationListener(new MyLocationListener());//定位监听

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

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//如丝般滑动
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorYellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//刷新
                Loading(tv_city.getText().toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btn_searchstadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转搜索界面
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
    private void setBGAbanner(){
        mContentBanner.setAdapter(new BGABanner.Adapter<ImageView,String>() {//加载服务器图片
        @Override
        public void fillBannerItem(BGABanner banner, ImageView imageView, @Nullable String model, int position) {
            Glide.with(BookingFragment.this)
                    .load(model)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageView);
        }
    });
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
            mDatas = new ArrayList<>();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        App app = new App();
                        app.setName(js.getString("sportsname"));
                        app.setIcon(URL_SPORTSTYPE + js.optString("sportsicon"));
                        mDatas.add(app);
                    }

                    pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);//计算页数
                    mViewList = new ArrayList<>();
                    for (int a = 0; a < pageCount; a++) {
                        //每个页面都是inflate出一个新实例
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

                    for (int i = 0; i < pageCount; i++) {//小圆点
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


    private void LoadingGongGao(){//装载公告和获取城市
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
            List<Notice>  testList = new ArrayList<>();
            if (!TextUtils.isEmpty(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    JSONArray noticeResults = results.getJSONArray("notice");
                    for (int i = 0; i < noticeResults.length(); i++) {
                        JSONObject js = noticeResults.getJSONObject(i);
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
                        flipper.addView(content); //向viewFlipper中动态添加View
                    }

                    mCity = new ArrayList();
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
                Toast.makeText(getContext(), "目前没有公告", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void Loading(String tv_city) {//根据选择的城市出现场馆
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
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext, R.style.ThemeDialog);
            progressDialog.setMessage("加载中，请稍后....");
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据："+s);
            final List<Stadium> mData = new ArrayList<>();
            if (!"null".equals(s) && s != null){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js = results.getJSONObject(i);
                        Stadium stadium = new Stadium();
                        stadium.setStadiumId(js.getInt("stadiumId"));
                        stadium.setStadiumname(js.getString("stadiumname"));
                        stadium.setStadiumtype(js.getString("stadiumtypename"));
                        stadium.setStadiumtel(js.optString("stadiumtel"));//没有就设置为null
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE + js.optString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setNum(js.getString("num"));
                        stadium.setOpentime(js.getString("opentime"));
                        stadium.setClosetime(js.getString("closetime"));
                        stadium.setGrade((float) js.getDouble("grade"));
                        stadium.setIconnum(js.getInt("iconnum"));
                        mData.add(stadium);
                    }

                    //banner数据源
                    List<String> mbagbanner = new ArrayList<>();//给banner加载图片，来自于场馆的图片
                    for(int i=0; i<3;i++){
                        mbagbanner.add(mData.get(i).getMainpicture());
                    }
                    List<String>mbagbanner1 = new ArrayList<>();
                    for(int i=0; i<3;i++){
                        mbagbanner1.add(mData.get(i).getStadiumname());
                    }

                   mContentBanner.setData(mbagbanner,mbagbanner1);
                   mContentBanner.setDelegate(new BGABanner.Delegate() {
                        @Override
                        public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {//点击事件
                            Stadium stadium = mData.get(position);
                            Intent intent = new Intent(getContext(), StadiumActivity.class);
                            Bundle mbundle = new Bundle();
                            mbundle.putSerializable("user",user);
                            mbundle.putSerializable("stadium",stadium);
                            intent.putExtras(mbundle);
                            startActivity(intent);
                        }
                    });
                    
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
                    StadiumAdapter adapter = new StadiumAdapter(mContext,mData,user);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);//适配器
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            progressDialog.dismiss();
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0,2000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(linearLayoutManager);//指定布局方式
                StadiumAdapter adapter = new StadiumAdapter(mContext,mData2,user);
                recyclerView.setAdapter(adapter);
                Toast.makeText(mContext,"该城市上没有体育场所加入",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    public void   doSelect(View view){//搜索列表选项城市
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


    private void requestLocation(){//开始定位
        initLocation();
        mLocationClient.start();

    }

    private void initLocation(){//实时更新位置
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mLocationClient.setLocOption(option);
    }


    private class MyLocationListener implements BDLocationListener {//定位的结果给注册的这个监听器中
        private MyLocationListener() {}

        public void onReceiveLocation(final BDLocation BDLocation)
        {

            getActivity().runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if ("".equals(BDLocation.getCity())) {
                        tv_city.setText("城市名");
                    }else {
                        tv_city.setText(BDLocation.getCity());//BDLocation调用方法
                        city=tv_city.getText().toString();//得到选择的城市
                        Loading(city);//调用服务器
                        mLocationClient.stop();
                        return;
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//停止定位
    }
}


//    private void LoadingCitys() {//获取城市
//        String url = URL_CITY;
//        new CitysAsyncTask().execute(url);
//    }
//    private class CitysAsyncTask extends AsyncTask<String, Integer, String> {
//        public CitysAsyncTask() {
//        }
//        @Override
//        protected String doInBackground(String... params) {
//            Response response = null;
//            String results = null;
//            JSONObject json = new JSONObject();
//            try {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
//                Request request = new Request.Builder()
//                        .url(params[0])
//                        .post(requestBody)
//                        .build();
//                response = okHttpClient.newCall(request).execute();
//                results = response.body().string();
//                //判断请求是否成功
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return results;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            System.out.println("返回的数据：" + s);
//            mCity = new ArrayList();
//            if (!"null".equals(s)) {
//                try {
//                    JSONArray results = new JSONArray(s);
//                    for (int i = 0; i < results.length(); i++) {
//                        JSONObject js = results.getJSONObject(i);
//                        String city = js.getString("cityname");
//                        mCity.add(city);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("结果为空");
//                Toast.makeText(mContext, "获取城市失败", Toast.LENGTH_LONG).show();
//            }
//        }
//    }