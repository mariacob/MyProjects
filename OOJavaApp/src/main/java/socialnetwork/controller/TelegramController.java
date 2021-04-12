package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.ServiceMesaje;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceUtilizatori;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TelegramController {


    @FXML
    TableView<Utilizator> tableViewTelegramUsers;

    @FXML
    TableColumn<Utilizator,String> tableColumnTelegramFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnTelegramLastName;

    @FXML
    Button buttonSendTelegram;

    @FXML
    TextArea textAreaMessage;


    private ServiceUtilizatori utilizatorService;
    private ServicePrietenii friendshipService;
    private ServiceMesaje messengerService;
    private Utilizator user;
    private Stage stage;
    private Scene previous;

    ObservableList<Utilizator> modelUsers = FXCollections.observableArrayList();

    public void setService(ServiceUtilizatori utilizatorService, ServicePrietenii friendshipService, ServiceMesaje messengerService, Utilizator user, Stage stage, Scene prev) {
        this.utilizatorService = utilizatorService;
        this.friendshipService = friendshipService;
        this.messengerService = messengerService;
        this.stage = stage;
        this.previous = prev;
        this.user = user;
        initModelUsers();

        tableViewTelegramUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initModelUsers(){

        Iterable<Utilizator> users = utilizatorService.getAll();
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        modelUsers.setAll(usersList);
    }


    @FXML
    public void initialize(){

        initializeFriendsTable();
    }

    /**
     * display friends in the table
     */
    private void initializeFriendsTable(){

        tableColumnTelegramFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        tableColumnTelegramLastName.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));
        tableViewTelegramUsers.setItems(modelUsers);
    }

    /**
     * handler for the send message button
     */
    public void handleSendTelegram(){

        List<Long> lista= tableViewTelegramUsers.getSelectionModel().getSelectedItems().stream()
                .map(Utilizator::getId)
                .collect(Collectors.toList());

        String mesaj=textAreaMessage.getText();

        messengerService.addMesaj(user.getId(),lista,mesaj);

        MessageInformation.showInformationMessage(null,"Mesajul a fost trimis");
    }

    @FXML
    public void handleBack(){
        stage.setScene(previous);
    }
}
