package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class SetStadiumAdapter extends RecyclerView.Adapter<SetStadiumAdapter.ViewHolder> {
    private List<Stadium> mstadiumlist;
    private Context mcontext;
    private SetStadiumOnClickListener setStadiumOnClickListener;//2.声明一个监听对象
    private Stadium stadium;


    public interface SetStadiumOnClickListener{//1.声明一个接口，定义点击事件的回调
        void onItemClick(Stadium stadium);
    }

    public void SetStadiumOnClickListener(SetStadiumOnClickListener setStadiumOnClickListener){//2.定义一个方法
        this.setStadiumOnClickListener=setStadiumOnClickListener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        View setstadiumAdapter;
        ImageView stadiumpicture;
        TextView stadiumname;
        TextView stadiumtype;
        TextView stadiumaddress;
        RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            setstadiumAdapter= view;
            stadiumpicture = (ImageView)view.findViewById(R.id.iv_stadiumpicture);
            ratingBar = (RatingBar)view.findViewById(R.id.rb_ratbar);
            stadiumname = (TextView)view.findViewById(R.id.tv_stadiumname);
            stadiumtype = (TextView)view.findViewById(R.id.tv_stadiumtype);
            stadiumaddress= (TextView)view.findViewById(R.id.tv_stadiumaddress);
        }
    }


    public SetStadiumAdapter(Context context,List<Stadium> stadiumList){
        mcontext = context;
        mstadiumlist = stadiumList;
    }

    @Override
    public SetStadiumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_changguan, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.setstadiumAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setStadiumOnClickListener != null){
                    int postion = holder.getLayoutPosition();
                    Stadium stadium = mstadiumlist.get(postion);
                    setStadiumOnClickListener.onItemClick(stadium);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SetStadiumAdapter.ViewHolder holder, int position) {
        stadium = mstadiumlist.get(position);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mcontext);
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.load)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(1000)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(stadium.getMainpicture(), holder.stadiumpicture,options);
        holder.stadiumname.setText(stadium.getStadiumname());
        holder.stadiumaddress.setText(stadium.getAdress());
        holder.stadiumtype.setText( stadium.getStadiumtype());
        holder.ratingBar.setRating(stadium.getGrade());
        holder.ratingBar.setIsIndicator(true);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mstadiumlist.size();
    }


}
