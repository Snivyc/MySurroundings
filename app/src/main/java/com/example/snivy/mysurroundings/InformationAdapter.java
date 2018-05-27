package com.example.snivy.mysurroundings;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.util.List;

/**
 * Created by Snivy on 2018/4/18.
 */

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>{

    private List<Point> mPointList;

    private LocationClient mLocationClient;

    private MainActivity mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView infoView;
        Button button;
        TextView distanceView;


        public ViewHolder(View view) {
            super(view);
            infoView = (TextView) view.findViewById(R.id.item_info);
            button = (Button) view.findViewById(R.id.item_satnav_button);
            distanceView = (TextView) view.findViewById(R.id.item_distance);
        }

    }

    public InformationAdapter(List<Point> pointList, LocationClient locationClient, MainActivity context) {
        mPointList = pointList;
        mLocationClient = locationClient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Point point = mPointList.get(position);
//                Toast.makeText(view.getContext(), "you clicked view" + point.getInformation(), Toast.LENGTH_SHORT).show();
                mContext.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mContext.point_clicked(position);
                mContext.move_to_clicked_point();
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng myll = new LatLng(location.getLatitude(), location.getLongitude());
                int position = holder.getAdapterPosition();
                Point point = mPointList.get(position);
                mContext.point_clicked(position);
                mContext.move_to_clicked_point();
                mContext.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.infoView.setText(mPointList.get(position).getInformation());
        holder.distanceView.setText(String.valueOf(mPointList.get(position).getDistance())+"m");
    }

    @Override
    public int getItemCount(){
        return mPointList.size();
    }
}
