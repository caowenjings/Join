package com.example.jingjing.xin.Find;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_JOINFIND;

/**
 * Created by jingjing on 2018/5/28.
 */

public class FindActivity  extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout tv_back;


    private TextView tv_stadiumname;
    private TextView tv_username;
    private TextView tv_time;
    private TextView tv_num;
    private TextView tv_num_join;
    private TextView tv_remark;
    private Button btn_join;

    private User user;
    private Need need;


    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.find);

        initView();
        initData();

    }
    private void initView(){

        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_title=(ImageView)findViewById(R.id.iv_title);
        tv_back=(RelativeLayout)findViewById(R.id.tv_back);
        tv_title.setText("发现详情");

        tv_username=(TextView)findViewById(R.id.user_name);
        tv_stadiumname=(TextView)findViewById(R.id.stadium_name);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_num=(TextView)findViewById(R.id.tv_num);
        tv_num_join=(TextView)findViewById(R.id.tv_num_join);
        tv_remark=(TextView)findViewById(R.id.tv_remark);
        btn_join=(Button)findViewById(R.id.btn_join);


    }
    private void initData(){
        user = (User) getIntent().getSerializableExtra("user");
        need = (Need) getIntent().getSerializableExtra("need");

        tv_username.setText(need.getUsername());
        tv_stadiumname.setText("地点："+need.getStadiumname());
        tv_time.setText("时间："+need.getTime());
        tv_num.setText("召集人数："+String.valueOf(need.getNum()));
        tv_num_join.setText("已加入人数："+String.valueOf(need.getNum_join()));
        tv_remark.setText("备注："+need.getRemark());

        tv_back.setOnClickListener(this);
        btn_join.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_join:
                findJoin(user.getUserId(),need.getNeedId());
                break;
            default:
                break;
        }

    }

    private void findJoin(int userId,int needId) {
        String loginUrl = URL_JOINFIND;
        new findJoinAsyncTask().execute(loginUrl,String.valueOf(userId),String.valueOf(needId));
    }

    private class findJoinAsyncTask extends AsyncTask<String, Integer, String> {
        public findJoinAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("userId",params[1]);
                json.put("needId",params[2]);
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
            System.out.println(s);
            if (s != null){
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if("1".equals(loginresult)){
                        Toast.makeText(FindActivity.this,"加入成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else if("2".equals(loginresult)){
                        Toast.makeText(FindActivity.this,"人数已满，加入失败",Toast.LENGTH_SHORT).show();
                    }else if("3".equals(loginresult)){
                        Toast.makeText(FindActivity.this,"已经加入，不能重复加入",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(FindActivity.this,"加入失败" ,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                Toast.makeText(FindActivity.this,"网络未连接",Toast.LENGTH_LONG).show();

            }
        }
    }


}
