package com.example.snivy.mysurroundings;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Snivy on 2018/4/17.
 */

public class PointSet {

    List<Point> allPoints = new ArrayList<>();

    MainActivity mMainActivity;

    PointSet(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    void reflashAllPoints() {
        sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url("http://192.168.31.119:8080/")
                            .build();
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
