/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.gui;

import com.busenzo.administratie.Administratie;
import com.busenzo.domein.Halte;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.busenzo.gui.FXMLDocumentController;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;

/**
 *
 * @author Beheerders
 */
public class FXMLDocumentController implements Initializable, MapComponentInitializedListener {
    @FXML
    private ArrayList<Halte> mapHaltes;
    private Button button;
    
    @FXML
    private GoogleMapView mapView;
    
    private GoogleMap map;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Administratie am = new Administratie();
        try {
            am.getHalteData();
            this.mapHaltes = am.getHaltes();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       mapView.addMapInializedListener(this);
        
    }    

    @Override
    public void mapInitialized() {

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(51.44799, 365.46328))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);
                   
        map = mapView.createMap(mapOptions);

        //Add markers to the map
        this.loadMapHaltes();
        
    }
    public void loadMapHaltes()
    {
        for(Halte a : this.mapHaltes){
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            LatLong mappos = new LatLong(cordsX, cordsY);
            MarkerOptions pointeropts = new MarkerOptions();
            //pointeropts.icon("C:\\Users\\Gebruiker\\Documents\\NetBeansProjects\\Bus-Tracker\\src\\com\\busenzo\\gui\\bstop.png");
            pointeropts.position(mappos);
            Marker pointer = new Marker(pointeropts);
            pointeropts.title(a.getNaam());
            //InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            //infoWindowOptions.content(a.getNaam());
            //InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
            //pointerInfoWindow.open(map, pointer);
            map.addMarker( pointer );
        }
            
        System.out.println("Added " + this.mapHaltes.size() + " items to map");
    }
    
}
