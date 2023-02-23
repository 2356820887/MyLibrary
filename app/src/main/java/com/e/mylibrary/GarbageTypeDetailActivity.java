package com.e.mylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.mylibrary.adapter.MyListLibraryAdapter;
import com.e.mylibrary.adapter.MyTypesDetailAdapter;
import com.e.mylibrary.entity.GarbageType;
import com.e.mylibrary.entity.KeyWord;
import com.e.mylibrary.entity.Library;
import com.e.mylibrary.entity.News;
import com.e.mylibrary.entity.NewsComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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

public class GarbageTypeDetailActivity extends AppCompatActivity {

    //定义对应的列表项
    private ImageView type_img;
    private TextView txt_name;
    private TextView txt_introduce;
    private TextView txt_guide;
    private int id;
    private String keyword;
    //数据来源
    private List<GarbageType> garbageTypes = new ArrayList<GarbageType>();

    //创建RecyclerView控件对象
    private RecyclerView myrecyclerview2;
    //创建一个数据适配器
    private MyTypesDetailAdapter myAdapter;
    //数据来源
    private List<GarbageType> garbageTypes_jl = new ArrayList<GarbageType>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_type_detail);
        //获取对应列表项
        type_img = findViewById(R.id.type_img);
        txt_name = findViewById(R.id.txt_name);
        txt_introduce = findViewById(R.id.txt_introduce);
        txt_guide = findViewById(R.id.txt_guide);

        //获垃圾分类编号
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");

        initDataTypes();

        //1.初始化控件
        myrecyclerview2 = findViewById(R.id.myrecyclerview2);
        //2.设置RecyclerView布局管理器
        myrecyclerview2.setLayoutManager(new LinearLayoutManager(GarbageTypeDetailActivity.this,LinearLayoutManager.VERTICAL,false));
        //3.初始化数据适配器
        myAdapter = new MyTypesDetailAdapter(garbageTypes_jl,GarbageTypeDetailActivity.this);
        //4.设置动画，默认动画
        myrecyclerview2.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        myrecyclerview2.setAdapter(myAdapter);

        //加载垃圾分类举例
        initDataTypeJls();


    }

    //加载数据
    private void initDataTypeJls(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/garbage-example/list?type="+id;
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
                    List<GarbageType> garbageTypesjl_net = new ArrayList<GarbageType>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("rows");
                        //指定版型使用 Gson 解析 Json
                        garbageTypesjl_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<GarbageType>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=2;  //标识线程,设置成功的指令为1
                    message.obj=garbageTypesjl_net;//列表
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });
    }

    //加载数据
    private void initDataTypes(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");


        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/garbage-classification/list?pageNum=1&pageSize=10&name=";
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
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=garbageTypes_net;//列表
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

                garbageTypes = (List<GarbageType>) msg.obj;

                GarbageType garbageType = null;
                for (int i = 0;i<garbageTypes.size();i++){
                    GarbageType garbageType_temp = garbageTypes.get(i);
                    if (garbageType_temp.getId() == id){
                        garbageType = garbageType_temp;
                        break;
                    }
                }
                if (garbageType!=null){
                    //通知UI线程更新数据
                    txt_name.setText(garbageType.getName());
                    txt_introduce.setText(garbageType.getIntroduce());
                    txt_guide.setText(Html.fromHtml(garbageType.getGuide()));

                    String imgUrl = "http://124.93.196.45:10001"+garbageType.getImgUrl();
                    Picasso.get().load(imgUrl).into(type_img);
                }



            }if(msg.what==2){
                garbageTypes_jl = (List<GarbageType>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadGarbageTypes(garbageTypes_jl);
                myAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });

}