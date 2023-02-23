package com.e.mylibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.mylibrary.NewsDetailActivity;
import com.e.mylibrary.R;
import com.e.mylibrary.entity.GarbageType;
import com.e.mylibrary.entity.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyListTypesAdapter extends RecyclerView.Adapter<MyListTypesAdapter.MyViewHolder> {

    //数据
    private List<GarbageType> pdata = new ArrayList<GarbageType>();
    //上下文
    private Context context;

    public MyListTypesAdapter(List<GarbageType> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadGarbageTypes(List<GarbageType> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_typelist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取数据
        GarbageType garbageType = pdata.get(position);
        holder.txt_name.setText(garbageType.getName());
        holder.txt_introduce.setText(garbageType.getIntroduce());
        holder.txt_guide.setText(Html.fromHtml(garbageType.getGuide()));



        String imgUrl = "http://124.93.196.45:10001"+garbageType.getImgUrl();
        Picasso.get().load(imgUrl).into(holder.type_img);

       }

    @Override
    public int getItemCount() {
        return pdata.size();
    }

    //定义内部类ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        //定义对应的列表项
        private ImageView type_img;
        private TextView txt_name;
        private TextView txt_introduce;
        private TextView txt_guide;


        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            type_img = itemView.findViewById(R.id.type_img);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_introduce = itemView.findViewById(R.id.txt_introduce);
            txt_guide = itemView.findViewById(R.id.txt_guide);
        }
    }
}
