package edu.uci.ics.contact;

import edu.uci.ics.localization.*;
import edu.uci.ics.metadata.Metadata;
import edu.uci.ics.model.*;

import java.util.ArrayList;
import java.util.*;

import java.sql.Timestamp;

/*
1. all macs are lowercase, check db first
 */
public class main {

    public static void main(String[] args) {
        //DataSet: Real UCI WiFi Connectivity Data in DBH from 2020.01.01 to 2020.02.01
        /*
            3 Query modes:
            1. reportQuery: an affected person reports his trajectories in given time range
                input: (mac of affected person's device, startTime, endTime)
                output: (mac, timeStamp, region, room)
            2. checkQuery: a user wants to check if she has contacted with any affected person in given time range
                input: (mac of user's device, startTime, endTime)
                output: boolean (yes/no), if yes, contacted with who (privacy*), when and where
            3. contactQuery: organization (campus) wants to know all the people who have contacted with any affected person in given time range
                input: (startTime, endTime)
                output: macs of contacted people's devices (when and where are also available)
         */
        String queryMode = "contactQuery";//reportQuery,checkQuery,contactQuery
        switch(queryMode){
            case "reportQuery":
                String startTimeStamp = "2020-01-01 00:00:00";
                String endTimeStamp = "2020-02-01 00:00:00";
                String macOfAffectedPerson = "c3aad8cf6280621da86c10880d8c694114c329d3";
                testReportQueryDB(startTimeStamp, endTimeStamp, macOfAffectedPerson);
                break;
            case "checkQuery":
                startTimeStamp = "2020-01-17 00:00:00";
                endTimeStamp = "2020-01-18 00:00:00";//2020-01-19 00:00:00
                String macOfUser = "de3cef605db4a8732036b0ed174f10f05d51402b";
                int semanticMode = 1;//1:summation, 2: continuous
                int contactMode = 1;//1:region, 2: room
                long contactTimeThreshold = 10;//minutes
                testCheckQuery(startTimeStamp,endTimeStamp,macOfUser,semanticMode,contactMode,contactTimeThreshold);
                break;
            case "contactQuery":
                startTimeStamp = "2020-01-18 12:00:00";
                endTimeStamp = "2020-01-18 14:00:00";
                semanticMode = 1;//1:summation, 2: continuous
                contactMode = 1;//1:region, 2: room
                contactTimeThreshold = 15;//minutes
                testContactQuery(startTimeStamp,endTimeStamp,semanticMode,contactMode,contactTimeThreshold);
                break;
        }
        /*
            semanticMode: 1. summation, 2. continuous
            1. Two users are said to be contacted with each other for total X minutes in same location
            2. Two users are said to be contacted with each other for continuous X minutes in same location
         */
        /*
            Pre-run reportQuery for the following 5 Affected People:
            //String mac = "099e54fa8f092119a3c4913970b0e007876f790e";//peeyush
            //String mac = "11D58FD604E31332D0E061F9E445058AFB453291";//dhrub
            //String mac = "1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc";//Sameera
            //String mac = "7ca5b3ecc4c6618b1106b4beadf355ae7d4904fc";//guoxi
            //String mac = "c3aad8cf6280621da86c10880d8c694114c329d3";//primal
         */
    }

    public static void testReportQuery(String startTime, String endTime, String mac){
        Query query = new Query();
        query.setStartTime(adjustTime(startTime, "-"));
        query.setEndTime(adjustTime(endTime, "-"));
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first
        contactTracing contact = new contactTracing();
        contact.printTrajectories(contact.ReportQuery(query));
    }

    public static void testReportQueryDB(String startTime, String endTime, String mac){
        Query query = new Query();
        query.setStartTime(adjustTime(startTime, "-"));
        query.setEndTime(adjustTime(endTime,"-"));
        query.setUserID(mac.toLowerCase());
        contactTracing contact = new contactTracing();
        contact.ReportQueryDB(query);
    }

    public static void testCheckQuery(String startTime, String endTime, String mac, int semanticMode, int contactMode, long timeThreshold){
        Query query = new Query();
        query.setStartTime(adjustTime(startTime, "-"));
        query.setEndTime(adjustTime(endTime, "-"));
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first
        query.setSemanticMode(semanticMode);
        query.setContactMode(contactMode);
        query.setTimeThreshold(timeThreshold);
        contactTracing contact = new contactTracing();
        contact.CheckQuery(query);
    }

    public static void testContactQuery(String startTime, String endTime, int semanticMode, int contactMode, long timeThreshold){
        Query query = new Query();
        query.setStartTime(adjustTime(startTime, "-"));
        query.setEndTime(adjustTime(endTime, "-"));
        query.setSemanticMode(semanticMode);
        query.setContactMode(contactMode);
        query.setTimeThreshold(timeThreshold);
        contactTracing contact = new contactTracing();
        contact.ContactQuery(query);
    }

    public static void testIndex(){
        Index indexList = new Index();
        index ID = new index();
        indexList = ID.readIndex();
        ID.printIndex(indexList);
    }


    public static void testFine(){
        fineLocalization FL = new fineLocalization();
        Interval interval = new Interval();
        interval.setRegionLabel("3142-clwa-2099");
        interval.setStartTimeStamp(Timestamp.valueOf("2018-01-17 13:48:24"));
        interval.setEndTimeStamp(Timestamp.valueOf("2018-01-17 14:10:02"));
        interval.setDeviceID("099E54FA8F092119A3C4913970B0E007876F790E");
        Metadata metadata = new Metadata();
        metadata.loadIdentityMetadata();
        metadata.loadAPCoverage();
        metadata.loadSpaceMetadata();
        FL.fineLocalization(interval,metadata);
    }

    public static void testRaw2Presence(){
        Query query = new Query();
        //String mac = "099e54fa8f092119a3c4913970b0e007876f790e";//peeyush
        //String mac = "11d58fd604e31332d0e061f9e445058afb453291";//dhrub
        String mac = "1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc";//Sameera
        //String mac = "7ca5b3ecc4c6618b1106b4beadf355ae7d4904fc";//guoxi
        //String mac = "c3aad8cf6280621da86c10880d8c694114c329d3";//primal
        query.setStartTime("2018-01-17 00:00:00");
        query.setEndTime("2018-01-18 00:00:00");//2018-02-01 00:00:00
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first

        contactTracing contact = new contactTracing();
        Presence presence = contact.RawToPresence(query, contact.readRawEvents(query));
    }

    public static String adjustTime(String time, String mode){
        Timestamp newTime = Timestamp.valueOf(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(newTime);
        if(mode.equals("+")){
            cal.add(Calendar.YEAR, 2);
        }else{
            cal.add(Calendar.YEAR, -2);
        }
        return new Timestamp(cal.getTime().getTime()).toString();
    }
}