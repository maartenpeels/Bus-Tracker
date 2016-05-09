/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.chauffeur.persistence;

import com.busenzo.domein.Melding;
import com.busenzo.domein.Rit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public interface IDataKoppeling {
    /**
     * verstuur een melding naar een beheerder
     * @param melding de te versturen melding
     */
    public void sendMessage(Melding melding);
    
    /**
     * ontvang een melding van een beheerder
     * @return de ontvangen melding
     */
    public Melding recieveMessage();
    
    /**
     * ontvang informatie over de te rijden rit
     * @return De te rijden rit
     */
    public Rit setRit();
    
    /**
     * recieve the types of notifications a busdriver can send
     * @return an arraylist of notification types
     */
    public List<String> getNotificationTypes();
}
