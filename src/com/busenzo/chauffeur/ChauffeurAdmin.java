/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.chauffeur;

import com.busenzo.chauffeur.persistence.IDataKoppeling;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Melding;
import com.busenzo.domein.Rit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex
 */
public class ChauffeurAdmin {

//*******************************Datavelden*********************************
    private IDataKoppeling dk;
    private Rit huidigeRit;
    private ObservableList<Melding> ontvangenMeldingen;
    private Halte huidigeHalte;
    private ObservableList<String> notificationTypes;
    
//*******************************Constructor********************************
    public ChauffeurAdmin(){
        this.ontvangenMeldingen = FXCollections.observableArrayList();
        this.huidigeRit = null;
        this.huidigeHalte = null;
    }
    
    public ChauffeurAdmin(IDataKoppeling dkop){
        this.dk = dkop;
        this.ontvangenMeldingen = FXCollections.observableArrayList();
        this.notificationTypes = FXCollections.observableArrayList(dk.getNotificationTypes());
        this.huidigeRit = null;
        this.huidigeHalte = null;
    }

//*******************************Propperties********************************
    public void setHuidigeRit(Rit huidigeRit) {
        //TODO: rit ontvangen over netwerk van beheer
        this.huidigeRit = huidigeRit;
        this.updateRit();
    }

//*******************************Methoden***********************************
    /**
     * Verstuur een melding naar het beheer (ontvanger -1), vanaf deze zender (busnummer) met een beschrijving
     * @param beschrijving: het daadwerkelijke bericht
     * @return De melding als deze succesvol aangemaakt en verzonden is, anders null
     */
    public Melding verstuurMelding(String beschrijving){
        String busNummer = new Integer(this.huidigeRit.getBus().getNummer()).toString();
        Melding m = new Melding(beschrijving, busNummer, "", LocalDateTime.now());
        //TODO: zorgen dat melding verstuurd wordt
        return m;
    }

    /**
     * Melding ontvangen vanaf het beheer
     * @param m de ontvangen melding
     */
    public void ontvangMelding(Melding m){
        //TODO: melding ontvangen van beheer via netwerk(Gebruik functie in Administratie)
        this.ontvangenMeldingen.add(m);
    }
    
    /**
     * Vraag een lijst op van alle ontvangen meldingen
     * @return 
     */
    public List<Melding> getAllMeldingen(){
        return Collections.unmodifiableList(ontvangenMeldingen);
    }
    
    /**
     * Vraag de laatst binnengekregen melding op
     * @return De laatst binnengekregen melding, of null als er nog geen meldingen voor deze bus zijn
     */
    public Melding getLatestMelding(){
        Melding output = null;
        for(Melding m : this.ontvangenMeldingen){
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
        for(Melding m : this.ontvangenMeldingen){
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
//        for(String s : this.huidigeRit.getLijn().getHalteNamen().subList(this.huidigeRit.getLijn().getHalteNamen().indexOf(this.huidigeHalte.getNaam()), this.huidigeRit.getLijn().getHalteNamen().size()-1)){
//            output.add(s);
//        }
        output.addAll(this.huidigeRit.getLijn().getHalteNamen());
        return output;
    }
    
    /**
     * Vraag de verwachtte aankomsttijd bij de eindhalte op
     * @return een string met de verwachtte aankomsttijd bij de eindhalte. format: hh:mm (+x), waarbij +x 
     * de eventuele vertraging is in minuten
     */
    public String getVerwachtteEindAankomsttijd(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime dateTime = this.huidigeRit.getVerwachteAankomstTijd();
        String output = dateTime.format(formatter);
        //TODO: kijken naar vertraging
        return output;
    }
    
    public String getLijnNummer(){
        return this.huidigeRit.getLijn().getId();
    }
    
    public ObservableList<String> getNotificationTypes(){
        return this.notificationTypes;
    }
    
    public boolean isLineSet(){
        return this.huidigeRit != null;
    }
    
    /**
     * wordt periodiek aangeroepen, zorgt ervoor dat bijgehouden wordt wat de huidige halte is.
     */
    public void updateRit(){
        
    }
}
