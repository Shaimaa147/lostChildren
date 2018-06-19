package com.iti.jets.lostchildren.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ahmed Ali on 6/19/2018.
 */

public class SearchResult  {
    @SerializedName("searechFoundList")
    @Expose
    private ArrayList<FoundChild> searechFoundList ;
    @SerializedName("searechLostList")
    @Expose
    private ArrayList<FoundChild> searechLostList ;

    public ArrayList<FoundChild> getSearechFoundList() {
        return searechFoundList;
    }

    public ArrayList<FoundChild> getSearechLostList() {
        return searechLostList;
    }

    public void setSearechFoundList(ArrayList<FoundChild> searechFoundList) {

        this.searechFoundList = searechFoundList;
    }

    public void setSearechLostList(ArrayList<FoundChild> searechLostList) {
        this.searechLostList = searechLostList;
    }
}
