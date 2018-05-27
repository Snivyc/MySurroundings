package com.example.snivy.mysurroundings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirstActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private String account, password;

    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApp)getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            account = pref.getString("account", "");
            password = pref.getString("password", "");
            sendRequestWithOkHttp();
        }else{
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();}
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", account)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url(myApp.getURL() + "login")
                            .post(requestBody)
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
        final LoginActivity.LoginInfo loginInfo = gson.fromJson(jsonData, LoginActivity.LoginInfo.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loginInfo.isSuccess) {
                    myApp.setAccount(account);
                    myApp.setPassword(password);
                    myApp.setID(loginInfo.ID);
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(FirstActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FirstActivity.this, SignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public class LoginInfo {
        public boolean isSuccess;
        public String errorInformation;
        public int ID;
    }
}
