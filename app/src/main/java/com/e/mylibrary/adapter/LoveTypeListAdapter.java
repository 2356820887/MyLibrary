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

import com.e.mylibrary.LoveTypeActivity;
import com.e.mylibrary.R;
import com.e.mylibrary.entity.LoveType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LoveTypeListAdapter extends RecyclerView.Adapter<LoveTypeListAdapter.MyViewHolder> {
    //数据
    private List<LoveType> pdata = new ArrayList<>();
    //上下文
    private Context context;

    public LoveTypeListAdapter(List<LoveType> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadlegalExpertise(List<LoveType> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_lovetypelist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取数据
        LoveType legalExpertise = pdata.get(position);
        holder.love_name.setText(legalExpertise.getName());

        String imgUrl = "http://124.93.196.45:10001" + legalExpertise.getImgUrl();
        Picasso.get().load(imgUrl).into(holder.love_imgUrl);

        int id = legalExpertise.getId();
        String name = legalExpertise.getName();
        //为图片添加监听器
        holder.love_imgUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoveTypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                bundle.putString("name",name);
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
        private ImageView love_imgUrl;
        private TextView love_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            love_imgUrl = itemView.findViewById(R.id.love_imgUrl);
            love_name = itemView.findViewById(R.id.love_name);

        }
    }
}
