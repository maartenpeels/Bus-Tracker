/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Alex
 */
public class BusTest {
    
    public int lon = 1, lat = 2;
    
    @Test
    public void testGetCoordinaten(){
        Bus b = new Bus(123);
        b.updateLocatie(lon, lat);
        int[] expected = {1,2};
     //   Assert.assertArrayEquals(expected, b.getCoordinaten());
    }
}
