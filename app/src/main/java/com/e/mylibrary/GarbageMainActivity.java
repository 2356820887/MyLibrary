package com.e.mylibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.e.mylibrary.adapter.MyFragmentPagerAdapter;
import com.e.mylibrary.entity.MyBanner;
import com.e.mylibrary.garbage.ui.Fragment_1;
import com.e.mylibrary.garbage.ui.Fragment_2;
import com.e.mylibrary.garbage.ui.Fragment_3;
import com.e.mylibrary.garbage.ui.Fragment_4;
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

public class GarbageMainActivity extends AppCompatActivity implements View.OnClickListener {

    //定义属性
    private TextView tab_1, tab_2, tab_3, tab_4;//顶部标签
    private ViewPager2 myViewPager;//切换区
    private List<Fragment> fragmentList;//Fragment列表
    private MyFragmentPagerAdapter fragmentPagerAdapter;//适配器

    private List<MyBanner> banners = new ArrayList<>();
    private Banner banner;

    private Button btnSeach, btnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_main);

        banner = findViewById(R.id.banner);
        btnSeach = findViewById(R.id.btnSeach);
        btnType = findViewById(R.id.btnType);
        //加载广告轮播图
        initDataBanners();

        //初始化
        initUI();
        initTab();
        //打开搜索页
        btnSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GarbageMainActivity.this, GarbageSeachActivity.class);
                startActivity(intent);
            }
        });

        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GarbageMainActivity.this, GarbageClassifyActivity.class);
                startActivity(intent);
            }
        });

    }

    //加载数据
    private void initDataBanners() {

        //获取token
        SharedPreferences sp = getSharedPreferences("logintoken", MODE_PRIVATE);
        String token = sp.getString("token", "");

        //获取电影列表的URL
        String url = "http://124.93.196.45:10001/prod-api/api/garbage-classification/ad-banner/list";
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
                    List<MyBanner> banners_net = new ArrayList<MyBanner>();
                    try {
                        JSONObject responseJsonData = new JSONObject(responseData);
                        //rows 为列表键
                        JSONArray subjects = responseJsonData.getJSONArray("data");
                        //指定版型使用 Gson 解析 Json
                        banners_net = gson.fromJson
                                (subjects.toString(), new TypeToken<List<MyBanner>>() {
                                }.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //封装消息，传递给主线程
                    Message message = handler.obtainMessage();
                    Log.e("YF", "onResponse: " + "YES");
                    message.what = 1;  //标识线程,设置成功的指令为1
                    message.obj = banners_net;//列表
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
                //轮播图
                banners = (List<MyBanner>) msg.obj;
                //通知UI线程更新数据列表
                banner.setAdapter(new BannerImageAdapter<MyBanner>(banners) {
                            @Override
                            public void onBindView(BannerImageHolder holder, MyBanner data, int position, int size) {
                                //图片加载自己实现
                                String imgUrl = "http://124.93.196.45:10001" + data.getImgUrl();
                                Picasso.get().load(imgUrl).into(holder.imageView);
                            }
                        }).addBannerLifecycleObserver(GarbageMainActivity.this)//添加生命周期观察者
                        .setIndicator(new CircleIndicator(GarbageMainActivity.this));
            }
            return false;
        }
    });

    /**
     * 初始化UI
     */
    private void initUI() {
        //初始化底部标签
        tab_1 = (TextView) findViewById(R.id.tv_tab1);
        tab_2 = (TextView) findViewById(R.id.tv_tab2);
        tab_3 = (TextView) findViewById(R.id.tv_tab3);
        tab_4 = (TextView) findViewById(R.id.tv_tab4);
        //为底部标签添加点击事件
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        tab_4.setOnClickListener(this);
        //初始化切换区
        myViewPager = findViewById(R.id.myViewPager);
    }

    /**
     * 初始化Fragment及第一个显示的标签
     */
    private void initTab() {

        //新建Fragment
        Fragment_1 fragment1 = new Fragment_1();
        Fragment_2 fragment2 = new Fragment_2();
        Fragment_3 fragment3 = new Fragment_3();
        Fragment_4 fragment4 = new Fragment_4();
        //建立列表
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        //新建适配器
        fragmentPagerAdapter = new MyFragmentPagerAdapter(GarbageMainActivity.this, fragmentList);
        //设置适配器
        myViewPager.setAdapter(fragmentPagerAdapter);

        //设置滑动监听
        myViewPager.registerOnPageChangeCallback(new MyPageChangeListennr());

        //显示第一个页面
        showFragment(0);

    }

    /**
     * 显示Fragment
     */
    private void showFragment(int num) {

        //按索引显示Fragment
        myViewPager.setCurrentItem(num);
        //改变底部标签
        if (num == 0) {
            tab_1.setBackgroundColor(Color.RED);
            tab_2.setBackgroundColor(Color.WHITE);
            tab_3.setBackgroundColor(Color.WHITE);
            tab_4.setBackgroundColor(Color.WHITE);
        } else if (num == 1) {
            tab_1.setBackgroundColor(Color.WHITE);
            tab_2.setBackgroundColor(Color.RED);
            tab_3.setBackgroundColor(Color.WHITE);
            tab_4.setBackgroundColor(Color.WHITE);
        } else if (num == 2) {
            tab_1.setBackgroundColor(Color.WHITE);
            tab_2.setBackgroundColor(Color.WHITE);
            tab_3.setBackgroundColor(Color.RED);
            tab_4.setBackgroundColor(Color.WHITE);
        } else if (num == 3) {
            tab_1.setBackgroundColor(Color.WHITE);
            tab_2.setBackgroundColor(Color.WHITE);
            tab_3.setBackgroundColor(Color.WHITE);
            tab_4.setBackgroundColor(Color.RED);
        }
    }

    /**
     * 底部标签点击事件
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tv_tab1) {
            //第一个标签被点击
            showFragment(0);
        } else if (view.getId() == R.id.tv_tab2) {
            //第二个标签被点击
            showFragment(1);
        } else if (view.getId() == R.id.tv_tab3) {
            //第二个标签被点击
            showFragment(2);
        } else if (view.getId() == R.id.tv_tab4) {
            //第二个标签被点击
            showFragment(3);
        }
    }

    /**
     * 定义页面滑动的监听类，用于页面滑动时，底部导航跟着变化
     */

    public class MyPageChangeListennr extends ViewPager2.OnPageChangeCallback {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            //改变底部标签
            if (position == 0) {
                tab_1.setBackgroundColor(Color.RED);
                tab_2.setBackgroundColor(Color.WHITE);
                tab_3.setBackgroundColor(Color.WHITE);
                tab_4.setBackgroundColor(Color.WHITE);
            } else if (position == 1) {
                tab_1.setBackgroundColor(Color.WHITE);
                tab_2.setBackgroundColor(Color.RED);
                tab_3.setBackgroundColor(Color.WHITE);
                tab_4.setBackgroundColor(Color.WHITE);
            } else if (position == 2) {
                tab_1.setBackgroundColor(Color.WHITE);
                tab_2.setBackgroundColor(Color.WHITE);
                tab_3.setBackgroundColor(Color.RED);
                tab_4.setBackgroundColor(Color.WHITE);
            } else if (position == 3) {
                tab_1.setBackgroundColor(Color.WHITE);
                tab_2.setBackgroundColor(Color.WHITE);
                tab_3.setBackgroundColor(Color.WHITE);
                tab_4.setBackgroundColor(Color.RED);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    }


}