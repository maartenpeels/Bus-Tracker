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
import com.busenzo.gui.markers.BusMarker;
import com.busenzo.gui.markers.HalteMarker;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import netscape.javascript.JSObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author Beheerders
 */
public class FXMLDocumentController implements Initializable, MapComponentInitializedListener, UIEventHandler {

    private Administratie admin;
    
    private FXMLDocumentController fdc;
    
    @FXML
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
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

        admin = new Administratie();
        admin.laadDataIn();
        this.fdc = this;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.refreshData = new Runnable() {
            @Override
            public void run() {
                reloadData();
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
        this.loadMapBussen();
        this.loadMapHaltes();
    }

    public void showBusses() {
        if (cbBusses.isSelected()) {
            this.showMapBussen();
        } else {
            this.clearMapBussen();
            System.out.println("deselected markers");
        }
    }

    @FXML
    public void showStops() {
        if (cbStops.isSelected()) {
            this.showMapHaltes();
        } else {
            this.clearMapHaltes();
            System.out.println("deselected markers");
        }
    }

    public void clearMapHaltes() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof HalteMarker) {
                m.setVisible(false);
            }
        }
    }

    public void clearMapBussen() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof BusMarker) {
                m.setVisible(false);
            }
        }
    }

    public void showMapHaltes() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof HalteMarker) {
                m.setVisible(true);
            }
        }
    }

    public void showMapBussen() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof BusMarker) {
                m.setVisible(true);
            }
        }
    }

    public void searchBusOrStop() throws InterruptedException {
        boolean stopFound = searchHalte(tfSearch.getText().trim());
        boolean busFound = searchBussen(tfSearch.getText().trim());
        if (!stopFound && !busFound) {
            System.out.println("No bus or stop found");
            JOptionPane.showMessageDialog(null, "Geen bus of halte gevonden, aub wijzig uw zoekterm!");
        }
    }

    public void resetMap() {
        LatLong eindhoven = new LatLong(51.44799, 365.46328);
        map.setCenter(eindhoven);
        map.setZoom(12);
        this.clearMapBussen();
        this.clearMapHaltes();
    }

    public void showBusDetails() {

    }

    public void loadMapHaltes() {
        for (Halte a : this.admin.getHaltes()) {
            HalteMarker hm = new HalteMarker(new MarkerOptions(), a);
            this.mapMarkers.add(hm);
            hm.setVisible(false);
            map.addMarker(hm);
            map.addUIEventHandler(hm, UIEventType.click, this);
        }
    }
    
    public void loadMapBussen() {
        for (Lijn l : this.admin.getBussen()) {
            Platform.runLater(new Runnable(){

                @Override
                public void run() {
                    BusMarker bm = new BusMarker(new MarkerOptions(), l);
                    mapMarkers.add(bm);
                    bm.setVisible(false);
                    map.addMarker(bm);
                    map.addUIEventHandler(bm, UIEventType.click, fdc);
                }
            });
        }
    }
    
    private void clearBusMarkers(){
        ArrayList<Marker> tempMarkers = new ArrayList<>();
        for(Marker m : this.mapMarkers){
            if(m instanceof BusMarker){
                tempMarkers.add(m);
            }
        }
        for(Marker m : tempMarkers){
            this.mapMarkers.remove(m);
            this.map.removeMarker(m);
        }
        tempMarkers.clear();      
    }

    public boolean searchHalte(String naam) throws InterruptedException {
        boolean stopfound = false;
        this.clearMapHaltes();
        ArrayList<Halte> result = new ArrayList<>();
        result.addAll(admin.zoekHalte(naam));
        if (!result.isEmpty()) {
            for (Marker m : this.mapMarkers) {
                if (m instanceof HalteMarker) {
                    HalteMarker hm = (HalteMarker) m;
                    for (Halte h : result) {
                        if (hm.getHalteID().equals(h.getId())) {
                            m.setVisible(true);
                            stopfound = true;
                            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                            infoWindowOptions.content(h.getNaam());
                            InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                            pointerInfoWindow.open(map, m);
                        }
                    }
                }
            }
        }
        return stopfound;

    }

    public boolean searchBussen(String naam) throws InterruptedException {
        this.clearMapBussen();
        boolean busFound = false;
        ArrayList<Lijn> result = new ArrayList<>();
        result.addAll(admin.zoekLijn(naam));
        if (!result.isEmpty()) {
            for (Marker m : this.mapMarkers) {
                if (m instanceof BusMarker) {
                    BusMarker bm = (BusMarker) m;
                    for (Lijn l : result) {
                        if (bm.getLijnID().equals(l.getId())) {
                            m.setVisible(true);
                            busFound = true;
                            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                            infoWindowOptions.content(l.getBeschrijving());
                            InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                            pointerInfoWindow.open(map, m);
                        }
                    }
                }
            }
        }
        return busFound;
    }

    public void reloadData() {
        System.out.println("Starting data refreshing");
        try {
            this.admin.HaalBusLocaties();
            this.clearBusMarkers();
            this.loadMapBussen();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Refreshing done");
    }

    public void clickedHalte(LatLong pos) {
        NumberFormat formatter = new DecimalFormat("#0.00000");
        for (Halte a : this.admin.getHaltes()) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            if (formatter.format(pos.getLongitude()).equals(formatter.format(cordsY)) && formatter.format(pos.getLatitude()).equals(formatter.format(cordsX))) {
                lblSelectedStop.setText(a.getNaam());
            }
        }
    }

    public void clickedBus(LatLong pos) {
        NumberFormat formatter = new DecimalFormat("#0.00000");
        for (Lijn l : this.admin.getBussen()) {
            for (Rit r : l.getRitten()) {
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
