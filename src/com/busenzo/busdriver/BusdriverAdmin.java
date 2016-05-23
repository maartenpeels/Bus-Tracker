/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.busdriver;

import com.busenzo.busdriver.persistence.IDataLink;
import com.busenzo.busdriver.persistence.PollingDataLink;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Melding;
import com.busenzo.domein.Rit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex
 */
public class BusdriverAdmin implements Observer {

//*******************************Datavelden*********************************
    private IDataLink dk;
    private Rit currentRoute;
    private ObservableList<Melding> recievedNotifications;
    private Halte currentStop;
    private ObservableList<String> notificationTypes;
    
//*******************************Constructor********************************
    public BusdriverAdmin(){
        this.recievedNotifications = FXCollections.observableArrayList();
        this.currentRoute = null;
        this.currentStop = null;
    }
    
    public BusdriverAdmin(IDataLink dkop){
        this.dk = dkop;
        this.recievedNotifications = FXCollections.observableArrayList();
        this.notificationTypes = FXCollections.observableArrayList(/**dk.getNotificationTypes()**/);
        this.currentRoute = null;
        this.currentStop = null;
        if(dkop instanceof PollingDataLink){
            System.out.println("hallo!");
            PollingDataLink pdl = (PollingDataLink)dkop;
            pdl.addObserver(this);
        }
    }

//*******************************Propperties********************************
    public void setCurrentRoute(Rit newRoute) {
        //TODO: rit ontvangen over netwerk van beheer
        this.currentRoute = newRoute;
        this.updateRit();
    }

//*******************************Methoden***********************************
    /**
     * Verstuur een melding naar het beheer (ontvanger -1), vanaf deze zender (busnummer) met een beschrijving
     * @param beschrijving: het daadwerkelijke bericht
     * @return De melding als deze succesvol aangemaakt en verzonden is, anders null
     */
    public Melding verstuurMelding(String beschrijving){
        String busNummer = Integer.toString(this.currentRoute.getBus().getNummer());
        Melding m = new Melding(0, beschrijving, busNummer, "", LocalDateTime.now());
        //TODO: zorgen dat melding verstuurd wordt
        return m;
    }

    /**
     * Melding ontvangen vanaf het beheer
     * @param m de ontvangen melding
     */
    public void ontvangMelding(Melding m){
        //TODO: melding ontvangen van beheer via netwerk(Gebruik functie in Administratie)
        this.recievedNotifications.add(m);
    }
    
    /**
     * Vraag een lijst op van alle ontvangen meldingen
     * @return 
     */
    public List<Melding> getAllMeldingen(){
        return Collections.unmodifiableList(recievedNotifications);
    }
    
    /**
     * Vraag de laatst binnengekregen melding op
     * @return De laatst binnengekregen melding, of null als er nog geen meldingen voor deze bus zijn
     */
    public Melding getLatestMelding(){
        Melding output = null;
        for(Melding m : this.recievedNotifications){
            if(output == null){
                output = m;
            }
            else{
                if(m.getTijdstip().isAfter(output.getTijdstip())){
                    output = m;
                }
            }
        }
        return null;
    }
    
    /**
     * Vraag alle actieve meldingen op voor deze bus
     * @return een lijst met alle actieve meldingen, kan leeg zijn
     */
    public ArrayList<Melding> getActieveMeldingen(){
        ArrayList<Melding> output = new ArrayList<>();
        for(Melding m : this.recievedNotifications){
            if(m.getActief()){
                output.add(m);
            }
        }
        return output;
    }
    
    /**
     * Vraag de namen van de haltes op waar deze bus nog moet stoppen
     * @return een lijst met haltenamen waar deze bus nog moet stoppen, kan leeg zijn.
     */
    public ObservableList<String> getVolgendeHaltes(){
        ObservableList<String> output = FXCollections.observableArrayList();
//        for(String s : this.currentRoute.getLijn().getHalteNamen().subList(this.currentRoute.getLijn().getHalteNamen().indexOf(this.currentStop.getNaam()), this.currentRoute.getLijn().getHalteNamen().size()-1)){
//            output.add(s);
//        }
        output.addAll(this.currentRoute.getLijn().getHalteNamen());
        return output;
    }
    
    /**
     * Vraag de verwachtte aankomsttijd bij de eindhalte op
     * @return een string met de verwachtte aankomsttijd bij de eindhalte. format: hh:mm (+x), waarbij +x 
     * de eventuele vertraging is in minuten
     */
    public String getVerwachtteEindAankomsttijd(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime dateTime = this.currentRoute.getVerwachteAankomstTijd();
        String output = dateTime.format(formatter);
        //TODO: kijken naar vertraging
        return output;
    }
    
    public String getLijnNummer(){
        return this.currentRoute.getLijn().getId();
    }
    
    public ObservableList<String> getNotificationTypes(){
        return this.notificationTypes;
    }
    
    public boolean isLineSet(){
        return this.currentRoute != null;
    }
    
    /**
     * wordt periodiek aangeroepen, zorgt ervoor dat bijgehouden wordt wat de huidige halte is.
     */
    public void updateRit(){
        
    }

    @Override
    public void update(Observable o, Object arg) {
        this.recievedNotifications.addAll((List<Melding>)arg);
        System.out.println(this.recievedNotifications.size());
    }
}
