/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Alex
 */
public class BusTracker extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");
        Alert altert = new Alert(Alert.AlertType.ERROR);
        altert.setHeaderText("example for Sergio");
        altert.setContentText("Good.");
        altert.showAndWait();
        
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
//        
//        Scene scene = new Scene(root);
//        
//        stage.setScene(scene);
//        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
