/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import administratie.DataLink;
import administratie.ILogin;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex
 */
public class FXMLLoginController implements Initializable {
    
    private ILogin admin;
    
    @FXML
    private TextField tfChooseLine;
    @FXML
    private Button chooseLine;
    @FXML
    private ListView<String> lvLines;
    @FXML
    private Button login;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            admin = new BusDriverAdmin();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void handleChooseLine(ActionEvent event) {
        String line = this.tfChooseLine.getText().trim();
        if(!line.isEmpty()){
            ArrayList<String> lines = new ArrayList<>();
            try {
                lines.addAll(admin.getRitbyName(line));
            } catch (Exception ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!lines.isEmpty()){
                lvLines.setItems(FXCollections.observableArrayList(lines));
                lvLines.setVisible(true);
                login.setVisible(true);
            }else{
                this.tfChooseLine.clear();
                this.showErrorDialog("Geen ritten gevonden", "Er zijn helaas geen ritten gevonden met dit nummer");
            }
        }else{
            this.tfChooseLine.clear();
            this.showErrorDialog("Geen zoekterm", "Vul een lijnnummer in in het zoekveld");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) throws RemoteException {
        if(!this.lvLines.getSelectionModel().getSelectedItem().isEmpty()){
            this.showMainScreen(this.lvLines.getSelectionModel().getSelectedItem());
        }
        else{
            this.showErrorDialog("Geen route geselecteerd", "Selecteer de te rijden route in de lijst");
        }
    }
    
    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showMainScreen(String routeNr) throws RemoteException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));

        Stage stage = (Stage) this.login.getScene().getWindow();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        FXMLDocumentController controller = loader.<FXMLDocumentController>getController();
        controller.setAdmin((BusDriverAdmin)this.admin);
        controller.setRoute(routeNr);
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);  

        stage.show();
    }
    
}
