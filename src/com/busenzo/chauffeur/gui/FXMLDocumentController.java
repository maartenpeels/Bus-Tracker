/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.chauffeur.gui;

import com.busenzo.chauffeur.ChauffeurAdmin;
import com.busenzo.chauffeur.persistence.IDataKoppeling;
import com.busenzo.chauffeur.persistence.MockDataKoppeling;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    private ChauffeurAdmin ca;
    private IDataKoppeling dk;
    
    @FXML
    private Label lijnLabel;
    @FXML
    private Label halteLabel;
    @FXML
    private ComboBox<String> cbMelding;
    @FXML
    private ListView<String> lv_volgendeHaltes;
    @FXML
    private Label expectedArrivalTime;
    @FXML
    private ListView lvIncomingNotifications;
    private ObservableList<NotificationLabel> lvItems;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lvItems = FXCollections.observableArrayList();
        this.lvIncomingNotifications.setItems(lvItems);
        this.dk = new MockDataKoppeling();
        this.ca = new ChauffeurAdmin(dk);
        this.ca.setHuidigeRit(dk.setRit());
        updateLabels();
    }
    
    @FXML
    public void handleSendNotification(){
        if(!this.cbMelding.getSelectionModel().isEmpty()){
            this.ca.verstuurMelding(cbMelding.getSelectionModel().getSelectedItem());
            NotificationLabel notlab = new NotificationLabel();
            notlab.setCategorie(this.cbMelding.getSelectionModel().getSelectedItem());
            notlab.setParent(lvIncomingNotifications);
            this.lvItems.add(notlab);
            //this.lvIncomingNotifications.setCellFactory(notlab);
        }
        else{
            //TODO error afhandelen
            System.out.println("geen item geselecteerd");
        }
    }
    
    private void updateLabels(){
        if(this.ca.isLineSet()){
            this.lijnLabel.setText("Buslijn: " + this.ca.getLijnNummer());
            this.halteLabel.setText("Volgende halte: " + this.ca.getVolgendeHaltes().get(0));
            this.cbMelding.setItems(this.ca.getNotificationTypes());
            this.lv_volgendeHaltes.setItems(this.ca.getVolgendeHaltes());
            this.expectedArrivalTime.setText("Verwachtte aankomsttijd: " + this.ca.getVerwachtteEindAankomsttijd());
        }else{
            this.lijnLabel.setText("Buslijn:");
            this.halteLabel.setText("Volgende halte:");
            this.expectedArrivalTime.setText("Verwachtte aankomsttijd:");
        }
    }
    
}
