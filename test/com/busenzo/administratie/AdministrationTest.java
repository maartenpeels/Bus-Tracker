/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Richting;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.List;

public class AdministrationTest {
    
    private Administratie admin;
    private Halte h1, h2, h3;
    private Lijn l1, l2, l3;
    
    @Before
    public void setUp(){
        admin = new Administratie();
        h1 = new Halte("1", "testhalte", "3.4", "5.7");
        h2 = new Halte("2", "testhalte2", "3.4", "5.7");
        h3 = new Halte("3", "ietsanders", "3.4", "5.7");
        admin.addHalte(h1);
        admin.addHalte(h2);
        admin.addHalte(h3);
        l1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        l2 = new Lijn("101", 2, Richting.HEEN, "test lijn2");
        l3 = new Lijn("3", 2, Richting.HEEN, "test lijn3");
        admin.addLijn(l1);
        admin.addLijn(l2);
        admin.addLijn(l3);
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
    @Test
    public void testGetLineInformation(){
        l1.addHalte(h1);
        l1.addHalte(h2);
        l1.addHalte(h3);
        List<Halte> actual = admin.geefLijnInformatie(1);
        Assert.assertEquals("3 haltes in lijn 1", 3, actual.size());
        Assert.assertEquals("eerste halte is h1", h1, actual.get(0));
    }
    
    /**
     * search for a specific line which contains the given searchterm.
     * param naam the term the user wants to search for
     * return a list of all line objects which contain the searchterm in their number
     */
    @Test
    public void testSearchLine() {
        List<Lijn> actual = admin.zoekLijn("1");
        Assert.assertEquals("1 element in lijst", 1, actual.size());
        Assert.assertEquals("lijnen zijn gelijk", l1, actual.get(0));
        actual = admin.zoekLijn("2");
        Assert.assertEquals("2 lijnen gevonden", 2, actual.size());
        Assert.assertEquals("lijnen zijn gelijk", l2, actual.get(0));
        Assert.assertEquals("lijnen zijn gelijk", l3, actual.get(1));
        actual = admin.zoekLijn("dezelijnbestaatniet");
        Assert.assertTrue(actual.isEmpty());
    }
    
    /**
     * search for all busstops with the searchterm in their name.
     * param naam the term the user wants to search for
     * return 
     */
    @Test
    public void testSearchBusstop() {
        List<Halte> actual = admin.zoekHalte("anders");
        Assert.assertEquals("1 element in lijst", 1, actual.size());
        Assert.assertEquals("haltes zijn gelijk", h3, actual.get(0));
        actual = admin.zoekHalte("halte");
        Assert.assertEquals("2 elementen in lijst", 2, actual.size());
        Assert.assertEquals("haltes zijn gelijk", h1, actual.get(0));
        Assert.assertEquals("haltes zijn gelijk", h2, actual.get(1));
        actual = admin.zoekHalte("dezehaltebestaatniet");
        Assert.assertTrue(actual.isEmpty());
    }
    
    /**
     * Add a new notifications to the database
     * param m the notification which has to be added
     * return true if the notification was succesfully added, otherwise false
     * throws Exception if a connection to the database cant be made
     */
    @Test@Ignore
    public void testAddNotification(){
        
    } 
      
}
