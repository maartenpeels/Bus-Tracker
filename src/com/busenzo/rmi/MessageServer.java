/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.rmi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stasiuz
 */
public class MessageServer {

    private static IMessageService ms;
    
    public MessageServer(IMessageService ms) {
        this.ms = ms;
        
        if (!startServer()) {
            System.out.println("Server could not be started");
        } else {
            System.out.println("Server started!");
        }
    }

    public static boolean startServer() {
        FileOutputStream out = null;
        try {
            String name = "messages";
            String address = java.net.InetAddress.getLocalHost().getHostAddress();
            int port = 1099;
            Properties props = new Properties();
            String rmiLink = address + ":" + port + "/" + name;
            System.out.println(rmiLink);

            props.setProperty("busenzo", rmiLink);
            out = new FileOutputStream(name + ".props");
            props.store(out, null);
            out.close();
            java.rmi.registry.LocateRegistry.createRegistry(port);
            Naming.rebind(name, ms);
            return true;

        } catch (IOException ex) {
            Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public IMessageService getMessageService() {
        return ms;
    }
}
