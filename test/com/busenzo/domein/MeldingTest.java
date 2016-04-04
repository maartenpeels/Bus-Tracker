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
public class MeldingTest {
       
    @Test(expected=IllegalArgumentException.class)
    public void testMeldingConstructorZonderOntEnV(){
        Melding melding = new Melding("test", -1, -1);
        Assert.fail("er mag geen melding zonder ontvanger en verzender aangemaakt worden");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMeldingConstructorOntvangerEnVerzender(){
        Melding melding = new Melding("test", 1, 1);
        Assert.fail("Er mag geen melding met ontvanger en verzender aangemaakt worden");
    }
}
