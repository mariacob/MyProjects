package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.DTOs.FriendDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.ServiceMesaje;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceReports;
import socialnetwork.service.ServiceUtilizatori;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SelectDateController {
    @FXML
    DatePicker fromDatePicker;
    @FXML
    DatePicker toDatePicker;
    @FXML
    Button buttonShowActivity;
    @FXML
    TableView<FriendDTO> usersTable;
    @FXML
    TableColumn<FriendDTO,String> firstNameColumn;
    @FXML
    TableColumn<FriendDTO,String> lastNameColumn;
    @FXML
    Button showConversationButton;

    ServiceUtilizatori serviceUtilizatori;
    ServicePrietenii servicePrietenii;
    ServiceMesaje serviceMesaje;
    ServiceReports serviceReports;
    Utilizator user;
    Stage stage;
    Scene previous;

    ObservableList<FriendDTO> modelUsers = FXCollections.observableArrayList();

    public void setService(ServiceUtilizatori serviceUtilizatori, ServicePrietenii servicePrietenii, ServiceMesaje serviceMesaje, ServiceReports serviceReports, Utilizator user,Stage stage, Scene previous) {
        this.serviceUtilizatori = serviceUtilizatori;
        this.servicePrietenii = servicePrietenii;
        this.serviceMesaje = serviceMesaje;
        this.serviceReports = serviceReports;
        this.stage = stage;
        this.user = user;
        this.previous = previous;
        initModelUsers();
    }

    private void initModelUsers(){
        Iterable<FriendDTO> users = servicePrietenii.getFriendsOfUser(user.getId());
        List<FriendDTO> usersList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        modelUsers.setAll(usersList);
    }

    @FXML
    public void initialize(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO,String>("lastName"));
        usersTable.setItems(modelUsers);
    }

    @FXML
    public void handleShowActivity(){
        try{
            if(fromDatePicker.getValue() == null || toDatePicker.getValue() == null)
                MessageAlert.showErrorMessage(null, "Please select a date");
            else {
                // create a new loader
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/activityReport.fxml"));
                AnchorPane root = loader.load();

                // create the next stage and a scene
                stage.setTitle("Report");
                Scene scene = new Scene(root);
                Scene prev = stage.getScene();
                stage.setScene(scene);

                // obtin controller-ul ferestrei si configurez service-urile
                ActivityReportController controller = loader.getController();
                controller.setDate(fromDatePicker.getValue(), toDatePicker.getValue());
                controller.setService(serviceUtilizatori, servicePrietenii, serviceMesaje, serviceReports, user, stage, prev);

                // arat fereastra
                //stage.show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleShowConversation(){
        try{
            if(fromDatePicker.getValue() == null || toDatePicker.getValue() == null)
                MessageAlert.showErrorMessage(null, "Please select a date");
            else {
                // create a new loader
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/conversationReport.fxml"));
                AnchorPane root = loader.load();

                // create the next stage and a scene
                stage.setTitle("Report");
                Scene scene = new Scene(root);
                Scene prev = stage.getScene();
                stage.setScene(scene);

                // obtin controller-ul ferestrei si configurez service-urile
                ConversationReportController controller = loader.getController();
                controller.setDate(fromDatePicker.getValue(), toDatePicker.getValue());
                controller.setService(serviceUtilizatori, servicePrietenii, serviceMesaje, serviceReports, user, serviceUtilizatori.findEntity(usersTable.getSelectionModel().getSelectedItem().getId()), stage, prev);

                // arat fereastra
                //stage.show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(){
        stage.setScene(previous);
    }
}
