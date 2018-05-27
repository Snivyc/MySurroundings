package com.example.snivy.mysurroundings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    private Button signup;

    private EditText accountEdit;

    private EditText passwordEdit;

    private EditText rePasswordEdit;

    private String password;

    private String account;

    private ProgressDialog mProgressDialog;

    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        myApp = (MyApp)getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        signup = findViewById(R.id.signup2);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rePasswordEdit = findViewById(R.id.re_password);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在登陆...");
        mProgressDialog.setCancelable(false);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = passwordEdit.getText().toString();
                account = accountEdit.getText().toString();
                if (!password.equals(rePasswordEdit.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "密码不相同", Toast.LENGTH_SHORT).show();
//                    return;
                } else if (!Validator.isUserName(account)) {
                    Toast.makeText(SignupActivity.this, "用户名不符合要求", Toast.LENGTH_SHORT).show();
                } else if (!Validator.isPassword(password)) {
                    Toast.makeText(SignupActivity.this, "密码不符合要求", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(SignupActivity.this, "密码相同", Toast.LENGTH_SHORT).show();

                    sendRequestWithOkHttp();
                }
//                mProgressDialog.show();
//                sendRequestWithOkHttp();
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
        mProgressDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("account", accountEdit.getText().toString())
                            .add("password", passwordEdit.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url(myApp.getURL() + "signup")
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
        final SignupActivity.SignupInfo signupInfo = gson.fromJson(jsonData, SignupInfo.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.cancel();
                if (signupInfo.isSuccess) {
                    Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "注册失败 "+signupInfo.errorInformation, Toast.LENGTH_SHORT).show();
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
