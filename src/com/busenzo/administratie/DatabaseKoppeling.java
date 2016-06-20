/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Melding;
import com.busenzo.domein.Richting;
import com.busenzo.domein.Rit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DatabaseKoppeling {

    private final String restServer;
    private final String restKey;

    public DatabaseKoppeling(String restServer, String restKey) {
        this.restServer = restServer;
        this.restKey = restKey;
    }

    /**
     * JSON webrequest URL opgebouwd via Restserver/restkey/query Geeft JSON
     * object terug
     */
    private JSONObject getJSONfromWeb(String query) throws Exception {
        //String getUrl = this.restServer + "/" + this.restKey + "/" + query;
        String getUrl = restServer + "api.php?key=" + restKey + "&action=" + query;

        JSONParser parser = new JSONParser();
        String json = readUrl(getUrl);
        // Page page = gson.fromJson(json, Page.class);
        Object obj = parser.parse(json);
        JSONObject jdata = (JSONObject) obj;
        return jdata;
    }

    /**
     * URL datareader functie Geeft databuffer terug
     */
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

     /**
     * changes active status of halte h
     * @param h Current halte
     * @param active integer 1 (active) or 0 (inactive)
     * @throws Exception
     */
    public void changeHalteStatus(Halte h, int active) throws Exception{
        String query = "changehalte&id=" + h.getId() + "&value=" + active;
        getJSONfromWeb(query);
    }
    
    /**
     * Get a list of all busstops from the database
     *
     * @return an arraylist of all busstops
     * @throws Exception
     */
    public ArrayList<Halte> getHalteData() throws Exception {
        ArrayList<Halte> output = new ArrayList<>();
        String query = "stops";
        JSONObject halteData = this.getJSONfromWeb(query);
        JSONArray halteArray = (JSONArray) halteData.get("data");
        for (Object halteArray1 : halteArray) {
            JSONObject objects = (JSONObject) halteArray1;
            String halteID = objects.get("id").toString();
            String halteNaam = objects.get("name").toString();
            String halteLat = objects.get("lat").toString();
            String halteLon = objects.get("lon").toString();
            String active = objects.get("active").toString();
            Halte addHalte = new Halte(halteID, halteNaam, halteLon, halteLat);
            if(active.equals("0")) addHalte.active = false;
            output.add(addHalte);
        }
        System.out.println("Added " + output.size() + " to application");
        return output;
    }

    /**
     * Get the route data for all currently known lines
     *
     * @param lijnen a list of currently known lines
     * @throws Exception
     */
    public void getRouteData(List<Lijn> lijnen) throws Exception {
        Random ran = new Random();
        //TODO: aanpassen in lijn
        lijnen.stream().forEach((l) -> {
            l.clearRitten();
        });
        String query = "ritten";
        JSONObject rittenData = this.getJSONfromWeb(query);
        JSONArray rittenArray = (JSONArray) rittenData.get("data");
        int ride = 0;
        for (Object rittenArray1 : rittenArray) {
            JSONObject objects = (JSONObject) rittenArray1;
            String ritID = objects.get("linekey").toString();
            String verwachteAankomstTijd = objects.get("exp_arrivaltime").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime vat;
            try {
                vat = LocalDateTime.parse(verwachteAankomstTijd, frm);
            } catch (Exception ex) {
                vat = LocalDateTime.now();
            }
            String busLat = objects.get("lat").toString();
            String busLon = objects.get("lon").toString();
            String busid = objects.get("id").toString();
            for (Lijn l : lijnen) {
                if (ritID.equals(l.getId())) {
                    ride++;
                    //Logger.getGlobal().log(Level.INFO, "Added rit to line " + l.getId());
                    Rit r = new Rit(vat, l, busid);
                    Bus b = new Bus(ran.nextInt(99999));
                    b.updateLocatie(Double.parseDouble(busLat), Double.parseDouble(busLon));
                    //Is random.. moet nog veranderd worden!1!
                    b.setHuidigeRit(r);
                    r.setBus(b);
                    l.addRit(r);
                }
            }
        }
        Logger.getGlobal().log(Level.INFO, "Added {0} current rides to application", ride);
    }
     /**
     * gets ip address of active starting with local subnet identifier
     * pushes ip to database trough setConfig function
     * 
     */
    public void setIP()
    {
        try
        {
            InetAddress IP = InetAddress.getLocalHost();
            String localIP = IP.getHostAddress();
            if(localIP.startsWith("192") || localIP.startsWith("172") || localIP.startsWith("10") || localIP.startsWith("127") || localIP.length() == 0|| localIP.startsWith("145"))
            {
                if(setConfig("master_ip", localIP))
                {
                    Logger.getGlobal().log(Level.INFO, "Sent local IP: {0} to remote host", localIP);
                }
                else
                {
                    Logger.getGlobal().log(Level.WARNING, "Error setting IP to {0}: ", localIP);
                }
            }
            else
            {
                Logger.getGlobal().log(Level.WARNING, "Error detecting local IP, are your network interfaces enabled?");
            }
        }
        catch(Exception e)
        {
             Logger.getGlobal().log(Level.WARNING, "Error broadcasting local ip, are you connected to a network?");
        }
    }
    /**
     * sets config item with specific key to value
     * @param key containing config item name
     * @param value containing new value for item with key
     * @return true if configur
     */
    public boolean setConfig(String key, String value) 
    {
         String query = "set&name="+key+"&value="+URLEncoder.encode(value);
         try
         {
             JSONObject respObj = this.getJSONfromWeb(query);
             return respObj.get("status").toString().equals("success");
         }
         catch (Exception e)
         {
             return false;
         }
    }
    /**
     * return a list of all line data from the database
     *
     * @param haltes a list of all busstops for which line data has to be pulled
     * @return an arraylist of line objects for the given busstops
     * @throws Exception
     */
    public ArrayList<Lijn> getLineData(List<Halte> haltes) throws Exception {
        ArrayList<Lijn> output = new ArrayList<>();
        String query = "lijnen";
        JSONObject lijnenData = this.getJSONfromWeb(query);
        JSONArray lijnenArray = (JSONArray) lijnenData.get("data");
        int blconnection = 0;
        for (Object lijnenArray1 : lijnenArray) {
            JSONObject objects = (JSONObject) lijnenArray1;
            String lijnId = objects.get("id").toString();
            int lijnNummer = Integer.parseInt(objects.get("linenum").toString());
            Richting direction = objects.get("direction").toString().equals("0") ? Richting.HEEN : Richting.TERUG;
            String beschrijving = objects.get("name").toString();//direction, halte->id
            Lijn addLijn = new Lijn(lijnId, lijnNummer, direction, beschrijving);
            //Logger.getGlobal().log(Level.INFO, "I hadded a line with id " + lijnId);
            //ArrayList<Halte> tempHaltes = new ArrayList<>();
            JSONArray haltesArray = (JSONArray) objects.get("stops");
            for (Object haltesArray1 : haltesArray) {
                JSONObject objectsHalte = (JSONObject) haltesArray1;
                String stopName = objectsHalte.get("id").toString();
                for (Halte h : haltes) {
                    if (h.getId().equals(stopName)) {
                        blconnection++;
                        addLijn.addHalte(h);
                    }
                }
            }
            output.add(addLijn);
        }
        Logger.getGlobal().log(Level.INFO, "Added {0} buslines to application", output.size());
        Logger.getGlobal().log(Level.INFO, "Added {0} busline -> busstop connections", blconnection);
        return output;
    }

    /**
     * add a notification to the database
     *
     * @param m the notification which has to be added
     * @return result true if the notification is succesfully added, otherwise
     * false
     * @throws Exception URLEncoder toegevoegd om speciale characters in het
     * tekstveld te converteren
     */
    public boolean addMelding(Melding m) throws Exception {

        String query = "addmelding" + ("-1".equals(m.getSender()) ? "" : "&from=" + m.getSender()) + "&to=" + m.getReceiver() + "&mtekst=" + URLEncoder.encode(m.getBeschrijving()) + "&mtype=Beheerder";
        Logger.getGlobal().log(Level.INFO, query);
        JSONObject halteData = this.getJSONfromWeb(query);
        JSONObject objects = (JSONObject) halteData;
        String status = objects.get("status").toString();

        return "succes".equals(status);
    }

    /**
     * get a list of all notifications from the database
     *
     * @return an ArrayList of all notifications present in the database
     * @throws Exception
     */
    public ArrayList<Melding> getMeldingen() throws Exception {
        ArrayList<Melding> output = new ArrayList<>();
        String query = "meldingen";
        JSONObject meldingenData = this.getJSONfromWeb(query);
        if (!meldingenData.get("status").toString().equals("success")) {
                return new ArrayList<>();
        }
        JSONArray meldingenArray = (JSONArray) meldingenData.get("data");
        for (Object meldingData : meldingenArray) {
            JSONObject objects = (JSONObject) meldingData;
            //Logger.getGlobal().log(Level.INFO, meldingData.toString());
            Integer meldingID = Integer.parseInt(objects.get("id").toString());
            String meldingTo = objects.get("to").toString();
            String meldingFrom;
            try {
                meldingFrom = objects.get("from").toString();
            } catch (Exception ex) {
                meldingFrom = "";
            }
            String meldingTekst = objects.get("message").toString();

            String meldingDateTime = objects.get("time").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime meldingTime = LocalDateTime.parse(meldingDateTime, frm);
            Melding addMelding = new Melding(meldingID, meldingTekst, meldingFrom, meldingTo, meldingTime);
            output.add(addMelding);
        }
        Logger.getGlobal().log(Level.INFO, "Added {0} messages to application", output.size());
        return output;
    }
}
