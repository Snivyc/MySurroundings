package com.example.snivy.mysurroundings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Snivy on 2018/4/18.
 */

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>{

    private List<Point> mPointList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;

        public ViewHolder(View view) {
            super(view);
            info = (TextView) view.findViewById(R.id.info);
        }

    }

    public InformationAdapter(List<Point> pointList) {
        mPointList = pointList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.info.setText(mPointList.get(position).getInformation());
    }

    @Override
    public int getItemCount(){
        return mPointList.size();
    }
}
