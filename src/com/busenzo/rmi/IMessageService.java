/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.rmi;

import java.rmi.RemoteException;

/**
 *
 * @author Stan Guldemond
 */
public interface IMessageService extends IMessageClient {
    /**
     * 
     * @param id is the 'ritnummer' of the bus signed in to a 'chauffeursscherm'
     * @param msc is the stub which is created at the 'chauffeursscherm'
     * @return true if the stub can be added and the 'ritnummer' is not yet added, false if can't be added
     * @throws RemoteException because method if called upon remote
     */
    public boolean connect(String id, IMessageClient msc) throws RemoteException;
}
