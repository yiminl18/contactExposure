package edu.uci.ics.model;

import javax.swing.text.Position;
import java.util.*;
public class IndexNode {
    String wifiAP;
    List<Interval> indexNodes = new ArrayList<>();
    boolean dirtyBit = false;

    public boolean isDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit() {
        this.dirtyBit = true;
    }

    public String getWifiAP() {
        return wifiAP;
    }

    public void setWifiAP(String wifiAP) {
        this.wifiAP = wifiAP;
    }

    public List<Interval> getIndexNodes() {
        return indexNodes;
    }

    public void setIndexNodes(List<Interval> indexNodes) {
        this.indexNodes = indexNodes;
    }

    public void addIndexNode(Interval interval){
        this.indexNodes.add(interval);
    }

    public class sortList implements Comparator<Interval> //EDGE is (struct name)
    {
        public int compare(Interval a, Interval b)
        {
            return ((int)a.getStartTimeStamp().getTime() - (int)b.getStartTimeStamp().getTime());
        }
    }

    public void sort(){
        Collections.sort(this.indexNodes, new IndexNode.sortList());
    }

    private Integer BinarySearch(String key, int low, int high){//use binary search to find the closest interval
        if(key.compareTo(indexNodes.get(low).getStartTimeStamp().toString())<0 || key.compareTo(indexNodes.get(high).getStartTimeStamp().toString())>0 || low < high){
            return low;
        }
        int mid = (low+ high) /2;
        if(key.compareTo(indexNodes.get(mid).getStartTimeStamp().toString()) > 0){
            return BinarySearch(key, mid, high);
        }
        else if(key.compareTo(indexNodes.get(mid).getStartTimeStamp().toString()) < 0){
            return BinarySearch(key, low, mid);
        }
        else{
            return mid;
        }
    }

    public Integer findClosestInterval(Interval interval){
        return BinarySearch(interval.getStartTimeStamp().toString(),0,indexNodes.size()-1);
    }
}
