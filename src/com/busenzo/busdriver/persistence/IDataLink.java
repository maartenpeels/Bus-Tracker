/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.busdriver.persistence;

import com.busenzo.domein.Melding;
import com.busenzo.domein.Rit;
import java.util.List;

/**
 *
 * @author Alex
 */
public interface IDataLink{
    /**
     * verstuur een melding naar een beheerder
     * @param melding de te versturen melding
     * @return true if the notification has been send, otherwise false
     * @throws java.lang.Exception generic error 
     */
    public boolean sendMessage(Melding melding) throws Exception;
    
    /**
     * recieve a message from the controller
     * @return de ontvangen melding
     */
    public Melding recieveMessage();
    
    /**
     * recieve a route which this bus will follow
     * @return De te rijden rit
     */
    public Rit setRoute();
    
    /**
     * recieve the types of notifications a busdriver can send
     * @return an arraylist of notification types
     */
    public List<String> getNotificationTypes();

    /**
     * Recieve a list of notifications for this bus
     * @return a list of notifications for this bus
     * @throws Exception generic error
     */
    public List<Melding> getNewNotifications() throws Exception;

}
