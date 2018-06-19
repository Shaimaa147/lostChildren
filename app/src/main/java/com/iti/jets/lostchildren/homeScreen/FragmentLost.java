package com.iti.jets.lostchildren.homeScreen;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.adapter.MyAdapter;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.service.LostChildServiceClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Ali on 6/10/2018.
 */

public class FragmentLost extends android.support.v4.app.Fragment implements RetriveLostChildren, SwipeRefreshLayout.OnRefreshListener {
    ArrayList<LostChild>  missingChildrenList;
    private RecyclerView recyclerView ;
    private MyAdapter adapter ;
    private LostChildServiceClient service;
    private SwipeRefreshLayout swipeRefresh;
    View v ;

   public FragmentLost(){

   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        service =LostChildServiceClient.getInstance();
        service.setLostFragment(this);
        service.retriveLosts();
       v = inflater.inflate(R.layout.lost_fragment,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void updateList(ArrayList<LostChild> lostChildArrayList, boolean flage) {
        if(flage == true){

            missingChildrenList = new ArrayList<>();
            missingChildrenList = lostChildArrayList;
            swipeRefresh = v.findViewById(R.id.refreshID);
            swipeRefresh.setOnRefreshListener(this);
            swipeRefresh.setColorSchemeColors(Color.BLUE);
            swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
            recyclerView = v.findViewById(R.id.recyclerViewID);
            adapter = new MyAdapter(getActivity(),missingChildrenList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
           adapter.notifyDataSetChanged();
           if(swipeRefresh.isRefreshing()){
               swipeRefresh.setRefreshing(false);
           }
        }
        else{

            Toast.makeText(getContext(),"FaliureCase", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        service =LostChildServiceClient.getInstance();
        service.setLostFragment(this);
        service.retriveLosts();


    }
}
