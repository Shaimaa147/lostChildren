package com.iti.jets.lostchildren.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iti.jets.lostchildren.R;

/**
 * Created by Ahmed Ali on 6/19/2018.
 */

public class SearchHolder extends   RecyclerView.Adapter<SearchHolder.SearchViewHolder> {


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public  class  SearchViewHolder extends  RecyclerView.ViewHolder{
        private ImageView img ;
        private ImageView imgBy;
        private TextView lostName;
        private TextView age;
        private TextView city;
        private TextView reportBy;
        private  TextView reporter;
        private LinearLayout customItem;

        public SearchViewHolder(View itemView) {
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

