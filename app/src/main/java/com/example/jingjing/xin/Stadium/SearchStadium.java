package com.example.jingjing.xin.Stadium;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.StadiumAdapter;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Fragment.BookingFragment;
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
import static com.example.jingjing.xin.constant.Conatant.URL_SEARCHSTADIUM;

/**
 * Created by jingjing on 2018/5/21.
 */

public class SearchStadium extends AppCompatActivity {
    private List<Stadium> mDate;
    private List<Stadium> mDate2;
    private SearchAdapter adapter;
    private ImageView tv_back;
    private RecyclerView recyclerView;
    private EditText et_Search;
    private ImageView iv_Delete;
    private LinearLayoutManager layoutManager;
    private User user;
    private String city, stadiumname;
    private Stadium stadium;
    private String type;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//取消设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);//设置颜色
        setContentView(R.layout.stadium_search);
        initView();
        initData();
    }

    private void initView() {
        tv_back=(ImageView)findViewById(R.id.tv_back);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        et_Search=(EditText)findViewById(R.id.et_search);
        iv_Delete=(ImageView)findViewById(R.id.iv_delete);

    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        city = (String) getIntent().getSerializableExtra("city");
        type = (String) getIntent().getSerializableExtra("type");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_Delete.setOnClickListener(new View.OnClickListener() {//清空editview
            @Override
            public void onClick(View v) {
                et_Search.setText("");
            }
        });
      //  editTextSearchListener();
        if(type != null ){
            Search(type,city);
            et_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String stadiuname = et_Search.getText().toString();
                        Search(stadiuname, city);
                        return false;
                    }
                    return false;
                }
            });
        } else {
            et_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        String stadiuname = et_Search.getText().toString();
                        if(stadiuname.length()==0){
                            iv_Delete.setVisibility(View.GONE);//如果输入框里面的内容为0,就隐藏
                        }else {
                            iv_Delete.setVisibility(View.VISIBLE);
                            Search(stadiuname,city);
                            return false;
                        }
                    }
                    return  false;
                }
            });
        }
    }
    private void editTextSearchListener() {//给软键盘添加动作监听
       et_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH){
                   String stadiuname = et_Search.getText().toString();
                   if(stadiuname.length()==0){
                       iv_Delete.setVisibility(View.GONE);//如果输入框里面的内容为0,就隐藏
                   }else {
                       iv_Delete.setVisibility(View.VISIBLE);
                       Search(stadiuname,city);
                       return false;
                   }
               }
               return  false;
           }
       });
    }

    private void Search(String stadiuname, String city) {
        String SearchUrl = URL_SEARCHSTADIUM;
        new SearchAsyncTask().execute(SearchUrl, stadiuname, city);
    }

    private class SearchAsyncTask extends AsyncTask<String, Integer, String> {
        public SearchAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumname", params[1]);
                json.put("city", params[2]);
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
            List<Stadium> mData = new ArrayList<>();
            if (!"null".equals(s) && s != null) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        Stadium stadium = new Stadium();
                        stadium.setStadiumId(js.getInt("stadiumId"));
                        stadium.setStadiumname(js.getString("stadiumname"));
                        stadium.setStadiumtype(js.getString("stadiumtypename"));
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE + js.optString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setNum(js.getString("num"));
                        stadium.setOpentime(js.getString("opentime"));
                        stadium.setClosetime(js.getString("closetime"));
                        stadium.setStadiumtel(js.optString("stadiumtel"));//没有就设置为null
                        stadium.setGrade((float)js.getDouble("grade"));
                        stadium.setIconnum(js.getInt("iconnum"));
                        mData.add(stadium);

                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(SearchStadium.this, DividerItemDecoration.VERTICAL));
                    StadiumAdapter adapter = new StadiumAdapter(SearchStadium.this, mData,user );
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(SearchStadium.this, DividerItemDecoration.VERTICAL));
                StadiumAdapter adapter = new  StadiumAdapter (SearchStadium.this, mData2,user );
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(SearchStadium.this, "没有查询到该场馆", Toast.LENGTH_SHORT).show();
            }
        }
    }

}


