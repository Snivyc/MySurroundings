package com.example.snivy.mysurroundings;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snivy on 2018/4/10.
 */

public class ActivityCollector
{
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivitty(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll(){
        Log.e("fuck", "finashAll"+activities.size());
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
