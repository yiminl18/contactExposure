package edu.uci.ics.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class NeighborUnit {
    Timestamp startTime;//each time interval is one month for now
    List<String> neighbors = new ArrayList<>();

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(String neighbor){
        if(neighbors.indexOf(neighbor)==-1){
            neighbors.add(neighbor);
        }
    }
}
