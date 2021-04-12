package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.DTOs.FRequestDTO;
import socialnetwork.domain.DTOs.FriendDTO;
import socialnetwork.domain.Status;
import socialnetwork.domain.Utilizator;
import socialnetwork.events.ChangeEventType;
import socialnetwork.events.FriendshipChangeEvent;
import socialnetwork.observer.Observer;
import socialnetwork.service.ServiceCereriPrietenie;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceUtilizatori;

import java.time.LocalDate;

public class SentRequestsController implements Observer<FriendshipChangeEvent> {

    @FXML
    TableView<FRequestDTO> requestsTable;
    @FXML
    TableColumn<FriendDTO, String> firstNameColumn;
    @FXML
    TableColumn<FriendDTO, String> lastNameColumn;
    @FXML
    TableColumn<FriendDTO, Status> statusColumn;
    @FXML
    TableColumn<FriendDTO, LocalDate> dateColumn;

    private ServiceUtilizatori utilizatorService;
    private ServicePrietenii friendshipService;
    private ServiceCereriPrietenie requestsService;
    private Utilizator user;
    private Stage stage;
    private Scene previous;

    ObservableList<FRequestDTO> modelRequests = FXCollections.observableArrayList();

    public void setService(ServiceUtilizatori servU, ServicePrietenii servP, ServiceCereriPrietenie requestsService,Utilizator u, Stage stage, Scene prev) {
        this.utilizatorService = servU;
        this.friendshipService = servP;
        this.requestsService = requestsService;
        this.user = u;
        this.stage = stage;
        this.previous = prev;
        friendshipService.addObserver(this);
        this.requestsService.addObserver(this);
        initModelRequests();
    }

    private void initModelRequests() {
        modelRequests.setAll(requestsService.getUserPendingRequests(user.getId()));
    }

    /**
     * load sent requests in the table
     */
    private void initializeRequestsTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("lastName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, Status>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, LocalDate>("friendRequestDate"));
        requestsTable.setItems(modelRequests);
    }

    @FXML
    public void initialize() {
        initializeRequestsTable();
    }

    /**
     * handler to withdraw request
     */
    @FXML
    public void handleWithdraw() {
        try {
            FRequestDTO reqDTO = requestsTable.getSelectionModel().getSelectedItem();
            requestsService.removePendingRequest(reqDTO.getId());
            MessageInformation.showInformationMessage(null, "Friend request withdrawed!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    /**
     * update method from the observable interface
     * @param friendshipChangeEvent friendship change event
     */
    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {


        if(friendshipChangeEvent.getType()== ChangeEventType.DELETE){


            initModelRequests();
        }
    }

    @FXML
    public void handleBack(){
        stage.setScene(previous);
    }
}
