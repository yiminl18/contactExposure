package edu.uci.ics.contact;

import edu.uci.ics.model.*;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;

public class index {

    public Index sortIndex(Index index){
        for(int i=0;i<index.getIndex().size();i++){
            if(index.getIndex().get(i).isDirtyBit()){//this node is dirty
                index.getIndex().get(i).sort();
            }
        }
        return index;
    }

    public void writeToDisk(Index index){
        File file = new File("Index.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<index.getIndex().size();i++){
                out.write(String.format("#,%s,%d",index.getIndex().get(i).getWifiAP(),index.getIndex().get(i).getIndexNodes().size()));
                out.newLine();
                for(int j=0;j<index.getIndex().get(i).getIndexNodes().size();j++){
                    out.write(String.format("%s,%s,%s,%s,%f",index.getIndex().get(i).getIndexNodes().get(j).getStartTimeStamp(), index.getIndex().get(i).getIndexNodes().get(j).getEndTimeStamp(), index.getIndex().get(i).getIndexNodes().get(j).getDeviceID(), index.getIndex().get(i).getIndexNodes().get(j).getRoomLabel(), index.getIndex().get(i).getIndexNodes().get(j).getRoomConfidence()));
                    out.newLine();
                }
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Index readIndex(){
        Index index = new Index();
        try {
            BufferedReader in = new BufferedReader(new FileReader("index.txt"));
            String row;
            List<IndexNode> nodes = new ArrayList<>();
            while ((row = in.readLine()) != null) {
                String[] data = row.split(",");
                if(data[0].equals("#")){
                    IndexNode node = new IndexNode();
                    node.setWifiAP(data[1]);
                    int numberOfInterval = Integer.valueOf(data[2]);
                    for(int i=0;i<numberOfInterval;i++){
                        row = in.readLine();
                        String[] dataRow = row.split(",");
                        Interval interval = new Interval();
                        interval.setStartTimeStamp(Timestamp.valueOf(dataRow[0]));
                        interval.setEndTimeStamp(Timestamp.valueOf(dataRow[1]));
                        interval.setDeviceID(dataRow[2]);
                        interval.setRoomLabel(dataRow[3]);
                        if(dataRow[4].equals("NaN") || dataRow[4].equals("null")){//ihe: deal with this case later
                            interval.setRoomConfidence(-1.0);
                        }
                        else{
                            interval.setRoomConfidence(Double.valueOf(dataRow[4]));
                        }
                        node.addIndexNode(interval);
                    }
                    nodes.add(node);
                }
            }
            index.setIndex(nodes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }

    public void printIndex(Index index){
        //for testing purpose
        for(int i=0;i<index.getIndex().size();i++){
            System.out.println(index.getIndex().get(i).getWifiAP());
            for(int j=0;j<index.getIndex().get(i).getIndexNodes().size();j++){
                System.out.println(index.getIndex().get(i).getIndexNodes().get(j).getStartTimeStamp() + " " + index.getIndex().get(i).getIndexNodes().get(j).getEndTimeStamp() + " " + index.getIndex().get(i).getIndexNodes().get(j).getDeviceID() + " " + index.getIndex().get(i).getIndexNodes().get(j).getRoomLabel());
            }
        }
    }


}
