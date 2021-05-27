package edu.uci.ics.model;

import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class NeighborSet {
    List<NeighborUnit> neighborSet;

    public List<NeighborUnit> getNeighborSet() {
        return neighborSet;
    }

    public void setNeighborSet(List<NeighborUnit> neighborSet) {
        this.neighborSet = neighborSet;
    }

    public List<NeighborUnit> readNeighborSet() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("neighbors.txt"));
            String row;
            List<NeighborUnit> neighborSet = new ArrayList<>();
            boolean isRead = false;
            while ((row = in.readLine()) != null) {
                isRead = true;
                String[] data = row.split(",");
                if(data[0].equals("#")){
                    NeighborUnit neighbor = new NeighborUnit();
                    neighbor.setStartTime(Timestamp.valueOf(data[1]));
                    int count = Integer.valueOf(data[2]);
                    List<String> neighbors = new ArrayList<>();
                    row = in.readLine();
                    String[] dataRow = row.split(",");
                    for(int i=0;i<count;i++){
                        neighbors.add(dataRow[i]);
                    }
                    neighbor.setNeighbors(neighbors);
                    neighborSet.add(neighbor);
                }
            }
            if(!isRead){//when the file is empty, initialize the set
                neighborSet = initializeNeighborSet();
            }
            setNeighborSet(neighborSet);
            in.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return neighborSet;
    }

    public void writeToDisk(){
        File file = new File("neighbors.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<neighborSet.size();i++){
                out.write(String.format("#,%s,%d",neighborSet.get(i).getStartTime().toString(),neighborSet.get(i).getNeighbors().size()));
                out.newLine();
                for(int j=0;j<neighborSet.get(i).getNeighbors().size();j++){
                    out.write(String.format("%s",neighborSet.get(i).getNeighbors().get(j)));
                    if(j!=neighborSet.get(i).getNeighbors().size()-1){
                        out.write(",");
                    }
                }
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<NeighborUnit> initializeNeighborSet(){
        List<NeighborUnit> neighborSet = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar(2017,7,1,0,0,0);
        for(int i=0;i<20;i++){
            calendar.add(Calendar.MONTH,1);
            Timestamp nowDate = new Timestamp(calendar.getTime().getTime());
            NeighborUnit neighborUnit = new NeighborUnit();
            neighborUnit.setStartTime(nowDate);
            neighborSet.add(neighborUnit);
        }
        return  neighborSet;
    }

    public List<Integer> findTimeInterval(Timestamp ST, Timestamp ET){
        List<Integer> indents = new ArrayList<>();
        int start=0, end=0;
        for(int i=1;i<neighborSet.size();i++){
            if(ST.getTime() >= neighborSet.get(i-1).getStartTime().getTime() && ST.getTime() <= neighborSet.get(i).getStartTime().getTime()){
                start = i-1;
            }
            if(ET.getTime() >= neighborSet.get(i-1).getStartTime().getTime() && ET.getTime() <= neighborSet.get(i).getStartTime().getTime()){
                end = i-1;
            }
        }
        for(int i=start;i<=end;i++){
            indents.add(i);
        }
        return indents;
    }

    public List<String> getGlobalNeighborSet(Timestamp ST, Timestamp ET){
        List<String> globalNeighbors = new ArrayList<>();
        List<Integer> indents = findTimeInterval(ST, ET);
        globalNeighbors = neighborSet.get(indents.get(0)).getNeighbors();
        for(int i=1;i<indents.size();i++){
            for(int j=0;j<neighborSet.get(indents.get(i)).getNeighbors().size();j++)
            if(globalNeighbors.indexOf(neighborSet.get(indents.get(i)).getNeighbors().get(j))==-1){
                globalNeighbors.add(neighborSet.get(indents.get(i)).getNeighbors().get(j));
            }
        }
        return globalNeighbors;
    }
}
