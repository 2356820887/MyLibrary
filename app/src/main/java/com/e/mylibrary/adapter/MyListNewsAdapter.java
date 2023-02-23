package com.e.mylibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.mylibrary.LibraryDetailActivity;
import com.e.mylibrary.NewsDetailActivity;
import com.e.mylibrary.R;
import com.e.mylibrary.entity.Library;
import com.e.mylibrary.entity.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyListNewsAdapter extends RecyclerView.Adapter<MyListNewsAdapter.MyViewHolder> {

    //数据
    private List<News> pdata = new ArrayList<News>();
    //上下文
    private Context context;

    public MyListNewsAdapter(List<News> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadNews(List<News> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_librarylist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取数据
        News news = pdata.get(position);
        holder.txt_title.setText(news.getTitle());
        holder.txt_author.setText(news.getAuthor());
        holder.txt_createTime.setText(news.getCreateTime());



        String imgUrl = "http://124.93.196.45:10001"+news.getImgUrl();
        Picasso.get().load(imgUrl).into(holder.news_img);

        int news_id = news.getId();

        //为图片添加监听器
        holder.news_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",news_id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdata.size();
    }

    //定义内部类ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        //定义对应的列表项
        private ImageView news_img;
        private TextView txt_title;
        private TextView txt_author;
        private TextView txt_createTime;


        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            news_img = itemView.findViewById(R.id.news_img);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_author = itemView.findViewById(R.id.txt_author);
            txt_createTime = itemView.findViewById(R.id.txt_createTime);
        }
    }
}
