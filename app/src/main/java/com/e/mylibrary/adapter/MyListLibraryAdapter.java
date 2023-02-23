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
import com.e.mylibrary.entity.Library;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyListLibraryAdapter extends RecyclerView.Adapter<MyListLibraryAdapter.MyViewHolder> {

    //数据
    private List<Library> pdata = new ArrayList<Library>();
    //上下文
    private Context context;

    public MyListLibraryAdapter(List<Library> pdata, Context context) {
        this.pdata = pdata;
        this.context = context;
    }

    public void loadLibrarys(List<Library> pdata) {
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

        //获取通讯录数据
        Library library = pdata.get(position);
        holder.item_title.setText(library.getName());
        holder.item_address.setText(library.getAddress());
        holder.item_date.setText(library.getBusinessHours());
        if (library.getBusinessState().equals("1")){
            holder.item_state.setText("开馆");
        }else {
            holder.item_state.setText("闭馆");
        }


        String imgUrl = "http://124.93.196.45:10001"+library.getImgUrl();
        Picasso.get().load(imgUrl).into(holder.item_img);

        int library_id = library.getId();

        //为图片添加监听器
        holder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LibraryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",library_id);
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
        private ImageView item_img;
        private TextView item_title;
        private TextView item_address;
        private TextView item_date;
        private TextView item_state;


        public MyViewHolder(View itemView) {
            super(itemView);
            //获取对应列表项
            item_img = itemView.findViewById(R.id.news_img);
            item_title = itemView.findViewById(R.id.txt_title);
            item_address = itemView.findViewById(R.id.txt_author);
            item_date = itemView.findViewById(R.id.txt_createTime);
            item_state = itemView.findViewById(R.id.txt_state);
        }
    }
}
