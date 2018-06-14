package com.example.jingjing.xin.Stadium;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Find.SetNumDialog;
import com.example.jingjing.xin.R;

public class SetTimeDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);//点击Dialog外围可以消除Dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);//设置高宽
        getDialog().setCanceledOnTouchOutside(false);

        View view = View.inflate(getActivity(), R.layout.list_num, null);//布局

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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }


    public interface SetNumListener {//设置接口
        void onSetNumComplete(int num);

        void onSetStadiumComplete(Stadium stadium);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

