package com.example.jingjing.xin.Stadium;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.SetPlaceAdapter;
import com.example.jingjing.xin.Bean.Place;
import com.example.jingjing.xin.Bean.Stadium;
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

import static com.example.jingjing.xin.constant.Conatant.URL_PLACENAME;


@SuppressLint("ValidFragment")
public class SetPlaceDialog extends DialogFragment {

    private List<Place> place = new ArrayList<>();
    private Place place_set;
    private SetPlaceListener setPlaceListener;
    private Stadium mStadium;
    private String mtime;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("ValidFragment")
    public SetPlaceDialog(Stadium Stadium,String time) {
        this.mStadium = Stadium;
        this.mtime = time;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.list_place, null);//布局
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_place);
        layoutManager = new LinearLayoutManager(getContext());
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.PlaceDialog);

    }

    @Override
    public void onStart() {//设置dialog的宽高，背景
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//当acvitity中的oncreate返回后，回调用这里方法
        super.onActivityCreated(savedInstanceState);
        getPlace(mStadium,mtime);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }


    public interface SetPlaceListener {//Fragment与activity的通信，内部回调接口

        void onSetPlaceComplete(Place place);//把方法封装在接口中，在activity中需要用到方法的实现这个接口即可
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            setPlaceListener = (SetPlaceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroy() {
        setPlaceListener.onSetPlaceComplete(place_set);
        super.onDestroy();
    }

    private void getPlace(Stadium stadium,String time) {
        String loadingUrl = URL_PLACENAME;
        new getPlaceAsyncTask().execute(loadingUrl,String.valueOf(stadium.getStadiumId()),time);
    }

    private class getPlaceAsyncTask extends AsyncTask<String, Integer, String> {
        public getPlaceAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("stadiumId",params[1]);
                json.put("timeorder",params[2]);
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
            System.out.println("返回的数据REPLACE："+s);
            List<Place> mData = new ArrayList<>();
            if (!"null".equals(s)){
                //s= s.substring(1);
                try {
                    JSONArray results = new JSONArray(s);
                    for(int i=0;i<results.length();i++){
                        JSONObject js= results.getJSONObject(i);
                        Place place = new Place();
                        place.setStadiumId(js.getInt("stadiumId"));
                        place.setPlaceId(js.getInt("placeId"));
                        place.setState(js.getString("state"));
                        place.setMaterial(js.getString("material"));
                        place.setPlacename(js.getString("placename"));
                        mData.add(place);
                    }
                    SetPlaceAdapter adapter = new SetPlaceAdapter(getContext(),mData);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adapter);
                    adapter.SetPlaceOnclick(new SetPlaceAdapter.SetPlaceOnClickListener() {
                        @Override
                        public void onItemClick(Place place) {
                            place_set =place;
                            onDestroy();
                            onDismiss(getDialog());
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                Toast.makeText(getContext(),"该场馆暂时没添加场地",Toast.LENGTH_LONG).show();

            }
        }
    }

}
