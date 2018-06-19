package com.iti.jets.lostchildren.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Ahmed Ali on 6/16/2018.
 */

public class FoundAdapater extends RecyclerView.Adapter<FoundAdapater.FoundViewHolder> {
    public static  final String imgeURL = "http://10.0.1.85:8084/LostChildren/found_images/";
    public  static  final  String userImageURL = "http://10.0.1.85:8084/LostChildren/users_images/";
    private LayoutInflater inflater ;
    List<FoundChild> data = Collections.emptyList();
    Context context;

    public FoundAdapater(Context context, List<FoundChild> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public FoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cstom_row,parent,false);
        final FoundAdapater.FoundViewHolder holder = new FoundAdapater.FoundViewHolder(view);
        holder.customItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "TestClick"+String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FoundViewHolder holder, int position) {
        FoundChild current = data.get(position);
        holder.lostName.setText(current.getFirstName()+" "+current.getLastName() );
        if(current.getFromAge().toString() == null || current.getToAge().toString() == null){
            holder.age.setText(" ");

        }
        else {
            holder.age.setText(current.getFromAge().toString() + " To " + current.getToAge().toString());
        }
        if(current.getCurrentLocation().toString() == null){

            holder.city.setText(" ");
        }
        else {
            holder.city.setText(current.getCurrentLocation().toString());
        }
        if(current.getFoundUserId().getLastName().toString() == null){
            holder.reporter.setText(" ");
        }
        else {
            holder.reporter.setText(" " + current.getFoundUserId().getLastName().toString());
        }
        String[] userPaths = current.getImageUrl().toString().split(Pattern.quote("\\"));
        String[] childPaths = current.getFoundUserId().getImageUrl().toString().split(Pattern.quote("\\"));
        Picasso.get().load(imgeURL+userPaths[1]).placeholder(R.drawable.placeholder).into(holder.img);
        Picasso.get().load(userImageURL+childPaths[1]).placeholder(R.drawable.placeholder).into(holder.imgBy);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class  FoundViewHolder extends  RecyclerView.ViewHolder{
        private ImageView img ;
        private ImageView imgBy;
        private TextView lostName;
        private TextView age;
        private TextView city;
        private TextView reportBy;
        private  TextView reporter;
        private LinearLayout customItem;

        public FoundViewHolder(View itemView) {
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
