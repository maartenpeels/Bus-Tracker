/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class HalteTest {
  
    @Test
    public void testHalteConstructor(){
        Halte h = new Halte("1", "testhalte", "3.4", "5.7");
        Assert.assertEquals("1", h.getId());
        Assert.assertEquals("testhalte", h.getNaam());
        Assert.assertEquals(3.4, h.getCoordinaten()[1], 0.0);
        Assert.assertEquals(5.7, h.getCoordinaten()[0], 0.0);
    }
    
    @Test@Ignore
    public void testAddEnGetLijnen(){
        Halte h = new Halte("1", "testhalte", "3.4", "5.7");
        Assert.assertNull(h.HaalLijnen());
        Lijn lijn1 = new Lijn("1", 1, Richting.HEEN, "test lijn");
        Lijn lijn2 = new Lijn("2", 5, Richting.HEEN, "test lijn2");
        h.AddLijn(lijn1);
        Assert.assertEquals(lijn1, h.HaalLijnen().get(0));
        h.AddLijn(lijn2);
        Assert.assertEquals(lijn2, h.HaalLijnen().get(1));
        Assert.assertEquals(2, h.HaalLijnen().size());
    }
    
}
