package sepm.ss16.e0828454.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.ls.LSException;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;

import java.time.LocalDate;
import java.util.*;

public class InvoiceCreateViewController extends MainViewController {
    private static final Logger logger = LogManager.getLogger(InvoiceCreateViewController.class);
    private Stage stage;
    @FXML
    private Label dateLabel;
    @FXML
    private Label sumLabel;
    @FXML
    private TableView<Article> tableView;
    @FXML
    private TableColumn<Article, String> col_name;
    @FXML
    private TableColumn<Article, Double> col_price;
    @FXML
    private Spinner<Integer> count;

    @FXML
    private RadioButton toggleCash;
    @FXML
    private RadioButton toggleCredit;

    private ToggleGroup group;

    private List<Article> articles;
    private HashMap<Article,Integer> purchasedArticles;
    private List<DetailInvoice> detailInvoices ;
    private double total;
    private String listing;
    private String payment;


    public void init() {
        try {
            detailInvoices = new ArrayList<DetailInvoice>();
            group = new ToggleGroup();
            toggleCash.setToggleGroup(group);
            toggleCredit.setToggleGroup(group);
            listing = "";
            payment = "";
            articles = service.getAllArticles();
            purchasedArticles = new HashMap<Article,Integer>();
            col_name.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
            col_price.setCellValueFactory(new PropertyValueFactory<Article, Double>("price"));
            count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,1,1));
            dateLabel.setText(LocalDate.now().toString());
            sumLabel.setText(String.valueOf(total));
            tableView.setItems(FXCollections.observableArrayList(articles));
            tableView.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            alertText(Alert.AlertType.ERROR, "init InvoiceCreateViewController", e.getMessage());
        }
    }

    @FXML
    public void onClickAddBtn() {


        Article article = tableView.getSelectionModel().getSelectedItem();
        // if article already exists in the order
        if(purchasedArticles.containsKey(article)) {
            // replace sold count of found article with new sold count
            purchasedArticles.replace(article, purchasedArticles.get(article)+count.getValue());

            // search for existing detail invoice
            for(DetailInvoice elem : detailInvoices) {
                if(article.getId() == elem.getArticleID()) {
                    // update sold count of article
                    elem.setQuantity(purchasedArticles.get(article));
                    break;
                }
            }
        }
        // if a new article is added to the order, set all attributes for detail invoice
        else {
            purchasedArticles.put(article, count.getValue());
            DetailInvoice detailInvoice = new DetailInvoice();
            detailInvoice.setArticleID(article.getId());
            detailInvoice.setName(article.getName());
            detailInvoice.setPurchasePrice(article.getPrice());
            detailInvoice.setQuantity(count.getValue());
            detailInvoice.setCashier("");
            detailInvoices.add(detailInvoice);
        }
        //calculate total amount for invoice and add article to order list
        total += article.getPrice() * count.getValue();
        listing += String.format("%s (%d á %.2f)\n", article.getName(), count.getValue(), article.getPrice());
        sumLabel.setText(String.format("%.2f", total));
    }

    @FXML
    public void onClickSaveBTn() throws ServiceException {

        // if order is empty
        if(listing.length() == 0) {
            alertText(Alert.AlertType.INFORMATION, "New Invoice", "No Articles added to the invoice!");
        }
        // if articles added to the order
        else {

            // set payment method
            if (toggleCash.isSelected()) {
                payment = "Cash";
            } else {
                payment = "Credit Card";
            }
            // show dialog with order listing, total price and payment method
            Optional<ButtonType> result = alertText(Alert.AlertType.CONFIRMATION, "New Invoice", "Issue new Invoice?\n" + listing + String.format("\nSum: %.2f\n", total) + "Payment method: " + payment);

            if (result.get() == ButtonType.OK) {

                // create new invoice and set attributes
                Invoice invoice = new Invoice();
                invoice.setDate(LocalDate.now());
                invoice.setSum(Math.round(total * 100.0) / 100.0);
                //logger.info("size: " +detailInvoices.size());
                invoice.setArticlesCount(purchasedArticles.size());
                invoice.setPayment(payment);
                int newId = service.createInvoice(invoice).getId();
                for (DetailInvoice elem : detailInvoices) {
                    //logger.info(elem.getName() + " " + elem.getQuantity());
                    elem.setInvoiceID(newId);
                }

                // create new detailInvoice with given invoice
                service.createDetailInvoice(detailInvoices);

                // update sold count from purchased articles

                for (Map.Entry<Article, Integer> entry : purchasedArticles.entrySet()) {
                    Article a = entry.getKey();
                    //logger.info(a.getName() + " " +entry.getValue());
                    a.setSold(a.getSold() + entry.getValue());
                    service.editArticle(a);
                }

                alertText(Alert.AlertType.INFORMATION, "New Invoice", "Successfully issued new Invoice!");
                stage.close();

            }
        }
    }


    @FXML
    public void onClickCancelBtn () {
        if(listing.length() > 0) {
            Optional<ButtonType> result = alertText(Alert.AlertType.CONFIRMATION, "Cancel invoice issue","You have unsaved order, are you sure to cancel?");
            if(result.get() == ButtonType.OK) {
                stage.close();
            }
        } else{
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(Service service) {
        this.service = service;
    }

}
