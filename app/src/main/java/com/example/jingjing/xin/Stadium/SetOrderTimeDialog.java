package com.example.jingjing.xin.Stadium;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jingjing.xin.Bean.Place;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;

@SuppressLint("ValidFragment")
public class SetOrderTimeDialog extends DialogFragment{


    private SetPlaceDialog.SetPlaceListener setPlaceListener;
    private Button btn_sure;
    private EasyPickerView ep_time;
    private Stadium mStadium;
    private String mDay;
    private String mHour;
    private String mtime;
    private SetOrdertime setOrdertime;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("ValidFragment")
    public SetOrderTimeDialog(Stadium stadium, String day){
        this.mStadium = stadium;
        this.mDay = day;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {//设置弹框样式
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.PlaceDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(true);//点击Dialog外围可以消除Dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);//设置高宽
        getDialog().setCanceledOnTouchOutside(false);

        View view = View.inflate(getActivity(),R.layout.set_ordertime,null);
        btn_sure = (Button)view.findViewById(R.id.btn_sure);
        ep_time = (EasyPickerView)view.findViewById(R.id.ep_time);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//当activity中oncreat返回后调用这里的方法
        super.onActivityCreated(savedInstanceState);
        initTime();
    }

    public interface SetOrdertime{ // 定义回调接口，用于传递数据给Activity
        void getordertime(String time);
    }

    @Override
    public void onAttach(Context context) {//调用接口对象方法
        super.onAttach(context);
        try{
            setOrdertime = (SetOrdertime) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initTime(){
        final ArrayList<String> numlist = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String this_day =(year + "年" + month + "月" + day + "日");
        int num = 0;
        if(this_day.equals(mDay)){//判断是否是今天
            int this_hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(this_hour<Integer.parseInt(mStadium.getOpentime())){
                num = Integer.parseInt(mStadium.getOpentime());
                for(int i = num; i< Integer.parseInt(mStadium.getClosetime());i++){
                    numlist.add(String.valueOf(i)+":00--"+String.valueOf(i+1)+":00");
                }
            }else {
                num = this_hour+1;
                for (int i = num; i < Integer.parseInt(mStadium.getClosetime()); i++) {
                    numlist.add(String.valueOf(i) + ":00--" + String.valueOf(i + 1) + ":00");
                }
                }
        }else {
            num = Integer.parseInt(mStadium.getOpentime());
            for(int i = num; i< Integer.parseInt(mStadium.getClosetime());i++){
                numlist.add(String.valueOf(i)+":00--"+String.valueOf(i+1)+":00");
            }
        }
        ep_time.setDataList(numlist);
        ep_time.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                mHour = numlist.get(curIndex);
                mtime = String.valueOf(mHour);
            }

            @Override
            public void onScrollFinished(int curIndex) {
                mHour = numlist.get(curIndex);
                mtime = String.valueOf(mHour);
            }
        });


        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("------------------》"+mtime);
                setOrdertime.getordertime(mtime);
                dismiss();
            }
        });
    }
}
