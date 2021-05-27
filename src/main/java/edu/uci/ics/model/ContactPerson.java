package edu.uci.ics.model;

import java.util.*;
public class ContactPerson {
    String userID;
    List<Interval> contactIntervals = new ArrayList<>();
    boolean isContact = false;//if is truly contact
    long contactTime;

    public long getContactTime() {
        return contactTime;
    }

    public void setContactTime(long contactTime) {
        this.contactTime = contactTime;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact() {
        isContact = true;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Interval> getContactIntervals() {
        return contactIntervals;
    }

    public void setContactIntervals(List<Interval> contactIntervals) {
        this.contactIntervals = contactIntervals;
    }
}

