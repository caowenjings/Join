package com.example.jingjing.xin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jingjing.xin.Bean.Place;
import com.example.jingjing.xin.R;

import java.util.List;

public class SetPlaceAdapter extends RecyclerView.Adapter<SetPlaceAdapter.ViewHolder> {

    private Context mcontext ;
    private Place place;
    private List<Place> mplace;
    private SetPlaceOnClickListener setPlaceOnClickListener = null;

    public SetPlaceAdapter(Context context, List<Place> place){
        this.mcontext = context;
        this.mplace = place;

    }

   public interface SetPlaceOnClickListener{//1.声明一个接口，定义点击事件的回调
       void onItemClick(Place place);
  }

  public void SetPlaceOnclick(SetPlaceOnClickListener listener){
       this.setPlaceOnClickListener = listener;

  }

    static  class ViewHolder extends RecyclerView.ViewHolder {
        View placeview;
        TextView tv_placename;
        TextView tv_placematerial;

        public ViewHolder(View view) {
            super(view);
            placeview = view;
            tv_placename = (TextView)view.findViewById(R.id.tv_placename);
            tv_placematerial= (TextView)view.findViewById(R.id.tv_placematerial);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_placedialog,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.placeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setPlaceOnClickListener != null){
                    int postion = holder.getLayoutPosition();
                    Place place = mplace.get(postion);
                    setPlaceOnClickListener.onItemClick(place);

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SetPlaceAdapter.ViewHolder holder, int position) {
        place = mplace.get(position);
        holder.tv_placename.setText(place.getPlacename());
        holder.tv_placematerial.setText(place.getMaterial());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mplace.size();
    }


}
