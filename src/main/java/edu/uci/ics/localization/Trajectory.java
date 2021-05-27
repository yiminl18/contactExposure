package edu.uci.ics.localization;

import edu.uci.ics.contact.index;
import edu.uci.ics.contact.visualization;
import edu.uci.ics.metadata.Metadata;
import edu.uci.ics.metadata.SQLGenerator;
import edu.uci.ics.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Map;

/*
    This class generates trajectories only coarse granularity, on for only frequent devices.
    The input is presence table.
 */
public class Trajectory {
    private static long gapT = 10;
    private static Metadata metadata = new Metadata();
    private static final String startDate = "2018-01-08 00:00:00";
    private static final String endDate = "2018-02-10 00:00:00";

    static List<String> frequentUsers = new ArrayList<>();

    public static void loadMetadata(){
        metadata.loadIdentityMetadata();
        metadata.loadAPCoverage();
        metadata.loadSpaceMetadata();
    }

    public static Presence getTrajectory(Query query){
        NeighborSet candidateIntervals = new NeighborSet();
        candidateIntervals.setNeighborSet(candidateIntervals.readNeighborSet());
        Presence presence = RawToPresence(query, readRawEvents(query));
        User user = new User();
        user.setUserID(query.getUserID());
        user.setVisitedAps(presence.getVisitedAPs());
        //System.out.println("Report Query: " + query.getUserID() + " From: " + query.getStartTime() + " To: " + query.getEndTime());

        for(int i=0;i<presence.getPresence().size();i++){
            coarseLocalization CL = new coarseLocalization();
            //fineLocalization FL = new fineLocalization();

            String label = CL.coarseLocalization(presence.getPresence().get(i),user);
            presence.getPresence().get(i).setRegionLabel(label);
            /*if(label.equals("out")){
                presence.getPresence().get(i).setRoomLabel("out");
            }
            else{
                presence.getPresence().set(i, FL.fineLocalization(presence.getPresence().get(i),metadata));
                //update candidate intervals
                UpdateCandidateIntervals(query, presence.getPresence().get(i).getNeighbors(),candidateIntervals);
            }*/
            //System.out.println(presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getRegionLabel());
        }
        return presence;
    }

    public static void UpdateCandidateIntervals(Query query, List<String> neighbors, NeighborSet neighborSet){
        List<Integer> indents = neighborSet.findTimeInterval(Timestamp.valueOf(query.getStartTime()), Timestamp.valueOf(query.getEndTime()));
        for(int i=0;i<indents.size();i++){
            for(int j=0;j<neighbors.size();j++){
                neighborSet.getNeighborSet().get(indents.get(i)).addNeighbor(neighbors.get(j));
            }
        }
    }

    public static long timeOverlap(Interval a, Interval b){//return time overlap of two intervals
        long STa = a.getStartTimeStamp().getTime();
        long ETa = a.getEndTimeStamp().getTime();
        long STb = b.getStartTimeStamp().getTime();
        long ETb = b.getEndTimeStamp().getTime();
        if(STa <= STb && ETa >= STb && ETa <= ETb) {
            return (ETa - STb)/60/1000;
        }
        else if(STa <= STb && ETa >= STb && ETa >= ETb){
            return (ETb - STb)/60/1000;
        }
        else if(STb <= STa && ETb >= STa && ETb <= ETa){
            return (ETb - STa)/60/1000;
        }
        else if(STb <= STa && ETb >= STa && ETb >= ETa){
            return (ETa - STa)/60/1000;
        }
        else{
            return 0;
        }
    }

    public static rawConnectivityEvents readRawEvents(Query query){
        SQLGenerator sqlGenerator = new SQLGenerator();
        rawConnectivityEvents events = new rawConnectivityEvents();

        Connect connectServer = new Connect("server");
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        //read raw connectivity data
        try {
            Statement stmtServer = serverConnection.createStatement();
            rs = stmtServer.executeQuery(sqlGenerator.readRawConnectivityData(query.getStartTime(), query.getEndTime(), query.getUserID()));
            events.initEvents();
            while (rs.next()) {
                rawConnectivityEvent event = new rawConnectivityEvent();
                event.setTimeStamp(Timestamp.valueOf(rs.getString(1)));
                event.setAP(rs.getString(2));
                events.getEvents().add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();
        return events;
    }


    public static Presence RawToPresence(Query query, rawConnectivityEvents events){
        Presence presence = new Presence();
        presence.setUserID(query.getUserID());
        String targetDevice = query.getUserID();

        //transform to presence table
        if(events.getEvents().size() == 0){
            presence.setCount(0);
            return presence;
        }

        HashMap<String, Integer> visitedAPs = new HashMap<String, Integer>();
        Timestamp preTime = events.getEvents().get(0).getTimeStamp();
        String preAP = events.getEvents().get(0).getAP();
        visitedAPs.put(preAP,1);
        presence.initPresence();

        for(int i=1;i<events.getEvents().size();i++){
            Timestamp nowTime = events.getEvents().get(i).getTimeStamp();
            String nowAP = events.getEvents().get(i).getAP();

            if(visitedAPs.containsKey(nowAP)){
                visitedAPs.put(nowAP,visitedAPs.get(nowAP)+1);
            }else{
                visitedAPs.put(nowAP,1);
            }

            long difference  = (nowTime.getTime() - preTime.getTime())/(60*1000);
            if(difference <= gapT){
                Interval interval = new Interval();
                interval.setDeviceID(targetDevice);
                interval.setStartTimeStamp(preTime);
                interval.setEndTimeStamp(nowTime);
                interval.setStartAP(preAP);
                interval.setEndAP(nowAP);
                presence.getPresence().add(interval);
            }
            else{
                //add interval
                Timestamp gapTime = new Timestamp(preTime.getTime() + gapT*60*1000);
                Interval interval = new Interval();
                interval.setDeviceID(targetDevice);
                interval.setStartTimeStamp(preTime);
                interval.setEndTimeStamp(gapTime);
                interval.setStartAP(preAP);
                interval.setEndAP(preAP);
                presence.getPresence().add(interval);
                //add gap
                Interval gap = new Interval();
                gap.setDeviceID(targetDevice);
                gap.setStartTimeStamp(gapTime);
                gap.setEndTimeStamp(nowTime);
                gap.setStartAP(preAP);
                gap.setEndAP(nowAP);
                presence.getPresence().add(gap);
            }
            preTime = nowTime;
            preAP = nowAP;
        }
        presence.setVisitedAps(visitedAPs);
        presence.setCount(events.getEvents().size());

        //debug
        /*System.out.println(events.getEvents().size());
        for(int i=0;i<events.getEvents().size();i++){
            System.out.println(events.getEvents().get(i).getTimeStamp() + " " + events.getEvents().get(i).getAP());
        }
        System.out.println(presence.getPresence().size());
        for(int i=0;i<presence.getPresence().size();i++){
            System.out.println(presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getStartAP() + " " + presence.getPresence().get(i).getEndAP());
        }*/
        return presence;
    }

    public static void printTrajectories(Presence presence){
        //System.out.println("USER: "+ presence.getUserID());
        for(int i=0;i<presence.getPresence().size();i++){
            System.out.println(presence.getPresence().get(i).getDeviceID() + " " +  presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getRegionLabel());
        }
    }

    public static void readFrequentUsers(){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("frequentUsers.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                frequentUsers.add(row);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToDisk(Presence presence){
        //write trajectory to disk
        try {
            FileWriter filePath = new FileWriter("trajectories1.txt",true);
            /*Formatter formatter = new Formatter(filePath);
            for(int i=0;i<presence.getPresence().size();i++){
                formatter.format("%s %s %s %s\n", presence.getPresence().get(i).getDeviceID(),presence.getPresence().get(i).getStartTimeStamp().toString(),presence.getPresence().get(i).getEndTimeStamp().toString(),presence.getPresence().get(i).getRegionLabel());
            }*/
            BufferedWriter bw = new BufferedWriter(filePath);
            for(int i=0;i<presence.getPresence().size();i++){
                String row = presence.getPresence().get(i).getDeviceID() + " " + presence.getPresence().get(i).getStartTimeStamp().toString() + " " + presence.getPresence().get(i).getEndTimeStamp().toString() + " " + presence.getPresence().get(i).getRegionLabel();
                bw.write(row);
                bw.newLine();
                //System.out.println(row);
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        readFrequentUsers();
        loadMetadata();
        for(int i=0;i<frequentUsers.size();i++){
            Query query = new Query();
            query.setUserID(frequentUsers.get(i));
            query.setStartTime(startDate);
            query.setEndTime(endDate);
            //System.out.println(i + " " + frequentUsers.get(i));
            /*if(frequentUsers.get(i).equals("ff6a0f3ad57af448efcc3fc79aedf5a9e78ab145")){
                writeToDisk(getTrajectory(query));
            }*/
            writeToDisk(getTrajectory(query));
        }
    }
}
