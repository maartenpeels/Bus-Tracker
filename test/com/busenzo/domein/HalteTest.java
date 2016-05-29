/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import org.junit.Assert;
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
}
