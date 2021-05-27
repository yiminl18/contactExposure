package edu.uci.ics.model;
import java.util.*;

public class HistoricalData {
    List<rawConnectivityEvents> events = new ArrayList<>();

    public List<rawConnectivityEvents> getEvents() {
        return events;
    }

    public void setEvents(List<rawConnectivityEvents> events) {
        this.events = events;
    }

    public class sortList implements Comparator<rawConnectivityEvents> //EDGE is (struct name)
    {
        public int compare(rawConnectivityEvents a, rawConnectivityEvents b)
        {
            return a.count - b.count;
        }
    }

    public void sort(){
        Collections.sort(this.events, new sortList());
    }

}
