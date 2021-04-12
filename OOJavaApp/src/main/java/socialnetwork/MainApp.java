package socialnetwork;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.LoginController;
import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.CererePrietenieDB;
import socialnetwork.repository.database.MesajeDB;
import socialnetwork.repository.database.PrietenieDatabase;
import socialnetwork.repository.database.UtilizatorDatabase;
import socialnetwork.service.*;
import socialnetwork.service.validators.CerereValidator;
import socialnetwork.service.validators.MesajValidator;
import socialnetwork.service.validators.PrietenieValidator;
import socialnetwork.service.validators.UtilizatorValidator;

import javafx.application.Application;
import java.io.IOException;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class MainApp extends Application {
        String fileNameU=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileNameP=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");

        //String fileName="data/users.csv";

//        Repository<Long,Utilizator> userFileRepository = new UtilizatorFile(fileNameU, new UtilizatorValidator());
        Repository<Tuple<Long,Long>,Prietenie> dbRepoP  = new PrietenieDatabase(new PrietenieValidator(), "jdbc:sqlserver://localhost:53570;integratedSecurity=true");
        Repository<Long,Utilizator> dbRepoU = new UtilizatorDatabase(new UtilizatorValidator(), "jdbc:sqlserver://localhost:53570;integratedSecurity=true");
        Repository<Long, Mesaj> dbRepoM= new MesajeDB(new MesajValidator(),"jdbc:sqlserver://localhost:53570;integratedSecurity=true", dbRepoU);
        Repository<Tuple<Long,Long>, CererePrietenie> dbRepoC = new CererePrietenieDB(new CerereValidator(),"jdbc:sqlserver://localhost:53570;integratedSecurity=true");
        ServiceUtilizatori servU = new ServiceUtilizatori(dbRepoU,dbRepoP);
        ServicePrietenii servP = new ServicePrietenii(dbRepoP,dbRepoU);
        ServiceMesaje servM = new ServiceMesaje(dbRepoM, dbRepoU);
        ServiceCereriPrietenie servC = new ServiceCereriPrietenie(dbRepoC,dbRepoP,dbRepoU,servP);
        ServiceReports serviceReports = new ServiceReports(servP,servM);
    public static void main(String[] args) throws SQLException {
        launch(args);

        //
//        UI ui = new UI(servU,servP,servM,servC);
//        try {
//            ui.startUI();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initView(primaryStage);
        primaryStage.show();
    }
    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/views/loginUtilizator.fxml"));
        AnchorPane loginLayout = loginLoader.load();
        primaryStage.setScene(new Scene(loginLayout));

        LoginController loginController = loginLoader.getController();
        loginController.setService(servU, servP, servM, servC, serviceReports, primaryStage);

    }
}


