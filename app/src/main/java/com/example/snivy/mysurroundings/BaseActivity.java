package com.example.snivy.mysurroundings;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Snivy on 2018/4/12.
 */

public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivitty(this);
        Log.e("fuck", "onCreate: addact");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.snivy.mysurroundings.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {

            Log.e("fuck", "onClick: 0");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Waring");
            builder.setMessage("forced to be offline");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.e("fuck", "onClick: 1");
                    ActivityCollector.finishAll();
                    Log.e("fuck", "onClick: 2");
                    Intent intent = new Intent(context, LoginActivity.class);
                    Log.e("fuck", "onClick: 3");
                    context.startActivity(intent);
                }
            });
            builder.show();

        }
    }
}
