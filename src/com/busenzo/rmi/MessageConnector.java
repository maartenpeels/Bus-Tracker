/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.rmi;

import administratie.BusDriverAdmin;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stasiuz
 */
public class MessageConnector extends UnicastRemoteObject {

    private IMessageService messageService;
    private final String IP;
    private final int PORT = 1099;
    private final String SERVICE = "messages";

    public MessageConnector(String IP) throws RemoteException {
        this.IP = IP;
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
            
            String rmiLink = IP + ":" + PORT + "/" + SERVICE;
            Logger.getLogger(BusDriverAdmin.class.getName()).log( Level.INFO, "RMI connection string: {0}", rmiLink);
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
