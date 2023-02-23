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
import android.widget.ImageView;
import android.widget.TextView;

import com.e.mylibrary.entity.Library;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LibraryDetailActivity extends AppCompatActivity {

    //定义对应的列表项
    private ImageView library_img;
    private TextView txt_title;
    private TextView txt_address;
    private TextView txt_date;
    private TextView txt_state;
    private TextView txt_description;
    private int library_id;
    private Library library;
    private Button btnGoComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        setContentView(R.layout.activity_library_detail);
        library_img = findViewById(R.id.news_img);
        txt_title = findViewById(R.id.txt_title);
        txt_address = findViewById(R.id.txt_author);
        txt_date = findViewById(R.id.txt_createTime);
        txt_state = findViewById(R.id.txt_state);
        txt_description = findViewById(R.id.txt_description);
        btnGoComment = findViewById(R.id.btnGoComment);

        //获取图书馆编号
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        library_id = bundle.getInt("id");

        //加载数据
        initDataLibrary();


        //点击评论按钮，显示评论列表
        btnGoComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryDetailActivity.this, CommentListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",library_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }



    //加载数据
    private void initDataLibrary(){

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
        String token = sp.getString("token","");

        //获取的URL
        String url = "http://124.93.196.45:10001/prod-api/api/digital-library/library/"+library_id;
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
                    Library library_net = null;
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //data 为列表键
                        JSONObject subject = responseJsonData.getJSONObject("data");
                        //指定版型使用 Gson 解析 Json
                        library_net = gson.fromJson(subject.toString(),Library.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: "+"YES" );
                    message.what=1;  //标识线程,设置成功的指令为1
                    message.obj=library_net;//列表
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
                library = (Library) msg.obj;
                //通知UI线程更新数据
                txt_title.setText(library.getName());
                txt_address.setText(library.getAddress());
                txt_date.setText(library.getBusinessHours());
                txt_description.setText(library.getDescription());
                if (library.getBusinessState().equals("1")){
                    txt_state.setText("开馆");
                }else {
                    txt_state.setText("闭馆");
                }
                String imgUrl = "http://124.93.196.45:10001"+library.getImgUrl();
                Picasso.get().load(imgUrl).into(library_img);
            }
            return false;
        }
    });

}