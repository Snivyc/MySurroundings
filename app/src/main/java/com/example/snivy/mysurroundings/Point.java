package com.example.snivy.mysurroundings;

/**
 * Created by Snivy on 2018/4/17.
 */

class Point{
    public double x, y;
    private String information;
    private int distance;

    Point(double x, double y, String i) {
        this.x = x;
        this.y = y;
        this.information = i;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInformation(){
        return information;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}