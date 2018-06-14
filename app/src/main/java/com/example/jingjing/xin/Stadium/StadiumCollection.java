package com.example.jingjing.xin.Stadium;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
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

    private ImageView tv_back;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Stadium stadium;
    private User user;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.stadium_collection);

        initView();
         initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void  initView(){
        tv_back=(ImageView) findViewById(R.id.iv_back);
        recyclerView =(RecyclerView)findViewById(R.id.rv_stadiumcollection);
        linearLayoutManager=new LinearLayoutManager(this);

    }
    private void initData(){
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        user = (User) getIntent().getSerializableExtra("user");
        stadiumcollection(user.getUserId());

        tv_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
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
            if (!"null".equals(s)){
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js= results.getJSONObject(i);
                        Stadium stadium = new Stadium();
                        stadium.setStadiumId(js.getInt("stadiumId"));
                        stadium.setStadiumname(js.getString("stadiumname"));
                        stadium.setStadiumtype(js.getString("stadiumtypeId"));
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE+js.getString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setNum(js.getString("num"));
//                        stadium.setOpentime(js.getString("opentime"));
                        mData.add(stadium);
                    }
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(StadiumCollection.this,DividerItemDecoration.VERTICAL));
                    StadiumAdapter adapter = new StadiumAdapter(StadiumCollection.this,mData,user);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);//适配器
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(linearLayoutManager);//指定布局方式
                recyclerView.addItemDecoration(new DividerItemDecoration(StadiumCollection.this,DividerItemDecoration.VERTICAL));
                StadiumAdapter adapter = new StadiumAdapter(StadiumCollection.this,mData2,user);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(StadiumCollection.this,"该场馆没有收藏",Toast.LENGTH_LONG).show();

            }
        }

    }
}