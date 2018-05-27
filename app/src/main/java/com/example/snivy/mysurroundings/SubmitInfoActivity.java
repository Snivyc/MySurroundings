package com.example.snivy.mysurroundings;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubmitInfoActivity extends AppCompatActivity {

    private String info;

    private MyApp myApp;

    private ProgressDialog mProgressDialog;

    private double x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_info);
        myApp = (MyApp)getApplicationContext();

        x = getIntent().getDoubleExtra("x", 0.d);
        y = getIntent().getDoubleExtra("y", 0.d);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCancelable(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.submitted_info);
                info = textView.getText().toString();
                if (info.equals("")) {
                    Toast.makeText(SubmitInfoActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                } else {
                    mProgressDialog.show();
                    sendRequestWithOkHttp();

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("info", info)
                            .add("ID", String.valueOf(myApp.getID()))
                            .add("x", String.valueOf(x))
                            .add("y", String.valueOf(y))
                            .add("password",myApp.getPassword())
                            .build();
                    Request request = new Request.Builder()
                            .url(myApp.getURL() + "submitinfo")
                            .post(requestBody)
                            .build();

//                    Request request = new Request.Builder()
//                            // 指定访问的服务器地址是电脑本机
//                            .url("http://192.168.31.119:8080/")
//                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        final SignupActivity.SignupInfo signupInfo = gson.fromJson(jsonData, SignupActivity.SignupInfo.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.cancel();
                if (signupInfo.isSuccess) {
//                    editor = pref.edit();
//                    if (rememberPass.isChecked()) {
//                        editor.putBoolean("remember_password", true);
//                        editor.putString("account", accountEdit.getText().toString());
//                        editor.putString("password", passwordEdit.getText().toString());
//                    } else {
//                        editor.clear();
//                    }
//                    editor.apply();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
                    Toast.makeText(SubmitInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SubmitInfoActivity.this, "提交失败 "+signupInfo.errorInformation, Toast.LENGTH_SHORT).show();
//                    mProgressDialog.hide();
                }
            }
        });
    }

    public class SignupInfo {
        public boolean isSuccess;
        public String errorInformation;
    }
}
