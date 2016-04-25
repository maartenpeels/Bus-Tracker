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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alex
 */
public class ChauffeurAdmin {

//*******************************Datavelden*********************************
    private IDataKoppeling dk;
    private Rit huidigeRit;
    private ArrayList<Melding> ontvangenMeldingen;
    private Halte huidigeHalte;
    
//*******************************Constructor********************************
    public ChauffeurAdmin(){
        this.ontvangenMeldingen = new ArrayList<>();
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
        Melding m = new Melding(beschrijving, this.huidigeRit.getBus().getNummer(), -1);
        //TODO: zorgen dat melding verstuurd wordt
        return m;
    }

    /**
     * Melding ontvangen vanaf het beheer
     * @param m de ontvangen melding
     */
    public void ontvangMelding(Melding m){
        //TODO: melding ontvangen van beheer via netwerk
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
                if(m.getTijdstip().after(output.getTijdstip())){
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
    public ArrayList<String> getVolgendeHaltes(){
        ArrayList<String> output = new ArrayList<>();
        
        return output;
    }
    
    /**
     * Vraag de verwachtte aankomsttijd bij de eindhalte op
     * @return een string met de verwachtte aankomsttijd bij de eindhalte. format: hh:mm (+x), waarbij +x 
     * de eventuele vertraging is in minuten
     */
    public String getVerwachtteEindAankomsttijd(){
        return null;
    }
    
    /**
     * wordt periodiek aangeroepen, zorgt ervoor dat bijgehouden wordt wat de huidige halte is.
     */
    public void updateRit(){
        
    }
}
