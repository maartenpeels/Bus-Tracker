/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.rmi;

import com.busenzo.domein.Melding;
import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author stasiuz
 */
public class MessageConnector extends UnicastRemoteObject {

    private IMessageService messageService;

    public MessageConnector() throws RemoteException {
        if (!connect()) {
            System.out.println("No connection!");
        } else {
            System.out.println("Connected with Beheerder!");

            //sendTestMessage();
        }
    }

    public boolean connect() {
        try {
            // TODO: property file uitlezen voor IP adres!
            // met hard ip address werkt het wel, goed genoeg voor nu
            
            String rmiLink = "145.93.136.95:1099/messages";
            IMessageService ms = (IMessageService) Naming.lookup("rmi://" + rmiLink);

            messageService = ms;
            return true;

        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }

    public void sendTestMessage() throws RemoteException {
        Melding m = new Melding(Integer.MIN_VALUE, "test bericht 123", "zender", "ontvanger", LocalDateTime.now());
        messageService.addMessage(m);
    }
    
    public IMessageService getMessageService() {
        return messageService;
    }
}
