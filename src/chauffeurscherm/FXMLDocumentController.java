/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import domein.Stop;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 *
 * @author Jules
 */
public class FXMLDocumentController implements Initializable {
    
    private BusDriverAdmin admin;
    private ObservableList<NotificationLabel> lvItems;
    
    //For main busdriver screen 
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
    //For login screen
    @FXML
    private Button chooseLine;
    @FXML
    private TextField tfChooseLine;
    @FXML
    private Button login;
    @FXML
    private ListView lvLines;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lvItems = FXCollections.observableArrayList();
        //this.lvIncomingNotifications.setItems(lvItems);
        admin = new BusDriverAdmin();
        try {        
            showLoginForm();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            NotificationLabel notlab = new NotificationLabel();
            notlab.setCategorie(this.cbNotifications.getSelectionModel().getSelectedItem());
            notlab.setParent(lvIncomingNotifications);
            this.lvItems.add(notlab);
        }
        else{
            //TODO error afhandelen
            System.out.println("geen item geselecteerd");
        }
    }
    
    @FXML
    public void handleChooseLine(){
        String searchTerm = tfChooseLine.getText().trim();
        if(searchTerm.isEmpty() /**|| admin.searchLine(searchTerm) == null**/){
            
        }
        else{
            this.lvLines.setVisible(true);
            this.login.setVisible(true);
            //this.lvLines.setItems();
        }
    }
    
    @FXML
    public void handleLogin(){
        if(lvLines.getSelectionModel().getSelectedItem() == null){//change to !=
            try {
                Stage stage = (Stage)this.login.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        else{
            
        }
    }
    private void showLoginForm() throws Exception
    {
        
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Chauffeurssysteem - Inloggen - Voer busnummer in");
        dialog.setHeaderText("Voer hier uw geplande busnummer in");
        dialog.setContentText("Busnummer:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        List<String> choices = new ArrayList<>();
        if (result.isPresent()){
            System.out.println("Searching for bus: " + result.get());
            choices = admin.getRitbyName(result.get());
        }
        ChoiceDialog<String> dialogBusSelect = new ChoiceDialog<>(null, choices);
        dialog.setTitle("Chauffeurssysteem - Inloggen - Selecteer rit");
        dialog.setHeaderText("Selecteer hieronder uw geplande rit");
        dialog.setContentText("Rit:");

        Optional<String> resultBusSelect = dialogBusSelect.showAndWait();
        if (resultBusSelect.isPresent()){
            this.admin.setRit(resultBusSelect.get());
            System.out.println("Gebruiker Rit: " + resultBusSelect.get());
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
