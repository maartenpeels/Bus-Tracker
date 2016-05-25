package administratie;

import domein.Melding;
import java.util.ArrayList;
import domein.Rit;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusDriverAdmin {

    private String restServer = "http://37.97.149.53/busenzo/api/";
    //private String restServer = "https://busenzo.stefanvlems.nl/api/";
    private String restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    private Rit rit;
    private DataLink dbKoppeling;
    
    private String BUS_ID = "CXX_20160523_L150_70_0";

    /**
     * Maak een nieuwe administratie aan
     */
    public BusDriverAdmin() {
        this.dbKoppeling = new DataLink(restServer, restKey);
    }
    public ArrayList<String> getRitbyName(String searchString) throws Exception
    {
        return this.dbKoppeling.getRittenbyBusID(searchString);
    }
    public void laadDataIn() {
        try {
            this.rit = dbKoppeling.getNextStops(BUS_ID);
        } catch (Exception ex) {
            Logger.getLogger(BusDriverAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setRit(String newRit)
    {
        this.BUS_ID = newRit;
    }
    public boolean sendMelding(String meldingText) throws Exception
    {
        Melding m = new Melding(0, meldingText, this.BUS_ID, "", LocalDateTime.now());
        boolean result = this.dbKoppeling.addMelding(m);
        //System.out.print(result);
        return result;
    }
    public ArrayList<Melding> getMeldingen() throws Exception
    {
        return this.dbKoppeling.getMeldingen(this.BUS_ID);
    }
    public Rit getRit()
    {
        return rit;
    }
    public String getRitID()
    {
        return this.BUS_ID;
    }
}