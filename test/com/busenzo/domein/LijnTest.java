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
public class LijnTest {

    @Test
    public void testLijnConstructor() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");

        assertEquals("1", lijn1.getId());
        assertEquals(1, lijn1.getNummer());
        assertEquals(Richting.HEEN, lijn1.getRichting());
        assertEquals("test lijn", lijn1.getBeschrijving());
    }

    @Test
    public void testGetId() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        assertEquals("1", lijn1.getId());
    }

    @Test
    public void testGetNummer() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        assertEquals(1, lijn1.getNummer());
    }

    @Test
    public void testGetRichting() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        assertEquals(Richting.HEEN, lijn1.getRichting());
    }

    @Test
    public void testGetBeschrijving() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        assertEquals("test lijn", lijn1.getBeschrijving());
    }
    
    @Test
    public void testAddHalte() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Halte halte1 = new Halte("1", "test halte", "51.4456651", "5.4656342");
        Halte halte2 = new Halte("2", "test halte 2", "51.4456651", "5.4656342");
        
        // toevoegen unieke halte
        assertTrue(lijn1.addHalte(halte1));
        
        // toevoegen unieke halte2
        assertTrue(lijn1.addHalte(halte2));
        
        // toevoegen bestaande halte
        assertFalse(lijn1.addHalte(halte1));
    }
    
    @Test
    public void testAddHaltes() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Halte halte1 = new Halte("1", "test halte", "51.4456651", "5.4656342");
        Halte halte2 = new Halte("2", "test halte 2", "51.4456651", "5.4656342");
        Halte halte3 = new Halte("3", "test halte 3", "51.4456651", "5.4656342");
        
        Halte[] haltes1 = {halte1, halte2};
        Halte[] haltes2 = {halte2, halte3};
        
        // toevoegen unieke haltes 
        assertTrue(lijn1.addHaltes(haltes1));
        
        // toevoegen bestaande halte(s)
        assertFalse(lijn1.addHaltes(haltes2));
    }
    
    @Test
    public void testAddRit() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Lijn lijn2 = new Lijn("2", 2, Richting.HEEN, "test lijn 2");
        LocalDateTime ldt1 = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0);
        
        Rit rit1 = new Rit(ldt1, lijn1, "1");
        
        // toevoegen rit over correcte lijn
        assertTrue(lijn1.addRit(rit1));
        
        // toevoegen zelfde rit over correcte lijn
        assertFalse(lijn1.addRit(rit1));
        
        // toevoegen rit over verkeerde lijn
        assertFalse(lijn2.addRit(rit1));
    }
    
    @Test
    public void testHaalRitten() {
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        LocalDateTime ldt1 = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0);
        LocalDateTime ldt2 = LocalDateTime.of(2000, Month.JANUARY, 1, 13, 0);
        
        Rit rit1 = new Rit(ldt1, lijn1, "1");
        Rit rit2 = new Rit(ldt2, lijn1, "1");
                
        lijn1.addRit(rit1);
        lijn1.addRit(rit2);
        
        assertFalse(lijn1.getRitten().isEmpty());
    }
    
    @Test
    public void testClearRitten(){
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        LocalDateTime ldt1 = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0);
        LocalDateTime ldt2 = LocalDateTime.of(2000, Month.JANUARY, 1, 13, 0);
        
        Rit rit1 = new Rit(ldt1, lijn1, "1");
        Rit rit2 = new Rit(ldt2, lijn1, "1");
                
        lijn1.addRit(rit1);
        lijn1.addRit(rit2);
        
        assertFalse(lijn1.getRitten().isEmpty());
        lijn1.clearRitten();
        assertTrue(lijn1.getRitten().isEmpty());
    }
    
    @Test
    public void testGetHalteNamen(){
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Halte halte1 = new Halte("1", "test halte", "51.4456651", "5.4656342");
        Halte halte2 = new Halte("2", "test halte 2", "51.4456651", "5.4656342");
        lijn1.addHalte(halte1);
        lijn1.addHalte(halte2);
        assertEquals("2 elementen in lijst", 2, lijn1.getHalteNamen().size());
        assertEquals("namen zijn gelijk", "test halte", lijn1.getHalteNamen().get(0));
        assertEquals("namen zijn gelijk", "test halte 2", lijn1.getHalteNamen().get(1));
    }
}
