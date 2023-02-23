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
import com.e.mylibrary.R;
import com.e.mylibrary.entity.Comment;
import com.e.mylibrary.entity.Library;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyListCommentAdapter extends RecyclerView.Adapter<MyListCommentAdapter.MyViewHolder> {

    //数据
    private List<Comment> pdata = new ArrayList<Comment>();
    //上下文
    private Context context;

    public MyListCommentAdapter(List<Comment> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadComments(List<Comment> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_commentlist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取通讯录数据
        Comment comment = pdata.get(position);
        holder.item_username.setText(comment.getUserName());
        holder.item_content.setText(comment.getContent());
        holder.item_num.setText("2");

        //获取评论编号
        int id = comment.getId();

        //为图片添加监听器
        holder.item_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        private TextView item_username;
        private TextView item_content;
        private TextView item_num;
        private ImageView item_dz;


        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            item_username = itemView.findViewById(R.id.txtUseName);
            item_content = itemView.findViewById(R.id.txtContent);
            item_num = itemView.findViewById(R.id.txtNum);
            item_dz = itemView.findViewById(R.id.imgDz);
        }
    }
}
