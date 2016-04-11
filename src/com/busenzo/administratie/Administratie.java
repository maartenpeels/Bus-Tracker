package com.busenzo.administratie;

import java.util.ArrayList;
import com.busenzo.domein.Bus;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Richting;
import com.busenzo.domein.Rit;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Administratie {
    private String restServer = "http://37.97.149.53/busenzo/api/";
    //private String restServer = "https://busenzo.stefanvlems.nl/api/";
    private String restKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    public ArrayList<Bus> bussen = new ArrayList<>();
    public ArrayList<Lijn> lijnen = new ArrayList<>();
    private ArrayList<Halte> haltes = new ArrayList<>();
    
    
    /**
     * JSON webrequest
     * URL opgebouwd via Restserver/restkey/query
     * Geeft JSON object terug
     */
    public JSONObject getJSONfromWeb(String query) throws Exception
    {
        //String getUrl = this.restServer + "/" + this.restKey + "/" + query;
        String getUrl = "http://37.97.149.53/busenzo/api/api.php?key=" + restKey+ "&action=" + query;
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
    
    private Halte findStop(String id)
    {
        for (Halte h : this.haltes) {
            if(h.getId().equals(id))
            {
                return h;
            }
        }
        return null;
    }
    private Lijn findLijn(String id)
    {
        for (Lijn l : this.lijnen) {
            if(l.getId().equals(id))
            {
                return l;
            }
        }
        return null;
    }

    /**
     * Maak een nieuwe administratie aan
     */
    public Administratie() 
    {
            //throw new UnsupportedOperationException();
    }

    /**
     * Zoek naar een specifieke bus
     * @param id: ID (chassisnummer) van de bus
     * @return De bus met het ingevoerde id, null als deze niet bestaat
     */
    public Bus zoekBus(int id) 
    {
            throw new UnsupportedOperationException();
    }

    /**
     * Update de locaties van alle bussen (deze worden opgehaald uit de database)
     */
    public void HaalBusLocaties() 
    {
            throw new UnsupportedOperationException();
    }

    /**
     * Haal alle lijnen op die bij een specifieke halte stoppen
     * @param halteNaam: De haltenaam waarvan je de lijnen wil weten, mag geen lege string zijn.
     * @return Een lijst van alle lijnen die bij deze halte stoppen, kan een lege lijst zijn als de halte 
     * niet gevonden wordt
     */
    public List<Lijn> GeefHalteInformatie(String halteNaam) 
    {
            throw new UnsupportedOperationException();
    }

    /**
     * Haal de haltes op van een specifieke lijn
     * @param nummer: Het lijnnummer waarvan je de haltes wil weten.
     * @return Een lijst van haltes waar deze lijn stopt, kan leeg zijn als het lijnnummer niet gevonden wordt
     */
    public List<Halte> GeefLijnInformatie(String nummer) 
    {
            throw new UnsupportedOperationException();
    }

    /**
     * Voeg een nieuwe bus toe aan de administratie
     * @param bus: De toe te voegen bus, er mag nog geen bus bestaan met hetzelfde chassisnummer 
     */
    public void addBus(Bus bus)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere nieuwe bussen toe aan de administratie
     * @param bussen: Een lijst met bussen die toegevoegd worden. Bussen worden alleen toegevoegd als er nog
     * geen bus bestaat met dat specifieke chassisnummer
     */
    public void addBussen(List<Bus> bussen)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Verwijder een specifieke bus uit de administratie
     * @param bus De te verwijderen bus
     */
    public void removeBus(Bus bus)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een halte toe aan de administratie
     * @param halte De toe te voegen halte, deze wordt alleen toegevoegd als deze halte nog niet bestaat
     */
    public void addHalte(Halte halte)
    {
        this.haltes.add(halte);
    }

    /**
     * Voeg meerdere haltes toe aan de administratie
     * @param haltes Een lijst van toe te voegen haltes, haltes worden alleen toegevoegd als deze nog niet 
     * bestaan
     */
    public void addHaltes(List<Halte> haltes)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg een specifieke lijn toe aan de administratie
     * @param lijn De toe te voegen lijn. Deze wordt alleen toegevoegd als deze lijn nog niet bestaat.
     */
    public void addLijn(Lijn lijn)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Voeg meerdere lijnene toe aan de administratie
     * @param lijnen De toe te voegen lijnen. Lijnen worden alleen toegevoegd als ze nog niet bestaan
     */
    public void addLijnen(List<Lijn> lijnen)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * haal de lijst van haltes op
     * @return een onwijzigbare lijst van haltes
     */
    public ArrayList<Halte> getHaltes() 
    {
        return this.haltes;
    }
    public ArrayList<Lijn> getBussen() 
    {
        return this.lijnen;
    }
    public void getHalteData() throws Exception
    {
        String query = "stops";
        JSONObject halteData = this.getJSONfromWeb(query);
        JSONArray halteArray = (JSONArray) halteData.get("data");
        for(int i = 0; i < halteArray.size(); i++)
        {
            JSONObject objects = (JSONObject)halteArray.get(i);
            String halteID = objects.get("id").toString();
            String halteNaam = objects.get("name").toString();
            String halteLat = objects.get("lat").toString();
            String halteLon = objects.get("lon").toString();
            Halte addHalte = new Halte(halteID, halteNaam, halteLon, halteLat);
            this.haltes.add(addHalte);
        }
        System.out.println("Added " + this.haltes.size() + " to application");
    }
    public void getRouteData() throws Exception
    {
        Random ran = new Random();
        for(Lijn l : this.lijnen) l.ritten.clear();
        String query = "ritten";
        JSONObject rittenData = this.getJSONfromWeb(query);
        JSONArray rittenArray = (JSONArray) rittenData.get("data");
        int ride = 0;
        for(int i = 0; i < rittenArray.size(); i++)
        {
            JSONObject objects = (JSONObject)rittenArray.get(i);
            String ritID = objects.get("linekey").toString();
            String verwachteAankomstTijd = objects.get("exp_arrivaltime").toString();
            DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime vat = LocalDateTime.parse(verwachteAankomstTijd, frm);
            String busLat = objects.get("lat").toString();
            String busLon = objects.get("lon").toString();
            Lijn l = this.findLijn(ritID);
            if(l != null)
            {
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
        System.out.println("Added " + ride + " current rides to application");
    }
    public void getLineData() throws Exception
    {
        String query = "lijnen";
        JSONObject lijnenData = this.getJSONfromWeb(query);
        JSONArray lijnenArray = (JSONArray) lijnenData.get("data");
        int blconnection = 0;
        for(int i = 0; i < lijnenArray.size(); i++)
        {
                JSONObject objects = (JSONObject)lijnenArray.get(i);
                String lijnId = objects.get("id").toString();
                int lijnNummer = Integer.parseInt(objects.get("linenum").toString());
                Richting direction = objects.get("direction").toString() == "0" ? Richting.HEEN : Richting.TERUG;
                String beschrijving = objects.get("name").toString();//direction, halte->id
                Lijn addLijn = new Lijn(lijnId, lijnNummer, direction, beschrijving);
                //System.out.println("I hadded a line with id " + lijnId);
                //ArrayList<Halte> tempHaltes = new ArrayList<>();
                JSONArray haltesArray = (JSONArray) objects.get("stops");
                for(int x = 0; x < haltesArray.size(); x++)
                {
                    JSONObject objectsHalte = (JSONObject)haltesArray.get(x);
                    String stopName = objectsHalte.get("id").toString();
                    Halte h = findStop(stopName);
                    if(h != null)
                    {
                        blconnection++;
                        addLijn.addHalte(h);
                    }
                }
              
                
                this.lijnen.add(addLijn);
        }
        System.out.println("Added " + this.lijnen.size() + " buslines to application");
        System.out.println("Added " + blconnection + " busline -> busstop connections");
    }
}