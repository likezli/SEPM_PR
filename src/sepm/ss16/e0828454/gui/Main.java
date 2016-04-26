package sepm.ss16.e0828454.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.dao.JDBCArticleDAO;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;
import sepm.ss16.e0828454.service.ServiceImpl;
import sepm.ss16.e0828454.util.DBHandler;

import java.sql.Connection;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            this.primaryStage = primaryStage;
            Service service = new ServiceImpl();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            controller.initializeView(service);
            controller.setPrimaryStage(primaryStage);
            this.primaryStage.setTitle("Wendys Oster Shop");
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.show();
            logger.info("Starting JavaFX Application");
            //JDBCArticleDAO aDao = new JDBCArticleDAO();
            //aDao.create(null);
        } catch (ServiceException e) {
            logger.info(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
