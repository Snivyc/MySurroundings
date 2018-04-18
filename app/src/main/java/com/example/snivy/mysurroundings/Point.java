package com.example.snivy.mysurroundings;

/**
 * Created by Snivy on 2018/4/17.
 */

class Point{
    public double x, y;
    private String Information;

    Point(double x, double y, String i) {
        this.x = x;
        this.y = y;
        this.Information = i;
    }

    public String getInformation(){
        return Information;
    }
}