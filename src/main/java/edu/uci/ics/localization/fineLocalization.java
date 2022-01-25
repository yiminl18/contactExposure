package edu.uci.ics.localization;

import edu.uci.ics.metadata.Metadata;
import edu.uci.ics.metadata.SQLGenerator;
import edu.uci.ics.model.*;

import javax.swing.plaf.synth.SynthLookAndFeel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class fineLocalization {

    private long epoch = 5;
    private long staticThreshold = 100;
    private long week = 60*1000*60*24*7;
    private double frequencyThreshold = 0.1;
    private long eps = 5;//compute similarity of two devices, the threshold to define together events
    HistoricalData HD = new HistoricalData();

    class Neighbor{
        String mac;
        String ap;
        List<String> overlappingRooms;
        int count;

        public void setCount() {
            this.count = overlappingRooms.size();
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getAp() {
            return ap;
        }

        public void setAp(String ap) {
            this.ap = ap;
        }

        public List<String> getOverlappingRooms() {
            return overlappingRooms;
        }

        public void setOverlappingRooms(List<String> overlappingRooms) {
            this.overlappingRooms = overlappingRooms;
        }
    }

    public class sortList implements Comparator<Neighbor> //EDGE is (struct name)
    {
        public int compare(Neighbor a, Neighbor b)
        {
            if(!a.mac.equals(b.mac)) {
                return a.mac.compareTo(b.mac);
            }else{
                return (a.count - b.count);
            }
        }
    }

    public void sortNeighbor(List<Neighbor> neighbors){
        Collections.sort(neighbors, new fineLocalization.sortList());
    }

    public Interval fineLocalization(Interval interval, Metadata metadata){
        Interval newInterval = interval;
        List<Neighbor> neighbors = findNeighbors(interval, metadata);
        neighbors = readNeighborData(neighbors, interval);
        //neighbors = filterNeighbors(neighbors, interval, metadata);

        List<String> candidateIntervals = new ArrayList<>();
        for(int i=0;i<neighbors.size();i++){
            candidateIntervals.add(neighbors.get(i).getMac());
        }
        newInterval.setNeighbors(candidateIntervals);
        //System.out.println("hi: " + interval.getStartTimeStamp() + " " + interval.getEndTimeStamp() + " " + interval.getRegionLabel() + " " + interval.getDeviceID());
        List<String> candidateRooms = metadata.getAPCoverage(interval.getRegionLabel());
        double value = -1.0;
        int roomIndex = 0;
        double sum = 0.0;
        for(int i=0;i<candidateRooms.size();i++){
            double prob = deviceAffinity(interval, neighbors, candidateRooms.get(i), metadata);
            sum += prob;
            if(prob > value){
                value = prob;
                roomIndex = i;
            }
        }
        value /= sum;
        newInterval.setRoomLabel(candidateRooms.get(roomIndex));
        newInterval.setRoomConfidence(value);
        //System.out.println(interval.getStartTimeStamp() + " " + interval.getEndTimeStamp() + " " + " " + interval.getRegionLabel() + " " + newInterval.getRoomLabel());
        return newInterval;
    }



    public List<Neighbor> findNeighbors(Interval interval, Metadata metadata){//contain target itself
        List<Neighbor> neighbors = new ArrayList<>();
        SQLGenerator sqlGenerator = new SQLGenerator();

        Connect connectServer = new Connect("server","mysql");
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        try {
            Statement stmtServer = serverConnection.createStatement();
            String ST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(interval.getStartTimeStamp());
            String ET = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(interval.getEndTimeStamp());
            rs = stmtServer.executeQuery(sqlGenerator.findNeighbors(ST, ET));
            while (rs.next()) {
                Neighbor neighbor = new Neighbor();
                neighbor.setMac(rs.getString(1));
                if(neighbor.getMac().equalsIgnoreCase(interval.getDeviceID())){
                    neighbor.setAp(interval.getRegionLabel());
                }
                else{
                    neighbor.setAp(rs.getString(2));
                }
                neighbors.add(neighbor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();
        List<String> candidateRooms = metadata.getAPCoverage(interval.getRegionLabel());

        for(int i=0;i<neighbors.size();i++){//filter out those with no common candidate rooms
            neighbors.get(i).setOverlappingRooms(findCommonRooms(candidateRooms, metadata.getAPCoverage(neighbors.get(i).ap)));
            neighbors.get(i).setCount();
            if(neighbors.get(i).getOverlappingRooms().size() == 0){
                neighbors.get(i).setMac("null");
            }
        }
        return neighbors;
    }

    public List<String> findCommonRooms(List<String> a, List<String> b){
        List<String> c = new ArrayList<>();
        for(int i=0;i<a.size();i++){
            if(b.indexOf(a.get(i))!=-1){//found
                c.add(a.get(i));
            }
        }
        return c;
    }

    public List<Neighbor> readNeighborData(List<Neighbor> neighbors, Interval interval){
        SQLGenerator sqlGenerator = new SQLGenerator();
        Timestamp ST = new Timestamp(interval.getStartTimeStamp().getTime() - 2*week);
        Timestamp ET = interval.getEndTimeStamp();
        String STStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ST);
        String ETStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ET);

        Connect connectServer = new Connect("server","mysql");
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        List<Neighbor> newNeighbors = new ArrayList<>();

        try {
            Statement stmtServer = serverConnection.createStatement();
            for(int i=0;i<neighbors.size();i++){
                if(neighbors.get(i).mac.equals("null")) continue;
                rawConnectivityEvents events = new rawConnectivityEvents();
                rs = stmtServer.executeQuery(sqlGenerator.readRawConnectivityData(STStr,ETStr,neighbors.get(i).mac));
                int count=0;
                events.initEvents();
                int staticCount = 0, hour = 0;
                boolean isStaticDevice = false;
                while (rs.next()) {
                    rawConnectivityEvent event = new rawConnectivityEvent();
                    event.setTimeStamp(Timestamp.valueOf(rs.getString(1)));
                    event.setAP(rs.getString(2));
                    events.getEvents().add(event);
                    count++;
                    //filter out static devices
                    hour = event.getTimeStamp().toLocalDateTime().getHour();
                    if(hour>=2 && hour <=6){
                        staticCount ++;
                    }
                    if(staticCount >=  staticThreshold){
                        isStaticDevice = true;
                        break;
                    }
                }
                if(!isStaticDevice){
                    if(count>=frequencyThreshold){//filter out infrequent devices
                        events.setCount(count);
                        events.setUserID(neighbors.get(i).mac);
                        HD.getEvents().add(events);
                        HD.addNeighbor(neighbors.get(i).mac);
                        newNeighbors.add(neighbors.get(i));
                    }
                }
            }
            HD.sort();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();
        return newNeighbors;
    }

    public List<Neighbor> filterNeighbors(List<Neighbor> neighbors, Interval interval, Metadata metadata){//filter out infrequent users
        int totalCount = 0;
        List<String> removedNeighbor = new ArrayList<>();
        for(int i=0;i<HD.getEvents().size();i++){
            totalCount += HD.getEvents().get(i).getCount();
        }

        int count=0;
        for(int i=0;i<HD.getEvents().size();i++){
            String neighborName = HD.getEvents().get(i).getUserID();
            if(neighborName.equalsIgnoreCase(interval.getDeviceID())) {
                continue;//should include target
            }
            count = HD.getEvents().get(i).getCount();
            if((count*1.0)/(totalCount*1.0)<frequencyThreshold ){//infrequent User
                removedNeighbor.add(neighborName);
            }
        }
        List<Integer> removedIndex = new ArrayList<>();
        for(int i=0;i<neighbors.size();i++){
            if(removedNeighbor.indexOf(neighbors.get(i).mac)!=-1 || neighbors.get(i).mac.equals("null")){//found this device to be remove
                removedIndex.add(i);
            }
        }
        //clean neighbors
        for(int i=removedIndex.size()-1;i>=0;i--){
            int index = removedIndex.get(i);
            neighbors.remove(index);
        }

        //clean duplicated devices
        removedIndex.clear();
        sortNeighbor(neighbors);
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).mac.equalsIgnoreCase(interval.getDeviceID())){//target device
                if(i>=1 && neighbors.get(i-1).mac.equalsIgnoreCase(interval.getDeviceID())){
                    removedIndex.add(i);
                }
            }
            else if(i>=1 && !neighbors.get(i).mac.equalsIgnoreCase(interval.getDeviceID())){//for neighbors
                if(neighbors.get(i).mac.equalsIgnoreCase(neighbors.get(i-1).mac)){
                    if(neighbors.get(i).overlappingRooms.size() > neighbors.get(i-1).overlappingRooms.size()){
                        removedIndex.add(i-1);
                    }
                    else{
                        removedIndex.add(i);
                    }
                }
            }
        }
        //update to neighbors
        for(int i=removedIndex.size()-1;i>=0;i--){
            int index = removedIndex.get(i);
            neighbors.remove(index);
        }

        /*for(int i=0;i<neighbors.size();i++){
            System.out.println("hello: " + neighbors.get(i).mac + " " + neighbors.get(i).ap + " " + neighbors.get(i).overlappingRooms.size());
        }*/
        return neighbors;
    }

    public Affinity roomAffinity(String mac, String wifiAP, Metadata metadata){
        Affinity affinity = new Affinity();
        List<String> candidateRooms = metadata.getAPCoverage(wifiAP);
        affinity.setCandidateRooms(candidateRooms);
        String office = metadata.getIdentity(mac).getOffice();
        List<Double> roomAffinity = new ArrayList<>();
        double sum = 0.0;
        for(int i=0;i<candidateRooms.size();i++){
            if(candidateRooms.get(i).equals(office)){
                roomAffinity.add(20.0);
                sum += 20.0;
            }
            else if(metadata.getRoom(candidateRooms.get(i)).getRoom().equals("null")){//this room is not in metadata
                roomAffinity.add(0.0);
            }
            else{
                roomAffinity.add(10.0 - metadata.getRoom(candidateRooms.get(i)).getImportance());
                sum += (10.0 -metadata.getRoom(candidateRooms.get(i)).getImportance());
            }
        }
        if(sum == 0.0){
            affinity.setRoomAffinity(roomAffinity);
            return  affinity;
        }
        for(int i=0;i<roomAffinity.size();i++){
            roomAffinity.set(i,roomAffinity.get(i)/sum);
        }
        affinity.setRoomAffinity(roomAffinity);
        return affinity;
    }

    public Double alphaD(int target, int neighbor){//target, neighbor is the index  of HD, ensure lena>lenb
        int pa=0, pb=0;
        double similarity = 0;
        int count = 0;
        long dif;
        int lena= HD.getEvents().get(target).getCount();
        int lenb = HD.getEvents().get(neighbor).getCount();
        if(lena <= lenb){//swap target and neighbor
            int temp = neighbor;
            neighbor = target;
            target = temp;

            int tempLen = lena;
            lena = lenb;
            lenb = tempLen;
        }
        Timestamp targetTime, neighborTime;
        String targetAP, neighborAP;
        while(pa<lena){
            if(pb>=lenb){
                break;
            }
            targetTime = HD.getEvents().get(target).getEvents().get(pa).getTimeStamp();
            neighborTime = HD.getEvents().get(neighbor).getEvents().get(pb).getTimeStamp();

            targetAP = HD.getEvents().get(target).getEvents().get(pa).getAP();
            neighborAP = HD.getEvents().get(neighbor).getEvents().get(pb).getAP();

            dif = (targetTime.getTime()-neighborTime.getTime())/60/1000;//a-b
            if(Math.abs(dif)<=eps){//if time difference between events from two devices are less than eps
                if(targetAP.equals(neighborAP)){//they are in same region
                    //and they are together: connect to same wifi ap, count them
                    count++;
                    pb++;
                }
                pa++;
            }else if(dif>eps){
                pb++;
                if(pb>=lenb){
                    break;
                }
                targetTime = HD.getEvents().get(target).getEvents().get(pa).getTimeStamp();
                neighborTime = HD.getEvents().get(neighbor).getEvents().get(pb).getTimeStamp();
                while((neighborTime.getTime()-targetTime.getTime())/60/1000>eps){
                    pb++;
                    if(pb>=lenb){
                        break;
                    }
                }
            }else {
                targetTime = HD.getEvents().get(target).getEvents().get(pa).getTimeStamp();
                neighborTime = HD.getEvents().get(neighbor).getEvents().get(pb).getTimeStamp();
                dif = (targetTime.getTime()-neighborTime.getTime())/60/1000;
                if(dif<-eps){
                    pa++;
                }
            }
        }
        similarity = Double.valueOf(count)/Math.min(lena, lenb);

        return similarity;
    }

    public Double alphaOverlapRoom(int targetIndex, int neighborIndex, List<Neighbor> neighbors, String room, Metadata metadata){//return two values: (P for target and neighbor)
        Affinity targetA = new Affinity();
        Affinity neighborA = new Affinity();
        targetA = roomAffinity(neighbors.get(targetIndex).mac,neighbors.get(targetIndex).ap, metadata);
        neighborA = roomAffinity(neighbors.get(neighborIndex).mac,neighbors.get(neighborIndex).ap, metadata);
        //check if room is the overlapping room of target and neighbor
        int neighborRoom = neighborA.getCandidateRooms().indexOf(room);
        if(neighborRoom==-1){
            return -1.0;
        }
        Double RisTarget = 0.0, RisNeighbor = 0.0;
        for(int i=0;i<neighbors.get(neighborIndex).overlappingRooms.size();i++){
            int indexTarget = targetA.getCandidateRooms().indexOf(neighbors.get(neighborIndex).overlappingRooms.get(i));
            RisTarget += targetA.getRoomAffinity().get(indexTarget);
            int indexNeighbor = neighborA.getCandidateRooms().indexOf(neighbors.get(neighborIndex).overlappingRooms.get(i));
            RisNeighbor += neighborA.getRoomAffinity().get(indexNeighbor);
        }
        Double target2Room = targetA.getRoomAffinity().get(targetA.getCandidateRooms().indexOf(room));
        Double neighbor2Room = neighborA.getRoomAffinity().get(neighborA.getCandidateRooms().indexOf(room));
        Double value = (target2Room/RisTarget)*(neighbor2Room/RisNeighbor)/neighbor2Room;
        return value;
    }


    public Double deviceAffinity(Interval interval, List<Neighbor> neighbors, String room, Metadata metadata){//given a room, return an affinity based on all neighbors
        int target = -1;
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).mac.equalsIgnoreCase(interval.getDeviceID())){
                target = i;
            }
        }
        Affinity targetA = roomAffinity(interval.getDeviceID(), interval.getRegionLabel(), metadata);

        double numerator = 1.0, denominator = 0.0;
        for(int i=0;i<neighbors.size();i++){
            if(i == target) continue;
            if(!HD.findNeighbor(neighbors.get(i).mac)){
                continue;
            }
            double alpha = alphaD(target,i);
            double conditionP = alphaOverlapRoom(target,i,neighbors,room, metadata);
            if(conditionP == -1.0 || alpha == 0.0) continue; //no common room
            numerator *= (alpha*conditionP);
        }
        double target2RoomAffinity = targetA.getRoomAffinity().get(targetA.getCandidateRooms().indexOf(room));
        if(numerator == 1.0){//no qualified neighbors
            return target2RoomAffinity;
        }
        double newNumerator = numerator*target2RoomAffinity;
        denominator = numerator*target2RoomAffinity + (1-numerator)*(1-target2RoomAffinity);
        //System.out.println(newNumerator + " " + denominator);

        return newNumerator / denominator;
    }

}
