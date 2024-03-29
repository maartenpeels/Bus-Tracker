/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stan Guldemond
 */
public class RitTest {
    
    private Lijn testLijn = new Lijn("1", 1, Richting.HEEN, "test lijn");
    private LocalDateTime testLDT = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0);
    
    @Test
    public void testRitConstructor() {        
        // rit met geldige lijn
        Rit rit1 = new Rit(testLDT, testLijn, "1");
        
        assertEquals(testLijn, rit1.getLijn());
        assertEquals(testLDT, rit1.getVerwachteAankomstTijd());
    }
    
    @Test
    public void testSetGetBus() {
        Rit rit1 = new Rit(testLDT, testLijn, "1");
        Bus bus1 = new Bus(1);
        
        // get bus van rit zonder bus toe te voegen
        assertEquals(null, rit1.getBus());
        
        // get bus van rit na toevoegen van bus
        rit1.setBus(bus1);
        assertEquals(bus1, rit1.getBus());
    }
    
    @Test
    public void testGetLijn() {
        // rit met geldige lijn
        Rit rit1 = new Rit(testLDT, testLijn, "1");
        
        assertEquals(testLijn, rit1.getLijn());
        
        // rit zonder lijn
        Rit rit2 = new Rit(testLDT, null, "1");
        
        assertEquals(null, rit2.getLijn());
    }
    
    @Test
    public void testGetVerwachteAankomstTijd() {
        Rit rit1 = new Rit(testLDT, testLijn, "1");
        
        assertEquals(testLDT, rit1.getVerwachteAankomstTijd());
    }
    
    @Test
    public void testSetGetAankomstTijd() {
        Rit rit1 = new Rit(testLDT, testLijn, "1");
        
        LocalDateTime ldt = LocalDateTime.of(2000, Month.JANUARY, 1, 14, 0);
        rit1.setAankomstTijd(ldt);
        
        assertEquals(ldt, rit1.getAankomstTijd());
    }
    
    

}
