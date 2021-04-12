package socialnetwork.controller;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import socialnetwork.domain.DTOs.FriendDTO;
import socialnetwork.domain.Mesaj;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.ServiceMesaje;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceReports;
import socialnetwork.service.ServiceUtilizatori;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public class ActivityReportController {

    @FXML
    ListView<FriendDTO> newFriendsList;
    @FXML
    ListView<Mesaj> newMessagesList;
    ServiceUtilizatori serviceUtilizatori;
    ServicePrietenii servicePrietenii;
    ServiceMesaje serviceMesaje;
    ServiceReports serviceReports;
    Utilizator user;
    Stage stage;
    Scene previous;
    LocalDate fromDate;
    LocalDate toDate;

    ObservableList<FriendDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<Mesaj> modelMessages = FXCollections.observableArrayList();


    public void setService(ServiceUtilizatori serviceUtilizatori, ServicePrietenii servicePrietenii, ServiceMesaje serviceMesaje, ServiceReports serviceReports, Utilizator user,Stage stage, Scene previous) {
        this.serviceUtilizatori = serviceUtilizatori;
        this.servicePrietenii = servicePrietenii;
        this.serviceMesaje = serviceMesaje;
        this.serviceReports = serviceReports;
        this.stage = stage;
        this.user = user;
        this.previous = previous;
        initModelFriends();
        initModelMesaje();
    }

    public void setDate(LocalDate from, LocalDate to){
        this.fromDate = from;
        this.toDate = to;
    }

    private void initModelFriends(){
        modelFriends.setAll(servicePrietenii.getFriendsDate(user.getId(),fromDate,toDate));
    }
    private void initModelMesaje(){
        modelMessages.setAll(serviceMesaje.getMesajeDate(user.getId(),fromDate,toDate));
    }

    @FXML
    private void initialize(){
        newFriendsList.setItems(modelFriends);
        newMessagesList.setItems(modelMessages);
    }

    /**
     *
     * @return path chosen by the user
     */
    private String getChosenPath(){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("C:\\Users\\Maria\\Desktop\\fac-sem3\\MAP"));
            directoryChooser.setTitle("Choose a path");
            File chosen = directoryChooser.showDialog(stage);
            return chosen.getAbsolutePath();
    }

    /**
     * handler for the save to pdf button
     */
    @FXML
    public void handleSaveToPDF(){
        try {
            serviceReports.activityReportPDF(user,fromDate,toDate,getChosenPath());
            MessageInformation.showInformationMessage(null, "PDF generated");
        } catch (FileNotFoundException e) {
           MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (DocumentException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
    @FXML
    public void handleBack(){
        stage.setScene(previous);
    }
}
