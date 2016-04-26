package sepm.ss16.e0828454.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class DetailInvoiceViewController extends MainViewController{

    private static final Logger logger = LogManager.getLogger(DetailInvoiceViewController.class);

    private Stage stage;
    private Invoice invoice;
    private DetailInvoice detailInvoice;


    @FXML
    private Label invoiceLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label paymentLabel;
    @FXML
    private Label countLabel;
    @FXML
    private Label sumLabel;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button okBtn;


    public void init() {
        invoiceLabel.setText(String.valueOf(invoice.getId()));
        dateLabel.setText(String.valueOf(invoice.getDate()));
        paymentLabel.setText(String.valueOf(invoice.getPayment()));
        countLabel.setText(String.valueOf(invoice.getArticlesCount()));
        sumLabel.setText(String.valueOf(invoice.getSum()));
        List<DetailInvoice> list = this.getDetailInvoice(invoice);
        this.getFormattedStrings(list);
    }

    public List<DetailInvoice> getDetailInvoice(Invoice invoice) {
        try {
            return service.readInvoiceDetails(invoice);
        } catch (ServiceException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    @FXML
    public void onClickOkBtn() {
        stage.close();
    }

    public void getFormattedStrings(List<DetailInvoice> list) {
        List<String> articles = new ArrayList<String>();
        for(DetailInvoice elem: list) {
            articles.add(String.format("%-30s %5d x %.2f", elem.getName(), elem.getQuantity(), elem.getPurchasePrice()));
        }

        //Test data with string formatting length
        //articles.add(String.format("%-30s %d x %.2f", "Riding Boots Leather Brown", 120, 100.00));

        listView.setItems(FXCollections.observableArrayList(articles));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public void initializeView(Service service) throws ServiceException {
        this.service = service;
    }
}
