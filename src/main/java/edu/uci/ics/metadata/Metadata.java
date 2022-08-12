package edu.uci.ics.metadata;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.localization.Connect;
import edu.uci.ics.model.*;

public class Metadata {

    String path = "ap2room.dat";

    Map<String, Identity> identities = new HashMap<>();
    Map<String, Room> roomMap = new HashMap<>();
    Map<String, List<String>> apMapRoom;
    List<Room> rooms = new ArrayList<>();
    int value;

    public void setValue(){
        this.value =10;
    }

    public int getValue(){
        return value;
    }

    public void writeAPCoverage() {//to generate APCoverage file, only to run if this file is missing
        List<String> aps;
        aps = new ArrayList<>();
        apMapRoom = new HashMap<>();
        try (Connect connect = new Connect("server","mysql")) {
            Connection connection = connect.get();
            Statement st1 = connection.createStatement();
            ResultSet rs1 = st1.executeQuery("select id from SENSOR where sensor_type_id = 1;");
            while (rs1.next()) {
                aps.add(rs1.getString(1));
            }
            PreparedStatement ps1 = connection.prepareStatement(
                    "select INFRASTRUCTURE.name from SENSOR, COVERAGE_INFRASTRUCTURE, INFRASTRUCTURE\n"
                            + "where SENSOR.id = ? and SENSOR.COVERAGE_ID = COVERAGE_INFRASTRUCTURE.id and COVERAGE_INFRASTRUCTURE.SEMANTIC_ENTITY_ID = INFRASTRUCTURE.SEMANTIC_ENTITY_ID;");
            for (String ap : aps) {
                ps1.setString(1, ap);
                ResultSet rs2 = ps1.executeQuery();
                List<String> rooms = new ArrayList<>();
                while (rs2.next()) {
                    rooms.add(rs2.getString(1));
                }
                apMapRoom.put(ap, rooms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(apMapRoom);
            System.out.println("Successfully write to disk.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadAPCoverage() {
        try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(path))) {
            this.apMapRoom = (Map<String, List<String>>) ooi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("AP number: " + apMapRoom.size());
        for (Map.Entry<String,List<String>> entry : apMapRoom.entrySet()){
            System.out.println("AP = " + entry.getKey());
            for(int i=0;i<entry.getValue().size();i++){
                System.out.print(entry.getValue().get(i) + " ");
            }
            System.out.println();
        }
    }

    public List<String> getAPCoverage(String ap) {
        List<String> aps = apMapRoom.get(ap);
        return aps;
    }

    public void loadIdentityMetadata() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("office.csv"));
            String row;
            int count = 0;
            while ((row = csvReader.readLine()) != null) {
                count++;
                if (count == 1)//skip first line which is schema description
                    continue;
                String[] data = row.split(",");
                Identity identity = new Identity();
                identity.setName(data[0]);
                identity.setEmail(data[1]);
                identity.setMac(data[2]);
                identity.setOffice(data[3]);//ihe2: create a hash table from mac to office
                this.identities.put(identity.getMac(),identity);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSpaceMetadata(){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("space_metadata.csv"));
            String row;
            int i = 0;
            while ((row = csvReader.readLine()) != null) {
                i++;
                if (i == 1)//skip first line which is schema description
                    continue;
                String[] data = row.split(",");
                Room room = new Room();
                room.setRoom(data[0]);
                room.setImportance(Integer.valueOf(data[1]));//lower, more important
                room.setRoomType(data[2]);
                rooms.add(room);
                this.roomMap.put(room.getRoom(),room);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Room> getRooms(){
        return rooms;
    }

    public Room getRoom(String room){
        if(!roomMap.containsKey(room)){
            Room r = new Room();
            r.setRoom("null");
            return r;
        }else{
            return roomMap.get(room);
        }
    }

    public Identity getIdentity(String mac){
        if(!identities.containsKey(mac)) {
            Identity identity = new Identity();
            identity.setOffice("null");
            return identity;
        }else{
            return identities.get(mac);
        }
    }

    public List<String> selectAffectedPeople(){
        SQLGenerator sqlGenerator = new SQLGenerator();
        List<String> people = new ArrayList<>();
        Connect connectServer = new Connect("local","mysql");
        Connection serverConnection = connectServer.get();
        ResultSet rs;
        try{
            Statement stmtServer = serverConnection.createStatement();
            rs = stmtServer.executeQuery(sqlGenerator.selectAffectedPeople());
            while (rs.next()) {
                people.add(rs.getString(1));
            }
        }catch (SQLException e) {
        e.printStackTrace();
        }
        connectServer.close();
        return people;
    }
}
