package com.busenzo.domein;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rit {
    
    private LocalDateTime expectedArrivalTime;
    private ArrayList<Stop> nextStops;

    public Rit(LocalDateTime expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }
    
    public void setExpectedArrivalTime(LocalDateTime time)
    {
        this.expectedArrivalTime = time;
    }
    
    public void setNextStops(ArrayList<Stop> s)
    {
        nextStops = s;
    }
    
    public ArrayList<String> getNextStops()
    {
        ArrayList<String> resp = new ArrayList<>();
        
        for(Stop st : nextStops)
        {
            resp.add(st.toString());
        }
        
        return resp;
    }
    
    public LocalDateTime getArrivalTime()
    {
        return expectedArrivalTime;
    }
}