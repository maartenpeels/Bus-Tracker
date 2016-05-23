/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Halte;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Richting;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AdministratieTest {
    
    Administratie admin;
    Bus bus1, bus2, bus3;
    Halte halte1, halte2, halte3;
    Lijn lijn1, lijn2, lijn3;
    ArrayList<Bus> inputBus;
    ArrayList<Halte> inputHalte;
    ArrayList<Lijn> inputLijn;
    
    @Before
    public void setUp(){
        admin = new Administratie();
        inputBus = new ArrayList<>();
        inputHalte = new ArrayList<>();
        inputLijn = new ArrayList<>();
        bus1 = new Bus(123);
        bus2 = new Bus(456);
        bus3 = new Bus(789);
        inputBus.add(bus1);
        inputBus.add(bus2);
        inputBus.add(bus3);
        halte1 = new Halte("1", "testhalte1", "1", "1");
        halte2 = new Halte("2", "testhalte2", "2", "2");
        halte3 = new Halte("3", "testhalte3", "3", "3");
        inputHalte.add(halte1);
        inputHalte.add(halte2);
        inputHalte.add(halte3);
        lijn1 = new Lijn("1", 1, Richting.HEEN, "lijn 1");
        lijn2 = new Lijn("2", 2, Richting.HEEN, "lijn 2");
        lijn3 = new Lijn("3", 3, Richting.HEEN, "lijn 3");
        inputLijn.add(lijn1);
        inputLijn.add(lijn2);
        inputLijn.add(lijn3);
    }
    
    /**
     * Maak een nieuwe administratie aan
     */
    @Test
    public void testAdministratieConstructor(){
        Assert.assertNotNull("Administratie mag niet null zijn", admin);
    }
    
    
    
    //* param halteNaam: De haltenaam waarvan je de lijnen wil weten, mag geen lege string zijn.
    @Test(expected=IllegalArgumentException.class)
    public void testGeefHalteInformatieLegeZoekstring(){
        admin.geefHalteInformatie("");
        Assert.fail("Lege zoekstring moet IllegalArgumentException geven!");
    }
    
    /**
     * return Een lijst van alle lijnen die bij deze halte stoppen, kan een lege lijst zijn als de halte 
     * niet gevonden wordt
     */
    @Test@Ignore
    public void testGeefHalteInformatieNietBestaandeHalte(){
        admin.addHaltes(inputHalte);
        List<Lijn> actual = admin.geefHalteInformatie("nietbestaand");
    }
    
    /**
     * Haal de haltes op van een specifieke lijn
     * param nummer: Het lijnnummer waarvan je de haltes wil weten.
     */
    @Test@Ignore
    public void testGeefLijnInformatie(){
        
    }
    
    //* return Een lijst van haltes waar deze lijn stopt, kan leeg zijn als het lijnnummer niet gevonden wordt
    @Test@Ignore
    public void testGeefLijnInformatieNietBestaandeLijn(){
        
    }
    
    /**
     * Voeg een nieuwe bus toe aan de administratie
     * param bus: De toe te voegen bus, er mag nog geen bus bestaan met hetzelfde chassisnummer 
     */
    @Test@Ignore
    public void testAddBus(){

    }

    /**
     * Voeg meerdere nieuwe bussen toe aan de administratie
     * param bussen: Een lijst met bussen die toegevoegd worden. Bussen worden alleen toegevoegd als er nog
     * geen bus bestaat met dat specifieke chassisnummer
     */
    @Test@Ignore
    public void testAddBussen(){
        
    }

    /**
     * Verwijder een specifieke bus uit de administratie
     * param bus De te verwijderen bus
     */
    @Test@Ignore
    public void testRemoveBus(){

    }

    /**
     * Voeg een halte toe aan de administratie
     * param halte De toe te voegen halte, deze wordt alleen toegevoegd als deze halte nog niet bestaat
     */
    @Test
    public void testAddHalte(){
        Assert.assertEquals("geen haltes aanwezig in admin", 0, admin.getHaltes().size());
        admin.addHalte(halte1);
        Assert.assertEquals("een halte aanwezig in admin", 1, admin.getHaltes().size());
        Assert.assertEquals(halte1, admin.getHaltes().get(0));
        admin.addHalte(halte1);
        Assert.assertEquals("halte wordt niet dubbel toegevoegd", 1, admin.getHaltes().size());
    }

    /**
     * Voeg meerdere haltes toe aan de administratie
     * param haltes Een lijst van toe te voegen haltes, haltes worden alleen toegevoegd als deze nog niet 
     * bestaan
     */
    @Test
    public void testAddHaltes(){
        Assert.assertEquals("geen haltes aanwezig in admin", 0, admin.getHaltes().size());
        admin.addHaltes(inputHalte);
        Assert.assertEquals("drie haltes aanwezig in admin", 3, admin.getHaltes().size());
        admin.addHaltes(inputHalte);
        Assert.assertEquals("haltes worden niet dubbel toegevoegd", 3, admin.getHaltes().size());
    }

    /**
     * Voeg een specifieke lijn toe aan de administratie
     * param lijn De toe te voegen lijn. Deze wordt alleen toegevoegd als deze lijn nog niet bestaat.
     */
    @Test@Ignore
    public void testAddLijn(){
        Assert.assertEquals("geen lijnen aanwezig in admin", 0, admin.getBussen().size());
        admin.addLijn(lijn1);
        Assert.assertEquals("een lijn aanwezig in admin", 1, admin.getBussen().size());
        Assert.assertEquals(lijn1, admin.getBussen().get(0));
        admin.addLijn(lijn1);
        Assert.assertEquals("lijn wordt niet dubbel toegevoegd", 1, admin.getBussen().size());
    }

    /**
     * Voeg meerdere lijnene toe aan de administratie
     * param lijnen De toe te voegen lijnen. Lijnen worden alleen toegevoegd als ze nog niet bestaan
     */
    @Test@Ignore
    public void testAddLijnen(){
        Assert.assertEquals("geen lijnen aanwezig in admin", 0, admin.getBussen().size());
        admin.addLijnen(inputLijn);
        Assert.assertEquals("drie lijnen aanwezig in admin", 3, admin.getBussen().size());
        admin.addLijnen(inputLijn);
        Assert.assertEquals("lijnen worden niet dubbel toegevoegd", 3, admin.getBussen().size());
    }
    
    @Test@Ignore
    public void testZoekLijn(){
        //1. Test zoeken zonder resultaat
        //2. Test zoeken 1 resultaat
        //3. Test zoeken meerdere resultaten
    }
    
    @Test
    public void testZoekHalte(){
        admin.addHaltes(inputHalte);
        Assert.assertEquals(0, admin.zoekHalte("blablabla").size());
        Assert.assertEquals(halte1, admin.zoekHalte(halte1.getNaam()).get(0));
        Assert.assertEquals(1, admin.zoekHalte(halte1.getNaam()).size());
        Assert.assertEquals(3, admin.zoekHalte("test").size());
        Assert.assertArrayEquals(inputHalte.toArray(), admin.zoekHalte("test").toArray());
    }
}
