package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Activity.LoginActivity;
import com.example.jingjing.xin.Activity.MainActivity;
import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.StadiumActivity;
import com.example.jingjing.xin.Stadium.StadiumOrderInformation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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

import static com.baidu.mapapi.BMapManager.getContext;
import static com.example.jingjing.xin.constant.Conatant.URL_DELETEORDERINFORMATION;

/**
 * Created by jingjing on 2018/5/28.
 */

public class OrderInfromationAdapter extends RecyclerView.Adapter<OrderInfromationAdapter.ViewHolder> {

    private List<Book> mbooking;
    private Context mcontext;
    private Book book;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OrderInfromationAdapter(Context context, List<Book> book){
        this.mcontext = context;
        this.mbooking = book;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View stadiumorderView;
        TextView  tv_stadiumname;
        TextView tv_place;
        TextView tv_time;
        TextView tv_ordertime;
        Button btn_cancel;

        public ViewHolder(View view) {
            super(view);
            stadiumorderView = view;

            tv_stadiumname = (TextView)view.findViewById(R.id.tv_stadiumname);
            tv_place = (TextView)view.findViewById(R.id.tv_place);
            tv_time  = (TextView)view.findViewById(R.id.tv_time);
            tv_ordertime = (TextView)view.findViewById(R.id.tv_time_order);
            btn_cancel = (Button)view.findViewById(R.id.btn_cancel);

        }
    }

    @Override
    public OrderInfromationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Book book = mbooking.get(position);//获取当前位置

        holder.tv_stadiumname.setText("场馆名："+book.getStadiumname());
        holder.tv_time.setText("今天时间："+book.getTime());
        holder.tv_ordertime.setText("预定时间："+book.getTime_order());
        holder.tv_place.setText(book.getPlaceName());
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleDialog(position, book);
            }
        });

    }


    private void cancleDialog(final int position, final Book book) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontext);
        builder.setTitle("提示");//设置标题
        builder.setMessage("亲确定删除预约吗？");//设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbooking.remove(position);//移除
                notifyItemRemoved(position);//更新
                notifyItemRangeChanged(0, mbooking.size());
                deleteorderInformation(book);//调用方法
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

    private void deleteorderInformation(Book book) {
        String loadingUrl = URL_DELETEORDERINFORMATION;
        new CancalOrderAsyncTask().execute(loadingUrl, String.valueOf(book.getBookingId()));
    }

    private class CancalOrderAsyncTask extends AsyncTask<String,Integer,String>{
        protected CancalOrderAsyncTask(){

        }
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("bookingId", params[1]);
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
            List<Book> mData = new ArrayList<>();
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mbooking.size();
    }
}
