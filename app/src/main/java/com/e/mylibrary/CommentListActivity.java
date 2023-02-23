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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.e.mylibrary.adapter.MyListCommentAdapter;
import com.e.mylibrary.adapter.MyListLibraryAdapter;
import com.e.mylibrary.entity.Comment;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentListActivity extends AppCompatActivity {

    //创建RecyclerView控件对象
    private RecyclerView mylistcomment;
    //创建一个数据适配器
    private MyListCommentAdapter myAdapter;
    //数据来源
    private List<Comment> comments = new ArrayList<Comment>();

    private int library_id;

    private EditText edcontent;
    private Button btncommtent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);


        edcontent = findViewById(R.id.edcontent);
        btncommtent = findViewById(R.id.btncommtent);

        //1.初始化控件
        mylistcomment = findViewById(R.id.mylistcomment);
        //2.设置RecyclerView布局管理器
        mylistcomment.setLayoutManager(new LinearLayoutManager(CommentListActivity.this,LinearLayoutManager.VERTICAL,false));
        //3.初始化数据适配器
        myAdapter = new MyListCommentAdapter(comments,CommentListActivity.this);
        //4.设置动画，默认动画
        mylistcomment.setItemAnimator(new DefaultItemAnimator());
        //5.设置适配器
        mylistcomment.setAdapter(myAdapter);


        //获取图书馆编号
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        library_id = bundle.getInt("id");

        //加载数据
        initDataComments();

        //发表评论
        btncommtent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doComment();
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
        String url = "http://124.93.196.45:10001/prod-api/api/digital-library/library-comment";
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //第二步创建RequestBody（Json数据表达）
        MediaType jsonType = MediaType.parse("application/json;charset=utf-8");
        JSONObject pdata = new JSONObject();
        try {
            pdata.put("libraryId",library_id);
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
    private void initDataComments(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/digital-library/library-comment/list?libraryId="+library_id+"&pageNum=1&pageSize=10";
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
                    List<Comment> comments_net = new ArrayList<Comment>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //data 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("data");
                        //指定版型使用 Gson 解析 Json
                        comments_net = gson.fromJson
                                (subjects.toString(),new TypeToken<List<Comment>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=comments_net;//列表
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
                comments = (List<Comment>) msg.obj;
                //通知UI线程更新数据列表
                myAdapter.loadComments(comments);
                myAdapter.notifyDataSetChanged();
            }else if (msg.what==2){
                //发表评论成功，更新列表
                //加载数据
                initDataComments();
            }
            return false;
        }
    });

}