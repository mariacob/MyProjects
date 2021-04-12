package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.DTOs.FriendDTO;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.events.ChangeEventType;
import socialnetwork.events.FriendshipChangeEvent;
import socialnetwork.observer.Observer;
import socialnetwork.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrieteniiController implements Observer<FriendshipChangeEvent> {

    // tabelul cu prietenii cu coloanele sale
    @FXML
    TableView<FriendDTO> friendsTable;
    @FXML
    TableColumn<FriendDTO, LocalDate> columnFriendDate;
    @FXML
    TableColumn<FriendDTO,String> columnFriendFirstName;
    @FXML
    TableColumn<FriendDTO,String> columnFriendLastName;

    // tabelul cu toti utilizatorii cu coloanele sale
    @FXML
    TableView<Utilizator> usersTable;
    @FXML
    TableColumn<Utilizator,Long> columnUserID;
    @FXML
    TableColumn<Utilizator,String> columnUserFirstName;
    @FXML
    TableColumn<Utilizator,String> columnUserLastName;

    @FXML
    TableColumn<String,String>  columnUserString;

    private ServiceUtilizatori utilizatorService;
    private ServicePrietenii friendshipService;
    private ServiceMesaje messengerService;
    private ServiceCereriPrietenie requestsService;
    private ServiceReports serviceReports;
    private Stage stage;
    private Scene previous;

    @FXML
    Label userLabel;


    private Utilizator user; // in functie de acest "user" pe care il primesc de la fereastra anterioara configurez tabelele si apelez functiile
    // lista pentru toti utilizatorii
    ObservableList<Utilizator> modelUsers = FXCollections.observableArrayList();
    // lista pentru prieteni
    ObservableList<FriendDTO> modelFriends = FXCollections.observableArrayList();

    public void setService(ServiceUtilizatori servU,  ServicePrietenii servP,ServiceMesaje messengerService, ServiceCereriPrietenie servC, ServiceReports serviceReports, Utilizator u, Stage stage, Scene prev){

        this.utilizatorService = servU;
        this.friendshipService = servP;
        this.messengerService=messengerService;
        this.requestsService = servC;
        this.serviceReports = serviceReports;
        this.user = u;
        userLabel.setText(user.getFirstName() + " " + user.getLastName());
        friendshipService.addObserver(this);
        this.stage = stage;
        this.previous = prev;

        initModelFriends();
        initModelUsers();
    }

    // adaug datele din repository in lista de utilizatori
    private void initModelUsers(){
        Iterable<Utilizator> users = utilizatorService.getAll();
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        modelUsers.setAll(usersList);
    }

    // adaug datele din repository in lista de prieteni ai utilizatorului
    private void initModelFriends(){
        modelFriends.setAll(friendshipService.getFriendsOfUser(user.getId()));
    }

    @FXML
    public void initialize(){
        initalizeUsersTable();
        initializeFriendsTable();
    }

    // incarc datele in tabela de prieteni
    private void initializeFriendsTable(){
        columnFriendDate.setCellValueFactory(new PropertyValueFactory<FriendDTO,LocalDate>("friendshipDate"));
        columnFriendFirstName.setCellValueFactory(new PropertyValueFactory<FriendDTO,String>("firstName"));
        columnFriendLastName.setCellValueFactory(new PropertyValueFactory<FriendDTO,String>("lastName"));
        friendsTable.setItems(modelFriends);
    }

    // incarc datele in tabela de utilizatori
    private void initalizeUsersTable(){
        columnUserID.setCellValueFactory(new PropertyValueFactory<Utilizator,Long>("id"));
        columnUserFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        columnUserLastName.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));
        usersTable.setItems(modelUsers);
    }

    /*
     metoda care se apeleaza cand se apasa butonul de add friend
     se ia utilizatorul selectat din tabela de utilizatori si i se trimite o cerere de prietenie (from: user(cel pentru care se incarca aplicatia))
     */
    @FXML
    public void handleAddFriends(){
        try {
            Utilizator u = usersTable.getSelectionModel().getSelectedItem();
            requestsService.addCerere(user.getId(),u.getId());
            MessageInformation.showInformationMessage(null,"Friend request sent!");
        }
        catch (Exception e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    /* metoda care se apeleaza cand se apasa butonul de delete friend
     sterge prietenia dintre user si utilizatorul selectat din lista de prieteni
     */
    @FXML
    public void handleDeleteFriends(){
        try {
            FriendDTO u = friendsTable.getSelectionModel().getSelectedItem();
            requestsService.removeEntity(new Tuple<>(user.getId(),u.getId()));
            MessageInformation.showInformationMessage(null, "Friend deleted!");
        }
        catch (Exception e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {

        if(friendshipChangeEvent.getType()== ChangeEventType.ADD) {
            initModelFriends();
        }

        if(friendshipChangeEvent.getType()==ChangeEventType.DELETE){
            initModelFriends();
        }
    }

    @FXML
    public void handleShowRequests() {
        showRequestsDialog();
    }

    @FXML
    public void handleShowSentReq(){
        showSentRequestsDialog();
    }

    private void showSentRequestsDialog(){
            try{
                // create a new loader
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/sentRequestsUtilizator.fxml"));
                AnchorPane root = loader.load();

                // create the next stage and a scene
                stage.setTitle("Sent Requests");
                //nextStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                Scene prev = stage.getScene();
                stage.setScene(scene);

                // obtin controller-ul ferestrei si configurez service-urile
                SentRequestsController requestsController = loader.getController(); //creeaza o instanta si aici se face initialize() care configureaza coloanele tabelei
                requestsController.setService(utilizatorService, friendshipService,requestsService,user,stage,prev); // in setService se apeleaza initModel care adauga datele in tabela

                // arat fereastra
               // stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void showRequestsDialog(){
        try{
            // create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendRequestsUtilizator.fxml"));
            AnchorPane root = loader.load();

            // create the next stage and a scene
            stage.setTitle("Requests");
            //stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            Scene prev = stage.getScene();
            stage.setScene(scene);

            // obtin controller-ul ferestrei si configurez service-urile
            RequestsController requestsController = loader.getController(); //creeaza o instanta si aici se face initialize() care configureaza coloanele tabelei
            requestsController.setService(utilizatorService, friendshipService,requestsService,user,stage,prev); // in setService se apeleaza initModel care adauga datele in tabela

            // arat fereastra
            //stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleConversation(){
        showConversationsDialog();
    }


    @FXML
    void handleShowTelegram(){

        try {
            // create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/telegramMessage.fxml"));

            AnchorPane root = loader.load();

            // create the next stage and a scene
            stage.setTitle("Telegram");
            //stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            Scene prev = stage.getScene();
            stage.setScene(scene);


            TelegramController telegramController = loader.getController(); //creeaza o instanta si aici se face initialize() care configureaza coloanele tabelei
            // requestsController.setService(utilizatorService, friendshipService,user); // in setService se apeleaza initModel care adauga datele in tabela

            telegramController.setService(utilizatorService,friendshipService,messengerService,user,stage,prev);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showConversationsDialog(){
        try{
            // create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/conversationsUtilizator.fxml"));
            AnchorPane root = loader.load();

            // create the next stage and a scene
            stage.setTitle("Conversation");
            //stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            Scene prev = stage.getScene();
            stage.setScene(scene);


            // obtin controller-ul ferestrei si configurez service-urile
            ConversationsController conversationsController = loader.getController(); //creeaza o instanta si aici se face initialize() care configureaza coloanele tabelei
            // selectia utilizatorului al doilea pentru conversatie va fi selectia din tabel
            Utilizator u2 = usersTable.getSelectionModel().getSelectedItem();
            conversationsController.setService(utilizatorService, friendshipService,messengerService,user,u2,stage,prev);

            // arat fereastra
            //stage.show();

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    private void handleBack(){
        stage.setScene(previous);
    }

    @FXML
    public void handleDate(){
        try{
            // create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/selectDate.fxml"));
            AnchorPane root = loader.load();

            // create the next stage and a scene
            stage.setTitle("Reports");
            //stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            Scene prev = stage.getScene();
            stage.setScene(scene);


            // obtin controller-ul ferestrei si configurez service-urile
            SelectDateController controller = loader.getController(); //creeaza o instanta si aici se face initialize() care configureaza coloanele tabelei
            controller.setService(utilizatorService, friendshipService,messengerService,serviceReports,user,stage,prev);

            // arat fereastra
            //stage.show();

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }
}
