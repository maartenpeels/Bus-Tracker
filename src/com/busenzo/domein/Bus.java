package com.busenzo.domein;

public class Bus {
    
    private int nummer;
    private double lon;
    private double lat;
    private Rit huidigeRit;

    /**
     * Maak een nieuwe bus aan met een specifiek chassisnummer. Er mag nog geen bus bestaan met het gegeven nummer
     * @param nummer Het chassisnummer dat deze bus moet krijgen. Altijd groter als 0.
     */
    public Bus(int nummer) {
        if(nummer < 0){
            throw new IllegalArgumentException();
        }
        this.nummer = nummer;
    }

    /**
     * Update de locatie van deze bus
     * @param lon: De nieuwe longditude van deze bus
     * @param lat: De nieuwe ladditude van deze bus
     */
    public void updateLocatie(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
    }

    /**
     * Haal het chassisnummer van de bus op
     * @return Het nummer van deze bus
     */
    public int getNummer(){
        return this.nummer;
    }

    /**
     * Vraag de huidige rit van deze bus op
     * @return De huidige rit of null als deze bus niet aan een rit bezig is.
     */
    public Rit getHuidigeRit(){
        return this.huidigeRit;
    }

    /**
     * Zet de huidige rit van deze bus
     * @param rit De rit die deze bus gaat maken. Nooit null
     */
    public void setHuidigeRit(Rit rit){
        this.huidigeRit = rit;
    }
    
    /**
     * Vraag de coordinaten op van de plek waar deze bus zich momenteel bevind
     * @return Een array met hierin de lon en lat. Kan leeg zijn als de coordinaten van deze bus niet gezet zijn
     */
    public double[] getCoordinaten(){
        double[] cords = {this.lat, this.lon};
        return cords;
    }
       
}