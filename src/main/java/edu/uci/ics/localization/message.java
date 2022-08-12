package edu.uci.ics.localization;

public class message {
    private String timeStamp;
    private String DBaddress;

    public message(String timeStamp, String DBaddress){
        this.timeStamp = timeStamp;
        this.DBaddress = DBaddress;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDBaddress() {
        return DBaddress;
    }

    public void setDBaddress(String DBaddress) {
        this.DBaddress = DBaddress;
    }
}
