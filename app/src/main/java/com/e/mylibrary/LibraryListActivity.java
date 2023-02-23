package com.e.mylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.e.mylibrary.adapter.MyListLibraryAdapter;
import com.e.mylibrary.entity.Library;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LibraryListActivity extends AppCompatActivity {
    //创建RecyclerView控件对象
    private RecyclerView mylistlibray;
    //创建一个数据适配器
    private MyListLibraryAdapter myAdapter;
    //数据来源
    private List<Library> librarys = new ArrayList<Library>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);
        //初始化数据
        initDataLibrarys();

        //1.初始化控件
        mylistlibray = findViewById(R.id.mylistlibray);
        //2.设置RecyclerView布局管理器
        mylistlibray.setLayoutManager(new LinearLayoutManager(LibraryListActivity.this,LinearLayoutManager.VERTICAL,false));
        //3.初始化数据适配器
        myAdapter = new MyListLibraryAdapter(librarys,LibraryListActivity.this);
        //4.设置动画，默认动画
        mylistlibray.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        mylistlibray.setAdapter(myAdapter);
    }

    //加载数据
    private void initDataLibrarys(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/digital-library/library/list?pageNum=1&pageSize=10";
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .get()
                .build();
        //第三步构建Call对象
        Call call = client.newCall(request);
        //第四步:异步get请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断是否成功
                if(response.isSuccessful()){
                    //获取返回的电影列表(字符串)
                    String responseData=response.body().string();
                    //解析Json数据
                    //创建Gson对象
                    Gson gson = new Gson();
                    //将返回的字符串转换为Json对象
                    //创建列表
                    List<Library> librarys_net = new ArrayList<Library>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("rows");
                        //指定版型使用 Gson 解析 Json
                        librarys_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<Library>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=librarys_net;//列表
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
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
            if(msg.what==1){
                librarys = (List<Library>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadLibrarys(librarys);
                myAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });



}