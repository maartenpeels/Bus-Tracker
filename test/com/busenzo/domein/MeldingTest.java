/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.domein;

import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Alex
 */
public class MeldingTest {
    private LocalDateTime ldt;
       
    @Test(expected=IllegalArgumentException.class)
    public void testMeldingConstructorZonderOntEnV(){
        ldt = LocalDateTime.now();
        Melding melding = new Melding(1, "beschrijving", "", "", ldt);
        Assert.fail("er mag geen melding zonder ontvanger en verzender aangemaakt worden");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMeldingConstructorOntvangerEnVerzender(){
        Melding melding = new Melding(1, "beschrijving", "zender", "ontvanger", ldt);
        Assert.fail("Er mag geen melding met ontvanger en verzender aangemaakt worden");
    }
    
    @Test
    public void testMeldingConstructor(){
        Melding melding = new Melding(1, "beschrijving", "zender", "ontvanger", ldt);
        Assert.assertEquals("beschrijving", melding.getBeschrijving());
        Assert.assertSame("zender", melding.getZender());
        Assert.assertSame("ontvanger", melding.getOntvanger());
        Assert.assertTrue(melding.getActief());
    }
}
