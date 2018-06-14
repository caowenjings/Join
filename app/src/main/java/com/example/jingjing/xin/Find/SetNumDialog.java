package com.example.jingjing.xin.Find;

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
import com.example.jingjing.xin.R;

/**
 * Created by jingjing on 2018/5/29.
 */

public class SetNumDialog extends DialogFragment implements  NumberPicker.OnValueChangeListener, NumberPicker.Formatter {

    private SetNumListener setNumListener;
    private NumberPicker numberPicker;
    private Button btn_sure;
    private int num;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);//点击Dialog外围可以消除Dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);//设置高宽
        getDialog().setCanceledOnTouchOutside(false);

        View view = View.inflate(getActivity(), R.layout.list_num, null);//布局
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
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
        initNum();
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
            setNumListener = (SetNumListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initNum() {
        numberPicker.setFormatter(this); //格式化数字，需重写format方法
        numberPicker.setOnValueChangedListener(this); //值变化监听事件
        numberPicker.setMinValue(1);//最小值
        numberPicker.setMaxValue(20);//最大值
        numberPicker.setValue(1);//设置初始选定值

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNumListener.onSetNumComplete(num);
                dismiss();
            }
        });
    }


    @Override
    public String format(int value) {//格式化数字
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // num=numberPicker.getValue();
        this.num=newVal;//获取选到的值
    }

}
