package com.busenzo.domein;

/**
 *
 * @author fhict
 */
public class Stop {
    private int timingPointCode;
    private String timingPointName;
    
    public Stop(int code, String name)
    {
        timingPointCode = code;
        timingPointName = name;
    }
    
    @Override
    public String toString()
    {
        return timingPointName;
    }
}