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
import org.junit.Test;

/**
 *
 * @author Alex
 */
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
        halte1 = new Halte("testhalte1", 1, 1);
        halte2 = new Halte("testhalte2", 2, 2);
        halte3 = new Halte("testhalte3", 3, 3);
        inputHalte.add(halte1);
        inputHalte.add(halte2);
        inputHalte.add(halte3);
        lijn1 = new Lijn(1, Richting.HEEN, "lijn 1");
        lijn2 = new Lijn(2, Richting.HEEN, "lijn 2");
        lijn3 = new Lijn(3, Richting.HEEN, "lijn 3");
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
    
    /**
     * Zoek naar een specifieke bus
     * param id: ID (chassisnummer) van de bus
     */
    @Test
    public void testZoekBus(){
        admin.addBus(bus1);
        Bus actual = admin.zoekBus(123);
        Assert.assertEquals("Gevonden bus komt overeen met bus1", bus1, actual);
    }
    
    //* return De bus met het ingevoerde id, null als deze niet bestaat
    @Test
    public void testZoekBusNietBestaand(){
        Bus actual = admin.zoekBus(404);
        Assert.assertNull("actual is null want bus 404 bestaat niet", actual);
    }
    
    /**
     * Update de locaties van alle bussen (deze worden opgehaald uit de database)
     */
    @Test
    public void testHaalBusLocatiesOp(){
        //TODO
    }
    
    /**
     * Haal alle lijnen op die bij een specifieke halte stoppen
     */
    @Test
    public void testGeefHalteInformatie(){
        
    }
    
    //* param halteNaam: De haltenaam waarvan je de lijnen wil weten, mag geen lege string zijn.
    @Test(expected=IllegalArgumentException.class)
    public void testGeefHalteInformatieLegeZoekstring(){
        admin.GeefHalteInformatie("");
        Assert.fail("Lege zoekstring moet IllegalArgumentException geven!");
    }
    
    /**
     * return Een lijst van alle lijnen die bij deze halte stoppen, kan een lege lijst zijn als de halte 
     * niet gevonden wordt
     */
    @Test
    public void testGeefHalteInformatieNietBestaandeHalte(){
        admin.addHaltes(inputHalte);
        List<Lijn> actual = admin.GeefHalteInformatie("nietbestaand");
    }
    
    /**
     * Haal de haltes op van een specifieke lijn
     * param nummer: Het lijnnummer waarvan je de haltes wil weten.
     */
    @Test
    public void testGeefLijnInformatie(){
        
    }
    
    //* return Een lijst van haltes waar deze lijn stopt, kan leeg zijn als het lijnnummer niet gevonden wordt
    @Test
    public void testGeefLijnInformatieNietBestaandeLijn(){
        
    }
    
    /**
     * Voeg een nieuwe bus toe aan de administratie
     * param bus: De toe te voegen bus, er mag nog geen bus bestaan met hetzelfde chassisnummer 
     */
    public void testAddBus(){
        Assert.assertEquals("lijst van bussen is leeg", 0, admin.bussen.size());
        admin.addBus(bus1);
        Assert.assertEquals("lijst van bussen bevat 1 bus", 1, admin.bussen.size());
        Assert.assertEquals("bus objecten zijn gelijk", bus1, admin.bussen.get(0));
        admin.addBus(bus1);
        Assert.assertEquals("bus is niet nogmaals toegevoegd", 1, admin.bussen.size());
    }

    /**
     * Voeg meerdere nieuwe bussen toe aan de administratie
     * param bussen: Een lijst met bussen die toegevoegd worden. Bussen worden alleen toegevoegd als er nog
     * geen bus bestaat met dat specifieke chassisnummer
     */
    public void testAddBussen(){
        Assert.assertEquals("lijst van bussen is leeg", 0, admin.bussen.size());
        admin.addBussen(inputBus);
        Assert.assertEquals("lijst van bussen bevat 3 bussen", 3, admin.bussen.size());
        admin.addBussen(inputBus);
        Assert.assertEquals("bussen zijn niet nogmaals toegevoegd", 3, admin.bussen.size());
    }

    /**
     * Verwijder een specifieke bus uit de administratie
     * param bus De te verwijderen bus
     */
    public void testRemoveBus(){
        admin.addBussen(inputBus);
        Assert.assertEquals("3 bussen toegevoegd", 3, admin.bussen.size());
        admin.removeBus(bus1);
        Assert.assertEquals("er zijn nog 2 bussen over", 2, admin.bussen.size());
        Assert.assertNull("bus met nummer 123 wordt niet gevonden",admin.zoekBus(bus1.getNummer()));
    }

    /**
     * Voeg een halte toe aan de administratie
     * param halte De toe te voegen halte, deze wordt alleen toegevoegd als deze halte nog niet bestaat
     */
    public void testAddHalte(){
        Assert.assertEquals("geen haltes aanwezig in admin", 0, admin.getHaltes().size());
        admin.addHalte(halte1);
        Assert.assertEquals("een halte aanwezig in admin", 1, admin.getHaltes().size());
        admin.addHalte(halte1);
        Assert.assertEquals("halte wordt niet dubbel toegevoegd", 1, admin.getHaltes().size());
    }

    /**
     * Voeg meerdere haltes toe aan de administratie
     * param haltes Een lijst van toe te voegen haltes, haltes worden alleen toegevoegd als deze nog niet 
     * bestaan
     */
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
    public void testAddLijn(){
        Assert.assertEquals("geen lijnen aanwezig in admin", 0, admin.lijnen.size());
        admin.addLijn(lijn1);
        Assert.assertEquals("een lijn aanwezig in admin", 1, admin.lijnen.size());
        admin.addLijn(lijn1);
        Assert.assertEquals("lijn wordt niet dubbel toegevoegd", 1, admin.lijnen.size());
    }

    /**
     * Voeg meerdere lijnene toe aan de administratie
     * param lijnen De toe te voegen lijnen. Lijnen worden alleen toegevoegd als ze nog niet bestaan
     */
    public void testAddLijnen(){
        Assert.assertEquals("geen lijnen aanwezig in admin", 0, admin.lijnen.size());
        admin.addLijnen(inputLijn);
        Assert.assertEquals("drie lijnen aanwezig in admin", 3, admin.lijnen.size());
        admin.addLijnen(inputLijn);
        Assert.assertEquals("lijnen worden niet dubbel toegevoegd", 3, admin.lijnen.size());
    }
}
