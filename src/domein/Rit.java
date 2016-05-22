package domein;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rit {
    
    private LocalDateTime ExpectedArrivalTime;
    private LocalDateTime TargetArrivalTime;
    private Stop lastStop;
    private Stop[] nextStops;

    /**
     * Maak een rit aan op een bepaalde lijn
     * @param verwachtteAankomstTijd De verwachtte aankomsttijd bij de eindhalte
     * @param lijn: De lijn waarbij deze rit hoort, nooit null
     */
    public Rit(LocalDateTime ExpectedArrivalTime, LocalDateTime TargetArrivalTime) {
        this.ExpectedArrivalTime = ExpectedArrivalTime;
        this.TargetArrivalTime = TargetArrivalTime;
    }

    public void setArrivalTime(LocalDateTime time)
    {
        this.TargetArrivalTime = time;
    }
    
    public void setExpectedArrivalTime(LocalDateTime time)
    {
        this.TargetArrivalTime = time;
    }
    
    public void setLastStop(Stop s)
    {
        this.lastStop = s;
    }
    
    public void setNextStops(ArrayList<Stop> s)
    {
        nextStops = (Stop[]) s.toArray();
    }

    /**
     * Haal de verwachtte aankomsttijd bij de eindhalte op
     * @return de verwachtte aankomsttijd bij de eindhalte
     */
    public LocalDateTime getExpectedArrivalTime() {
        return ExpectedArrivalTime;
    }

    
}