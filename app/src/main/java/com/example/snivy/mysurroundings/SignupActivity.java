package com.example.snivy.mysurroundings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.signup2);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rePasswordEdit = findViewById(R.id.re_password);
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

    private void sendRequestWithOkHttp() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", accountEdit.getText().toString())
                            .add("password", passwordEdit.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url("http://192.168.31.119:8080/signup")
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
        final SignupActivity.SignupInfo SignupInfo = gson.fromJson(jsonData, SignupInfo.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (SignupInfo.isSuccess) {
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
                    Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
