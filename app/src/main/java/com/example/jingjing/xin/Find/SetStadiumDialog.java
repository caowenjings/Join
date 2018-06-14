package com.example.jingjing.xin.Find;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingjing.xin.Adapter.SetStadiumAdapter;
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

import static com.example.jingjing.xin.constant.Conatant.URL_PICTURE;
import static com.example.jingjing.xin.constant.Conatant.URL_SEARCHSTADIUM;

@SuppressLint("ValidFragment")
public class SetStadiumDialog extends DialogFragment{

    private EditText et_search;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String city;
    private Stadium set_stadium;
    private ImageButton iv_delete;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

   private SetStadiumDialog.SetStadiumListener setStadiumListener;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);//点击Dialog外围可以消除Dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);//设置高宽
        getDialog().setCanceledOnTouchOutside(false);

        View view = View.inflate(getContext(), R.layout.list_stadium,null);
       et_search = (EditText) view.findViewById(R.id.et_search);
       iv_delete=(ImageButton)view.findViewById(R.id.iv_delete);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//风格
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.PlaceDialog);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//当acvitity中的oncreate返回后，回调用这里方法
        super.onActivityCreated(savedInstanceState);
//        city =(String)getActivity().getIntent().getSerializableExtra("city");
       initStadium();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }


    public interface SetStadiumListener {//设置接口
        void onSetStadiumComplete(Stadium stadium);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            setStadiumListener= (SetStadiumDialog.SetStadiumListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    public void onDestroy() {
        setStadiumListener.onSetStadiumComplete(set_stadium);
        super.onDestroy();
    }


    private void initStadium(){
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        city = "成都市";
        System.out.println(city);
        SetStadiumDialog("",city);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String stadiuname = et_search.getText().toString();
                    if(stadiuname.length()==0){
                        iv_delete.setVisibility(View.GONE);//如果输入框里面的内容为0,就隐藏
                    }else {
                        iv_delete.setVisibility(View.VISIBLE);
                        SetStadiumDialog(stadiuname,city);
                        return false;
                    }
                }
                return false;
            }
        });
    }
    private void SetStadiumDialog(String stadiuname,String city) {
        String SearchUrl =URL_SEARCHSTADIUM;
        new SetStadiumDialogAsyncTask().execute(SearchUrl,stadiuname,city);
    }

    private class SetStadiumDialogAsyncTask extends AsyncTask<String, Integer, String> {
        public  SetStadiumDialogAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json=new JSONObject();
            try {
                json.put("stadiumname",params[1]);
                json.put("city",params[2]);
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
                        stadium.setStadiumtype(js.getString("stadiumtypename"));
                        stadium.setArea(js.getString("area"));
                        stadium.setIndoor(js.getInt("indoor"));
                        stadium.setAircondition(js.getInt("aircondition"));
                        stadium.setCity(js.getString("city"));
                        stadium.setMainpicture(URL_PICTURE+js.getString("mainpicture"));
                        stadium.setAdress(js.getString("adress"));
                        stadium.setOpentime(js.getString("opentime"));
                        stadium.setClosetime(js.getString("closetime"));
                        stadium.setNum(js.getString("num"));
                        stadium.setGrade((float)js.getDouble("grade"));
                        mData.add(stadium);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                    SetStadiumAdapter adapter = new SetStadiumAdapter(getContext(),mData);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);

                    adapter.SetStadiumOnClickListener(new SetStadiumAdapter.SetStadiumOnClickListener() {
                        @Override
                        public void onItemClick(Stadium stadium) {//实现接口
                            set_stadium = stadium;
                            onDestroy();
                            onDismiss(getDialog());//关闭窗口
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("结果为空");
                List<Stadium> mData2 = new ArrayList<>();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                SetStadiumAdapter adapter = new SetStadiumAdapter(getContext(),mData2);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(adapter);
                Toast.makeText(getContext(),"没有查询到该场馆",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
