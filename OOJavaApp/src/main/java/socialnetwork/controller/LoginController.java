package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.*;

import java.io.IOException;

/*
Prima fereastra, cea de logare, care o va porni pe a doua specifica pentru un utilizator cand se da "Search!"
 */
public class LoginController {
    // are un singur textfield unde se va introduce user id.
    @FXML
    private TextField textID;

    //cele doua service-uri vor fi transmise catre a doua fereastra. Aici le folosim doar ca sa gasim utilizatorul
    private ServiceUtilizatori utilizatorService;
    private ServicePrietenii friendshipService;
    private ServiceMesaje messengerService;
    private ServiceCereriPrietenie requestsService;
    private ServiceReports serviceReports;

    private Stage stage;

    public void setService(ServiceUtilizatori servU, ServicePrietenii servP, ServiceMesaje messengerService, ServiceCereriPrietenie requestsService, ServiceReports serviceReports,Stage stage){
        this.utilizatorService = servU;
        this.friendshipService = servP;
        this.messengerService = messengerService;
        this.requestsService = requestsService;
        this.serviceReports = serviceReports;
        this.stage = stage;
    }

    /**
     * Search for the user in the repository. If found, display the second window
     */
    @FXML
    public void handleCauta(){
        try {

            if(!textID.getText().equals("")) {

                Utilizator u = utilizatorService.findEntity(Long.parseLong(textID.getText()));
                if (u != null) {
                    showFriendsDialog(u);
                }
                else {
                    MessageAlert.showErrorMessage(null, "This user doesn't exist!");
                }
            }
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


    // metoda care porneste a doua fereastra, specifica pentru un user
    private void showFriendsDialog(Utilizator u) {
        try{
            // create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendsUtilizator.fxml"));
            AnchorPane root = loader.load();
            // create the next stage and a scene
            stage.setTitle("Friendships");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/views/mainstyle.css");
            Scene prev = stage.getScene();
            stage.setScene(scene);

            // obtin controller-ul ferestrei si configurez service-urile
            PrieteniiController prieteniiMeniuController = loader.getController();

            prieteniiMeniuController.setService(utilizatorService, friendshipService,messengerService,requestsService,serviceReports,u,stage,prev);


            // arat fereastra
            //stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
