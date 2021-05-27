package edu.uci.ics.model;

import java.util.*;
public class Index {
    List<IndexNode> index;
    List<String> aps = new ArrayList<>();

    public Integer findAP(String ap){
        return aps.indexOf(ap);
    }

    public void initializeAPs(){
        for(int i=0;i<index.size();i++){
            aps.add(index.get(i).getWifiAP());
        }
    }

    public void insertAP(String ap){
        if(findAP(ap)==-1) aps.add(ap);
    }

    public List<IndexNode> getIndex() {
        return index;
    }

    public void setIndex(List<IndexNode> index) {
        this.index = index;
    }
}
