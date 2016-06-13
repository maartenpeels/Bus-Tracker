/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.rmi;

import com.busenzo.domein.Melding;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Stan Guldemond
 */
public interface IMessageClient extends Remote {
    /**
     * 
     * @param message is the message to be send
     * @return true if the message can be send, false if it couldn't
     * @throws RemoteException because method if called upon remote
     */
    public boolean addMessage(Melding message) throws RemoteException;
        
    /**
     * 
     * @param message is the messsage to be removed
     * @return true if the message can be removed, false if it couldn't
     * @throws RemoteException because method if called upon remote
     */
    public boolean removeMessage(Melding message) throws RemoteException;
}
