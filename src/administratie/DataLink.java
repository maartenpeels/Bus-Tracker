package administratie;

import domein.Melding;
import domein.Rit;
import domein.Stop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLink {

    private String restServer;
    private String restKey;
    
    public DataLink(String restServer, String restKey){
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
    
   private String httpsGet(final String https_url) {
        String ret = "";

        URL url;
        try {

            
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            ret = getContent(con);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
   
    private String getContent(HttpsURLConnection con){
            if(con!=null){

            try {

               BufferedReader br = 
                    new BufferedReader(
                            new InputStreamReader(con.getInputStream()));

               String input;
               StringBuilder rtndata = new StringBuilder();		
               while ((input = br.readLine()) != null){
                  rtndata.append(input);
               }
               br.close();
               return rtndata.toString();

            } catch (IOException e) {
               e.printStackTrace();
               return "";
            }	
           }
            return "";
   }
   
   public Rit getNextStops(String busId) throws Exception
   { 
       Rit rit = null;
       ArrayList<Stop> stops = new ArrayList<>();
       String query = "getfuturestops&limit=8&id="+busId;
       
       JSONObject respObj = this.getJSONfromWeb(query);
       int count = Integer.parseInt(respObj.get("count").toString());
       
       if(count > 0)
       {
            String arv = respObj.get("arrivaltime").toString();
       
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime arrivalTime = LocalDateTime.parse(arv, formatter);

            rit = new Rit(arrivalTime);
       
            JSONArray respArr = (JSONArray) respObj.get("data");
            for (Object respData : respArr)
            {
                JSONObject respRow = (JSONObject) respData;
                int halteCode = Integer.parseInt(respRow.get("halteCode").toString());
                String halteName = respRow.get("name").toString();

                Stop s = new Stop(halteCode, halteName);
                stops.add(s);
            }
           rit.setNextStops(stops);
       }else{
           rit = new Rit(LocalDateTime.now());
           ArrayList<Stop> stps = new ArrayList<>();
           
           String msg = respObj.get("message").toString();
           stps.add(new Stop(123456, msg));
           rit.setNextStops(stps);
       }
       
       return rit;
   }
   
   public ArrayList<Rit> getActuals()
   {
       return new ArrayList<Rit>();
   }
   
   public List<Melding> getNotifications() throws Exception {
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
   
    public boolean sendMessage(Melding melding) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
//        String query = "addmelding"+(melding.getZender().equals("-1") ? "" : "&from="+melding.getZender())+"&to="+melding.getOntvanger()+"&mtekst="+melding.getBeschrijving()+"&mtype=Beheerder";
//        JSONObject halteData = this.getJSONfromWeb(query);
//        JSONObject objects = (JSONObject) halteData;
//        String status = objects.get("status").toString();
//        
//        return status.equals("succes"); 
    }
}