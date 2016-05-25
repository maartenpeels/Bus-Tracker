/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import domein.Melding;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private TextField notificationTextInput;
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
        Runnable task = new Runnable() {
            @Override
            public void run(){
                try {
                    admin.laadDataIn();
                    updateLabels();
                    updateListBox();
                    laadMeldingen();
                } catch (Exception ex) {
                    //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Update next stops & notifications done!");
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
    public void laadMeldingen() throws Exception
    {
        
        ArrayList<Melding> meldingenArray = this.admin.getMeldingen();
        ObservableList<String> meldingenTekstArray = FXCollections.observableArrayList();
        for(Melding m : meldingenArray)
        {
            //System.out.println(m.getOntvanger());
            //System.out.println(m.getZender());
            meldingenTekstArray.add((m.getZender() == null || m.getZender().isEmpty() ? "Beheerder" : m.getOntvanger()) + " > " + m.getBeschrijving());
        }
        lvIncomingNotifications.setItems(meldingenTekstArray);
    }
    @FXML
    public void handleSendNotification() throws Exception{
        //System.out.println(notificationTextInput.getText());
        if(this.admin.sendMelding(notificationTextInput.getText()))
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Melding toevoegen");
            alert.setContentText("Melding is verstuurd!");
            alert.showAndWait();
            notificationTextInput.setText("");
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Melding toevoegen");
            alert.setContentText("Fout tijdens toevoegen!");
            alert.showAndWait();
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
    /*
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

    */
    @FXML
    public void sendNotification(){
        
    }
    private void showLoginForm() throws Exception
    {
        List<String> choices = new ArrayList<>();
        while(true)
        {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Chauffeurssysteem - Inloggen - Voer busnummer in");
            dialog.setHeaderText("Voer hier uw geplande busnummer in, Lege invoer geeft een lijst van alle actuele ritten");
            dialog.setContentText("Busnummer:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
           
            if (result.isPresent()){
                System.out.println("Searching for bus: " + result.get());
                choices = admin.getRitbyName(result.get());
                if(choices.size() > 0)
                {
                    break;
                }
            }
            else
            {
                System.out.println("Applicatie sluiten");
                System.exit(0);
            }
        }
        while(true)
        {
            ChoiceDialog<String> dialogBusSelect = new ChoiceDialog<>(null, choices);
            dialogBusSelect.setTitle("Chauffeurssysteem - Inloggen - Selecteer rit");
            dialogBusSelect.setHeaderText("Selecteer hieronder uw geplande rit");
            dialogBusSelect.setContentText("Rit:");

            Optional<String> resultBusSelect = dialogBusSelect.showAndWait();
            if (resultBusSelect.isPresent()){
                if(resultBusSelect.get().length() > 0)
                {
                    this.admin.setRit(resultBusSelect.get());
                    break;
                }
            }
            else
            {
                System.out.println("Applicatie sluiten");
                System.exit(0);
            }
        }
        System.out.println("Geselecteerde rit: " + admin.getRitID());
    }
    private void updateLabels(){
        lineLabel.setText("Buslijn:");
        busstopLabel.setText("Volgende haltes:");
        expectedArrivalTime.setText("Verwachtte aankomsttijd volgende halte:\n" + DateTimeFormatter.ofPattern("HH:mm:ss").format(admin.getRit().getArrivalTime()));
    } 
    
    private void updateListBox(){
        ObservableList<String> stops = FXCollections.observableArrayList(admin.getRit().getNextStops());
        lv_nextStops.setItems(stops);
    }
}
