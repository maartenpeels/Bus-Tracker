/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Assert;
import org.junit.Test;

public class BusTest {
    
    @Test 
    public void testBusConstructor(){
        Bus b = new Bus(123);
        Assert.assertEquals("123 is het busnummer", 123, b.getNummer());
        Assert.assertNull("nog geen huidige rit", b.getHuidigeRit());
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testBusConstructorInvalidnumber(){
        Bus b = new Bus(-1);
        Assert.fail("Er mag geen bus met een negatief nummer aangemaakt worden");
    }
    
    @Test
    public void testGetCoordinatenNietGezet(){
        Bus b = new Bus(123);
        Assert.assertEquals(0.0, b.getCoordinaten()[0], 0.0);
        Assert.assertEquals(0.0, b.getCoordinaten()[1], 0.0);
    }
    
    @Test
    public void testUpdateAndGetCoordinaten(){
        double lat = 2.5;
        double lon = 5.0;
        Bus b = new Bus(123);
        Assert.assertEquals(0.0, b.getCoordinaten()[0], 0.0);
        Assert.assertEquals(0.0, b.getCoordinaten()[1], 0.0);
        b.updateLocatie(lat, lon);
        Assert.assertEquals(lat, b.getCoordinaten()[0], 0.0);
        Assert.assertEquals(lon, b.getCoordinaten()[1], 0.0);
    }
    
    @Test
    public void testSetAndGetHuidigeRit(){
        Bus b = new Bus(123);
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        LocalDateTime ldt1 = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0);
        Rit r = new Rit(ldt1, lijn1);
        Assert.assertNull(b.getHuidigeRit());
        b.setHuidigeRit(r);
        Assert.assertEquals(r, b.getHuidigeRit());
    }
}
