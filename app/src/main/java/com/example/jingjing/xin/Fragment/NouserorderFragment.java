package com.example.jingjing.xin.Fragment;

import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jingjing.xin.Adapter.OrderInfromationAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.Book;
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

import static com.example.jingjing.xin.constant.Conatant.URL_ORDERINFORMATION;

public class NouserorderFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView tv_nouser;
    private User user;
    private LinearLayoutManager layoutManager;


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.nouserorder_fragment, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        tv_nouser = (TextView)view.findViewById(R.id.tv_nouser);
        layoutManager = new LinearLayoutManager(getContext());
        return view;
    }

    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        nouserorderfragment(user);

    }

    private void nouserorderfragment(User user){
        String loadingUrl = URL_ORDERINFORMATION;
        new nouserorderAsyncTask().execute(loadingUrl, String.valueOf(user.getUserId()));
    }

    private class nouserorderAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            int method = 2;
            JSONObject json = new JSONObject();
            try {
                json.put("userId", params[1]);
                json.put("method", method);
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
                    JSONArray results = new JSONArray(s);
                    for (int i=results.length()-1;i>=0;i--) {
                        JSONObject js = results.getJSONObject(i);
                        Book book = new Book();
                        book.setBookingId(js.getInt("bookingId"));
                        book.setStadiumname(js.getString("stadiumname"));
                        book.setPlaceName(js.getString("placename"));
                        book.setTime(js.getString("time"));
                        book.setTime_order(js.getString("time_order"));
                        mData.add(book);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                    OrderInfromationAdapter adapter = new  OrderInfromationAdapter(mContext, mData);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
                List<Book> mData2 = new ArrayList<>();
                tv_nouser.setVisibility(View.VISIBLE);
                tv_nouser.setText("当前没有使用过的预约订单");
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                OrderInfromationAdapter adapter = new OrderInfromationAdapter(mContext, mData2);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);

            }
        }
    }
}

