package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Book;
import com.example.jingjing.xin.R;
import com.example.jingjing.xin.Stadium.EvaluateActivity;
import com.example.jingjing.xin.Stadium.MyEvaluate;

import java.util.List;

public class EvaluatePastAdapter extends RecyclerView.Adapter<EvaluatePastAdapter.ViewHolder> {
    private Context mcontext;
    private List<Book> mbooking;
    private Book book;

    public EvaluatePastAdapter( Context context,List<Book> book){
        this.mcontext = context;
        this.mbooking = book;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View evaluateView;
        TextView tv_stadiumname;
        TextView tv_place;
        TextView tv_time;
        TextView tv_ordertime;
        LinearLayout btn_evaluate_past;

        public ViewHolder(View view) {
            super(view);
            evaluateView = view;

            tv_stadiumname = (TextView)view.findViewById(R.id.tv_stadiumname);
            tv_place = (TextView)view.findViewById(R.id.tv_place);
            tv_time  = (TextView)view.findViewById(R.id.tv_time);
            tv_ordertime = (TextView)view.findViewById(R.id.tv_time_order);
            btn_evaluate_past = (LinearLayout)view.findViewById(R.id.btn_evaluate_past);
        }
    }

    @Override
    public EvaluatePastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_evaluate_past,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.btn_evaluate_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Book book = mbooking.get(position);
                Intent intent = new Intent(mcontext, MyEvaluate.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("book", book);
                intent.putExtras(mBundle);
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = mbooking.get(position);//获取当前位置

        holder.tv_stadiumname.setText("场馆名："+book.getStadiumname());
        holder.tv_time.setText("今天时间："+book.getTime());
        holder.tv_ordertime.setText("预定时间："+book.getTime_order());
        holder.tv_place.setText(book.getPlaceName());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mbooking.size();
    }

}