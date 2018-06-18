package com.iti.jets.lostchildren.homeScreen;

import android.app.Fragment;
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
import com.iti.jets.lostchildren.adapter.FoundAdapater;
import com.iti.jets.lostchildren.adapter.MyAdapter;
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.service.LostChildServiceClient;

import java.util.ArrayList;

/**
 * Created by Ahmed Ali on 6/10/2018.
 */

public class FragmentFound extends android.support.v4.app.Fragment implements RetriveFoundChildren, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<FoundChild>foundChildrenList;
    private RecyclerView recyclerView ;
    private FoundAdapater adapter ;
    private LostChildServiceClient service;
    private SwipeRefreshLayout swipeRefresh;
    View v;
    public FragmentFound(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        service =LostChildServiceClient.getInstance();
        service.setFragmentFound(this);
        Log.i("first","callllled");
        service.retriveFounds();
        v = inflater.inflate(R.layout.found_fragment,container,false);
        return  v;
    }


    @Override
    public void updateList(ArrayList<FoundChild> foundChildrenList1, Boolean flag) {
        if(flag == true){

            foundChildrenList = new ArrayList<>();
            foundChildrenList = foundChildrenList1;
            swipeRefresh = v.findViewById(R.id.refreshFoundID);
            swipeRefresh.setOnRefreshListener(this);
            swipeRefresh.setColorSchemeColors(Color.BLUE);
            swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
            recyclerView = v.findViewById(R.id.recyclerViewFoundID);
            adapter = new FoundAdapater(getActivity(),foundChildrenList);
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
        service.setFragmentFound(this);
        service.retriveFounds();
    }
}
