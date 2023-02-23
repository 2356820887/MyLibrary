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

import com.e.mylibrary.NewsDetailActivity;
import com.e.mylibrary.R;
import com.e.mylibrary.entity.News;
import com.e.mylibrary.entity.NewsComment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyListNewsCommentAdapter extends RecyclerView.Adapter<MyListNewsCommentAdapter.MyViewHolder> {

    //数据
    private List<NewsComment> pdata = new ArrayList<NewsComment>();
    //上下文
    private Context context;

    public MyListNewsCommentAdapter(List<NewsComment> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadComments(List<NewsComment> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_newscommentlist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取数据
        NewsComment newsComment = pdata.get(position);
        holder.txt_content.setText(newsComment.getContent());
        holder.txt_createTime.setText(newsComment.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return pdata.size();
    }

    //定义内部类ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        //定义对应的列表项
        private TextView txt_content;
        private TextView txt_createTime;


        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            txt_content = itemView.findViewById(R.id.txt_content);
            txt_createTime = itemView.findViewById(R.id.txt_createTime);
        }
    }
}
