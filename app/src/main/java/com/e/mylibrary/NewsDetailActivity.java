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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.mylibrary.adapter.MyListNewsCommentAdapter;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsDetailActivity extends AppCompatActivity {

    //定义对应的列表项
    private ImageView news_img;
    private TextView txt_title;
    private TextView txt_author;
    private TextView txt_createTime;
    private TextView txt_content;
    private Button btnSend;
    private EditText edcontent;

    //创建RecyclerView控件对象
    private RecyclerView mylistcomment;
    //创建一个数据适配器
    private MyListNewsCommentAdapter myAdapter;

    private int news_id;
    private News news;
    private List<NewsComment> comments = new ArrayList<NewsComment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        news_img = findViewById(R.id.news_img);
        txt_title = findViewById(R.id.txt_title);
        txt_author = findViewById(R.id.txt_author);
        txt_createTime = findViewById(R.id.txt_createTime);
        txt_content = findViewById(R.id.txt_content);
        btnSend = findViewById(R.id.btnSend);
        edcontent = findViewById(R.id.edcontent);

        //获取图书馆编号
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        news_id = bundle.getInt("id");

        //加载数据
        initDataNews();


        //1.初始化控件
        mylistcomment = findViewById(R.id.mylistnews);
        //2.设置RecyclerView布局管理器
        mylistcomment.setLayoutManager(new LinearLayoutManager(NewsDetailActivity.this,LinearLayoutManager.VERTICAL,false));
        //3.初始化数据适配器
        myAdapter = new MyListNewsCommentAdapter(comments,NewsDetailActivity.this);
        //4.设置动画，默认动画
        mylistcomment.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        mylistcomment.setAdapter(myAdapter);

        //发表评论
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doComment();
            }
        });

        //初始化评论列表
        initDataComments();

    }

    //加载数据
    private void initDataComments(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/news-comment/list?newsId="+news_id;
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
                    List<NewsComment> comments_net = new ArrayList<NewsComment>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //data 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("rows");
                        //指定版型使用 Gson 解析 Json
                        comments_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<NewsComment>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=3;  //标识线程,设置成功的指令为1
                    message.obj=comments_net;//列表
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });
    }


    //发表评论
    private void doComment(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        String content = edcontent.getText().toString();

        //获取URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/news-comment";
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //第二步创建RequestBody（Json数据表达）
        MediaType jsonType = MediaType.parse("application/json;charset=utf-8");
        JSONObject pdata = new JSONObject();
        try {
            pdata.put("newsId",news_id);
            pdata.put("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jdata = pdata.toString();//转Json字符串
        RequestBody body = RequestBody.create(jsonType,jdata);
        //第三步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
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
                if(response.isSuccessful()){
                    //获取返回的数据(字符串)
                    String responseData=response.body().string();
                    String code = "";
                    //解析Json数据
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //code
                        code = responseJsonData.getString("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=2;  //标识线程,设置成功的指令为2
                    message.obj=code;//code
                    handler.sendMessage(message);//将指令和数据传出去
                }else {
                    Log.e("YF", "onResponse: "+"NO" );
                    handler.sendEmptyMessage(0);//设置其他指令为零，然后进入handler
                }
            }
        });

    }



    //加载数据
    private void initDataNews(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/news/"+news_id;
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
                    News news_net = null;
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //data 为列表键
                        JSONObject subject = responseJsonData.getJSONObject("data");
                        //指定版型使用 Gson 解析 Json
                        news_net = gson.fromJson(subject.toString(), News.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=news_net;//列表
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
                news = (News) msg.obj;
                //通知UI线程更新数据
                txt_title.setText(news.getTitle());
                txt_author.setText(news.getAuthor());
                txt_createTime.setText(news.getCreateTime());
                txt_content.setText(Html.fromHtml(news.getContent()));

                String imgUrl = "http://124.93.196.45:10001"+news.getImgUrl();
                Picasso.get().load(imgUrl).into(news_img);
            }if(msg.what==3){
                comments = (List<NewsComment>) msg.obj;
                //通知UI线程更新数据
                //通知UI线程更新数据列表
                myAdapter.loadComments(comments);
                myAdapter.notifyDataSetChanged();

            }else if (msg.what==2){
            //发表评论成功，更新列表
                Toast.makeText(NewsDetailActivity.this,"发表成功",Toast.LENGTH_LONG).show();
                //初始化评论列表
                initDataComments();
        }
            return false;
        }
    });
}