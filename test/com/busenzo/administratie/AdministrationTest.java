/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Richting;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AdministrationTest {
    
    Administratie admin;
    Bus bus1, bus2, bus3;
    Halte halte1, halte2, halte3;
    Lijn lijn1, lijn2, lijn3;
    ArrayList<Bus> inputBus;
    ArrayList<Halte> inputHalte;
    ArrayList<Lijn> inputLijn;
    
    @Before
    public void setUp(){
        admin = new Administratie();
        inputBus = new ArrayList<>();
        inputHalte = new ArrayList<>();
        inputLijn = new ArrayList<>();
        bus1 = new Bus(123);
        bus2 = new Bus(456);
        bus3 = new Bus(789);
        inputBus.add(bus1);
        inputBus.add(bus2);
        inputBus.add(bus3);
        halte1 = new Halte("1", "testhalte1", "1", "1");
        halte2 = new Halte("2", "testhalte2", "2", "2");
        halte3 = new Halte("3", "testhalte3", "3", "3");
        inputHalte.add(halte1);
        inputHalte.add(halte2);
        inputHalte.add(halte3);
        lijn1 = new Lijn("1", 1, Richting.HEEN, "lijn 1");
        lijn2 = new Lijn("2", 2, Richting.HEEN, "lijn 2");
        lijn3 = new Lijn("3", 3, Richting.HEEN, "lijn 3");
        inputLijn.add(lijn1);
        inputLijn.add(lijn2);
        inputLijn.add(lijn3);
    }
    
    /**
     * Construct a new administration. All lists should be initialized and a databaselink should be setup
     * with the server and key coming from a properties file
     */
    @Test
    public void testAdministrationConstructor(){
        Assert.assertNotNull("List of lines is initialized", admin.getBussen());
        Assert.assertNotNull("List of busstops is initialized", admin.getHaltes());
        Assert.assertNotNull("List of notifications is initialized", admin.getAllMeldingen());
        Assert.assertNotNull("Database connection is initialized", admin.getDatabaseKoppeling());
    }
    
    /**
     * Load all available data (busstops, lines, routedata and notifications) from the database. All data is
     * added to the lists in this class.
    */
    @Test@Ignore
    public void testLoadAllData(){
        
    }
    
    /**
     * Update the locations of all the currently active busses. These are returned from the database
    */
    @Test@Ignore
    public void testGetBusLocations(){
        
    }
    
    /**
     * Get a list of all busstops a specific line stops at
     * param nummer: the linenumber of which you want to know the stops
     * return a list of all busstops this line stops at. Can be empty if the number isn't found
    */
    @Test@Ignore
    public void testGetLineInformation(){
        
    }
    
    /**
     * search for a specific line which contains the given searchterm.
     * param naam the term the user wants to search for
     * return a list of all line objects which contain the searchterm in their number
     */
    @Test@Ignore
    public void testSearchLine() {
        
    }
    
    /**
     * search for all busstops with the searchterm in their name.
     * param naam the term the user wants to search for
     * return 
     */
    @Test@Ignore
    public void testSearchBusstop() {
        
    }
    
    /**
     * Add a new notifications to the database
     * param m the notification which has to be added
     * return true if the notification was succesfully added, otherwise false
     * throws Exception if a connection to the database cant be made
     */
    @Test@Ignore
    public void testAddNotification (){
        
    } 
    
    
}
