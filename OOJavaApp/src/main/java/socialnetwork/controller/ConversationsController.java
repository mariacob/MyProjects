package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Mesaj;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.ServiceMesaje;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceUtilizatori;

import java.io.IOException;
import java.util.stream.Collectors;

public class ConversationsController {

    @FXML
    ListView<String> conversationsList;

    private ServiceUtilizatori utilizatorService;
    private ServicePrietenii friendshipService;
    private ServiceMesaje messageService;
    private Utilizator user1;
    private Utilizator user2;
    private Stage stage;
    private Scene previous;

    ObservableList<String> modelConversations = FXCollections.observableArrayList();

    public void setService(ServiceUtilizatori servU, ServicePrietenii servP, ServiceMesaje servM, Utilizator u1, Utilizator u2, Stage stage, Scene prev) {
        this.utilizatorService = servU;
        this.friendshipService = servP;
        this.messageService = servM;
        this.user1 = u1;
        this.user2 = u2;
        this.stage = stage;
        this.previous = prev;
        initModelConversations();
    }

    private void initModelConversations(){
        modelConversations.setAll(messageService.getSortedMesaje(user1.getId(),user2.getId())
                .stream()
                .map(Mesaj::toString)
                .collect(Collectors.toList())
        );
    }

    /**
     * display the conversations between user1 and user2 in the list
     */
    private void initializeConversationsList(){
        conversationsList.setItems(modelConversations);
    }

    @FXML
    public void initialize(){
        initializeConversationsList();
    }

    @FXML
    public void handleBack(){
        stage.setScene(previous);
    }
}
