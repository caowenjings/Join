package com.example.jingjing.xin.Stadium;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingjing.xin.R;

public class SetOrderTimeDialog extends DialogFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.PlaceDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);//点击Dialog外围可以消除Dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);//设置高宽
        getDialog().setCanceledOnTouchOutside(false);

        View view = View.inflate(getActivity(), R.layout.set_ordertime, null);//布局
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//当acvitity中的oncreate返回后，回调用这里方法
        super.onActivityCreated(savedInstanceState);
    }

    public interface initOrdertime{//接口

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
/*
 private List<Place> place = new ArrayList<>();
    private Place place_set;
    private SetPlaceListener setPlaceListener;
    private Stadium mStadium;
    private String mtime;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @SuppressLint("ValidFragment")
    public SetPlaceDialog(Stadium Stadium,String time) {
        this.mStadium = Stadium;
        this.mtime = time;
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
}

 */