package com.e.mylibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText txtName, txtPwd;
    private Button btnLogin, btnLibrary, btnGarbage, btnPostHouse;
    private TextView txtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = findViewById(R.id.txtName);
        txtPwd = findViewById(R.id.txtPwd);
        txtMsg = findViewById(R.id.txtMsg);
        btnLogin = findViewById(R.id.btnLogin);
        btnLibrary = findViewById(R.id.btnLibrary);
        btnGarbage = findViewById(R.id.btnGarbage);
        btnPostHouse = findViewById(R.id.btnPosthouse);
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        //打开数字图书馆
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibraryListActivity.class);
                startActivity(intent);
            }
        });
        //打开垃圾分类
        btnGarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GarbageMainActivity.class);
                startActivity(intent);
            }
        });
        //打开爱心捐助
        btnPostHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoveActivity.class);
                startActivity(intent);
            }
        });
    }


    //登录功能
    private void doLogin() {
        //获取账号及密码
        String uname = txtName.getText().toString();
        String upwd = txtPwd.getText().toString();

        //获取URL
        String url = "http://124.93.196.45:10001/prod-api/api/login";
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //第二步创建RequestBody（Json数据表达）
        MediaType jsonType = MediaType.parse("application/json;charset=utf-8");
        JSONObject pdata = new JSONObject();
        try {
            pdata.put("username", uname);
            pdata.put("password", upwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jdata = pdata.toString();//转Json字符串
        RequestBody body = RequestBody.create(jsonType, jdata);
        //第三步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //第四步构建Call对象
        Call call = client.newCall(request);
        //第五步:异步get请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断是否成功
                if (response.isSuccessful()) {
                    //获取返回的数据(字符串)
                    String responseData = response.body().string();
                    String token = "";
                    //解析Json数据
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //获取登录的token
                        token = responseJsonData.getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: " + "YES");
                    message.what = 1;  //标识线程,设置成功的指令为1
                    message.obj = token;//登录的token
                    handler.sendMessage(message);//将指令和数据传出去
                } else {
                    Log.e("YF", "onResponse: " + "NO");
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });

    }


    //建立一个Handler对象，用于主线程和子线程之间进行通信
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            //msg.what 用于判断从那个线程传递过来的消息
            if (msg.what == 1) {
                String token = msg.obj.toString();
                //将token存储，提供给后续功能使用
                SharedPreferences sp = getSharedPreferences("logintoken", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", token);
                editor.commit();
                txtMsg.setText("登录成功");
            }
            return false;
        }
    });

}