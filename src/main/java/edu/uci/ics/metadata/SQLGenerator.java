package edu.uci.ics.metadata;

public class SQLGenerator {
    public String readRawConnectivityData(String startTime, String endTime, String deviceMac){
        return String.format("select timeStamp, sensor_id from OBSERVATION_CLEAN where timeStamp >= '%s' and timeStamp <= '%s' and payload = '%s';", startTime, endTime, deviceMac);
    }

    public String readRawConnectivityHashedData(String startTime, String endTime, String deviceMac){
        return String.format("select time, ap_id from deid_connectivity where time >= '%s' and time <= '%s' and client_id = '%s';", startTime, endTime, deviceMac);
    }

    public String findNeighbors (String startTime, String endTime){
        return String.format("select distinct payload, sensor_id from OBSERVATION_CLEAN where timeStamp >= '%s' and timeStamp <= '%s';", startTime, endTime);
    }

    public String findNeighborsPostgres (String startTime, String endTime){
        return String.format("select distinct client_id, ap_id from deid_connectivity where time >= '%s' and time <= '%s';", startTime, endTime);
    }

    public String findFrequentNeighbors(String startTime, String endTime, int threshold){
        return String.format("select C.payload from " +
                "(select  payload, count(*) as connections from OBSERVATION_CLEAN " +
                "where timeStamp >= '%s' " +
                "and timeStamp <= '%s' " +
                "and sensor_id in ('3142-clwa-2099','3142-clwa-2059','3142-clwa-2065') " + //ihe this line is only for current affected people set, should be removed later
                "group by payload) as C " +
                "where C.connections >= %d",
                startTime, endTime, threshold);
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
