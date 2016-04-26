package sepm.ss16.e0828454.gui;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.dao.DaoException;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;
import sun.security.provider.ConfigFile;

import java.time.LocalDate;
import java.util.*;

public class StatisticsViewController extends MainViewController{
    private static final Logger logger = LogManager.getLogger(StatisticsViewController.class);
    private Stage stage;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private ComboBox<String> articleChooser;
    @FXML
    private Label articleName;
    @FXML
    private Label articleCount;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField sold_tf;
    @FXML
    private RadioButton minMaxCheck;
    @FXML
    private RadioButton minCheck;
    @FXML
    private RadioButton maxCheck;
    @FXML
    private RadioButton topFlopCheck;
    @FXML
    private Spinner<Integer> count;
    @FXML
    private RadioButton topCheck;
    @FXML
    private RadioButton flopCheck;
    @FXML
    private ComboBox<Integer> percentage;
    @FXML
    private TextField amount_tf;

    private ToggleGroup pricingGroup;
    private ToggleGroup minMaxGroup;
    private ToggleGroup topFlopGroup;

    private List<Article> articles;
    private ObservableList<String> articleNames;
    private ObservableList<String> chosenArticle;
    private ObservableList<Integer> percentage_;

    public void initialize() {
        barChart.setTitle("Article sold Statistics");
        articleNames = FXCollections.observableArrayList();
        chosenArticle = FXCollections.observableArrayList();
        percentage_ = FXCollections.observableArrayList();

        pricingGroup = new ToggleGroup();
        minMaxCheck.setToggleGroup(pricingGroup);
        topFlopCheck.setToggleGroup(pricingGroup);

        minMaxGroup = new ToggleGroup();
        minCheck.setToggleGroup(minMaxGroup);
        maxCheck.setToggleGroup(minMaxGroup);

        topFlopGroup = new ToggleGroup();
        topCheck.setToggleGroup(topFlopGroup);
        flopCheck.setToggleGroup(topFlopGroup);



        minMaxCheck.setSelected(true);

        pricingGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(minMaxCheck.isSelected()) {
                    sold_tf.setDisable(false);
                    minCheck.setDisable(false);
                    minCheck.setSelected(true);
                    maxCheck.setDisable(false);
                    count.setDisable(true);
                    topCheck.setDisable(true);
                    flopCheck.setDisable(true);
                    logger.info("disabled");
                } else {
                    sold_tf.setDisable(true);
                    minCheck.setDisable(true);
                    maxCheck.setDisable(true);
                    count.setDisable(false);
                    topCheck.setDisable(false);
                    flopCheck.setDisable(false);
                    logger.info("yo");
                }
            }
        });

        amount_tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.trim().length() > 0){
                    percentage.setDisable(true);
                } else {
                    percentage.setDisable(false);
                }
            }
        });
    }

    public void initializeView(Service service) throws ServiceException {
        this.service = service;

        percentage_.addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100);
        percentage.setItems(percentage_);
        count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,1,1));

        articles = service.getAllArticles();
        for(Article elem : articles) {
            articleNames.add(elem.getName());
        }
        articleChooser.setItems(articleNames);
        xAxis.setCategories(articleNames);
        barChart.setTitle("Article sold Statistics");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for(int i = 0; i < articles.size(); i++) {
            series.getData().add(new XYChart.Data<>(articleNames.get(i), articles.get(i).getSold()));
        }

        barChart.getData().add(series);

    }

    @FXML
    public void onClickShowStatsBtn() throws ServiceException{
        articles = service.getAllArticles();

        if(articleNames.contains(articleChooser.getValue())) {
            barChart.getData().clear();
            if(!chosenArticle.isEmpty()) {
                chosenArticle.clear();
            }
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            String name = articleChooser.getValue();
            chosenArticle.add(name);
            xAxis = new CategoryAxis(chosenArticle);
            int sold = 0;
            for(Article elem : articles) {
                if(elem.getName().equals(name)) {
                    sold = elem.getSold();
                }
            }
            series.getData().add(new XYChart.Data<>(name, sold));
            barChart.getData().add(series);
            articleName.setText(" "+name);
            articleCount.setText("Sold: "+String.valueOf(sold));
        } else {
            articleNames.clear();
            for(Article elem : articles) {
                articleNames.add(elem.getName());
            }

            articleChooser.setItems(articleNames);
            xAxis.setCategories(articleNames);
            barChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for(int i = 0; i < articles.size(); i++) {
                series.getData().add(new XYChart.Data<>(articleNames.get(i), articles.get(i).getSold()));
            }

            barChart.getData().add(series);
            articleName.setText("");
            articleCount.setText("");
        }
    }

    @FXML public void onClickAdaptBtn() throws ServiceException{

        if(datePicker.getValue() != null) {
            Invoice from = new Invoice(9999, datePicker.getValue(), 0.0, 0, "");
            Invoice to = new Invoice(9999, LocalDate.now(), Double.MAX_VALUE, Integer.MAX_VALUE, "");
            List<Invoice> invoices = service.search(from, to);

            ArrayList<DetailInvoice> detailInvoices = new ArrayList<DetailInvoice>();
            HashMap<Integer, Integer> articlesSold = new HashMap<Integer, Integer>();
            logger.info(invoices.isEmpty());
            for(Invoice elem : invoices) {
                //logger.info(elem);
                detailInvoices.addAll(service.readInvoiceDetails(elem));
            }

            for(DetailInvoice elem : detailInvoices) {
                //logger.info(elem);
                if(articlesSold.containsKey(elem.getArticleID())) {
                    articlesSold.put(elem.getArticleID(), articlesSold.get(elem.getArticleID())+elem.getQuantity());
                } else {
                    articlesSold.put(elem.getArticleID(), elem.getQuantity());
                }
            }

            for(Map.Entry<Integer,Integer> entry: articlesSold.entrySet()) {
                logger.info("AID: " +entry.getKey() + "| quantity: " +entry.getValue());
            }

            ArrayList<Article> editArticles = new ArrayList<Article>();

            if (minMaxCheck.isSelected()) {
                if (!sold_tf.getText().trim().isEmpty() && isInteger(sold_tf.getText().trim())) {
                    if (maxCheck.isSelected()) {
                        for(Map.Entry<Integer,Integer> entry: articlesSold.entrySet()) {
                            if(entry.getValue() <= Integer.valueOf(sold_tf.getText().trim())) {
                                //logger.info("KEY: " +entry.getKey());
                                editArticles.add(service.getArticleById(entry.getKey()));
                            }
                        }
                        Optional<ButtonType> result = alertText(Alert.AlertType.CONFIRMATION, "Adapt price", "Adapt price for " +editArticles.size() +" elements?");

                        if(result.get() == ButtonType.OK) {
                            for(Article elem: editArticles) {
                                // set new price with amount from textfield amount_tf, minimum price of 0.1
                                if(percentage.isDisabled() && amount_tf.getText().trim() != null && isDouble(amount_tf.getText().trim())) {
                                    double temp = Math.max(elem.getPrice() - (Double.valueOf(amount_tf.getText().trim())), 0.1);
                                    elem.setPrice(Math.round(temp*10)/10.0); // round to 1 decimal
                                    service.editArticle(elem);
                                    // set new price with percentage from combobox percentage, minimum of 0.1
                                } else if(!percentage.isDisabled() &&  percentage.getValue() != null && percentage.getValue() > 0) {
                                    double temp = Math.max(elem.getPrice() - (elem.getPrice() * (percentage.getValue()/100.0)), 0.1);
                                    elem.setPrice(Math.round(temp*10)/10.0); // round to 1 decimal
                                    service.editArticle(elem);
                                } else {
                                    alertText(Alert.AlertType.INFORMATION, "Adapt Price", "Please enter correct amount or percentage for price adaption!");
                                }
                            }
                        }

                    } else {
                        for(Map.Entry<Integer,Integer> entry: articlesSold.entrySet()) {
                            if(entry.getValue() >= Integer.valueOf(sold_tf.getText().trim())) {
                                editArticles.add(service.getArticleById(entry.getKey()));
                            }
                        }

                        Optional<ButtonType> result = alertText(Alert.AlertType.CONFIRMATION, "Adapt price", "Adapt price for " +editArticles.size() +" elements?");

                        if(result.get() == ButtonType.OK) {
                            for(Article elem: editArticles) {
                                // set new price with amount from textfield amount_tf, minimum price of 0.1
                                if(percentage.isDisabled() && amount_tf.getText().trim() != null && isDouble(amount_tf.getText().trim())) {
                                    double temp = elem.getPrice() + (Double.valueOf(amount_tf.getText().trim()));
                                    elem.setPrice(Math.round(temp*10)/10.0); // round to 1 decimal
                                    service.editArticle(elem);
                                    // set new price with percentage from combobox percentage, minimum of 0.1
                                } else if(!percentage.isDisabled() &&  percentage.getValue() != null && percentage.getValue() > 0) {
                                    double temp = elem.getPrice() + (elem.getPrice() * (percentage.getValue()/100.0));
                                    elem.setPrice(Math.round(temp*10)/10.0); // round to 1 decimal
                                    service.editArticle(elem);
                                } else {
                                    alertText(Alert.AlertType.INFORMATION, "Adapt Price", "Please enter correct amount or percentage for price adaption!");
                                }
                            }
                        }
                    }


                } else {
                    alertText(Alert.AlertType.WARNING, "Adapt price", "Invalid sold input! Enter a correct Integer value!");
                }
            } else {
                if(count.getValue() != null && count.getValue()>0) {
                    if(topCheck.isSelected()) {

                    } else {

                    }
                }
            }
        } else {
            alertText(Alert.AlertType.WARNING, "Adapt Price", "Please choose a date!");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
