package com.example.jingjing.xin.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Find.JoinNeedInformation;
import com.example.jingjing.xin.Find.PostNeedFlaotInformation;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.EvaluateInformation;
import com.example.jingjing.xin.Stadium.StadiumCollection;
import com.example.jingjing.xin.Stadium.StadiumOrderInformation;
import com.example.jingjing.xin.User.SettingActivity;
import com.example.jingjing.xin.User.UserInformationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jingjing.xin.constant.Conatant.URL_SELECTUSERBYUSERID;

/**
 * Created by jingjing on 2018/4/24.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {
    private TextView tv_username;
    private User  user ;

    private LinearLayout btn_exit;
    private ImageView btn_information;
    private LinearLayout btn_order;
    private LinearLayout btn_need;
    private LinearLayout btn_collect;
    private LinearLayout btn_joinedneed;
    private LinearLayout btn_setting;
    private LinearLayout btn_evaluate;
    private String userId ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.myfragment, null);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        btn_exit= (LinearLayout) view.findViewById(R.id.btn_exit);
        btn_information = (ImageView) view.findViewById(R.id.btn_informatoin);
        btn_order = (LinearLayout) view.findViewById(R.id.btn_order);
        btn_need= (LinearLayout) view.findViewById(R.id.btn_need);
        btn_collect = (LinearLayout) view.findViewById(R.id.btn_collect);
        btn_joinedneed = (LinearLayout) view.findViewById(R.id.btn_joinedneed);
        btn_setting=(LinearLayout)view.findViewById(R.id.btn_setting);
        btn_evaluate = (LinearLayout)view.findViewById(R.id.btn_evaluate);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        RefrshUser(userId);

    }

    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        userId = String.valueOf(user.getUserId());
        btn_exit.setOnClickListener(this);
        btn_information.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_need.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        btn_joinedneed.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        RefrshUser(userId);//更新


    }

    private void RefrshUser(String userId) {
        String loginUrl = URL_SELECTUSERBYUSERID;
        new myfragmentAsyncTask().execute(loginUrl,userId);
    }

    private class myfragmentAsyncTask extends AsyncTask<String, Integer, String> {
        public myfragmentAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("userId",params[1]);
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
                    System.out.println("22");
                    System.out.println(loginresult);
                    user = new User();
                    if(!"0".equals(loginresult)){
                        user.setUserId(results.getInt("userId"));
                        user.setUsername(results.getString("username"));
                        user.setPassword(results.getString("password"));
                        user.setRealname(results.getString("realname"));
                        user.setSex(results.getString("sex"));
                        user.setTel(results.getString("tel"));
                        user.setMyright(results.getString("myRight"));

                        tv_username.setText(user.getUsername());//用户名

                    }else{
                        Toast.makeText(mContext,"更新错误",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                Toast.makeText(mContext,"网络未连接",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_informatoin:
                Intent intent = new Intent(getContext(), UserInformationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_order:
                Intent intent1 = new Intent(getContext(),StadiumOrderInformation.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user",user);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.btn_need:
                Intent intent2= new Intent(getContext(),PostNeedFlaotInformation.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("user",user);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.btn_collect:
                Intent intent3 = new Intent(getContext(), StadiumCollection.class);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("user",user);
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            case R.id.btn_joinedneed:
                Intent intent4 = new Intent(getContext(), JoinNeedInformation.class);
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("user",user);
                intent4.putExtras(bundle4);
                startActivity(intent4);
                break;

            case R.id.btn_setting:
                Intent intent5 = new Intent(getContext(), SettingActivity.class);
                Bundle bundle5 = new Bundle();
                bundle5.putSerializable("user",user);
                intent5.putExtras(bundle5);
                startActivity(intent5);
                break;
            case R.id.btn_evaluate:
                Intent intent6 = new Intent(getContext(), EvaluateInformation.class);
                Bundle bundle6= new Bundle();
                bundle6.putSerializable("user",user);
                intent6.putExtras(bundle6);
                startActivity(intent6);
                break;
            case R.id.btn_exit:
                Exit();
                break;
            default:
                break;
        }
    }

    private void Exit(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("提示");//设置标题
        builder.setMessage("退出当前可能会使你看不到重要的消息，确定退出？");//设置内容
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();//关闭对话框
            }
        });
        AlertDialog dialog=builder.create();//获取dialog
        dialog.show();//显示对话框
    }
}
