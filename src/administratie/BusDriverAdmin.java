package administratie;

import com.busenzo.domein.Melding;
import java.util.ArrayList;
import com.busenzo.domein.Rit;
import com.busenzo.rmi.IMessageClient;
import com.busenzo.rmi.IMessageService;
import com.busenzo.rmi.MessageConnector;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusDriverAdmin extends Observable implements ILogin, IMessageClient {

    private String restServer = "http://37.97.149.53/busenzo/api/";
    //private String restServer = "https://busenzo.stefanvlems.nl/api/";
    private String restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    private Rit rit;
    private DataLink dbKoppeling;
    
    private String BUS_ID = "CXX_20160523_L150_70_0";
    
    private IMessageService messageService;
    private List<Melding> messages;


    /**
     * Maak een nieuwe administratie aan
     */
    public BusDriverAdmin() throws RemoteException, IOException {
        UnicastRemoteObject.exportObject(this, 0);
        
        GetPropertyValues properties = new GetPropertyValues();
        
        String[] props = new String[2];
         
        try {
           props = properties.getPropValues();
        } catch (IOException ex) {
            Logger.getLogger(BusDriverAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.dbKoppeling = new DataLink(props[0], props[1]);
        String IP =  this.dbKoppeling.getConfig("master_ip");
        messages = new ArrayList<>();
           
       
        
        try {
            MessageConnector messageConnector = new MessageConnector(IP);
            messageService = messageConnector.getMessageService();
         
        } catch (RemoteException ex) {
            Logger.getLogger(BusDriverAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } 
     
        System.out.println(props[0] + props[1]);
        
        
    }
    
    @Override
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
    
    public void setRit(String newRit) throws RemoteException {
        this.BUS_ID = newRit;
        
        connectClient();
    }
    
    public boolean sendMelding(String meldingText) throws Exception
    {
        Melding m = new Melding(0, meldingText, this.BUS_ID, "", LocalDateTime.now());
        return messageService.addMessage(m);
    }
    
    public List<Melding> getMeldingen() throws Exception
    {
        return messages;
    }
    
    public boolean connectClient() throws RemoteException {
        System.out.println(getRitID());
        return messageService.connect(getRitID(), this);
    }
    
    public Rit getRit()
    {
        return rit;
    }
    
    public String getRitID()
    {
        return this.BUS_ID;
    }

    @Override
    public boolean addMessage(Melding message) throws RemoteException {
        System.out.println(message.getBeschrijving());

        messages.add(message);

        // notify GUI to show message
        this.setChanged();
        this.notifyObservers();
        

        return true;
    }

    @Override
    public boolean removeMessage(Melding message) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}