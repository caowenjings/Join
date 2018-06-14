package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.jingjing.xin.Base.BaseFragment;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class GridPictureAdapter extends BaseAdapter {
    private List<String> mlist;
    private Context mcontext;

    public  GridPictureAdapter(Context context,List<String> list){
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//创建View,然后对控件进行赋值
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mcontext,R.layout.list_picture,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.iv_stadiumpicture);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        final int pos = position;
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mcontext);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(0)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(mlist.get(pos), holder.imageView,options);
        return convertView;
    }

    final class ViewHolder{//定义内部类
        private ImageView imageView;
    }
}

