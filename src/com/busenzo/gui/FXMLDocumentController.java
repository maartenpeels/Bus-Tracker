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
import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.DirectionsWaypoint;
import com.lynden.gmapsfx.service.directions.TravelModes;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Beheerders
 */
public class FXMLDocumentController implements Initializable, MapComponentInitializedListener, UIEventHandler, DirectionsServiceCallback {

    private Administratie admin;
    private String lastSearchedListObject = "";
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
    private CheckBox cbHalteOff;

    @FXML
    private Label lblSelectedStop;
    @FXML
    private Label lblBusNumber;
    @FXML
    private Label lblBusId;

    @FXML
    private TextField tfSearch;

    @FXML
    private ListView lvStops;

    @FXML
    private CheckBox cbBusses;

    @FXML
    private ComboBox cbSelectBus;

    @FXML
    private ListView lbNotifications;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    private String selectedStop;
    protected DirectionsPane directions;
    @FXML
    private Button btnSendNotification;

    @FXML
    private TextArea taNotificationText;

    private Polyline poly;
    private Halte selectedHalte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        admin = new Administratie();
        admin.laadDataIn();
        this.fdc = this;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.refreshData = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        reloadData();
                        showBusses();
                        showStops();
                    }
                });
            }
        };
        //set listview selected item change listner    
//        lvStops.getSelectionModel().selectedItemProperty().addListener(
//            new ChangeListener<String>() {
//                public void changed(ObservableValue<? extends String> ov, 
//                    String old_val, String selectedHalte) {
//                    //geselecteerdeHalte = selectedHalte;
//            }
//        });
        lvStops.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String haltenaam = lvStops.getSelectionModel().getSelectedItem().toString();
                    String[] haltenaamSplit = haltenaam.split(", ");
                    searchStops(haltenaamSplit[1], true);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage() + " setonmouseclick Handle");
                    //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Application shutdown detected.. Quitting current jobs");
                executor.shutdown();
            }
        });
        executor.scheduleAtFixedRate(this.refreshData, 2, 2, TimeUnit.MINUTES);
        mapView.addMapInializedListener(this);
        showNotifications();
        for (Lijn bus : admin.getBussen()) {
            for (Rit r : bus.getRitten()) {
                cbSelectBus.getItems().add(r.getRitID() + " - " + bus.getNummer());
            }
        }

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
        directions = mapView.getDirec();

        //Add markers to the map
        this.loadMapBusses();
        this.loadMapStops();
    }

    public void showBusses() {
        if (cbBusses.isSelected()) {
            this.showMapBusses();
        } else {
            this.clearMapBusses();
            System.out.println("deselected markers");
        }
    }

    public void showNotifications() {
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Melding m : admin.getAllNotifications()) {
            items.add((m.getSender() == "" ? "Beheerder" : m.getSender()) + " > " + (m.getReceiver() == "" ? "Beheerder" : m.getReceiver()) + " (" + m.getID() + ")");
            this.lbNotifications.setItems(items);
            this.lbNotifications.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    Pattern r = Pattern.compile("\\((\\d+)\\)$");
                    Matcher m = r.matcher(newValue);

                    if (m.find() && !m.group(1).equals(lastSearchedListObject)) {
                        lastSearchedListObject = m.group(1);
                        System.out.println("Found message id: " + m.group(1));
                        for (Melding melding : admin.getAllNotifications()) {
                            if (melding.getID().equals(Integer.parseInt(m.group(1)))) {
                                System.out.println(melding.getBeschrijving());
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Melding informatie");
                                alert.setHeaderText("Bericht van: " + (melding.getSender() == "" ? "Beheerder" : melding.getSender()) + "\nNaar: " + (melding.getReceiver() == "" ? "Beheerder" : melding.getReceiver()) + "\nOp: " + melding.getTijdstip().toString().replace("T", " "));
                                alert.setContentText(melding.getBeschrijving());
                                //ButtonType buttonTyperemovemelding = new ButtonType("Verwijder");
                                ButtonType buttonTypeCancel = new ButtonType("Sluiten", ButtonData.CANCEL_CLOSE);
                                alert.getButtonTypes().setAll(buttonTypeCancel);
                                alert.showAndWait();
                            }
                        }

                    }

                }
            });

        }
    }

    @FXML
    public void showStops() {
        if (cbStops.isSelected()) {
            this.showMapStops();
        } else {
            this.clearMapStops();
            System.out.println("deselected markers");
        }
    }

    public void clearMapStops() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof HalteMarker) {
                m.setVisible(false);
            }
        }
    }

    public void clearMapBusses() {
        if (!cbBusses.isSelected()) {
            for (Marker m : this.mapMarkers) {
                if (m instanceof BusMarker) {
                    m.setVisible(false);
                }
            }
        }
    }

    public void showMapStops() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof HalteMarker) {
                m.setVisible(true);
            }
        }
    }

    public void showMapBusses() {
        for (Marker m : this.mapMarkers) {
            if (m instanceof BusMarker) {
                m.setVisible(true);
            }
        }
    }
    int countSearchBusOrStop = 0;

    public void searchBusOrStop() throws InterruptedException {

        boolean stopFound = searchStops(tfSearch.getText().trim(), false);
        boolean busFound = searchBusses(tfSearch.getText().trim());
        if (busFound) {
            cbBusses.setSelected(false);
            cbStops.setSelected(false);
        }
        lblBusId.setText("-");
        lblBusNumber.setText("-");
        if (countSearchBusOrStop == 0) {
            countSearchBusOrStop = 1;
            searchBusOrStop();
        } else {
            countSearchBusOrStop = 0;
            if (!stopFound && !busFound) {
                System.out.println("No bus or stop found");
                JOptionPane.showMessageDialog(null, "Geen bus of halte gevonden, aub wijzig uw zoekterm!");
            }
        }

    }

    public void resetMap() {
        LatLong eindhoven = new LatLong(51.44799, 365.46328);
        map.setCenter(eindhoven);
        map.setZoom(12);
        this.clearMapBusses();
        this.clearMapStops();
    }

    public void showBusDetails() {

    }

    public void loadMapStops() {
        for (Halte a : this.admin.getHaltes()) {
            HalteMarker hm = new HalteMarker(new MarkerOptions(), a);
            this.mapMarkers.add(hm);
            hm.setVisible(false);
            map.addMarker(hm);
            map.addUIEventHandler(hm, UIEventType.click, this);
        }
    }

    public void loadMapBusses() {
        for (Lijn l : this.admin.getBussen()) {
            BusMarker bm = new BusMarker(new MarkerOptions(), l);
            mapMarkers.add(bm);
            bm.setVisible(false);
            map.addMarker(bm);
            map.addUIEventHandler(bm, UIEventType.click, fdc);
        }
    }

    private void clearBusMarkers() {
        ArrayList<Marker> tempMarkers = new ArrayList<>();
        for (Marker m : this.mapMarkers) {
            if (m instanceof BusMarker) {
                tempMarkers.add(m);
            }
        }
        for (Marker m : tempMarkers) {
            this.mapMarkers.remove(m);
            this.map.removeMarker(m);
        }
        tempMarkers.clear();
    }

    public boolean searchStops(String naam, Boolean fromLV) throws InterruptedException {
        boolean stopfound = false;
        ObservableList<String> items = FXCollections.observableArrayList();
        this.clearMapStops();
        //Refresht de map zodat er niet onnodige haltes worden weergegeven
        map.setZoom(map.getZoom() + 1);
        map.setZoom(map.getZoom() - 1);
        if (!fromLV) {
            this.clearMapBusses();
        }
        ArrayList<Halte> result = new ArrayList<>();
        result.addAll(admin.zoekHalte(naam));
        if (!result.isEmpty()) {
            for (Marker m : this.mapMarkers) {
                if (m instanceof HalteMarker) {
                    HalteMarker hm = (HalteMarker) m;
                    int haltesCount = 0;
                    for (Halte h : result) {
                        haltesCount++;
                        if (haltesCount < 11) {
                            if (hm.getHalteID().equals(h.getId())) {
                                items.add(h.getNaam());

                                m.setVisible(true);
                                stopfound = true;
                                if (haltesCount < 1) {
                                    InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                                    infoWindowOptions.content(h.getNaam());
                                    InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                                    pointerInfoWindow.open(map, m);
                                }
                            }
                        }
                        if (!fromLV) {
                            lvStops.setItems(items);
                        }
                    }
                }
            }
        }
        return stopfound;

    }

    public boolean searchBusses(String naam) throws InterruptedException {
        this.clearMapBusses();
        this.clearMapStops();
        boolean busFound = false;
        ArrayList<Lijn> result = new ArrayList<>();
        result.addAll(admin.zoekLijn(naam));
        if (!result.isEmpty()) {
            //clearBusMarkers();
            drawRoute(result.get(0));
            for (Marker m : this.mapMarkers) {
                if (m instanceof BusMarker) {
                    BusMarker bm = (BusMarker) m;
                    int lijnCount = 0;
                    for (Lijn l : result) {
                        lijnCount++;
                        if (bm.getLijnID().equals(l.getId())) {
                            //set stops/haltes to listview
                            ObservableList<String> items = FXCollections.observableArrayList(l.getHalteNamen());
                            lvStops.setItems(items);

                            m.setVisible(true);
                            busFound = true;
                            if (lijnCount < 1) {
                                lblBusId.setText("-");
                                lblBusNumber.setText("-");
                                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                                infoWindowOptions.content(l.getBeschrijving());
                                InfoWindow pointerInfoWindow = new InfoWindow(infoWindowOptions);
                                pointerInfoWindow.open(map, m);

                            }

                        }
                    }
                }
            }
        }
        return busFound;
    }

    public void drawRoute(Lijn l) {
        if (poly != null) {
            map.removeMapShape(poly);
        }
        ArrayList<String> haltes = l.getHalteNamen();
        ArrayList<LatLong> positions = new ArrayList();
        System.out.println(haltes.size());
        for (Halte a : this.admin.geefLijnInformatie(l.getNummer())) {
            positions.add(new LatLong(a.getCoordinaten()[0], a.getCoordinaten()[1]));
        }
        System.out.println(positions.size());
        MVCArray pmvc = new MVCArray(positions.toArray());
        PolylineOptions polyOpts = new PolylineOptions()
                .path(pmvc)
                .strokeColor("red")
                .strokeWeight(2);

        poly = new Polyline(polyOpts);
        map.addMapShape(poly);  
    }
    
    public void drawRouteFromBus(Bus b) {
        
        ArrayList<Halte> stops = (ArrayList<Halte>) this.admin.geefLijnInformatie(b.getHuidigeRit().getLijn().getNummer());
        
        Halte currentStop = stops.get(0);
        int currentStopIndex = 0;
        
        for(Halte h : stops)
        {
            Halte temp = admin.zoekHalte(b.getCoordinaten()).get(0);
            if(h.getId() == temp.getId())
            {
                //TODO: Pls help, heb laatste halte van bus nodig dmv de coordinaten(dit werkt niet)
                currentStop = h;
                break;
            }
            currentStopIndex++;
        }
        
        stops.subList(0, currentStopIndex-1).clear();
        
        int size = stops.size()>8 ? 8 : stops.size();
        DirectionsWaypoint[] waypoints = new DirectionsWaypoint[size];
        
        for(int i = 0; i <= size; i++)
        {
            Halte ht = stops.get(i);
            double lat = ht.getCoordinaten()[0];
            double lon = ht.getCoordinaten()[1];
            LatLong latlon = new LatLong(lat, lon);
            DirectionsWaypoint point = new DirectionsWaypoint(latlon);
            waypoints[i] = point;
        }
        
        DirectionsRequest req = new DirectionsRequest(
                 new LatLong(stops.get(0).getCoordinaten()[0], stops.get(0).getCoordinaten()[1])
                ,new LatLong(stops.get(stops.size()).getCoordinaten()[0], stops.get(stops.size()).getCoordinaten()[1])
                ,TravelModes.DRIVING
                ,waypoints);
        
        //TODO: help, hoe voeg ik toe aan map?
    }

    public void reloadData() {
        System.out.println("Starting data refreshing");
        try {
            this.admin.haalBusLocaties();
            this.clearBusMarkers();
            this.loadMapBusses();
        } catch (Exception ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Refreshing done");
    }

    @FXML
    public void changeHalteSetting(){
        double cordsX = selectedHalte.getCoordinaten()[0];
        double cordsY = selectedHalte.getCoordinaten()[1];
        NumberFormat formatter = new DecimalFormat("#0.000000");
        for(Marker m : mapMarkers){
            // marker positie vergelijken met geselecteerde halte coordinaten en plaatje aanpassen
        }
        if(cbHalteOff.isSelected()){
           selectedHalte.active = false;
        } else {
           selectedHalte.active = true;
        }
    }
    
    public boolean clickedStop(LatLong pos) {
        boolean stopLocated = false;
        ObservableList<String> items = FXCollections.observableArrayList();
        NumberFormat formatter = new DecimalFormat("#0.000000");
        for (Halte a : this.admin.getHaltes()) {
            double cordsX = a.getCoordinaten()[0];
            double cordsY = a.getCoordinaten()[1];
            if (formatter.format(pos.getLongitude()).equals(formatter.format(cordsY)) && formatter.format(pos.getLatitude()).equals(formatter.format(cordsX))) {
                stopLocated = true;
                lblBusId.setText("-");
                lblBusNumber.setText("-");
                items.add(a.getNaam());
                lvStops.setItems(items);
                selectedHalte = a;
                if(a.active)
                    cbHalteOff.setSelected(false);
                else 
                    cbHalteOff.setSelected(true);
            }
        }
        return stopLocated;
    }

    public boolean clickedBus(LatLong pos) throws InterruptedException {
        boolean busLocated = false;
        NumberFormat formatter = new DecimalFormat("#0.00000");
        for (Lijn l : this.admin.getBussen()) {
            for (Rit r : l.getRitten()) {
                Bus b = r.getBus();
                if (b != null) {
                    double cordsX = b.getCoordinaten()[0];
                    double cordsY = b.getCoordinaten()[1];
                    if (formatter.format(pos.getLongitude()).equals(formatter.format(cordsY)) && formatter.format(pos.getLatitude()).equals(formatter.format(cordsX))) {
                        busLocated = true;
                        drawRouteFromBus(b);
                        lblBusId.setText("" + b.getNummer());
                        lblBusNumber.setText("" + b.getHuidigeRit().getLijn().getNummer());
                        cbSelectBus.getSelectionModel().select(r.getRitID() + " - " + b.getNummer());
                        searchBusses(b.getHuidigeRit().getLijn().getNummer() + "");
                    }
                }
            }
        }
        return busLocated;
    }

    @Override
    public void handle(JSObject obj) {
        boolean busLocated = false;
        NumberFormat formatter = new DecimalFormat("#0.00000");
        LatLong pos = new LatLong((JSObject) obj.getMember("latLng"));
        if (cbBusses.isSelected() && !cbStops.isSelected()) {
            try {
                busLocated = clickedBus(pos);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        } else if (cbStops.isSelected() && !cbBusses.isSelected()) {
            clickedStop(pos);
        } else if (!cbStops.isSelected() && !cbBusses.isSelected()) {
            try {
                busLocated = clickedBus(pos);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!busLocated) {
                clickedStop(pos);
            }
        }

    }

    @Override
    public void directionsReceived(DirectionsResult dr, DirectionStatus ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private void removeInfoWindowOptions() {
//        for (InfoWindowOptions infoWindow : infoWindowOptionsList){
//            //infoWindow.zIndex(0)
//        }
//    }
    @FXML
    public void sendMessage() throws Exception {
        // Geselecteerde item ophalen 
        if (cbSelectBus.getSelectionModel().getSelectedIndex() > -1) {
            String tempSelectie = cbSelectBus.getSelectionModel().getSelectedItem().toString();

            // String splitten om eerste gedeelte van string te benaderen
            String selectie[] = tempSelectie.split(" - ");
            // Text van de TextArea ophalen
            String meldingBechrijving = taNotificationText.getText();

            if (meldingBechrijving.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Geen bericht ingevuld! Probeer opnieuw.");
                return;
            }

            // Aanmaken van nieuwe melding
            Melding m = new Melding(0, meldingBechrijving, "-1", selectie[0], LocalDateTime.now());
            admin.addMelding(m);

            System.out.println("Melding verzonden van: Beheerder naar: " + m.getReceiver() + ". Met als melding: " + m.getBeschrijving());
        }
    }

}
