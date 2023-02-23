package com.e.mylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.e.mylibrary.entity.GarbageType;
import com.e.mylibrary.entity.KeyWord;
import com.e.mylibrary.entity.MyBanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

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

public class GarbageClassifyActivity extends AppCompatActivity implements View.OnClickListener {
    private List<MyBanner> banners = new ArrayList<>();
    private Banner banner;
    private Button btnKhsw,btnYhlj,btnSlj,btnGlj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_classify);
        banner = findViewById(R.id.banner);
        btnKhsw = findViewById(R.id.btnKhsw);
        btnYhlj = findViewById(R.id.btnYhlj);
        btnSlj = findViewById(R.id.btnSlj);
        btnGlj = findViewById(R.id.btnGlj);

        btnKhsw.setOnClickListener(this);
        btnYhlj.setOnClickListener(this);
        btnSlj.setOnClickListener(this);
        btnGlj.setOnClickListener(this);
        //加载广告轮播图
        initDataBanners();
    }

    @Override
    public void onClick(View v) {
        int id = 0;
        String keyword="";

        if (v.getId()==R.id.btnKhsw){
            id = 8;
            keyword="可回收垃圾";
        }else if (v.getId()==R.id.btnYhlj){
            id = 9;
            keyword="有害垃圾";
        }else if (v.getId()==R.id.btnSlj){
            id = 10;
            keyword="湿垃圾";
        }else if (v.getId()==R.id.btnGlj){
            id = 11;
            keyword="干垃圾";
        }

        Intent intent = new Intent(GarbageClassifyActivity.this, GarbageTypeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("keyword",keyword);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //加载数据
    private void initDataBanners(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/poster/list";
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
                    List<MyBanner> banners_net = new ArrayList<MyBanner>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("data");
                        //指定版型使用 Gson 解析 Json
                        banners_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<MyBanner>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=banners_net;//列表
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
                //轮播图
                banners = (List<MyBanner>) msg.obj;
                //通知UI线程更新数据列表
                banner.setAdapter(new BannerImageAdapter<MyBanner>(banners) {
                    @Override
                    public void onBindView(BannerImageHolder holder, MyBanner data, int position, int size) {
                        //图片加载自己实现
                        String imgUrl = "http://124.93.196.45:10001"+data.getImgUrl();
                        Picasso.get().load(imgUrl).into(holder.imageView);
                    }
                }).addBannerLifecycleObserver(GarbageClassifyActivity.this)//添加生命周期观察者
                        .setIndicator(new CircleIndicator(GarbageClassifyActivity.this));
            }
            return false;
        }
    });



}