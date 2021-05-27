package edu.uci.ics.localization;

import edu.uci.ics.model.*;

public class coarseLocalization {

    private long lt = 20;
    private long rt = 100;

    public String coarseLocalization(Interval interval, User user){
        return heuristicCoarse(interval, user);
    }

    public String heuristicCoarse(Interval interval, User user){
        String label = "out";
        long minutes = (interval.getEndTimeStamp().getTime() - interval.getStartTimeStamp().getTime())/(60*1000);
        if(minutes <= rt){
            if(interval.getStartAP().equals(interval.getEndAP())){
                label = interval.getStartAP();
            }
            else{
                if(user.getVisitedAps().containsKey(interval.getStartAP()) && user.getVisitedAps().containsKey(interval.getEndAP())){
                    if(user.getVisitedAps().get(interval.getStartAP()) > user.getVisitedAps().get(interval.getEndAP())){
                        label = interval.getStartAP();
                    }
                    else{
                        label = interval.getEndAP();
                    }
                }
                else if(user.getVisitedAps().containsKey(interval.getStartAP())){
                    label = interval.getStartAP();
                }
                else if(user.getVisitedAps().containsKey(interval.getEndAP())){
                    label = interval.getEndAP();
                }
                else{
                    label = interval.getStartAP();
                }
            }
        }
        return label;
    }
}
