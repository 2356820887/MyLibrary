package com.e.mylibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.mylibrary.LoveDetailActivity;
import com.e.mylibrary.LoveTypeActivity;
import com.e.mylibrary.R;
import com.e.mylibrary.entity.Love;

import java.util.ArrayList;
import java.util.List;


public class LoveTypeAdapter extends RecyclerView.Adapter<LoveTypeAdapter.MyViewHolder> {

    //数据
    private List<Love> pdata = new ArrayList<Love>();
    //上下文
    private Context context;

    public LoveTypeAdapter(List<Love> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadloveList(List<Love> pdata) {
        this.pdata = pdata;
    }

    @NonNull
    @Override
    //返回一个自定义的ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //填充布局,获取列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_typelove,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //获取数据
        Love love = pdata.get(position);
        System.out.println(love);
        holder.loveName2.setText(love.getName());
        holder.author2.setText(love.getAuthor());
        holder.activityAt2.setText("活动时间:"+love.getActivityAt());
        holder.moneyNow2.setText("已筹款数额:"+love.getMoneyNow()+"");
        //holder.description2.setText(love.getDescription());
        holder.donateCount2.setText("捐款次数:"+love.getDonateCount()+"");

        int id = love.getId();
        //为按钮添加监听器
        holder.btnAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoveDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                intent.putExtras(bundle);
                //context.startActivity(intent);
                Toast.makeText(context, "捐款成功", Toast.LENGTH_SHORT).show();
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
        private ImageView love_imgUrl2;
        private TextView loveName2;
        private TextView author2;
        private TextView activityAt2;
        private TextView moneyNow2;
        private TextView description2;
        private TextView donateCount2;
        private Button btnAdvice;

        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            loveName2 = itemView.findViewById(R.id.loveName2);
            author2 = itemView.findViewById(R.id.author2);
            activityAt2 = itemView.findViewById(R.id.activityAt2);
            moneyNow2 = itemView.findViewById(R.id.moneyNow2);
            donateCount2 = itemView.findViewById(R.id.donateCount2);
            btnAdvice = itemView.findViewById(R.id.btnAdvice2);

        }
    }
}
