package com.example.jingjing.xin.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jingjing.xin.Adapter.EvaluateAdapter;
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

import static com.example.jingjing.xin.constant.Conatant.URL_EVALUATEINFORMATION;

public class EvaluateFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView tv_noevaluate;
    private User user;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.nouserorder_fragment, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        tv_noevaluate = (TextView)view.findViewById(R.id.tv_noevaluate);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        layoutManager = new LinearLayoutManager(getContext());
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initData() {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        evaluatefragment(user);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                evaluatefragment(user);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void evaluatefragment(User user){
        String loadingUrl = URL_EVALUATEINFORMATION;
        new EvaluateAsyncTask().execute(loadingUrl, String.valueOf(user.getUserId()));
    }


    private class EvaluateAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            int method = 1 ;
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
            System.out.println(s);
            List<Book> mDate = new ArrayList<>();
            if(!s.equals("")){
                try {
                    JSONArray jsonArray = new JSONArray(s);//定义一个JSON数组
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);//循环遍历数组
                        Book book = new Book();
                        book.setUserId(user.getUserId());
                        book.setBookingId(js.getInt("bookingId"));
                        book.setPlaceName(js.getString("placename"));
                        book.setTime(js.getString("time"));
                        book.setTime_order(js.getString("time_order"));
                        book.setStadiumname(js.getString("stadiumname"));
                        book.setStadiumpicture(js.getString("mainpicture"));
                        mDate.add(book);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    EvaluateAdapter adapter = new EvaluateAdapter(getContext(),mDate);
                    recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {
                System.out.println("结果为空");
                List<Book> mDate2 = new ArrayList<>();
                tv_noevaluate.setVisibility(View.VISIBLE);
                tv_noevaluate.setText("当前没有待评论的预约订单");
                recyclerView.setLayoutManager(layoutManager);
                EvaluateAdapter adapter = new EvaluateAdapter(getContext(),mDate);
                recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
