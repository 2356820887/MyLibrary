package com.e.mylibrary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.mylibrary.adapter.LoveAdapter;
import com.e.mylibrary.entity.Love;
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

public class LoveSearchActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    //创建RecyclerView控件对象
    private RecyclerView loveList;
    //创建一个数据适配器
    private LoveAdapter myAdapter;
    //数据来源
    private List<Love> loves = new ArrayList<Love>();

    private int id;
    private Button btnSearch;
    private EditText edSearchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_love_search);

        btnSearch = findViewById(R.id.btnSearch);
        edSearchName = findViewById(R.id.editTextPersonName);

        //1.初始化控件
        loveList = findViewById(R.id.lawyerList);
        //2.设置RecyclerView布局管理器
        loveList.setLayoutManager(new LinearLayoutManager(LoveSearchActivity.this, LinearLayoutManager.VERTICAL, false));
        //3.初始化数据适配器
        myAdapter = new LoveAdapter(loves, LoveSearchActivity.this);
        //4.设置动画，默认动画
        loveList.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        loveList.setAdapter(myAdapter);


        //搜索功能
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int keyword = Integer.parseInt(edSearchName.getText().toString());
                System.out.println("搜索词："+keyword);
                System.out.println(loves);
                initDataTypes(keyword);
            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId){

    }

    //加载数据
    private void initDataTypes(int id) {

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken", MODE_PRIVATE);
        String token = sp.getString("token", "");

        //获取电影列表的URL
        String url="http://124.93.196.45:10001/prod-api/api/public-welfare/public-welfare-activity/list?typeId="+id;
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
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
                if (response.isSuccessful()) {
                    //获取返回的电影列表(字符串)
                    String responseData = response.body().string();
                    //解析Json数据
                    //创建Gson对象
                    Gson gson = new Gson();
                    //将返回的字符串转换为Json对象
                    //创建列表
                    List<Love> lawyerList_net = new ArrayList<Love>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("rows");
                        //指定版型使用 Gson 解析 Json
                        lawyerList_net = gson.fromJson
                                (subjects.toString(), new TypeToken<List<Love>>() {
                                }.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: " + "YES");
                    message.what = 3;  //标识线程,设置成功的指令为1
                    message.obj = lawyerList_net;//列表
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
            if(msg.what==1){
                loves = (List<Love>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadloveList(loves);
                myAdapter.notifyDataSetChanged();
            }else if(msg.what==2){
                loves = (List<Love>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadloveList(loves);
                myAdapter.notifyDataSetChanged();
            }else if(msg.what==3){
                loves = (List<Love>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadloveList(loves);
                myAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
}