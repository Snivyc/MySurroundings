package com.example.snivy.mysurroundings;


import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Snivy on 2018/4/17.
 */

public class PointSet {

    List<Point> allPoints = new ArrayList<>();

    MainActivity mMainActivity;

    private String s;

    PointSet(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    void reflashAllPoints() {
        s = "";
        sendRequestWithOkHttp();
    }

    void reflashAllPoints(String s) {
        this.s = "search/" + s;
        sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BDLocation location= mMainActivity.mLocationClient.getLastKnownLocation();
                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("x", String.valueOf(location.getLatitude()))
                            .add("y", String.valueOf(location.getLongitude()))
                            .add("ID", String.valueOf(mMainActivity.myApp.getID()))
                            .add("password",mMainActivity.myApp.getPassword())
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url(mMainActivity.myApp.getURL() + s)
                            .post(requestBody)
                            .build();

//                    Request request = new Request.Builder()
//                            // 指定访问的服务器地址是电脑本机
//                            .url("http://192.168.31.119:8080/")
//                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
//                    parseJSONWithJSONObject(responseData);
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
//                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        allPoints = gson.fromJson(jsonData, new TypeToken<List<Point>>() {}.getType());
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainActivity.undo_point_clicked();
                mMainActivity.printPoints();
            }
        });
    }

    List<Point> getAllPoints(){
        return allPoints;
    }

    Point getPoint(int i) {
        return allPoints.get(i);
    }



}
