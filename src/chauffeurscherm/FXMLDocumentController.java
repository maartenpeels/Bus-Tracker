/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import domein.Stop;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 * @author Jules
 */
public class FXMLDocumentController implements Initializable {
    
    private BusDriverAdmin admin;
    
    @FXML
    private Label label;
    
    @FXML
    private Label lineLabel;
    @FXML
    private Label busstopLabel;
    @FXML
    private ComboBox<String> cbNotifications;
    @FXML
    private ListView<String> lv_nextStops;
    @FXML
    private Label expectedArrivalTime;
    @FXML
    private ListView lvIncomingNotifications;
    private ObservableList<com.busenzo.busdriver.gui.NotificationLabel> lvItems;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lvItems = FXCollections.observableArrayList();
        this.lvIncomingNotifications.setItems(lvItems);
        admin = new BusDriverAdmin();
        
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                admin.laadDataIn();
                
                updateLabels();
                updateListBox();
                
                System.out.println("Update next stops done!");
                
                return true;
            }
        };
        
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Application shutdown detected.. Quitting current jobs");
                service.shutdown();
            }
        });
    }
    
    @FXML
    public void handleSendNotification(){
        if(!this.cbNotifications.getSelectionModel().isEmpty()){
            //this.ca.verstuurMelding(cbNotifications.getSelectionModel().getSelectedItem());
            com.busenzo.busdriver.gui.NotificationLabel notlab = new com.busenzo.busdriver.gui.NotificationLabel();
            notlab.setCategorie(this.cbNotifications.getSelectionModel().getSelectedItem());
            notlab.setParent(lvIncomingNotifications);
            this.lvItems.add(notlab);
        }
        else{
            //TODO error afhandelen
            System.out.println("geen item geselecteerd");
        }
    }
    
    private void updateLabels(){
        Platform.runLater( () -> this.lineLabel.setText("Buslijn:"));
        Platform.runLater( () -> this.busstopLabel.setText("Volgende haltes:"));
        Platform.runLater( () -> this.expectedArrivalTime.setText("Verwachtte aankomsttijd volgende halte:\n" + DateTimeFormatter.ofPattern("HH:mm:ss").format(admin.getRit().getArrivalTime())));
    } 
    
    private void updateListBox(){
        ObservableList<String> stops = FXCollections.observableArrayList(admin.getRit().getNextStops());
        this.lv_nextStops.setItems(stops);
    }
}
