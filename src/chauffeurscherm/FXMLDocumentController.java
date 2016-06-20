/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chauffeurscherm;

import administratie.BusDriverAdmin;
import com.busenzo.domein.Melding;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Jules
 */
public class FXMLDocumentController implements Initializable, Observer {

    private BusDriverAdmin admin;
    private ObservableList<NotificationLabel> lvItems;
    private final String EXARTIME = "Verwachtte aankomsttijd volgende halte:\n";
    private final String BUSLINE = "Buslijn: ";

    @FXML
    private Label lineLabel;
    @FXML
    private TextField notificationTextInput;
    @FXML
    private Label busstopLabel;
    @FXML
    private ListView<String> lv_nextStops;
    @FXML
    private Label expectedArrivalTime;
    @FXML
    private ListView lvIncomingNotifications;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lvItems = FXCollections.observableArrayList();
        initLabels();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            admin.laadDataIn();
                            updateLabels();
                            updateListBox();
                            laadMeldingen();
                        } catch (Exception ex) {
                            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                System.out.println("Update next stops & notifications done!");
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(task, 5, 30, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Application shutdown detected.. Quitting current jobs");
                service.shutdown();
            }
        });
        

    }

    public void laadMeldingen() throws Exception {

        List<Melding> meldingenArray = admin.getMeldingen();
        ObservableList<String> meldingenTekstArray = FXCollections.observableArrayList();
        //ObservableList<NotificationLabel> meldingenGraphic = FXCollections.observableArrayList();
        for (Melding m : meldingenArray) {
            meldingenTekstArray.add((m.getZender() == null || m.getZender().isEmpty() ? "Beheerder" : "Beheerder") + " > " + m.getBeschrijving());
//            NotificationLabel notlab = new NotificationLabel();
//            notlab.setParent(lvIncomingNotifications);
//            notlab.setDescription(m.getBeschrijving());
//            notlab.setCategorie(m.getZender());
//            meldingenGraphic.add(notlab);
        }

        // moet zo worden aangeroepen omdat de call vanuit een RMI thread komt ipv JavaFX thread
        Platform.runLater(() -> {
            lvIncomingNotifications.setItems(meldingenTekstArray);
        });
    }
    
    @FXML
    public void handleClose(){
        System.exit(0);
    }
    
    @FXML
    public void handleLogoff(){
        logOff();
        showLogInScreen();
    }
    
    private void logOff(){
        //Clear data
    }
    
    private void showLogInScreen(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));

        Stage stage = (Stage) this.lvIncomingNotifications.getScene().getWindow();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);  

        stage.show();
    }

    public void handleSendNotification() throws Exception {
        //System.out.println(notificationTextInput.getText());
        if (this.admin.sendMelding(notificationTextInput.getText())) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Melding toevoegen");
            alert.setContentText("Melding is verstuurd!");
            alert.showAndWait();
            notificationTextInput.setText("");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Melding toevoegen");
            alert.setContentText("Fout tijdens toevoegen!");
            alert.showAndWait();
        }
    }

    private void initLabels() {
        lineLabel.setText(BUSLINE);
        busstopLabel.setText("Volgende haltes:");
        expectedArrivalTime.setText(this.EXARTIME);
    }

    private void updateLabels() {
        if (lineLabel.getText().equals(BUSLINE)) {
            lineLabel.setText(BUSLINE + admin.getRitID().split("_")[2].substring(1));
        }
        expectedArrivalTime.setText(this.EXARTIME + DateTimeFormatter.ofPattern("HH:mm:ss").format(admin.getRit().getArrivalTime()));
    }

    private void updateListBox() {
        ObservableList<String> stops = FXCollections.observableArrayList(admin.getRit().getNextStops());
        lv_nextStops.setItems(stops);
    }

    public void setAdmin(BusDriverAdmin bda) throws RemoteException {
        this.admin = bda;
        admin.addObserver(this);
    }

    public void setRoute(String routeNr) throws RemoteException {
        this.admin.setRit(routeNr);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            laadMeldingen();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
