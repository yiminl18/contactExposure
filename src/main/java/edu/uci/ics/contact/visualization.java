package edu.uci.ics.contact;

import org.neo4j.driver.*;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.Calendar;

public class visualization {

    Driver driver;
    Session session;

    public void connect(){
        driver = GraphDatabase.driver(
                "neo4j+s://bdb3b893.databases.neo4j.io:7687", AuthTokens.basic("neo4j", "pSi2ZA689K-owtfC50LsERE-XFzeyJj4Q0TG04QTud0"));
        session = driver.session();
    }

    public void close(){
        session.close();
        driver.close();
    }

    public void visualization(){
        session.run(String.format("MATCH (n) RETURN *"));
    }

    public void clearGraph(){
        session.run("MATCH (n) DETACH DELETE n");
    }

    public void createPersonNode(String label, String mac){//label = User or Affected Person
        //System.out.println(String.format("CREATE(n: %s {MacAddress : '%s'})", label, mac));
        session.run(String.format("CREATE(n: %s {MacAddress : '%s'})", label, mac));
    }

    public String adjustTime(String time){
        Timestamp newTime = Timestamp.valueOf(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(newTime);// w ww.  j ava  2  s  .co m
        cal.add(Calendar.YEAR, 2); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime()).toString();
    }

    public void createEventNode(int id, String startTime, String endTime, String regionLabel, String roomLabel){
        startTime = adjustTime(startTime);
        endTime = adjustTime(endTime);

        if(roomLabel.equalsIgnoreCase("null")){
            //System.out.println(String.format("CREATE(n: Event {startTime : '%s', endTime: '%s', regionLabel : '%s'})", startTime, endTime, regionLabel));
            //print this event
            System.out.println(String.format("startTime : '%s', endTime: '%s', regionLabel : '%s'", startTime, endTime, regionLabel));
            session.run(String.format("CREATE(n: Interval {intervalID: 'Interval%s', startTime : '%s', endTime: '%s', regionLabel : '%s'})", String.valueOf(id), startTime, endTime, regionLabel));
        }
        else{
            //System.out.println(String.format("CREATE(n: Interval {startTime : '%s', endTime: '%s', regionLabel : '%s', roomLabel: '%s'})", startTime, endTime, regionLabel, roomLabel));
            //print this event
            System.out.println(String.format("startTime : '%s', endTime: '%s', regionLabel : '%s', roomLabel: '%s'", startTime, endTime, regionLabel, roomLabel));
            session.run(String.format("CREATE(n: Interval {intervalID: 'Interval%s', startTime : '%s', endTime: '%s', regionLabel : '%s', roomLabel: '%s'})", String.valueOf(id), startTime, endTime, regionLabel, roomLabel));
        }
    }

    public void createRelationship(String fromNode, String relationshipType, String endNode){
        //System.out.println(String.format("MATCH (a:%s), (b:%s) CREATE (a)-[r:%s]->(b)",fromNode, endNode, relationshipType));
        session.run(String.format("MATCH (a:%s), (b:%s) CREATE (a)-[r:%s]->(b)",fromNode, endNode, relationshipType));
    }

    public void createConstraintRelationship(String fromNode, String fromMac,  String endNode, String endMac, String relationshipType){
        //System.out.println(String.format("MATCH (a:%s {MacAddress : '%s'}), (b:%s {MacAddress : '%s'}) CREATE (a)-[r:%s]->(b)",fromNode, fromMac, endNode, endMac, relationshipType));
        session.run(String.format("MATCH (a:%s {MacAddress : '%s'}), (b:%s {MacAddress : '%s'}) CREATE (a)-[r:%s]->(b)",fromNode, fromMac, endNode, endMac, relationshipType));
    }

    public void deleteNode(String mac){
        session.run(String.format("MATCH (n: AffectedPerson {MacAddress: '%s'}) DELETE n", mac));
    }
}
