package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.R;

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

import static com.example.jingjing.xin.constant.Conatant.URL_DELETENEEDINFORMATION;

/**
 * Created by jingjing on 2018/6/2.
 */

public class PostNeedAdapter extends RecyclerView.Adapter<PostNeedAdapter.ViewHolder> {

    private List<Need> mneed;
    private Context mcontext;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public PostNeedAdapter(Context context,List<Need> need){

        mcontext = context;
        mneed = need;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

       View postneedview;
        TextView tv_stadiumname;
        TextView tv_time;
        TextView tv_num;
        TextView tv_num_join;
        TextView tv_remark;
        Button btn_delete;

        public ViewHolder(View view) {
            super(view);
            postneedview=view;

            tv_stadiumname = (TextView) view.findViewById(R.id.tv_stadiumname);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_num_join = (TextView) view.findViewById(R.id.tv_num_join);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            btn_delete = (Button) view.findViewById(R.id.btn_delete);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_postneed,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Need need = mneed.get(position);
        holder.tv_stadiumname.setText("场馆名："+ need.getStadiumname());
        holder.tv_time.setText("时间:" + need.getTime());
        holder.tv_num.setText("召集人数:" + need.getNum());
        holder.tv_num_join.setText("加入人数:" + need.getNum_join());
        holder.tv_remark.setText("备注:" + need.getRemark());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleDialog(position,need);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mneed.size();
    }

    private void cancleDialog(final int position, final Need need) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontext);
        builder.setTitle("提示");//设置标题
        builder.setMessage("删除约的运动就不能愉快的玩耍了哦!您还确定删除发布的需求吗？");//设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mneed.remove(position);//移除
                notifyItemRemoved(position);//更新
                notifyItemRangeChanged(0, mneed.size());
                deleteorderInformation(need);//调用方法
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog=builder.create();//获取dialog
        builder.show();//显示对话框
    }

    private void deleteorderInformation(Need need) {
        String loadingUrl = URL_DELETENEEDINFORMATION;
        new deleteorderInformationAsyncTask().execute(loadingUrl, String.valueOf(need.getNeedId()));
    }

    private class deleteorderInformationAsyncTask extends AsyncTask<String, Integer, String> {
        public deleteorderInformationAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("needId", params[1]);
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
            if (!"null".equals(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    String result = results.getString("result");
                    if ("1".equals(result)) {
                        Toast.makeText(mcontext, "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mcontext, "删除成功", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");

            }
        }
    }
}
