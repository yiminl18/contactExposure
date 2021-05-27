package edu.uci.ics.metadata;

public class SQLGenerator {
    public String readRawConnectivityData(String startTime, String endTime, String deviceMac){
        return String.format("select timeStamp, sensor_id from OBSERVATION_CLEAN where timeStamp >= '%s' and timeStamp <= '%s' and payload = '%s';", startTime, endTime, deviceMac);
    }

    public String findNeighbors (String startTime, String endTime){
        return String.format("select distinct payload, sensor_id from OBSERVATION_CLEAN where timeStamp >= '%s' and timeStamp <= '%s';", startTime, endTime);
    }

    public String insertAffectedPeople (String userID, String startTime, String endTime, String regionLabel, String roomLabel){
        return String.format("INSERT INTO affectedPeople (userID, startTime, endTime, regionlabel, roomlabel) VALUES('%s','%s','%s','%s', '%s')", userID, startTime, endTime, regionLabel, roomLabel);
    }

    public String selectAffectedPeople(){
        return String.format("select distinct userID from affectedPeople");
    }

    public String readCleanPresence(String startTime, String endTime, String mac){
        return String.format("select * from affectedPeople where userID = '%s' and startTime >= '%s' and endTime <= '%s'",mac, startTime, endTime);
    }
}
