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
        testContactQuery3();
    }

    public static void testQuery1(){
        Query query = new Query();
        String mac = "099e54fa8f092119a3c4913970b0e007876f790e";
        query.setStartTime("2018-01-01 00:00:00");
        query.setEndTime("2018-02-01 00:00:00");
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first
        contactTracing contact = new contactTracing();
        contact.printTrajectories(contact.ReportQuery(query));
    }

    public static void testMetaData(){
        Metadata MD = new Metadata();
        MD.loadIdentityMetadata();
        System.out.println(MD.getIdentity("2869e85fb0539d1b77712d6f837b94efde930703").getEmail() + " " + MD.getIdentity("2869e85fb0539d1b77712d6f837b94efde930703").getName() + " " + MD.getIdentity("2869e85fb0539d1b77712d6f837b94efde930703").getMac() + " " + MD.getIdentity("2869e85fb0539d1b77712d6f837b94efde930703").getOffice());
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

    public static void testCoarse(){
        coarseLocalization CL = new coarseLocalization();
        Interval interval = new Interval();
        interval.setStartTimeStamp(Timestamp.valueOf("2018-01-17 13:48:24"));
        interval.setEndTimeStamp(Timestamp.valueOf("2018-01-17 14:10:02"));
        interval.setDeviceID("099E54FA8F092119A3C4913970B0E007876F790E");
    }

    public static void testRemove(){
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(10);
        a.add(8);
        a.remove(0);
        System.out.println(a.size());
        a.clear();

        System.out.println(a.size());
    }

    public static void testContactQuery1(){
        Query query = new Query();
        //String mac = "099e54fa8f092119a3c4913970b0e007876f790e";//peeyush
        String mac = "11D58FD604E31332D0E061F9E445058AFB453291";//dhrub
        query.setStartTime("2018-01-18 00:00:00");
        query.setEndTime("2018-01-19 00:00:00");
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first
        contactTracing contact = new contactTracing();
        contact.ReportQueryDB(query);
    }

    public static void testContactQuery2(){
        Query query = new Query();
        String mac = "11D58FD604E31332D0E061F9E445058AFB453291";//dhrub
        //String mac = "1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc";//Sameera
        query.setStartTime("2018-01-16 10:55:48");
        query.setEndTime("2018-01-17 10:56:57");
        query.setUserID(mac.toLowerCase());//all macs are lowercase, check db first
        query.setSemanticMode(1);//summation, ##
        //query.setSemanticMode(2);//max, ##
        //query.setContactMode(1);//corase, ##
        query.setContactMode(2);//fine #

        query.setTimeThreshold(10);
        contactTracing contact = new contactTracing();
        contact.CheckQuery(query);
    }

    public static void testContactQuery3(){
        Query query = new Query();
        query.setStartTime("2018-01-17 00:00:00");
        query.setEndTime("2018-01-18 00:00:00");
        query.setSemanticMode(1);//summation, ##
        //query.setSemanticMode(2);//max, ##
        //query.setContactMode(1);//corase, #
        query.setContactMode(2);//fine #

        query.setTimeThreshold(15);
        contactTracing contact = new contactTracing();
        contact.ContactQuery(query);
    }

    public static void testIndex(){
        Index indexList = new Index();
        index ID = new index();
        indexList = ID.readIndex();
        ID.printIndex(indexList);
    }

    public static void testNeighbor(){
        NeighborSet ns = new NeighborSet();
    }


}