/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
        admin.laadDataIn();
        updateLabels();
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
    
    private void updateLabels(){
//        if(this.ca.isLineSet()){
//            this.lineLabel.setText("Buslijn: " + this.ca.getLijnNummer());
//            this.busstopLabel.setText("Volgende halte: " + this.ca.getVolgendeHaltes().get(0));
//            this.cbNotifications.setItems(this.ca.getNotificationTypes());
//            this.lv_nextStops.setItems(this.ca.getVolgendeHaltes());
//            this.expectedArrivalTime.setText("Verwachtte aankomsttijd: " + this.ca.getVerwachtteEindAankomsttijd());
//        }else{
//            this.lineLabel.setText("Buslijn:");
//            this.busstopLabel.setText("Volgende halte:");
//            this.expectedArrivalTime.setText("Verwachtte aankomsttijd:");
//        }
    } 
    
}
