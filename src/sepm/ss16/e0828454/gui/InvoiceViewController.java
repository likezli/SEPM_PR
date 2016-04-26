package sepm.ss16.e0828454.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Li on 30.03.2016.
 */

public class InvoiceViewController extends MainViewController{

    private static final Logger logger = LogManager.getLogger(InvoiceViewController.class);
    @FXML
    private TableView<Invoice> tableView;
    @FXML
    private TableColumn<Invoice, Integer> col_id;
    @FXML
    private TableColumn<Invoice, LocalDate> col_date;
    @FXML
    private TableColumn<Invoice, Double> col_sum;
    @FXML
    private TableColumn<Invoice, Integer> col_count;
    @FXML
    private TableColumn<Invoice, String> col_payment;
    @FXML
    private Button resetBtn;
    @FXML
    private Button showDetailsBtn;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private TextField sumFrom;
    @FXML
    private TextField sumTo;

    private double sumFrom_;
    private double sumTo_;
    private LocalDate dateFrom_;
    private LocalDate dateTo_;

    private List<Invoice> invoices;



    public void initialize() {
        logger.info("Init in InvoiceViewController");
        col_id.setCellValueFactory(new PropertyValueFactory<Invoice, Integer>("id"));
        col_date.setCellValueFactory(new PropertyValueFactory<Invoice, LocalDate>("date"));
        col_sum.setCellValueFactory(new PropertyValueFactory<Invoice, Double>("sum"));
        col_count.setCellValueFactory(new PropertyValueFactory<Invoice, Integer>("articlesCount"));
        col_payment.setCellValueFactory(new PropertyValueFactory<Invoice, String>("payment"));
    }

    public void initializeView(Service service) throws ServiceException {
        this.service = service;
        invoices = service.getAllInvoices();
        tableView.setItems(FXCollections.observableArrayList(invoices));
        tableView.getSelectionModel().selectFirst();

    }

    @FXML
    public void searchOnEnter() {

        if(isValidInput()) {
            try {
                Invoice from = new Invoice();
                Invoice to = new Invoice();
                from.setId(9999);
                from.setSum(sumFrom_);
                from.setDate(dateFrom_);
                from.setPayment("");
                from.setArticlesCount(0);
                to.setId(9999);
                to.setSum(sumTo_);
                to.setDate(dateTo_);
                to.setPayment("");
                to.setArticlesCount(Integer.MAX_VALUE);
                invoices = service.search(from, to);
                refresh(invoices);
                if(invoices.isEmpty()) {
                    alertText(Alert.AlertType.INFORMATION, "Invoice Search", "No invoices found with given information");
                }
            } catch (ServiceException e) {

                alertText(Alert.AlertType.ERROR, "Search Invoice", e.getMessage());
            }
        }
    }

    @FXML
    public void onClickResetBtn() {
        dateFrom.setValue(null);
        dateTo.setValue(null);
        sumFrom.clear();
        sumTo.clear();

        try {
            invoices = service.getAllInvoices();
            refresh(invoices);
        } catch (ServiceException e) {
            alertText(Alert.AlertType.WARNING, "Clear Search", "Clear search fields failed");
        }

    }

    @FXML
    public void onClickDetailsBtn () {
        Invoice inv = tableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailInvoiceView.fxml"));
            AnchorPane titledPane = loader.load();
            Scene scene = new Scene(titledPane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Invoice Details");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            DetailInvoiceViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setService(service);
            controller.setInvoice(inv);
            controller.init();

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onClickCreateInvoice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InvoiceCreateView.fxml"));
            AnchorPane titledPane = loader.load();
            Scene scene = new Scene(titledPane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Issue an Invoice");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            InvoiceCreateViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setService(service);
            controller.init();
            stage.showAndWait();
            try {
                invoices = service.getAllInvoices();
            } catch (ServiceException e) {
                alertText(Alert.AlertType.ERROR, "Issue Invoice", "Error while creating new invoice!");
            }
            refresh(invoices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh (List<Invoice> invoices) {
        tableView.setItems(FXCollections.observableArrayList(invoices));
        tableView.getSelectionModel().selectFirst();
        tableView.getColumns().get(0).setVisible(false);
        tableView.getColumns().get(0).setVisible(true);
    }

    private boolean isValidInput() {
        String msg = "";
        if(!sumFrom.getText().trim().isEmpty()) {
            if(!isDouble(sumFrom.getText().trim())) {
                msg += "Invalid sum from input!\n";
            } else {
                sumFrom_ = Double.parseDouble(sumFrom.getText().trim());
            }
        } else {
            sumFrom_ = 0.0;
        }

        if(!sumTo.getText().trim().isEmpty()) {
            if(!isDouble(sumTo.getText().trim())) {
                msg += "Invalid sum to input!\n";
            } else {
                sumTo_ = Double.parseDouble(sumTo.getText().trim());
            }
        } else {
            sumTo_ = Double.MAX_VALUE;
        }

        // if not date from set -> 10 days before current date
        if(dateFrom.getValue() != null) {
            dateFrom_ = dateFrom.getValue();
        } else {
            dateFrom_ = LocalDate.now().minusDays(10);
        }

        if(dateTo.getValue() != null) {
            dateTo_ = dateTo.getValue();
        } else {
            dateTo_ = LocalDate.now();
        }

        if(sumTo_ < sumFrom_) {
            double temp = sumFrom_;
            sumFrom_ = sumTo_;
            sumTo_ = temp;
        }

        if(sumFrom_ < 0.0 || sumTo_ < 0.0) {
            msg += "sum input should be positive!\n";
        }

        if(msg.length() == 0) {
            return true;
        } else {
            alertText(Alert.AlertType.ERROR, "Search Invoice", msg);
            return false;
        }
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
}
