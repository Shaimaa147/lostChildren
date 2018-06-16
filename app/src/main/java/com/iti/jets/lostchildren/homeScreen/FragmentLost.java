package com.iti.jets.lostchildren;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Ali on 6/10/2018.
 */

public class FragmentLost extends android.support.v4.app.Fragment{
    private RecyclerView recyclerView ;
    private MyAdapter adapter ;
    View v ;

   public FragmentLost(){

   }
    public  static List<Information> getData(){
        List<Information> data = new ArrayList<>();
        int [] icons = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four};
        String [] lostNames= {"First Name","Secound Name","Third Name","Fourth Name"};
        String [] phones= {"01111","02222","03333","04444"};
        for (int i = 0 ; i<icons.length && i<lostNames.length;i++) {
            Information current = new Information();
            current.setPhoto(icons[i]) ;
            current.setName(lostNames[i]);
            current.setPhone(phones[i]);
            data.add(current);
        }
        return  data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.lost_fragment,container,false);
        recyclerView = v.findViewById(R.id.recyclerViewID);
        adapter = new MyAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }
}
