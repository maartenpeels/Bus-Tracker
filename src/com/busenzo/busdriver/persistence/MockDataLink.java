/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.busdriver.persistence;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Melding;
import com.busenzo.domein.Richting;
import com.busenzo.domein.Rit;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alex
 */
public class MockDataLink implements IDataLink {

    private ArrayList<String> notificationTypes;
    private ArrayList<Melding> notifications;
    private Rit rit;
    
    public MockDataLink(){
        this.notificationTypes = new ArrayList<>();
        this.notifications = new ArrayList<>();
        initTypes();
        initRit();
    }
    
    private void initTypes(){
        this.notificationTypes.add("Defect");
        this.notificationTypes.add("File");
        this.notificationTypes.add("Bus vol");
    }
    
    private void initRit(){
        Lijn lijn = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Halte halte1 = new Halte("1", "test halte", "51.4456651", "5.4656342");
        Halte halte2 = new Halte("2", "test halte 2", "51.4456651", "5.4656342");
        Halte halte3 = new Halte("3", "test halte 3", "51.4456651", "5.4656342");
        lijn.addHalte(halte1);
        lijn.addHalte(halte2);
        lijn.addHalte(halte3);
        LocalDateTime ldt = LocalDateTime.of(2016, Month.APRIL, 19, 12, 06);
        this.rit = new Rit(ldt, lijn);
        rit.setBus(new Bus(123));
    }
    
    @Override
    public boolean sendMessage(Melding melding) {
        this.notifications.add(melding);
        return true;
    }

    @Override
    public Melding recieveMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rit setRoute() {
        return rit;
    }

    @Override
    public List<String> getNotificationTypes() {
        return Collections.unmodifiableList(notificationTypes);
    }

    @Override
    public List<Melding> getNewNotifications() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
