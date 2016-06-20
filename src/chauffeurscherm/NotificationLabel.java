/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chauffeurscherm;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Alex
 */
public class NotificationLabel extends AnchorPane {

//*******************************Datavelden*********************************
    private final String categorie = "Verzender: ";
    private final String description = "Omschrijving: ";
    
    @FXML
    private Label lblCategorie;
    @FXML
    private Label lblDescription;
    @FXML
    private Button btnRemoveNotification;
    @FXML
    private CheckBox cbNotificationActive;
    @FXML
    private AnchorPane AnchorPane;
    private ListView parent;

//*******************************Constructor********************************
    public NotificationLabel(){
        load();
    }
    
    private void load(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLNotificationPanel.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void setParent(ListView lv){
        this.parent = lv;
    }

//*******************************Propperties********************************

//*******************************Methoden***********************************
    public void setCategorie(String newCat){
        if(newCat.equals("-1")){
            lblCategorie.setText(categorie + "Beheerder");
        }
        else{
            lblCategorie.setText(categorie + newCat);
        }
    }
    
    public void setDescription(String desc){
        lblDescription.setText(description + desc);
    }
    
    @FXML
    public void cbChanged(){
       System.out.println("Hello"); 
    }
    
    @FXML
    public void removeNotificationPanel(){
        parent.getItems().remove(this);
    }
}
