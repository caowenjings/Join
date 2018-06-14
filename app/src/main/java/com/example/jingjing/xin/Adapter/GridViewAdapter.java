package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.App;
import com.example.jingjing.xin.Bean.AppGrid;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by jingjing on 2018/5/3.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<App> mDatas;
    private LayoutInflater inflater;
    private int curIndex;//页数下标,从0开始(当前是第几页)
    private int pageSize;//每一页显示的个数
    private Context mContext;
    private User mUser;

    public GridViewAdapter(Context context, List<App> mDatas, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }
    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_icon,parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.text_icon);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.image_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int mposition = position + curIndex * pageSize;//计算正确的position = position + curIndex * pageSize，

        //viewHolder.tv.setText(mDatas.get( mposition ).getIconName());
        //viewHolder.iv.setImageResource(mDatas.get(mposition).getIcon());

        viewHolder.tv.setText(mDatas.get( mposition ).getName());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(0)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(mDatas.get(mposition).getIcon(), viewHolder.iv,options);
        return convertView;
    }
    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}

