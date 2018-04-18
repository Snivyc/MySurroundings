package com.example.snivy.mysurroundings;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snivy on 2018/4/17.
 */

public class PointSet {


    List<Point> allPoints = new ArrayList<>();

    PointSet(){
        reflashAllPoints();
    }

    void reflashAllPoints() {
        allPoints.clear();
        Point p;
        p = new Point(32.07, 118.75, "火锅");
        allPoints.add(p);
        p = new Point(32.07, 118.57, "电影");
        allPoints.add(p);
        p = new Point(31.939, 118.65, "自助餐");
        allPoints.add(p);
        p = new Point(32.0, 118.7, "修电脑");
        allPoints.add(p);
        p = new Point(32.024, 118.59, "火锅");
        allPoints.add(p);
    }

    List<Point> getAllPoints(){
        return allPoints;
    }

    Point getPoint(int i) {
        return allPoints.get(i);
    }



}
