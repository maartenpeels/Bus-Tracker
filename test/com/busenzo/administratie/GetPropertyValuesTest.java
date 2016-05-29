/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Alex
 */
public class GetPropertyValuesTest {
    
    private GetPropertyValues propval;
    private final String expectedServer = "http://37.97.149.53/busenzo/api/";
    private final String expectedKey = "9709d02bfb3b1460fd0dd45f6706a81a8c33afaf";
    
    @Before
    public void setUp(){
        this.propval = new GetPropertyValues();
    }
    
    @Test
    public void testGetPropValues(){
        try {
            String[] actual = propval.getPropValues();
            Assert.assertEquals("Server address is equal", expectedServer, actual[0]);
            Assert.assertEquals("API key is equal", expectedKey, actual[1]);
        } catch (IOException ex) {
            Logger.getLogger(GetPropertyValuesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
