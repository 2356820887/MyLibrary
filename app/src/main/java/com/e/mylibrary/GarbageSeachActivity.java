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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.e.mylibrary.adapter.MyListNewsAdapter;
import com.e.mylibrary.adapter.MyListTypesAdapter;
import com.e.mylibrary.entity.GarbageType;
import com.e.mylibrary.entity.KeyWord;
import com.e.mylibrary.entity.MyBanner;
import com.e.mylibrary.entity.News;
import com.e.mylibrary.garbage.ui.Fragment_1;
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

public class GarbageSeachActivity extends AppCompatActivity {

    private List<MyBanner> banners = new ArrayList<>();
    private List<KeyWord> keyWords = new ArrayList<>();
    private Banner banner;
    private TextView txtSeachKey;
    //创建RecyclerView控件对象
    private RecyclerView mytyperecyclerview;
    //创建一个数据适配器
    private MyListTypesAdapter myAdapter;
    //数据来源
    private List<GarbageType> garbageTypes = new ArrayList<GarbageType>();

    private Button btnSeach;
    private EditText edSeachName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_seach);
        banner = findViewById(R.id.banner);
        txtSeachKey = findViewById(R.id.txtSeachKey);
        btnSeach= findViewById(R.id.btnSeach);
        edSeachName = findViewById(R.id.edSeachName);


        //1.初始化控件
        mytyperecyclerview = findViewById(R.id.mytyperecyclerview);
        //2.设置RecyclerView布局管理器
        mytyperecyclerview.setLayoutManager(new LinearLayoutManager(GarbageSeachActivity.this,LinearLayoutManager.VERTICAL,false));
        //3.初始化数据适配器
        myAdapter = new MyListTypesAdapter(garbageTypes,GarbageSeachActivity.this);
        //4.设置动画，默认动画
        mytyperecyclerview.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        mytyperecyclerview.setAdapter(myAdapter);

        //加载广告轮播图
        initDataBanners();
        initKeyWord();//加载热词
        //初始化数据
        initDataTypes("");

        //搜索功能
        btnSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String keyword = edSeachName.getText().toString();
                initDataTypes(keyword);

            }
        });
    }


    //加载数据
    private void initDataTypes(String keyword){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");


        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/garbage-classification/list?pageNum=1&pageSize=10&name="+keyword;
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
                    List<GarbageType> garbageTypes_net = new ArrayList<GarbageType>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("rows");
                        //指定版型使用 Gson 解析 Json
                        garbageTypes_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<GarbageType>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=3;  //标识线程,设置成功的指令为1
                    message.obj=garbageTypes_net;//列表
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });
    }

    //获取热词
    private void initKeyWord(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/garbage-classification/hot/list?pageNum=1&pageSize=10";
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
                    List<KeyWord> keyWords_net = new ArrayList<KeyWord>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("data");
                        //指定版型使用 Gson 解析 Json
                        keyWords_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<KeyWord>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=2;  //标识线程,设置成功的指令为1
                    message.obj=keyWords_net;//列表
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });
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
                }).addBannerLifecycleObserver(GarbageSeachActivity.this)//添加生命周期观察者
                        .setIndicator(new CircleIndicator(GarbageSeachActivity.this));
            }else if (msg.what == 2){
                keyWords = (List<KeyWord>) msg.obj;

                for (int i = 0 ;i<keyWords.size();i++){
                    KeyWord keyWord = keyWords.get(i);
                    txtSeachKey.setText(keyWord.getKeyword()+"  ");
                }

            } if(msg.what==3){
                garbageTypes = (List<GarbageType>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadGarbageTypes(garbageTypes);
                myAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });

}