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
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import netscape.javascript.JSObject;

/**
 *
 * @author Beheerders
 */
public class FXMLDocumentController implements Initializable, MapComponentInitializedListener, UIEventHandler {

    @FXML
    private ArrayList<Halte> mapHaltes;
    private Button button;

    @FXML
    private CheckBox cbStops;

    @FXML
    private TextField tfSearch;

    @FXML
    private CheckBox cbBusses;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Administratie am = new Administratie();
        try {
            am.getHalteData();
            am.getLineData();
            am.getRouteData();
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
        map.addUIEventHandler(UIEventType.click, this);

        //Add markers to the map
        //this.loadMapHaltes(true);
    }

    public void showBusses() {

    }

    @FXML
    public void showStops() {
        if (cbStops.isSelected()) {
            loadMapHaltes(true);
        } else {
            loadMapHaltes(false);
            System.out.println("deselected");
        }
    }

    public void searchBusOrStop() throws InterruptedException {
        searchHalte(tfSearch.getText());
    }

    public void resetMap() {

    }

    public void showBusDetails() {

    }

    public void loadMapHaltes(boolean show) {
        for (Halte a : this.mapHaltes) {
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
            if (show) {
                map.addMarker(pointer);
            } else {
                map.removeMarker(pointer);
            }
        }

        // System.out.println("Added " + this.mapHaltes.size() + " items to map");
    }

    public void searchHalte(String naam) throws InterruptedException {
        for (Halte a : this.mapHaltes) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            LatLong mappos = new LatLong(cordsX, cordsY);
            MarkerOptions pointeropts = new MarkerOptions();
            //pointeropts.icon("C:\\Users\\Gebruiker\\Documents\\NetBeansProjects\\Bus-Tracker\\src\\com\\busenzo\\gui\\bstop.png");
            pointeropts.position(mappos);
            Marker pointer = new Marker(pointeropts);
            pointeropts.title(a.getNaam());
            if (a.getNaam().toLowerCase().contains(naam.toLowerCase())) {
                map.setCenter(mappos);
                map.setZoom(18);
                //map.addMarker( pointer );
                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content(a.getNaam());
                InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                pointeropts.position(mappos);
                pointer = new Marker(pointeropts);
                pointerInfoWindow.open(map, pointer);
                break;
            } 
        }

        System.out.println("Added " + this.mapHaltes.size() + " items to map");
    }

    /*
    public void loadMapRitten()
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
     */

    @Override
    public void handle(JSObject obj) {
        System.out.println("Click");
    }

}
