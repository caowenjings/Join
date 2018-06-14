package com.example.jingjing.xin.Fragment;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.OrderInfromationAdapter;
import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.StadiumOrderInformation;

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

public class UserorderFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView tv_nobooking;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout frame_one;
    private FrameLayout frame_wu;
    private FrameLayout frame_you;
    private List<Book> mData = null;
    Book book = new Book();
    private User user;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.userorder_information, null);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        tv_nobooking = (TextView)view.findViewById(R.id.tv_nobooking);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        frame_one=(FrameLayout)view.findViewById(R.id.frame_one);
        frame_wu=(FrameLayout)view.findViewById(R.id.frame_wu);
        frame_you=(FrameLayout)view.findViewById(R.id.frame_you);
        layoutManager = new LinearLayoutManager(getContext());

        frame_one.removeView(frame_wu);
        frame_one.removeView(frame_you);//移除

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                frame_one.removeView(frame_wu);
                frame_one.removeView(frame_you);
                stadiumOrderInformation(user);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    protected void initData() {

        user = (User)getActivity().getIntent().getSerializableExtra("user");
        stadiumOrderInformation(user);

    }

    private void stadiumOrderInformation(User user) {

        String loadingUrl = URL_ORDERINFORMATION;
        new OrderInformationAsyncTask().execute(loadingUrl, String.valueOf(user.getUserId()));

    }

    private class OrderInformationAsyncTask extends AsyncTask<String, Integer, String> {
        public OrderInformationAsyncTask() {

        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            int method = 1;//用于判断服务器执行什么操作
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
            if (!"null".equals(s)) {
                try {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject js = results.getJSONObject(i);
                        Book book = new Book();
                        book.setBookingId(js.getInt("bookingId"));
                        book.setStadiumname(js.getString("stadiumname"));
                        book.setPlaceName(js.getString("placename"));
                        book.setTime(js.getString("time"));
                        book.setTime_order(js.getString("time_order"));
                        mDate.add(book);
                    }
                    frame_one.addView(frame_you);//添加布局
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    OrderInfromationAdapter adapter = new OrderInfromationAdapter(getContext(), mDate);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("结果为空");
                List<Book> mData2 = new ArrayList<>();
                frame_one.addView(frame_wu);//添加布局
                tv_nobooking.setText("目前你没有预约场地");
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                OrderInfromationAdapter adapter = new OrderInfromationAdapter(getContext(), mDate);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);//适配器

                Toast.makeText(getContext(), "您还没有预定", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
