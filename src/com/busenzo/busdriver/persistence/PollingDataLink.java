/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.busdriver.persistence;

import com.busenzo.domein.Melding;
import com.busenzo.domein.Rit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Alex
 */
public class PollingDataLink extends Observable implements IDataLink {

//*******************************Datavelden*********************************
    private String restServer;
    private String restKey;
    private Timer timer;
    private final long DELAY = 2000;
    private ArrayList<Melding> notifications;

//*******************************Constructor********************************
    public PollingDataLink(String server, String key){
        this.restServer = server;
        this.restKey = key;
        this.notifications = new ArrayList<>();
        initTimer();
    }
    
    //For debug/test only!!!
    public PollingDataLink(){
        this.restServer = "http://37.97.149.53/busenzo/api/api.php?key=";
        this.restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
        this.notifications = new ArrayList<>();
        initTimer();
    }

//*******************************Propperties********************************
    public List<Melding> getNotifications(){
        return Collections.unmodifiableList(notifications);
    }

//*******************************Methoden***********************************
    @Override
    public boolean sendMessage(Melding melding) throws Exception {
        String query = "addmelding"+(melding.getZender().equals("-1") ? "" : "&from="+melding.getZender())+"&to="+melding.getOntvanger()+"&mtekst="+melding.getBeschrijving()+"&mtype=Beheerder";
        JSONObject halteData = this.getJSONfromWeb(query);
        JSONObject objects = (JSONObject) halteData;
        String status = objects.get("status").toString();
        
        return status.equals("succes"); 
    }

    @Override
    public Melding recieveMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rit setRoute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getNotificationTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Melding> getNewNotifications() throws Exception {
        ArrayList<Melding> output = new ArrayList<>();
        String query = "meldingen"; 
        JSONObject meldingenData = this.getJSONfromWeb(query);
        JSONArray meldingenArray = (JSONArray) meldingenData.get("data");
        for (Object meldingData : meldingenArray) {
            JSONObject objects = (JSONObject) meldingData;
            Integer meldingID = Integer.parseInt(objects.get("id").toString());
            String meldingTo = objects.get("to").toString();
            String meldingFrom;
            try
            {
                meldingFrom = objects.get("from").toString();
            }
            catch(Exception ex)
            {
                System.out.println("hier gaat iets fout");
                meldingFrom = "";
            }
            String meldingTekst = objects.get("message").toString();
           
            String meldingDateTime = objects.get("time").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime meldingTime = LocalDateTime.parse(meldingDateTime, frm);
            Melding addMelding = new Melding(meldingID, meldingTekst, meldingFrom, meldingTo, meldingTime);
            System.out.println(addMelding.getBeschrijving());
            output.add(addMelding);
        }
        System.out.println("Added " + output.size() + " messages to application");
        return output;
    }
    
    /**
     * JSON webrequest
     * URL opgebouwd via Restserver/restkey/query
     * Geeft JSON object terug
     * @param query
     * @return 
     * @throws java.lang.Exception
     */
    public JSONObject getJSONfromWeb(String query) throws Exception
    {
        String getUrl = restServer + restKey+ "&action=" + query;       
        JSONParser parser = new JSONParser();
        String json = readUrl(getUrl);
        Object obj = parser.parse(json);
        JSONObject jdata = (JSONObject)obj;
        return jdata;
    }
    
    /**
     * URL datareader functie
     * Geeft databuffer terug
     */
    private static String readUrl(String urlString) throws Exception 
    {
        BufferedReader reader = null;
        try 
        {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
            {
                buffer.append(chars, 0, read); 
            }

            return buffer.toString();
        } finally 
        {
            if (reader != null)
            {
                reader.close();
            }
        }

    }
    
    private void addNewNotifications(List<Melding> notifications){
        System.out.println("trying to add notifications");
        boolean hasChanged = false;
        for(Melding m : notifications){
            if(!this.notifications.contains(m)){
                this.notifications.add(m);
                hasChanged = true;
            }
        }
        if(hasChanged){
            this.setChanged();
            this.notifyObservers(this.notifications);
        }
    }
    
    private void initTimer(){
        this.timer = new Timer(true);
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                try {
                    addNewNotifications(getNotifications());
                } catch (Exception ex) {
                    Logger.getLogger(PollingDataLink.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 10, DELAY);
    }
}
