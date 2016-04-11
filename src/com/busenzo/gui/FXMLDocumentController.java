/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.gui;

import com.busenzo.administratie.Administratie;
import com.busenzo.domein.*;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import netscape.javascript.JSObject;
import java.lang.Runtime;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.scene.control.Label;

/**
 *
 * @author Beheerders
 */
public class FXMLDocumentController implements Initializable, MapComponentInitializedListener, UIEventHandler {

    @FXML
    private ArrayList<Halte> mapHaltes;
    private ArrayList<Lijn> mapLijnen;
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private ArrayList<Marker> mapBussen = new ArrayList<>();
    public ScheduledExecutorService executor;
    private Runnable refreshData;
    @FXML
    private ImageView busEnzoLogo;

    @FXML
    private CheckBox cbStops;

    @FXML
    private Label lblSelectedStop;
    @FXML
    private Label lblBusNumber;
    @FXML
    private Label lblBusId;

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
            this.mapLijnen = am.getBussen();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.refreshData = new Runnable() {
            public void run() {
                reloadData(am);
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Application shutdown detected.. Quitting current jobs");
                executor.shutdown();
            }
        });
        executor.scheduleAtFixedRate(this.refreshData, 2, 2, TimeUnit.MINUTES);
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
                .zoomControl(true)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add markers to the map
        //this.loadMapHaltes(true);
    }

    public void showBusses() {
        if (cbBusses.isSelected()) {
            this.clearMapBussen();
            this.loadMapBussen();
        } else {
            this.clearMapBussen();
            System.out.println("deselected markers");
        }
    }

    @FXML
    public void showStops() {
        if (cbStops.isSelected()) {
            this.clearMapHaltes();
            this.loadMapHaltes();
        } else {
            this.clearMapHaltes();
            System.out.println("deselected markers");
        }
    }

    public void clearMapHaltes() {
        for (Marker m : this.mapMarkers) {
            map.removeMarker(m);
        }
    }

    public void clearMapBussen() {
        for (Marker m : this.mapBussen) {
            map.removeMarker(m);
        }
    }

    public void searchBusOrStop() throws InterruptedException {
        boolean stopFound = searchHalte(tfSearch.getText());
        boolean busFound = searchBussen(tfSearch.getText());
        if (!stopFound && !busFound) {
            System.out.println("No bus or stop found");
            JOptionPane.showMessageDialog(null, "Geen bus of halte gevonden, aub wijzig uw zoekterm!");
        }
    }

    public void resetMap() {
        LatLong eindhoven = new LatLong(51.44799, 365.46328);
        map.setCenter(eindhoven);
        map.setZoom(12);
    }

    public void showBusDetails() {

    }

    public void loadMapHaltes() {
        for (Halte a : this.mapHaltes) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            LatLong mappos = new LatLong(cordsX, cordsY);
            MarkerOptions pointeropts = new MarkerOptions();
            pointeropts.icon("http://37.97.149.53//busenzo/external/resources/busstop.png");
            pointeropts.position(mappos);
            Marker pointer = new Marker(pointeropts);
            pointeropts.title(a.getNaam());
            //InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            //infoWindowOptions.content(a.getNaam());
            //InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
            //pointerInfoWindow.open(map, pointer);
            this.mapMarkers.add(pointer);
            map.addMarker(pointer);
            map.addUIEventHandler(pointer, UIEventType.click, this);
            //removeMarker werkt hier niet omdat er steeds nieuwe instanties van een Marker wordt aagemaakt
        }

        // System.out.println("Added " + this.mapHaltes.size() + " items to map");
    }

    public void loadMapBussen() {
        for (Lijn l : this.mapLijnen) {
            for (Rit r : l.ritten) {
                Bus b = r.getBus();
                if (b != null) {
                    double cordsX = b.getCoordinaten()[0];
                    double cordsY = b.getCoordinaten()[1];
                    LatLong mappos = new LatLong(cordsX, cordsY);
                    MarkerOptions pointeropts = new MarkerOptions();
                    pointeropts.icon("http://37.97.149.53/busenzo/external/resources/bus.png");
                    pointeropts.position(mappos);
                    Marker pointer = new Marker(pointeropts);
                    pointeropts.title(l.getBeschrijving());
                    //InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                    //infoWindowOptions.content(a.getNaam());
                    //InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                    //pointerInfoWindow.open(map, pointer);
                    this.mapBussen.add(pointer);
                    map.addMarker(pointer);
                    map.addUIEventHandler(pointer, UIEventType.click, this);
                    //removeMarker werkt hier niet omdat er steeds nieuwe instanties van een Marker wordt aagemaakt
                }
            }
        }

        // System.out.println("Added " + this.mapHaltes.size() + " items to map");
    }

    public boolean searchHalte(String naam) throws InterruptedException {
        boolean stopfound = false;
        this.clearMapHaltes();
        for (Halte a : this.mapHaltes) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            LatLong mappos = new LatLong(cordsX, cordsY);
            MarkerOptions pointeropts = new MarkerOptions();
            pointeropts.icon("http://37.97.149.53/busenzo/external/resources/busstop.png");
            pointeropts.position(mappos);
            Marker pointer;
            pointeropts.title(a.getNaam());
            if (a.getNaam().toLowerCase().contains(naam.toLowerCase())) {
                stopfound = true;
                map.setCenter(mappos);
                map.setZoom(18);
                //map.addMarker( pointer );
                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content(a.getNaam());
                InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                pointeropts.position(mappos);
                pointer = new Marker(pointeropts);
                pointerInfoWindow.open(map, pointer);
                this.mapMarkers.add(pointer);
                map.addMarker(pointer);
                break;
            }
        }
        System.out.println("Applied filter to map");

        return stopfound;

    }

    public boolean searchBussen(String naam) throws InterruptedException {
        this.clearMapBussen();
        boolean busFound = false;
        for (Lijn l : this.mapLijnen) {
            for (Rit r : l.ritten) {
                Bus b = r.getBus();
                if (b != null) {
                    double cordsX = b.getCoordinaten()[0];
                    double cordsY = b.getCoordinaten()[1];
                    LatLong mappos = new LatLong(cordsX, cordsY);
                    MarkerOptions pointeropts = new MarkerOptions();
                    pointeropts.icon("http://37.97.149.53/busenzo/external/resources/bus.png");
                    pointeropts.position(mappos);
                    Marker pointer;
                    pointeropts.title(l.getBeschrijving());
                    int busNummer = l.getNummer();
                    String busNumberSearcher = String.valueOf(busNummer);
                    if (l.getBeschrijving().toLowerCase().contains(naam.toLowerCase()) || busNumberSearcher.contains(naam)) {
                        map.setCenter(mappos);
                        map.setZoom(18);
                        //map.addMarker( pointer );
                        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                        infoWindowOptions.content(l.getBeschrijving());
                        InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                        pointeropts.position(mappos);
                        pointer = new Marker(pointeropts);
                        pointerInfoWindow.open(map, pointer);
                        this.mapBussen.add(pointer);
                        map.addMarker(pointer);
                        System.out.println("Applied filter to map");
                        busFound = true;
                    }
                }

            }
        }
        return busFound;
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
    public void reloadData(Administratie am) {
        System.out.println("Starting data refreshing");
        try {
            am.getRouteData();
            this.mapLijnen = am.getBussen();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Refreshing done");
    }

    public void clickedHalte(LatLong pos) {
        NumberFormat formatter = new DecimalFormat("#0.00000");
        for (Halte a : this.mapHaltes) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            if (formatter.format(pos.getLongitude()).equals(formatter.format(cordsY)) && formatter.format(pos.getLatitude()).equals(formatter.format(cordsX))) {
                lblSelectedStop.setText(a.getNaam());
            }
        }
    }

    public void clickedBus(LatLong pos) {
        NumberFormat formatter = new DecimalFormat("#0.00000");
        for (Lijn l : this.mapLijnen) {
            for (Rit r : l.ritten) {
                Bus b = r.getBus();
                if (b != null) {
                    double cordsX = b.getCoordinaten()[0];
                    double cordsY = b.getCoordinaten()[1];
                    if (formatter.format(pos.getLongitude()).equals(formatter.format(cordsY)) && formatter.format(pos.getLatitude()).equals(formatter.format(cordsX))) {
                        lblBusId.setText("" + b.getNummer());
                        lblBusNumber.setText("" + b.getHuidigeRit().getLijn().getNummer());
                    }
                }
            }
        }
    }

    @Override
    public void handle(JSObject obj) {
        NumberFormat formatter = new DecimalFormat("#0.00000");
        LatLong pos = new LatLong((JSObject) obj.getMember("latLng"));
        if (cbBusses.isSelected() && !cbStops.isSelected()) {
            clickedBus(pos);
        } else if (cbStops.isSelected() && !cbBusses.isSelected()) {
            clickedHalte(pos);
        }
    }

}
