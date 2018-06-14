package com.example.jingjing.xin.Stadium;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;


/**
 * Created by jingjing on 2018/5/22.
 */


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private List<Stadium> mstadium;
    private Context mContext;
    private User mUser;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public SearchAdapter(Context context,List<Stadium> mstadiumDate,User user) {
        mContext=context;
        mstadium=mstadiumDate;
        mUser = user;
    }
    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  View.inflate(parent.getContext(), R.layout.item_search, null);
        final SearchHolder searchHolder = new SearchHolder(view);
        searchHolder.stadiumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                int position = searchHolder.getAdapterPosition();
                Stadium stadium = mstadium.get(position);//通过position拿到stadium实例
                Intent intent = new Intent(mContext, .class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", mUser);
                mBundle.putSerializable("stadium", stadium);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);  */
            }
        });



        return searchHolder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, final int position) {
        Stadium  stadium =mstadium.get(position);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.star) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.star1)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .displayer(new RoundedBitmapDisplayer(20))//设置为圆角弧度
                .displayer(new FadeInBitmapDisplayer(100))//图片加载好渐入的动画时间
                .delayBeforeLoading(1000)  // 下载前的延迟时间
                .build();//构建完成
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(stadium.getMainpicture(), holder.stadiumpictuer,options);

        holder.tv_stadiunaddress.setText(stadium.getAdress());
        holder.tv_stadiumname.setText(stadium.getStadiumname());
        holder.tv_stadiumtype.setText(stadium.getStadiumtype());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.OnItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mstadium.size();
    }

   static class SearchHolder extends RecyclerView.ViewHolder{
       View stadiumView;

        private ImageView stadiumpictuer;
        private TextView tv_stadiumname;
        private TextView tv_stadiumtype;
        private RatingBar ratingBar;
        private TextView tv_stadiunaddress;
        private LinearLayout linearLayout;

        public SearchHolder(View view) {
            super(view);
           stadiumView = view;
           stadiumpictuer = (ImageView)view.findViewById(R.id.iv_stadiumpicture);
           tv_stadiumname=(TextView)view.findViewById(R.id.tv_stadiumname);
           tv_stadiumtype=(TextView)view.findViewById(R.id.tv_stadiumtype);
           tv_stadiunaddress=(TextView)view.findViewById(R.id.tv_stadiumaddress);
           linearLayout=(LinearLayout)view.findViewById(R.id.item_list);
        }
    }

}
