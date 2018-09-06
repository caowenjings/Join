package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.Need;
import com.example.jingjing.xin.Bean.Stadium;
import com.example.jingjing.xin.Bean.User;
import com.example.jingjing.xin.Find.MyFindinformation;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.StadiumActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import okhttp3.MediaType;

public class JoinUserAdapter extends RecyclerView.Adapter<JoinUserAdapter.ViewHolder> {

    private Context mcontext;
    private Need need;
    private List<User> muser;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JoinUserAdapter(Context context,List<User> user){
        this.mcontext = context;
        this.muser = user;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View joinuserview;

        ImageView user_proflie;
        TextView tv_username;
        TextView tv_usersex;
        TextView tv_usertel;
        TextView tv_call;

        public ViewHolder(View view) {
            super(view);
            joinuserview = view;
            user_proflie =(ImageView)view.findViewById(R.id.user_proflie);
            tv_username = (TextView)view.findViewById(R.id.tv_username);
            tv_usersex = (TextView)view.findViewById(R.id.tv_usersex);
            tv_usertel = (TextView)view.findViewById(R.id.tv_usertel);
            tv_call = (TextView)view.findViewById(R.id.tv_call);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_information,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = muser.get(position);


        holder.tv_username.setText(user.getUsername());
        holder.tv_usersex.setText("性别："+user.getSex());
        holder.tv_usertel.setText("电话:"+user.getTel());

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mcontext);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(100)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(user.getProflie(), holder.user_proflie,options);

        if("女".equals(user.getSex())){
            holder.tv_call.setText("联系她");
        }else{
            holder.tv_call.setText("联系他");
        }

        holder.tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent intent = new Intent("andriod.intent.action.CALL", Uri.parse("tel:"+user.getTel()));
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //  mcontext.startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+user.getTel()));//设置活动类型，电话号码
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

}
