package com.iti.jets.lostchildren.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.iti.jets.lostchildren.homeScreen.Informations;
import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ahmed Ali on 6/11/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public static  final String imgeURL = "http://192.168.1.3:8084/LostChildren/lost_images/";
    public  static  final  String userImageURL = "http://192.168.1.3:8084/LostChildren/users_images/";
    private LayoutInflater inflater ;
    Context context;
    List<LostChild> data = Collections.emptyList();
    public MyAdapter(Context context, ArrayList<LostChild> data ){
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
        LostChild current = data.get(position);
        holder.lostName.setText(current.getFirstName()+" "+current.getLastName() );
      holder.age.setText(current.getAge().toString());
     holder.city.setText(current.getOrginalAddress().toString());
     holder.reporter.setText(" "+current.getLostUserId().getLastName().toString());
     //Log.i("name","http://localhost:8084/LostChildren/lost_images/"+current.getImageUrl());
        //Picasso.get().load("http://localhost:8084/LostChildren/found_images/"+current.getImageUrl()).placeholder().into(holder.img);
        Picasso.get().load(imgeURL+current.getImageUrl()).placeholder(R.drawable.one).into(holder.img);
        Picasso.get().load(userImageURL+current.getLostUserId().getImageUrl()).placeholder(R.drawable.two).into(holder.imgBy);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public  class  MyViewHolder extends  RecyclerView.ViewHolder{
        private ImageView img ;
        private ImageView imgBy;
        private TextView lostName;
        private TextView age;
        private TextView city;
        private TextView reportBy;
        private  TextView reporter;
        private LinearLayout customItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgID);
            imgBy = itemView.findViewById(R.id.reportByImgID);
            lostName = itemView.findViewById(R.id.textOneID);
            age = itemView.findViewById(R.id.textTwoID);
            city = itemView.findViewById(R.id.textThreeID);
            reportBy = itemView.findViewById(R.id.reportByID);
            reporter = itemView.findViewById(R.id.reporterID);
            customItem = itemView.findViewById(R.id.itemID);

        }
    }
}

