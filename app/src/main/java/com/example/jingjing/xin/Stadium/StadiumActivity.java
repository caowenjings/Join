package com.example.jingjing.xin.Stadium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jingjing.xin.Adapter.StadiumEvaluateAdapter;
import com.example.jingjing.xin.Bean.Evaluation;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static com.example.jingjing.xin.constant.Conatant.URL_DELETECOLLECTION;
import static com.example.jingjing.xin.constant.Conatant.URL_GETEVALUATEINFORMATION;
import static com.example.jingjing.xin.constant.Conatant.URL_INSERTCOLLECTION;
import static com.example.jingjing.xin.constant.Conatant.URL_ISCOLLECTED;
import static com.example.jingjing.xin.constant.Conatant.URL_LOADINGORDER;
import static com.example.jingjing.xin.constant.Conatant.URL_PROFLIE;

/**
 * Created by jingjing on 2018/5/21.
 */

public class StadiumActivity extends AppCompatActivity  implements View.OnClickListener{

    private TextView tv_stadiumname;
    private TextView tv_stadiumname1;
    private TextView tv_area;
    private TextView tv_stadiumtype;
    private TextView tv_stadiumtel;
    private TextView tv_num;
    private TextView tv_indoor;
    private TextView tv_aircondition;
    private TextView tv_adress;
    private TextView tv_opentime;
    private TextView tv_evaluate_num;
    private TextView tv_noevaluate;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Stadium stadium;
    private ImageView tv_back;
    private ImageView btn_share;
    private ImageView iv_stadiumpicture;
    private ImageView iv_tel;
    private TextView tv_picture_num;
    private RatingBar ratingBar;
    private Button btn_order;
    private User user;
    private boolean firstcollect = true;
    private boolean deletecollect = true;
    private ToggleButton btn_collection;
    private String userId, stadiumId;
   // public static final String PACKEGE_BAIDU="com.baidu.BaiduMap";//百度地图安装包


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.stadium_information);
        initview();
        initdata();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initview() {
        tv_stadiumname = (TextView) findViewById(R.id.tv_stadiumname);
        tv_back = (ImageView) findViewById(R.id.iv_back);
        btn_share = (ImageView) findViewById(R.id.btn_share);
        iv_stadiumpicture = (ImageView) findViewById(R.id.icon_stadium);
        iv_tel =(ImageView)findViewById(R.id.iv_tel);
        tv_stadiumname1 = (TextView) findViewById(R.id.tv_stadiumname1);
        tv_stadiumtype = (TextView) findViewById(R.id.tv_stadiumtype);
        tv_stadiumtel = (TextView) findViewById(R.id.tv_stadiumtel);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_indoor = (TextView) findViewById(R.id.tv_indoor);
        tv_aircondition = (TextView) findViewById(R.id.tv_aircondition);
        tv_opentime = (TextView) findViewById(R.id.tv_opentime);
        tv_adress = (TextView) findViewById(R.id.tv_stadiumaddress);
        ratingBar = (RatingBar) findViewById(R.id.rb_ratbar);
        tv_evaluate_num= (TextView) findViewById(R.id.tv_number);
        tv_picture_num = (TextView) findViewById(R.id.tv_picture_num);
        tv_noevaluate = (TextView) findViewById(R.id.tv_noevaluate);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_collection = (ToggleButton) findViewById(R.id.btn_collection);
        layoutManager = new LinearLayoutManager(this);

    }


    private void initdata() {
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        user = (User) getIntent().getSerializableExtra("user");
        stadiumId = String.valueOf(stadium.getStadiumId());
        userId = String.valueOf(user.getUserId());
        iscollection(stadium.getStadiumId(),user.getUserId());//调用方法
        evaluate(stadium.getStadiumId());

        System.out.println("userId:" + user.getUserId());
        tv_stadiumname.setText(stadium.getStadiumname());
        tv_stadiumname1.setText(stadium.getStadiumname());
        tv_stadiumtype.setText(stadium.getStadiumtype());
        tv_area.setText(stadium.getArea() + "平方米");
        tv_num.setText(stadium.getNum() + "人");
        tv_picture_num.setText(String.valueOf(stadium.getIconnum()));//强制转换
        tv_opentime.setText(stadium.getOpentime());
        ratingBar.setRating(stadium.getGrade());
        ratingBar.setIsIndicator(true);
        tv_stadiumtel.setText(stadium.getStadiumtel());
        if (stadium.getIndoor() == 1) {
            tv_indoor.setText(" 是");
        } else {
            tv_indoor.setText(" 否");
        }
        if (stadium.getAircondition() == 1) {
            tv_aircondition.setText(" 是");
        } else {
            tv_aircondition.setText(" 否");
        }
        tv_adress.setText(stadium.getCity() + stadium.getAdress());

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.load)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(1000)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(stadium.getMainpicture(), iv_stadiumpicture, options);

        btn_collection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_collection.setBackgroundResource(isChecked ? R.drawable.stadium_collection1 :R.drawable.stadium_collection);
                if (buttonView.isChecked()) {
                    if (firstcollect) {
                        collection(stadium.getStadiumId(), user.getUserId(), true);
                     //   Toast.makeText(StadiumActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } else {
                    if (deletecollect) {
                        collection(stadium.getStadiumId(), user.getUserId(), false);
                     //   Toast.makeText(StadiumActivity.this, "取消收藏", Toast.LENGTH_LONG).show();
                    } else {

                    }
                }
            }
        });

            btn_order.setOnClickListener(this);
            btn_share.setOnClickListener(this);
            iv_stadiumpicture.setOnClickListener(this);
            iv_tel.setOnClickListener(this);
            tv_back.setOnClickListener(this);
            tv_adress.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_order:
                Intent intent = new Intent(StadiumActivity.this, StadiumOrder.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", user);
                mBundle.putSerializable("stadium", stadium);
                intent.putExtras(mBundle);
                startActivity(intent);
                break;

            case R.id.btn_share:
                String text = stadium.getStadiumname()
                        +"\n地址："
                        +tv_adress.getText().toString()
                        +"\n项目："+stadium.getStadiumtype()
                        +"\n场馆面积："+tv_area.getText().toString()
                        +"\n营业时间："+tv_opentime.getText().toString()
                        +"\n评分："+stadium.getGrade();
                shareText("分享场馆","热点",text);
                // shareImg("分享场馆","热点",stadium.getStadiumname(),stadium.getMainpicture());
                break;

            case R.id.iv_stadiumpicture:
                Intent intent1 = new Intent(StadiumActivity.this, StadiumPicture.class);
                Bundle mBundle1 = new Bundle();
                mBundle1.putSerializable("user", user);
                mBundle1.putSerializable("stadium", stadium);
                intent1.putExtras(mBundle1);
                startActivity(intent1);
                break;

            case R.id.iv_tel:
                showPasswordSetDailog();
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_stadiumaddress:
                if(isInstallBaidu("com.baidu.BaiduMap")){//传入指定应用包名
                    try {
                        Intent intent2 = Intent.getIntent("intent://map/direction?destination="+stadium.getAdress() + // 终点
                                "&mode=driving&" + // 导航路线方式
                                "&src=#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent2); // 启动调用
                    } catch (Exception e) {
                    }
                } else{//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    Toast.makeText(StadiumActivity.this, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent   intent3 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent3); //启动调用
                }
                break;
                default:
                    break;
        }
    }

    private boolean isInstallBaidu(String packageName) {//判断是否安装了百度地图
        return new File("/data/data/" + packageName).exists();
    }


    private void  collection(int stadiunmId, int userId, boolean flag) {//收藏
        String SearchUrl = null;
        if (flag) {
            SearchUrl = URL_INSERTCOLLECTION;//添加收藏
        } else {
            SearchUrl = URL_DELETECOLLECTION;//取消收藏
        }
        new StadiumCollectionAsyncTask().execute(SearchUrl, String.valueOf(stadiunmId), String.valueOf(userId));
    }

    private class StadiumCollectionAsyncTask extends AsyncTask<String, Integer, String> {
        public StadiumCollectionAsyncTask(){
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                json.put("userId", params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
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
            if (!"".equals(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if (loginresult.equals("1")) {
                        Toast.makeText(StadiumActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else if (loginresult.equals("2")) {
                        Toast.makeText(StadiumActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    } else if (loginresult.equals("3")) {

                    } else {
                        Toast.makeText(StadiumActivity.this, "系统异常", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
            }
        }
    }



    private  void iscollection(int stadiumId, int userId){//记住收藏的状态
        String SearchUrl = URL_ISCOLLECTED;
        new IscollectionAsyncTask().execute(SearchUrl,String.valueOf(stadiumId),String.valueOf(userId));

    }
    private class IscollectionAsyncTask extends AsyncTask<String,Integer,String>{
        public IscollectionAsyncTask(){

        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                json.put("userId", params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
           if(s != null){
               try {
                   JSONObject result = new JSONObject(s);
                   String js = result.getString("result");
                   if(js.equals("1")){
                       btn_collection.setChecked(false);
                   }else {
                       btn_collection.setChecked(true);
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }else {
               System.out.println("结果为空");
           }
        }
    }



    private void evaluate(int stadiumId){//评论
        String loginUrl = URL_GETEVALUATEINFORMATION;
        new EvaluateAsyncTask().execute(loginUrl,String.valueOf(stadiumId));
    }

    private class  EvaluateAsyncTask extends  AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
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
            List<Evaluation> mData = new ArrayList<>();
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    //循环拿出接受的数据并赋值给stadium对象
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        Evaluation evaluation = new Evaluation();
                        evaluation.setUsername(js.getString("username"));
                        evaluation.setContent(js.getString("content"));
                        evaluation.setIcon(URL_PROFLIE+js.getString("proflie"));
                        evaluation.setGrade(js.getDouble("grade"));
                        evaluation.setEvaluatetime(js.getString("evaluatetime"));
                        mData.add(evaluation);
                    }
                    tv_evaluate_num.setText("--用户评论("+results.length()+")--");
                    StadiumEvaluateAdapter adapter = new StadiumEvaluateAdapter (StadiumActivity.this,mData);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(StadiumActivity.this, DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Evaluation> mData2 = new ArrayList<>();
                tv_evaluate_num.setText("--用户评论("+mData2.size()+")--");
                tv_noevaluate.setVisibility(View.VISIBLE);
                tv_noevaluate.setText("暂无评论");
                recyclerView.addItemDecoration(new DividerItemDecoration(StadiumActivity.this, DividerItemDecoration.VERTICAL));
                StadiumEvaluateAdapter adapter = new StadiumEvaluateAdapter(StadiumActivity.this, mData2);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(StadiumActivity.this, "该场馆暂无评论", Toast.LENGTH_SHORT).show();
            }
        }
    }


       private void shareText(String dlgTitle, String subject, String content){//分享文本
           if (content == null || "".equals(content)) {
               return;
           }
           Intent intent = new Intent(Intent.ACTION_SEND);
           intent.setType("text/plain");
           if (subject != null && !"".equals(subject)) {
               intent.putExtra(Intent.EXTRA_SUBJECT, subject);
           }

           intent.putExtra(Intent.EXTRA_TEXT, content);

           // 设置弹出框标题
           if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
               startActivity(Intent.createChooser(intent, dlgTitle));
           } else { // 系统默认标题
               startActivity(intent);
           }
       }
       /*
    //分享文本和照片
        public void  shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {

           Intent intent = new Intent(Intent.ACTION_SEND);

           if (imgPath == null || imgPath.equals("")) {
               intent.setType("text/plain"); // 纯文本
           } else
   {
       File f = new File(imgPath);

       if (f != null && f.exists() && f.isFile()) {

           intent.setType("image/jpg");
           Uri u = Uri.fromFile(f);
           intent.putExtra(Intent.EXTRA_STREAM, u);
           }
           }
           intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
           intent.putExtra(Intent.EXTRA_TEXT, msgText);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(Intent.createChooser(intent, activityTitle));
       }

*/

    //打电话的弹框
    private void showPasswordSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.tel_dialog, null);  // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0

        final TextView tv_telnumber =(TextView)view.findViewById(R.id.tv_telnumber);
        tv_telnumber.setText(stadium.getStadiumtel());
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_call);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+stadium.getStadiumtel());
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

//导航
public static boolean isAvilible(Context context, String packageName){
    //获取packagemanager
    final PackageManager packageManager = context.getPackageManager();
    //获取所有已安装程序的包信息
    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
    //用于存储所有已安装程序的包名
    List<String> packageNames = new ArrayList<String>();
    //从pinfo中将包名字逐一取出，压入pName list中
    if(packageInfos != null){
        for(int i = 0; i < packageInfos.size(); i++){
            String packName = packageInfos.get(i).packageName;
            packageNames.add(packName);
        }
    }
    //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
    return packageNames.contains(packageName);
 }
}