package socialnetwork.controller;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import socialnetwork.domain.Mesaj;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.ServiceMesaje;
import socialnetwork.service.ServicePrietenii;
import socialnetwork.service.ServiceReports;
import socialnetwork.service.ServiceUtilizatori;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public class ConversationReportController {

    @FXML
    ListView<Mesaj> listMessages;

    ServiceUtilizatori serviceUtilizatori;
    ServicePrietenii servicePrietenii;
    ServiceMesaje serviceMesaje;
    ServiceReports serviceReports;
    Utilizator user;
    Utilizator user2;
    Stage stage;
    Scene previous;
    LocalDate fromDate;
    LocalDate toDate;

    ObservableList<Mesaj> modelMessages = FXCollections.observableArrayList();


    public void setService(ServiceUtilizatori serviceUtilizatori, ServicePrietenii servicePrietenii, ServiceMesaje serviceMesaje, ServiceReports serviceReports, Utilizator user, Utilizator user2, Stage stage, Scene previous) {
        this.serviceUtilizatori = serviceUtilizatori;
        this.servicePrietenii = servicePrietenii;
        this.serviceMesaje = serviceMesaje;
        this.serviceReports = serviceReports;
        this.stage = stage;
        this.user = user;
        this.user2 = user2;
        this.previous = previous;
        initModelMesaje();
    }

    public void setDate(LocalDate from, LocalDate to){
        this.fromDate = from;
        this.toDate = to;
    }
    private void initModelMesaje(){
        modelMessages.setAll(serviceMesaje.getConversatieDate(user.getId(), user2.getId(),fromDate,toDate));
    }
    @FXML
    private void initialize(){
        listMessages.setItems(modelMessages);
    }

    @FXML
    public void handleBack(){
        stage.setScene(previous);
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
            serviceReports.conversationReportPDF(user,user2,fromDate,toDate,getChosenPath());
            MessageInformation.showInformationMessage(null, "PDF generated");
        } catch (FileNotFoundException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (DocumentException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
