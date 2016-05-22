package administratie;

import domein.Halte;
import domein.Rit;
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

public class DatabaseKoppeling {

    private String restServer;
    private String restKey;
    
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
   
   public ArrayList<Rit> getRit(int busNum) throws Exception
   {
       String bus = String.format("%03d", busNum);
       ArrayList<Rit> rit = new ArrayList<>();
       String query = "apiname";
       
       //Get rit
       //Change api to get laststop and 5 next stops, with busnum, direction, and laststop from rit
       
       return rit;
   }
}