package sepm.ss16.e0828454.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;
import sepm.ss16.e0828454.service.ServiceImpl;
import sepm.ss16.e0828454.util.DBHandler;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Mainview of the GUI
 * Contains ArticleViewController, InvoiceViewController, and StatisticsViewController
 */
public class MainViewController{
    private static final Logger logger = LogManager.getLogger(MainViewController.class);

    private Connection conn;
    /**
     * IMPORTANT NOTE else NullPointerException!!
     * name of controller must match fx:id of MainView.fxml!
     * eg.:
     * invoiceController -> fx:id="invoice"
     * invoiceViewController -> fx:id=invoiceView"
     */
    @FXML
    private InvoiceViewController invoiceController;
    @FXML
    private ArticleViewController articleController;
    @FXML
    private StatisticsViewController statisticsController;
    @FXML
    private DetailInvoiceViewController detailInvoiceViewController;


    protected Service service;
    protected Stage primaryStage;

    public void initializeView(Service service) throws ServiceException {
        this.service = service;
        articleController.initializeView(service);
        invoiceController.initializeView(service);
        statisticsController.initializeView(service);
        //detailInvoiceViewController.initializeView(service);

    }

    @FXML
    public void onAboutClicked() {
        alertText(Alert.AlertType.INFORMATION, "About", "Wendy's Oster Shop 1.0");
    }

    @FXML
    public void onCloseClicked() {
        Platform.exit();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        articleController.setPrimaryStage(primaryStage);
        invoiceController.setPrimaryStage(primaryStage);
        statisticsController.setPrimaryStage(primaryStage);
    }

    /**
     *
     * @param type Alerttype
     * @param title set alert title text
     * @param content set alert content text
     * @return
     */
    protected Optional<ButtonType> alertText(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(content);
        return alert.showAndWait();
    }

    protected boolean isDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }

    }

    protected boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

