package com.example.jingjing.xin.Stadium;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jingjing.xin.R;


/**
 * Created by whieenz on 2017/7/19.
 */
//自定义搜索按钮
public class DialogSearchView extends LinearLayout implements View.OnClickListener {
    private EditText etInput;//输入框
    private ImageView ivDelete;//删除键
    private Context mContext;//上下文对象
    private DialogSearchViewListener mListener;//搜索回调接口
    public void setDialogSearchViewListener(DialogSearchViewListener listener) {//设置搜索回调接口监听
        mListener = listener;
    }

    public DialogSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_search_layout, this);
        initViews();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.et_search_text);
        ivDelete = (ImageView) findViewById(R.id.imb_search_clear);
        ivDelete.setOnClickListener(this);
        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);

    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                if (mListener != null) { //更新autoComplete数据
                    mListener.onQueryTextChange(charSequence + ""); }
            } else {
                ivDelete.setVisibility(GONE);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imb_search_clear:
                etInput.setText("");
                if (mListener != null) {
                    mListener.onQueryTextChange("");
                }
                ivDelete.setVisibility(GONE);
                break;
        }
    }
    public interface DialogSearchViewListener {//search view回调方法
        boolean onQueryTextChange(String text);
    }
}  