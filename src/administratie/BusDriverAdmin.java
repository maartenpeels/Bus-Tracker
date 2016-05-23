package administratie;

import java.util.ArrayList;
import domein.Rit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusDriverAdmin {

    private String restServer = "http://37.97.149.53/busenzo/api/";
    //private String restServer = "https://busenzo.stefanvlems.nl/api/";
    private String restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    private Rit rit;
    private DataLink dbKoppeling;
    
    private static final String BUS_ID = "CXX_20160523_L150_70_0";

    /**
     * Maak een nieuwe administratie aan
     */
    public BusDriverAdmin() {
        this.dbKoppeling = new DataLink(restServer, restKey);
    }

    public void laadDataIn() {
        try {
            this.rit = dbKoppeling.getNextStops(BUS_ID);
        } catch (Exception ex) {
            Logger.getLogger(BusDriverAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Rit getRit()
    {
        return rit;
    }
}