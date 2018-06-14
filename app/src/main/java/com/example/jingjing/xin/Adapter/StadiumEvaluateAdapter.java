package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.Bean.Evaluation;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.EvaluateActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class StadiumEvaluateAdapter extends RecyclerView.Adapter<StadiumEvaluateAdapter.ViewHolder> {

    private Context mcontext;
    private List<Evaluation> mevaluate;
    private Evaluation evaluation;

    public StadiumEvaluateAdapter(Context context, List<Evaluation> evaluate){
        this.mcontext = context;
        this.mevaluate = evaluate;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View evaluateview;

        ImageView iv_profile;
        TextView tv_username;
        TextView tv_evaluatetime;
        TextView tv_grade;
        TextView tv_content;

        public ViewHolder(View view) {
            super(view);
            evaluateview = view ;
            iv_profile = (ImageView) view.findViewById(R.id.iv_proflie);
            tv_username = (TextView)view.findViewById(R.id.tv_username);
            tv_grade  = (TextView)view.findViewById(R.id.tv_grade);
            tv_evaluatetime  = (TextView)view.findViewById(R.id.tv_evaluatetime);
            tv_content = (TextView)view.findViewById(R.id.tv_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_evaluate_stadium,parent,false);
       final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Evaluation evaluation = mevaluate.get(position);//获取当前位置

        holder.tv_username.setText(evaluation.getUsername());
        holder.tv_grade.setText("评分："+evaluation.getGrade());
        holder.tv_content.setText("评价:"+evaluation.getContent());
        holder.tv_evaluatetime.setText(evaluation.getEvaluatetime());
/*
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mcontext);
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error) // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .delayBeforeLoading(0)  // 下载前的延迟时间
                .build();
        ImageLoader.getInstance().displayImage(evaluation.getIcon(), holder.iv_profile,options);
      */
    }

    @Override
    public int getItemCount() {
        return mevaluate.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}