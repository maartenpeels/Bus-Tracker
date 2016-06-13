package com.busenzo.administratie;

import java.util.ArrayList;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Melding;
import com.busenzo.rmi.IMessageClient;
import com.busenzo.rmi.IMessageService;
import com.busenzo.rmi.MessageServer;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Administratie extends Observable implements IMessageService{

    private ArrayList<Lijn> lijnen;
    private ArrayList<Halte> haltes;
    private ArrayList<Melding> meldingen;
    private DatabaseKoppeling dbKoppeling;
    
    private HashMap<String, IMessageClient> connections;

    /**
     * Construct a new administration. All lists should be initialized and a databaselink should be setup
     * with the server and key coming from a properties file
     */
    public Administratie() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);

        this.lijnen = new ArrayList<>();
        this.haltes = new ArrayList<>();
        this.meldingen = new ArrayList<>();
        
        GetPropertyValues properties = new GetPropertyValues();
        
        String[] props = new String[2];
        try {
            props = properties.getPropValues();
        } catch (IOException ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.dbKoppeling = new DatabaseKoppeling(props[0], props[1]);
        
        new MessageServer(this);
        connections = new HashMap<>();
    }
    
    public List<Melding> getMeldingen() {
        return meldingen;
    }

    /**
     * Load all available data (busstops, lines, routedata and notifications) from the database. All data is
     * added to the lists in this class.
     */
    public void laadDataIn() {
        try {
            this.haltes.addAll(dbKoppeling.getHalteData());
            this.lijnen.addAll(dbKoppeling.getLineData(haltes));
            this.dbKoppeling.getRouteData(lijnen);
            this.meldingen.addAll(this.dbKoppeling.getMeldingen());
        } catch (Exception ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update the locations of all the currently active busses. These are returnt from the database
     */
    public void haalBusLocaties() {
        this.lijnen.clear();
        try {
            this.lijnen.addAll(dbKoppeling.getLineData(haltes));
            this.dbKoppeling.getRouteData(lijnen);
        } catch (Exception ex) {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get a list of all busstops a specific line stops at
     * @param nummer: the linenumber of which you want to know the stops
     * @return a list of all busstops this line stops at. Can be empty if the number isn't found
     */
    public List<Halte> geefLijnInformatie(int nummer) 
    {
        Lijn lijn = null;
        List<Halte> lijnHaltes = new ArrayList();
        for(Lijn l : lijnen){
            if(l.getNummer() == nummer){
                lijn = l;
            }
        }
        if(lijn != null){
            ArrayList<String> namen = lijn.getHalteNamen();
            System.out.println(namen);
            
            for(String s : namen){
                for(Halte a: haltes){
                    if(a.getNaam().equals(s)){
                        lijnHaltes.add(a);
                        break;
                    }
                }
            }
        }
        return lijnHaltes;
    }

    /**
     * return a list of all busstops
     * @return an unmodifiable list of busstops
     */
    public List<Halte> getHaltes() {
        return Collections.unmodifiableList(haltes);
    }

    /**
     * get a list of all active busses
     * @return an unmodifiable list of line objects (which contain the busses)
     */
    public List<Lijn> getBussen() {
        return Collections.unmodifiableList(lijnen);
    }

    /**
     * search for a specific line which contains the given searchterm.
     * @param naam the term the user wants to search for
     * @return a list of all line objects which contain the searchterm in their number
     */
    public List<Lijn> zoekLijn(String naam) {
        ArrayList<Lijn> output = new ArrayList<>();
        for (Lijn l : this.lijnen) {
            String busNumber = String.valueOf(l.getNummer());
            if (busNumber.equals(naam)) {
                output.add(l);
            }
        }
        return output;
    }

    /**
     * search for all busstops with the searchterm in their name.
     * @param naam the term the user wants to search for
     * @return 
     */
    public List<Halte> zoekHalte(String naam) {
        ArrayList<Halte> output = new ArrayList<>();
        if (!naam.isEmpty()) {
            for (Halte h : this.haltes) {
                if (h.getNaam().toLowerCase().contains(naam.toLowerCase())) {
                    output.add(h);
                }
            }
        }
        return output;
    }
    
    /**
     * search for all busstops with the specified coords
     * @param coords the coords
     * @return 
     */
    public List<Halte> zoekHalte(double[] coords) {
        ArrayList<Halte> output = new ArrayList<>();
        for (Halte h : this.haltes) {
            if (h.getCoordinaten()[0] == coords[0]
                    && h.getCoordinaten()[1] == coords[1]) {
                output.add(h);
            }
        }
        return output;
    }
    
    
    /**
     * Get a list of all notifications
     * @return an unmodifiablelist of all notifications
     */
    public List<Melding> getAllNotifications() {
        return Collections.unmodifiableList(meldingen);
    }
    
    /**
     * Add a new notifications to the database
     * @param m the notification which has to be added
     * @return true if the notification was succesfully added, otherwise false
     * @throws Exception if a connection to the database cant be made
     */
    public boolean sendMessage(Melding m) throws Exception {
        IMessageClient mc = getMessageClient(m.getReceiver());

        if (mc != null) {
            return mc.addMessage(m);
        }

        return false;
    }
    
    /**
     * Get the current databaseconnection
     * @return databaselink object 
     */
    public DatabaseKoppeling getDatabaseKoppeling(){
        return this.dbKoppeling;
    }
    
    public void addHalte(Halte h){
        this.haltes.add(h);
    }
    
    public void addLijn(Lijn l){
        this.lijnen.add(l);
    }

    @Override
    public boolean addMessage(Melding message) throws RemoteException {
        System.out.println("Message: " + message.getBeschrijving() + " - from " + message.getSender());
        
        meldingen.add(message);
        
        // notify GUI
        this.setChanged();        
        this.notifyObservers();
        
        return true;
    }

    @Override
    public boolean removeMessage(Melding message) throws RemoteException {
        // TODO:
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean connect(String id, IMessageClient msc) throws RemoteException {
        if (connections.get(id) == null) {
            connections.put(id, msc);
            return true;
        }

        return false;
    }

    public IMessageClient getMessageClient(String id) {
        if (connections.get(id) != null) {
            return connections.get(id);
        }

        return null;
    }
}
