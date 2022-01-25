package edu.uci.ics.contact;

import edu.uci.ics.metadata.*;
import edu.uci.ics.model.*;
import edu.uci.ics.localization.*;
import java.sql.Timestamp;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;

public class contactTracing {

    private long gapT = 10;

    String UserLabel = "User";
    String AffectedPersonLabel = "AffectedPerson";
    String EventLabel = "Interval";

    String ContactType = "Contact";
    String INType = "IN";

    Map<String, Boolean> isCreatedNode = new HashMap<String, Boolean>();


    public void loadMetadata(Metadata metadata){
        metadata.loadIdentityMetadata();
        metadata.loadAPCoverage();
        metadata.loadSpaceMetadata();
    }

    public Index loadIndex(){
        index ID = new index();
        Index index = new Index();
        index = ID.readIndex();
        return index;
    }

    public void constructQ2(){//for demo purpose
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        List<String> users = new ArrayList<>();
        List<String> affected = new ArrayList<>();
        users.add("9867312b6133ba7e9832f2ce3c74236ed4be16fc");
        users.add("0482dfa058c1bb990b44def72c3c4b5b8e25c9e3");
        users.add("7d0a18f9c3d697670147acf767003cca6613fe3c");
        users.add("3af12eed51509db0210485ee464c13554415e9e7");
        users.add("3ab1c1f06d00cfa5ac53573cf9787790f8b51ff8");
        users.add("1fd0eb72fe4065c751213d8f4c93b4c31b3d6261");
        users.add("45671fa12afbf228d56344502f78ae9bb3bc0b99");
        /*
        users.add("6ad3bcadc421332c7c3b3afd7f78d9cb4d5dedb2");
        users.add("6b15a19f4d8a5c0586be43ad698ac8d02a56bbd7");
        users.add("434b1e0209aa4f94a03868c5b2deffdb42b28e21");
        users.add("50e7c991627614e6fd0b15cc408f04c14d5f5c7e");
        users.add("1b75dd5d5bf04e1cf7cc1b0fa3967d7c1c39854f");
        users.add("2e2a3e06449721a6888e4ab5af7989c10a1db45d");
        */

        for(int i=0;i<users.size();i++){
            VS.createPersonNode(UserLabel, users.get(i));
        }
        affected.add("000f6968b5d80b90196e85380e9e3fb691350126");
        affected.add("099e54fa8f092119a3c4913970b0e007876f790e");
        affected.add("1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc");
        affected.add("0505ce28e0a1b230653de64959e47d72060b3b56");
        affected.add("128600d27acd064f9be48f37a513bd5bceae2847");
        affected.add("1228388a903cf2270cde3443325d2772a9c4d305");
        affected.add("3bcd6ef806f9657a6c280067ed6eaf0603b69f94");
        affected.add("670f78f7c4ba72ba05e744387c97971305f9c03f");
        affected.add("190cf1da5d9bd9812ddcfa20ac056f5fe2bfaefa");
        affected.add("047cc8e269a12c8ad54c08ed2199545f6bc4a823");
        affected.add("5d1e9b176b8dd1cbe27e20ed1316199832d3563c");
        affected.add("44a93a4283689a7f661258a25b730ba2daf6ecdc");
        affected.add("58989a52695abd35bc4942602a074c3fb42c2ecf");
        affected.add("2f4220eceeaf53220ef0d96fed162c14bb403025");
        affected.add("206f292567e7c66ca197743c794eae8f2d23a1a1");
        for(int i=0;i<affected.size();i++){
            VS.createPersonNode(AffectedPersonLabel, affected.get(i));
        }
        int n = users.size();
        int m = affected.size();
        Random rand = new Random(); //instance of random class

        List<Integer> is = new ArrayList<>();

        for(int i=0;i<affected.size();i++){
            int randnum = rand.nextInt(3);
            if(randnum==0) randnum=1;
            for(int j=0;j<randnum;j++){
                int randid = rand.nextInt(n-1);
                is.add(randid);
                VS.createConstraintRelationship(UserLabel, users.get(randid), AffectedPersonLabel, affected.get(i), ContactType);
            }
        }

        for(int i=0;i<users.size();i++){
            if(is.indexOf(i)==-1){
                VS.deleteNode(users.get(i));
            }
        }

        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"000f6968b5d80b90196e85380e9e3fb691350126", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"099e54fa8f092119a3c4913970b0e007876f790e", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"0505ce28e0a1b230653de64959e47d72060b3b56", ContactType);

        VS.visualization();
        VS.close();
    }

    public void constructQ2Small(){//for demo purpose
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        List<String> users = new ArrayList<>();
        List<String> affected = new ArrayList<>();
        users.add("9867312b6133ba7e9832f2ce3c74236ed4be16fc");
        users.add("0482dfa058c1bb990b44def72c3c4b5b8e25c9e3");
        users.add("7d0a18f9c3d697670147acf767003cca6613fe3c");
        users.add("3af12eed51509db0210485ee464c13554415e9e7");
        users.add("3ab1c1f06d00cfa5ac53573cf9787790f8b51ff8");
        users.add("1fd0eb72fe4065c751213d8f4c93b4c31b3d6261");
        users.add("45671fa12afbf228d56344502f78ae9bb3bc0b99");

        for(int i=0;i<users.size();i++){
            VS.createPersonNode(UserLabel, users.get(i));
        }
        affected.add("000f6968b5d80b90196e85380e9e3fb691350126");
        affected.add("099e54fa8f092119a3c4913970b0e007876f790e");
        affected.add("1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc");
        affected.add("0505ce28e0a1b230653de64959e47d72060b3b56");
        affected.add("128600d27acd064f9be48f37a513bd5bceae2847");
        affected.add("1228388a903cf2270cde3443325d2772a9c4d305");
        affected.add("3bcd6ef806f9657a6c280067ed6eaf0603b69f94");
        affected.add("670f78f7c4ba72ba05e744387c97971305f9c03f");
        affected.add("190cf1da5d9bd9812ddcfa20ac056f5fe2bfaefa");
        /*
        affected.add("047cc8e269a12c8ad54c08ed2199545f6bc4a823");
        affected.add("5d1e9b176b8dd1cbe27e20ed1316199832d3563c");
        affected.add("44a93a4283689a7f661258a25b730ba2daf6ecdc");
        affected.add("58989a52695abd35bc4942602a074c3fb42c2ecf");
        affected.add("2f4220eceeaf53220ef0d96fed162c14bb403025");
        affected.add("206f292567e7c66ca197743c794eae8f2d23a1a1");*/
        for(int i=0;i<affected.size();i++){
            VS.createPersonNode(AffectedPersonLabel, affected.get(i));
        }
        int n = users.size();
        int m = affected.size();
        Random rand = new Random(); //instance of random class

        List<Integer> is = new ArrayList<>();

        for(int i=0;i<affected.size();i++){
            int randnum = rand.nextInt(3);
            if(randnum==0) randnum=1;
            for(int j=0;j<randnum;j++){
                int randid = rand.nextInt(n-1);
                is.add(randid);
                VS.createConstraintRelationship(UserLabel, users.get(randid), AffectedPersonLabel, affected.get(i), ContactType);
            }
        }

        for(int i=0;i<users.size();i++){
            if(is.indexOf(i)==-1){
                VS.deleteNode(users.get(i));
            }
        }

        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"000f6968b5d80b90196e85380e9e3fb691350126", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"099e54fa8f092119a3c4913970b0e007876f790e", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"1bb16e6e140dfac7c5d0f223c35d5dc9ca5834bc", ContactType);
        //VS.createConstraintRelationship(UserLabel,"9867312b6133ba7e9832f2ce3c74236ed4be16fc",AffectedPersonLabel,"0505ce28e0a1b230653de64959e47d72060b3b56", ContactType);

        VS.visualization();
        VS.close();
    }

    public Presence ReportQuery(Query query){
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        index ID = new index();
        Index indexList = new Index();
        indexList = loadIndex();
        indexList.initializeAPs();
        NeighborSet candidateIntervals = new NeighborSet();
        candidateIntervals.setNeighborSet(candidateIntervals.readNeighborSet());
        VS.createPersonNode(AffectedPersonLabel, query.getUserID());

        Metadata metadata = new Metadata();
        loadMetadata(metadata);
        Presence presence = RawToPresence(query, readRawEvents(query));
        User user = new User();
        user.setUserID(query.getUserID());
        user.setVisitedAps(presence.getVisitedAPs());
        System.out.println("Report Query: " + query.getUserID() + " From: " + query.getStartTime() + " To: " + query.getEndTime());

        SQLGenerator sqlGenerator = new SQLGenerator();

        Connect connectServer = new Connect("local","mysql");
        Connection serverConnection = connectServer.get();
        ResultSet rs;
        try{
            Statement stmtServer = serverConnection.createStatement();
            for(int i=0;i<presence.getPresence().size();i++){
                coarseLocalization CL = new coarseLocalization();
                fineLocalization FL = new fineLocalization();

                String label = CL.coarseLocalization(presence.getPresence().get(i),user);
                presence.getPresence().get(i).setRegionLabel(label);
                if(label.equals("out")){
                    presence.getPresence().get(i).setRoomLabel("out");
                }
                else{
                    presence.getPresence().set(i, FL.fineLocalization(presence.getPresence().get(i),metadata));
                    //update candidate intervals
                    UpdateCandidateIntervals(query, presence.getPresence().get(i).getNeighbors(),candidateIntervals);
                }
                //write to database
                String userID = presence.getPresence().get(i).getDeviceID();
                String startTime = presence.getPresence().get(i).getStartTimeStamp().toString();
                String endTime = presence.getPresence().get(i).getEndTimeStamp().toString();
                String region = presence.getPresence().get(i).getRegionLabel();
                String room = presence.getPresence().get(i).getRoomLabel();
                stmtServer.executeUpdate(sqlGenerator.insertAffectedPeople(userID,startTime,endTime,region,room));
                //update index
                int apID = indexList.findAP(region);
                if(apID != -1){
                    indexList.getIndex().get(apID).getIndexNodes().add(presence.getPresence().get(i));
                    indexList.getIndex().get(apID).setDirtyBit();
                }else{
                    IndexNode node = new IndexNode();
                    node.setWifiAP(region);
                    node.addIndexNode(presence.getPresence().get(i));
                    node.setDirtyBit();
                    indexList.getIndex().add(node);
                    indexList.insertAP(region);
                }
                VS.createEventNode(i, presence.getPresence().get(i).getStartTimeStamp().toString(),presence.getPresence().get(i).getEndTimeStamp().toString(),presence.getPresence().get(i).getRegionLabel(),presence.getPresence().get(i).getRoomLabel());
                //System.out.println(presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getRegionLabel() + " " +presence.getPresence().get(i).getRoomLabel());
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();

        indexList = ID.sortIndex(indexList);
        ID.writeToDisk(indexList);
        candidateIntervals.writeToDisk();
        VS.createRelationship(AffectedPersonLabel, INType, EventLabel);
        VS.visualization();
        VS.close();
        return presence;
    }

    public void ReportQueryDB(Query query){//for visualization purpose print the clean data in DB
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        VS.createPersonNode(AffectedPersonLabel, query.getUserID());

        SQLGenerator sqlGenerator = new SQLGenerator();
        rawConnectivityEvents events = new rawConnectivityEvents();

        Connect connectServer = new Connect("local","mysql");
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        System.out.println("Report Query: " + query.getUserID() + " From: " + query.getStartTime() + " To: " + query.getEndTime());


        //read raw connectivity data
        try {
            Statement stmtServer = serverConnection.createStatement();
            rs = stmtServer.executeQuery(sqlGenerator.readCleanPresence(query.getStartTime(), query.getEndTime(), query.getUserID()));
            events.initEvents();
            int c=0;
            while (rs.next()) {
                VS.createEventNode(c++,rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();
        VS.createRelationship(AffectedPersonLabel, INType, EventLabel);
        VS.visualization();
        VS.close();
    }

    public Boolean CheckQuery(Query query){
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        Index indexList = new Index();
        indexList = loadIndex();
        indexList.initializeAPs();
        Metadata metadata = new Metadata();
        loadMetadata(metadata);
        VS.createPersonNode(UserLabel, query.getUserID());

        NeighborSet candidateIntervals = new NeighborSet();
        candidateIntervals.readNeighborSet();

        List<String> candidateNeighbors = new ArrayList<>();
        candidateNeighbors = candidateIntervals.getGlobalNeighborSet(Timestamp.valueOf(query.getStartTime()), Timestamp.valueOf(query.getEndTime()));

        if(candidateNeighbors.indexOf(query.getUserID()) == -1){
            VS.visualization();
            VS.close();
            return false;
        }

        boolean flag = isContact(query, indexList, metadata,2, VS);
        VS.createRelationship(UserLabel,ContactType,AffectedPersonLabel);
        VS.createRelationship(UserLabel, INType, EventLabel);
        VS.createRelationship(AffectedPersonLabel, INType, EventLabel);
        VS.visualization();
        VS.close();
        return flag;
    }

    public List<String> ContactQuery(Query query){
        visualization VS = new visualization();
        VS.connect();
        VS.clearGraph();
        List<String> contactUsers = new ArrayList<>();

        Index indexList = new Index();
        indexList = loadIndex();
        indexList.initializeAPs();
        Metadata metadata = new Metadata();
        loadMetadata(metadata);
        NeighborSet candidateIntervals = new NeighborSet();
        candidateIntervals.readNeighborSet();

        List<String> People = metadata.selectAffectedPeople();
        System.out.println("Affected People:");

        for(int i=0;i<People.size();i++){
            System.out.println(People.get(i));
        }

        List<String> affectedPeople = filterAffectedPeople(People, query);
        List<String> candidateNeighbors;

//        System.out.println("Real Affected People:");
//
//        for(int i=0;i<affectedPeople.size();i++){
//            System.out.println(affectedPeople.get(i));
//        }

        //candidateNeighbors = candidateIntervals.getGlobalNeighborSet(Timestamp.valueOf(query.getStartTime()), Timestamp.valueOf(query.getEndTime()));
        //candidateNeighbors should be constrained by time
        candidateNeighbors = findNeighbors(query, "mysql");
        System.out.println(String.format("The number of candidates is %d.",candidateNeighbors.size()));
        for(int i=0;i<candidateNeighbors.size();i++){
            if(affectedPeople.indexOf(candidateNeighbors.get(i)) != -1) continue;
            //if(query.getContactMode() == 2){
                System.out.println(String.format("Now checking %d-th person with device mac %s.", i, candidateNeighbors.get(i)));
            //}
            Query newQuery = query;
            newQuery.setUserID(candidateNeighbors.get(i));
            if(isContact(newQuery, indexList, metadata,3, VS)){
                contactUsers.add(candidateNeighbors.get(i));
            }
        }
        VS.createRelationship(UserLabel, INType, EventLabel);
        VS.createRelationship(AffectedPersonLabel, INType, EventLabel);
        VS.visualization();
        VS.close();
        return contactUsers;
    }

    public boolean isContact(Query query, Index indexList, Metadata metadata, int printMode, visualization VS){
        boolean isAffected = false;
        boolean isPrintTargetNode = false;
        index ID = new index();
        Presence presence = RawToPresence(query, readRawEvents(query));
        User user = new User();
        user.setUserID(query.getUserID());
        user.setVisitedAps(presence.getVisitedAPs());
        Map<String, Boolean> isContainedInEdge = new HashMap<String, Boolean>();

        coarseLocalization CL = new coarseLocalization();
        fineLocalization FL = new fineLocalization();

        int numOfFineLocalization = 0;
        long validOverlaps = 0;

        List<ContactPerson> contactPersonList = new ArrayList<>();//store final contact answers
        List<String> contactPeopleName = new ArrayList<>();

        for(int i=0;i<presence.getPresence().size();i++){
            //iterate each interval of checked person
            Interval interval = presence.getPresence().get(i);
            String label = CL.coarseLocalization(interval, user);
            interval.setRegionLabel(label);
            //System.out.println(interval.getStartTimeStamp() + " " + interval.getEndTimeStamp() + " " + interval.getRegionLabel());
            if(label.equals("out")) continue;


            List<Interval> candidateIntervals = new ArrayList<>();
            List<Integer> overlapTime = new ArrayList<>();

            int indent = indexList.findAP(interval.getRegionLabel());
            long sumOverlaps = 0;
            long maxOverlap = -1;
            if(indent != -1){
                //for one interval, find all the intervals of contacted people
                int closestIntervalIndent = indexList.getIndex().get(indent).findClosestInterval(interval);
                for(int j = closestIntervalIndent; j<indexList.getIndex().get(indent).getIndexNodes().size();j++){
                    if(interval.getEndTimeStamp().getTime() <= indexList.getIndex().get(indent).getIndexNodes().get(j).getStartTimeStamp().getTime()){
                        //if this interval is ahead of the first interval of affected people, done
                        break;
                    }
                    long overlap = timeOverlap(interval, indexList.getIndex().get(indent).getIndexNodes().get(j));
                    if(overlap >= query.getTimeThreshold() && query.getSemanticMode() == 2){
                        indexList.getIndex().get(indent).getIndexNodes().get(j).setRegionLabel(indexList.getIndex().get(indent).getWifiAP());
                        candidateIntervals.add(indexList.getIndex().get(indent).getIndexNodes().get(j));
                    }
                    else if(query.getSemanticMode() == 1 && overlap > 0){
                        sumOverlaps += overlap;
                        overlapTime.add((int)overlap);
                        indexList.getIndex().get(indent).getIndexNodes().get(j).setRegionLabel(indexList.getIndex().get(indent).getWifiAP());
                        candidateIntervals.add(indexList.getIndex().get(indent).getIndexNodes().get(j));
                    }
                }
            }
            //deal with candidateIntervals
            //System.out.println(candidateIntervals.size());

            if(query.getSemanticMode() == 2){//continuous/max semantic
                if(query.getContactMode() == 1){//region
                    for(int j=0;j<candidateIntervals.size();j++){
                        if(candidateIntervals.get(j).getRegionLabel().equals(interval.getRegionLabel())){
                            String contactDevice = candidateIntervals.get(j).getDeviceID();
                            if(contactPeopleName.indexOf(contactDevice) == -1){
                                contactPeopleName.add(contactDevice);
                                ContactPerson person = new ContactPerson();
                                person.setUserID(contactDevice);
                                //person.setContactTime();
                                person.setContact();
                                person.getContactIntervals().add(candidateIntervals.get(j));
                                contactPersonList.add(person);
                            }else{
                                contactPersonList.get(contactPeopleName.indexOf(contactDevice)).setContact();
                                contactPersonList.get(contactPeopleName.indexOf(contactDevice)).getContactIntervals().add(candidateIntervals.get(j));
                            }
                            isAffected = true;
                            break;
                        }
                    }
                }else{//room level
                    FL.fineLocalization(interval, metadata);
                    numOfFineLocalization++;
                    for(int j=0;j<candidateIntervals.size();j++){
                        if(candidateIntervals.get(j).getRoomLabel().equals(interval.getRoomLabel())){
                            isAffected = true;
                            ContactPerson person = new ContactPerson();
                            person.setContact();
                            person.setUserID(candidateIntervals.get(j).getDeviceID());
                            person.getContactIntervals().add(candidateIntervals.get(j));
                            contactPersonList.add(person);
                            break;
                        }
                    }
                }
            }
            else{//summation semantic
                if(candidateIntervals.size() > 0 && sumOverlaps >= query.getTimeThreshold()){
                    if(query.getContactMode() == 1){//coarse level
                        for(int j=0;j<candidateIntervals.size();j++){
                            String contactDevice = candidateIntervals.get(j).getDeviceID();
                            int id = contactPeopleName.indexOf(contactDevice);
                            if(id == -1){
                                contactPeopleName.add(contactDevice);
                                ContactPerson person = new ContactPerson();
                                person.setUserID(contactDevice);
                                person.setContactTime(overlapTime.get(j));
                                person.getContactIntervals().add(candidateIntervals.get(j));
                                contactPersonList.add(person);
                                if(overlapTime.get(j) > maxOverlap){
                                    maxOverlap = overlapTime.get(j);
                                }
                                if(maxOverlap > query.getTimeThreshold()){
                                    isAffected = true;
                                    contactPersonList.get(0).setContact();
                                    break;
                                }
                            }
                            else{
                                contactPersonList.get(id).getContactIntervals().add(candidateIntervals.get(j));
                                contactPersonList.get(id).setContactTime(contactPersonList.get(id).getContactTime() + overlapTime.get(j));
                                if(contactPersonList.get(id).getContactTime() > maxOverlap) {
                                    maxOverlap = contactPersonList.get(id).getContactTime();
                                }
                                if(maxOverlap > query.getTimeThreshold()){
                                    isAffected = true;
                                    contactPersonList.get(id).setContact();
                                    break;
                                }
                            }
                        }
                    }else{//fine level
                        numOfFineLocalization++;
                        FL.fineLocalization(interval, metadata);
                        for(int j=0;j<candidateIntervals.size();j++){
                            if(candidateIntervals.get(j).getRoomLabel().equals(interval.getRoomLabel())){
                                String contactDevice = candidateIntervals.get(j).getDeviceID();
                                int id = contactPeopleName.indexOf(contactDevice);
                                if(id == -1){
                                    contactPeopleName.add(contactDevice);
                                    ContactPerson person = new ContactPerson();
                                    person.setUserID(contactDevice);
                                    person.setContactTime(overlapTime.get(j));
                                    person.getContactIntervals().add(candidateIntervals.get(j));
                                    contactPersonList.add(person);
                                    if(overlapTime.get(j) > maxOverlap){
                                        maxOverlap = overlapTime.get(j);
                                    }
                                    if(maxOverlap > query.getTimeThreshold()){
                                        isAffected = true;
                                        contactPersonList.get(0).setContact();
                                        break;
                                    }
                                }else{
                                    contactPersonList.get(id).getContactIntervals().add(candidateIntervals.get(j));
                                    contactPersonList.get(id).setContactTime(contactPersonList.get(id).getContactTime() + overlapTime.get(j));
                                    if(contactPersonList.get(id).getContactTime() > maxOverlap) {
                                        maxOverlap = contactPersonList.get(id).getContactTime();
                                    }
                                    if(maxOverlap > query.getTimeThreshold()){
                                        isAffected = true;
                                        contactPersonList.get(id).setContact();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(printMode == 2){//check query
            if(!isAffected){
                System.out.println(String.format("The user with device %s have not contacted with any affected person from %s to %s", query.getUserID(), query.getStartTime().toString(), query.getEndTime().toString()));
            }else{
                System.out.println(String.format("The user with device %s have contacted with the affected person from %s to %s in the following events", query.getUserID(), query.getStartTime().toString(), query.getEndTime().toString()));
                int intervalID = 0;
                for(int i=0;i<contactPersonList.size();i++){
                    if(contactPersonList.get(i).isContact()){
                        System.out.println(String.format("Contacted person: %s.", contactPersonList.get(i).getUserID()));
                        VS.createPersonNode(AffectedPersonLabel, contactPersonList.get(i).getUserID());
                        for(int j=0;j<contactPersonList.get(i).getContactIntervals().size();j++){
                            if(query.getContactMode() == 1) {//region
                                System.out.println(adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(),"+") + " " + adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(),"+") + " " + contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel());
                                VS.createEventNode(intervalID,contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel(), "null");
                            }
                            else{//room
                                System.out.println(adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(),"+") + " " + adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(),"+") + " " + contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel() + " " + contactPersonList.get(i).getContactIntervals().get(j).getRoomLabel());
                                VS.createEventNode(intervalID,contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel(), contactPersonList.get(i).getContactIntervals().get(j).getRoomLabel());
                            }
                            intervalID ++;
                        }
                    }
                }

                //System.out.println(String.format("The number of calling fine localization is %d.",numOfFineLocalization));
            }
        }
        else{//contact query
            if(!isAffected){
                System.out.println(String.format("The user with device %s has not contacted with any affected person from %s to %s", query.getUserID(), query.getStartTime().toString(), query.getEndTime().toString()));
            }else{
                if(!isPrintTargetNode){
                    isPrintTargetNode = true;
                    VS.createPersonNode(UserLabel, query.getUserID());
                }
                System.out.println(String.format("The user with device %s has contacted with the affected person from %s to %s in the following events", query.getUserID(), query.getStartTime().toString(), query.getEndTime().toString()));

                int intervalID = 0;

                for(int i=0;i<contactPersonList.size();i++){

                    if(contactPersonList.get(i).isContact()){
                        System.out.println(String.format("Contacted person: %s.", contactPersonList.get(i).getUserID()));
                        if(!isCreatedNode.containsKey(contactPersonList.get(i).getUserID())){
                            isCreatedNode.put(contactPersonList.get(i).getUserID(), true);
                            VS.createPersonNode(AffectedPersonLabel, contactPersonList.get(i).getUserID());
                        }
                        if(!isContainedInEdge.containsKey(contactPersonList.get(i).getUserID())){
                            isContainedInEdge.put(contactPersonList.get(i).getUserID(), true);
                            VS.createConstraintRelationship(UserLabel, query.getUserID(), AffectedPersonLabel, contactPersonList.get(i).getUserID(), ContactType);
                        }
                        for(int j=0;j<contactPersonList.get(i).getContactIntervals().size();j++){
                            if(query.getContactMode() == 1) {//weak{
                                System.out.println(adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(),"+") + " " + adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(),"+") + " " + contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel());
                                VS.createEventNode(intervalID,contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel(), "null");
                            }
                            else{
                                System.out.println(adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(),"+") + " " + adjustTime(contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(),"+") + " " + contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel() + " " + contactPersonList.get(i).getContactIntervals().get(j).getRoomLabel());
                                VS.createEventNode(intervalID,contactPersonList.get(i).getContactIntervals().get(j).getStartTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getEndTimeStamp().toString(), contactPersonList.get(i).getContactIntervals().get(j).getRegionLabel(), contactPersonList.get(i).getContactIntervals().get(j).getRoomLabel());
                            }
                            intervalID ++;
                        }
                    }
                }
                //System.out.println(String.format("The number of calling fine localization is %d.",numOfFineLocalization));
            }
        }


        return isAffected;
    }

    public void UpdateCandidateIntervals(Query query, List<String> neighbors, NeighborSet neighborSet){
        List<Integer> indents = neighborSet.findTimeInterval(Timestamp.valueOf(query.getStartTime()), Timestamp.valueOf(query.getEndTime()));
        for(int i=0;i<indents.size();i++){
            for(int j=0;j<neighbors.size();j++){
                neighborSet.getNeighborSet().get(indents.get(i)).addNeighbor(neighbors.get(j));
            }
        }
    }

    public long timeOverlap(Interval a, Interval b){//return time overlap of two intervals
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

    public rawConnectivityEvents readRawEvents(Query query){
        SQLGenerator sqlGenerator = new SQLGenerator();
        rawConnectivityEvents events = new rawConnectivityEvents();

        Connect connectServer = new Connect("server","mysql");
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

    public rawConnectivityEvents readRawEventsFromHashedMac(Query query){
        SQLGenerator sqlGenerator = new SQLGenerator();
        rawConnectivityEvents events = new rawConnectivityEvents();

        Connect connectServer = new Connect("server","postgres");
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        //read raw connectivity data
        try {
            Statement stmtServer = serverConnection.createStatement();
            rs = stmtServer.executeQuery(sqlGenerator.readRawConnectivityHashedData(query.getStartTime(), query.getEndTime(), query.getUserID()));
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

    public List<String> findNeighbors(Query query, String type){
        List<String> neighbors = new ArrayList<>();
        SQLGenerator sqlGenerator = new SQLGenerator();

        Connect connectServer = new Connect("server",type);
        Connection serverConnection = connectServer.get();
        ResultSet rs;

        try {
            Statement stmtServer = serverConnection.createStatement();
            String ST = query.getStartTime();
            String ET = query.getEndTime();
            if(type.equals("mysql")){
                rs = stmtServer.executeQuery(sqlGenerator.findFrequentNeighbors(ST, ET, 3));
            }else{
                rs = stmtServer.executeQuery(sqlGenerator.findNeighborsPostgres(ST, ET));
            }
            while (rs.next()) {
                neighbors.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectServer.close();

        return neighbors;
    }

    public List<String> filterAffectedPeople(List<String> affectedPeople, Query query){
        List<String> people = new ArrayList<>();
        for(int i=0;i<affectedPeople.size();i++){
            Query newQuery = query;
            newQuery.setUserID(affectedPeople.get(i));
            Presence presence = RawToPresence(newQuery, readRawEvents(newQuery));
            presence.setDuration();
            if(presence.getCount() > 0 && presence.getDuration()/1000/60 >= query.getTimeThreshold()){
                people.add(affectedPeople.get(i));
                //System.out.println(affectedPeople.get(i) + " " + presence.getDuration()/1000/60);
            }
        }
        return people;
    }

    public Presence RawToPresence(Query query, rawConnectivityEvents events){
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

        Interval interval = new Interval();

        for(int i=1;i<events.getEvents().size();i++){
            Timestamp nowTime = events.getEvents().get(i).getTimeStamp();
            String nowAP = events.getEvents().get(i).getAP();

            if(visitedAPs.containsKey(nowAP)){
                visitedAPs.put(nowAP,visitedAPs.get(nowAP)+1);
            }else{
                visitedAPs.put(nowAP,1);
            }

            long difference  = (nowTime.getTime() - preTime.getTime())/(60*1000);


            if(nowAP.equals(preAP)){
                //merge current interval with previous interval
                interval.setEndTimeStamp(nowTime);
            }else{
                //add previous interval first
                if(interval.getStartTimeStamp() != null){
                    presence.getPresence().add(interval);
                }

                //add current interval
                if(difference <= gapT){
                    interval = new Interval();
                    interval.setDeviceID(targetDevice);
                    interval.setStartTimeStamp(preTime);
                    interval.setEndTimeStamp(nowTime);
                    interval.setStartAP(preAP);
                    interval.setEndAP(nowAP);
                    //presence.getPresence().add(interval);
                }
                else{
                    //add interval
                    Timestamp gapTime = new Timestamp(preTime.getTime() + gapT*60*1000);
                    interval = new Interval();
                    interval.setDeviceID(targetDevice);
                    interval.setStartTimeStamp(preTime);
                    interval.setEndTimeStamp(gapTime);
                    interval.setStartAP(preAP);
                    interval.setEndAP(preAP);
                    presence.getPresence().add(interval);
                    //add gap
                    interval = new Interval();
                    interval.setDeviceID(targetDevice);
                    interval.setStartTimeStamp(gapTime);
                    interval.setEndTimeStamp(nowTime);
                    interval.setStartAP(preAP);
                    interval.setEndAP(nowAP);
                }
            }


            preTime = nowTime;
            preAP = nowAP;
        }
        presence.setVisitedAps(visitedAPs);
        presence.setCount(events.getEvents().size());

        //debug
//        System.out.println(events.getEvents().size());
//        for(int i=0;i<events.getEvents().size();i++){
//            System.out.println(events.getEvents().get(i).getTimeStamp() + " " + events.getEvents().get(i).getAP());
//        }
//        System.out.println(presence.getPresence().size());
//        for(int i=0;i<presence.getPresence().size();i++){
//            System.out.println(presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getStartAP() + " " + presence.getPresence().get(i).getEndAP());
//        }
        return presence;
    }

    public void printTrajectories(Presence presence){
        System.out.println("Trajectories of user: "+ presence.getUserID());
        for(int i=0;i<presence.getPresence().size();i++){
            System.out.println(presence.getPresence().get(i).getStartTimeStamp() + " " + presence.getPresence().get(i).getEndTimeStamp() + " " + presence.getPresence().get(i).getRegionLabel());
        }
    }

    public String adjustTime(String time, String mode){
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
