package com.iti.jets.lostchildren;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.List;

/**
 * Created by Ahmed Ali on 6/11/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LayoutInflater inflater ;
    Context context;
    List<Information> data = Collections.emptyList();
    public MyAdapter(Context context, List<Information> data){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cstom_row,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.customItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "TestClick"+String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.lostName.setText(current.getName());
        holder.img.setImageResource(current.getPhoto());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public  class  MyViewHolder extends  RecyclerView.ViewHolder{
        private ImageView img ;
        private TextView lostName;
        private TextView phoneNumber;
        private LinearLayout customItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgID);
            lostName = itemView.findViewById(R.id.textOneID);
            phoneNumber = itemView.findViewById(R.id.textTwoID);
            customItem = itemView.findViewById(R.id.itemID);
        }
    }
}

