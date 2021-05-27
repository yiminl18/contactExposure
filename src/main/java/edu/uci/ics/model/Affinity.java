package edu.uci.ics.model;

import java.util.List;

public class Affinity {
    String mac;
    String timeStamp;
    String wifiAp;
    public List<Double> roomAffinity;
    List<String> candidateRooms;
    public List<Double> deviceAffinity;//store device affinity between target device and all neighbors
    List<String> neighbors;

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }

    public List<String> getCandidateRooms() {
        return candidateRooms;
    }

    public void setCandidateRooms(List<String> candidateRooms) {
        this.candidateRooms = candidateRooms;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWifiAp() {
        return wifiAp;
    }

    public void setWifiAp(String wifiAp) {
        this.wifiAp = wifiAp;
    }

    public List<Double> getRoomAffinity() {
        return roomAffinity;
    }

    public void setRoomAffinity(List<Double> roomAffinity) {
        this.roomAffinity = roomAffinity;
    }

    public List<Double> getDeviceAffinity() {
        return deviceAffinity;
    }

    public void setDeviceAffinity(List<Double> deviceAffinity) {
        this.deviceAffinity = deviceAffinity;
    }
}
