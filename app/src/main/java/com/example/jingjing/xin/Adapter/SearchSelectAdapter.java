package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.jingjing.xin.R;

import java.util.List;


public class SearchSelectAdapter extends BaseAdapter {//自定义Listviewd的adapter
    private List<String> Datas;
    private Context context;
    private LayoutInflater inflater;

    public SearchSelectAdapter(Context ctx, List<String> datas) {
        this.context = ctx;
        this.Datas = datas;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return Datas.size();
    }

    @Override
    public String getItem(int i) {
        return Datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.list_cell_select_single, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.info.setText(Datas.get(i));
        return view;
    }


    static class ViewHolder {
        TextView info;

        public ViewHolder(View view) {
            info = (TextView) view.findViewById(R.id.tv_select_info);
        }
    }

}
