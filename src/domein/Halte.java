package domein;

import java.util.ArrayList;
import java.util.List;

public class Halte {
    
    private String naam;
    private double lon;
    private double lat;
    private String id;

    /**
     * Maak een nieuwe halte aan, er mag nog geen halte bestaan met dezelfde naam en lat/lon
     * @param naam: Naam van de halte, mag geen lege string zijn
     * @param lon: Longditude van de halte, mag niet null zijn
     * @param lat: Ladditude van de halte, mag niet null zijn
     */
    public Halte(String id, String naam, String lon, String lat) {
            this.naam = naam;
            this.lat = Double.parseDouble(lat);
            this.lon = Double.parseDouble(lon);
            this.id = id;
    }

    /**
     * Vraag de haltenaam op
     * @return De naam van deze halte
     */
    public String getNaam(){
        return this.naam;
    }
    
    /**
     * Vraag het id op
     * @return De id van deze halte
     */
    public String getId(){
        return this.id;
    }

    /**
     * Vraag de coordinaten op van deze halte
     * @return Een int array met hierin lon en lat
     */
    public double[] getCoordinaten(){
        double[] cords = {this.lat, this.lon};
        return cords;
    }
}