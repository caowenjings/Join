package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.StadiumActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by jingjing on 2018/5/19.
 */

public class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.ViewHolder> {
    private List<Stadium> mStadiumlist;
    private Context mContext;
    private User mUser;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View stadiumView;
        ImageView stadiumpicture;
        TextView stadiumname;
        TextView stadiumtype;
        TextView stadiumadress;
        RatingBar ratingBar;

        public ViewHolder(View view) {//创建实例
            super(view);
            stadiumView = view;
            stadiumpicture = (ImageView) view.findViewById(R.id.iv_stadiumpicture);
            stadiumname = (TextView) view.findViewById(R.id.tv_stadiumname);
            stadiumadress = (TextView) view.findViewById(R.id.tv_stadiumaddress);
            stadiumtype = (TextView) view.findViewById(R.id.tv_stadiumtype);
            ratingBar =(RatingBar)view.findViewById(R.id.rb_ratbar);
        }
    }

    public StadiumAdapter(Context context, List<Stadium> stadiumList, User user) {//传入要展示的数据源
        mContext = context;
        mStadiumlist = stadiumList;
        mUser = user;
    }
    @Override
    public StadiumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//加载布局，创建实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookingfragment_list_stadium, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.stadiumView.setOnClickListener(new View.OnClickListener() {//设置监听
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Stadium stadium = mStadiumlist.get(position);//通过position拿到stadium实例
                Intent intent = new Intent(mContext,StadiumActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", mUser);
                mBundle.putSerializable("stadium", stadium);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(StadiumAdapter.ViewHolder holder, int position) {//绑定数据
        Stadium stadium = mStadiumlist.get(position);//获取当前位置
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.load)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .displayer(new RoundedBitmapDisplayer(20))//设置为圆角弧度
                .displayer(new FadeInBitmapDisplayer(100))//图片加载好渐入的动画时间
                .delayBeforeLoading(1000)  // 下载前的延迟时间
                .build();//构建完成
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(stadium.getMainpicture(), holder.stadiumpicture,options);


        holder.stadiumname.setText(stadium.getStadiumname());
        holder.stadiumadress.setText(stadium.getAdress());
        holder.stadiumtype.setText( stadium.getStadiumtype());
        holder.ratingBar.setRating(stadium.getGrade());
        holder.ratingBar.setIsIndicator(true);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {//获取条目
        return mStadiumlist.size();
    }

}