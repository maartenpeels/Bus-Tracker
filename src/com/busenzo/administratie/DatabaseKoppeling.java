/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.administratie;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Richting;
import com.busenzo.domein.Rit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DatabaseKoppeling {

    private String restServer;// = "http://37.97.149.53/busenzo/api/";
    private String restKey;// = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    
    public DatabaseKoppeling(String restServer, String restKey){
        this.restServer = restServer;
        this.restKey = restKey;
    }
    
    /**
     * JSON webrequest
     * URL opgebouwd via Restserver/restkey/query
     * Geeft JSON object terug
     */
    public JSONObject getJSONfromWeb(String query) throws Exception
    {
        String getUrl = this.restServer + "/" + this.restKey + "/" + query;
        JSONParser parser = new JSONParser();
        String json = readUrl(getUrl);
        // Page page = gson.fromJson(json, Page.class);
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
    
   public ArrayList<Halte> getHalteData() throws Exception
    {
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
            Halte addHalte = new Halte(halteID, halteNaam, halteLon, halteLat);
            output.add(addHalte);
        }
        System.out.println("Added " + output.size() + " to application");
        return output;
    }
   
    public void getRouteData(List<Lijn> lijnen) throws Exception
    {
        Random ran = new Random();
        //TODO: aanpassen in lijn
        //for(Lijn l : lijnen) l.clearRitten();
        String query = "ritten";
        JSONObject rittenData = this.getJSONfromWeb(query);
        JSONArray rittenArray = (JSONArray) rittenData.get("data");
        int ride = 0;
        for (Object rittenArray1 : rittenArray) {
            JSONObject objects = (JSONObject) rittenArray1;
            String ritID = objects.get("linekey").toString();
            String verwachteAankomstTijd = objects.get("exp_arrivaltime").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime vat = LocalDateTime.parse(verwachteAankomstTijd, frm);
            String busLat = objects.get("lat").toString();
            String busLon = objects.get("lon").toString();
            for(Lijn l : lijnen){
                if(ritID.equals(l.getId())){
                    ride++;
                    //System.out.println("Added rit to line " + l.getId());
                    Rit r = new Rit(vat, l);
                    Bus b = new Bus(ran.nextInt(99999));
                    b.updateLocatie(Double.parseDouble(busLat), Double.parseDouble(busLon));
                    //Is random.. moet nog veranderd worden!1!
                    b.setHuidigeRit(r);
                    r.setBus(b);
                    l.addRit(r);
                }
            }
        }
        System.out.println("Added " + ride + " current rides to application");
    }
    
    public ArrayList<Lijn> getLineData(List<Halte> haltes) throws Exception
    {
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
            //System.out.println("I hadded a line with id " + lijnId);
            //ArrayList<Halte> tempHaltes = new ArrayList<>();
            JSONArray haltesArray = (JSONArray) objects.get("stops");
            for (Object haltesArray1 : haltesArray) {
                JSONObject objectsHalte = (JSONObject) haltesArray1;
                String stopName = objectsHalte.get("id").toString();
                for(Halte h : haltes){
                    blconnection++;
                    addLijn.addHalte(h);
                }
            }
            output.add(addLijn);
        }
        System.out.println("Added " + output.size() + " buslines to application");
        System.out.println("Added " + blconnection + " busline -> busstop connections");
        return output;
    }
}
