package sepm.ss16.e0828454.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Li on 15.03.2016.
 */
public class ArticleViewController extends MainViewController{

    private static final Logger logger = LogManager.getLogger(ArticleViewController.class);

    @FXML
    private TableView<Article> tableView;
    @FXML
    private TableColumn<Article, Integer> col_id;
    @FXML
    private TableColumn<Article, String> col_name;
    @FXML
    private TableColumn<Article, Double> col_price;
    @FXML
    private TableColumn<Article, String> col_producer;
    @FXML
    private TableColumn<Article, Integer> col_sold;
    @FXML
    private ImageView articleImageView;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private TextField soldFrom;
    @FXML
    private TextField soldTo;
    @FXML
    private Button resetBtn;


    private String name;
    private double priceFrom_;
    private double priceTo_;
    private int soldFrom_;
    private int soldTo_;
    private List<Article> articles = null;

    public void initialize() {
        logger.info("Init in ArticleViewController");
        col_id.setCellValueFactory(new PropertyValueFactory<Article,Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
        col_price.setCellValueFactory(new PropertyValueFactory<Article, Double>("price"));
        col_producer.setCellValueFactory(new PropertyValueFactory<Article, String>("producer"));
        col_sold.setCellValueFactory(new PropertyValueFactory<Article, Integer>("sold"));
    }

    public void initializeView(Service service) throws ServiceException{
        this.service = service;
        articles = service.getAllArticles();
        tableView.setItems(FXCollections.observableArrayList(articles));
        tableView.getSelectionModel().selectFirst();
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Article>() {
            @Override
            public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
                //logger.info("User selected row - showing picture for the selected horse");
                File file;
                if(newValue==null || newValue.getPicture().isEmpty() || !newValue.getPicture().startsWith("res")){
                    file = new File("res\\img\\dummy.jpg");

                } else {
                    file = new File(newValue.getPicture());
                }
                Image image = null;
                try {
                    //logger.info(file.toURI().toURL().toExternalForm());
                    //logger.info(file.getPath());
                    image = new Image(file.toURI().toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                articleImageView.setImage(image);
                articleImageView.setVisible(true);
            }
        });
    }

    @FXML
    public void onClickDeleteBtn() {
        Article a = tableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alertText(Alert.AlertType.CONFIRMATION, "Delete Horse", "Are you sure you want to delete this horse?");
            if(result.get() == ButtonType.OK) {
                service.deleteArticle(a);
                tableView.getItems().remove(a);
                logger.info("User deleted an article");
                alertText(Alert.AlertType.INFORMATION, "Delete Horse", "Horse successfully deleted");
            } else {
                logger.info("User cancelled");
            }

        } catch (ServiceException e) {
            logger.info("Could not delete Article:" + e.getMessage());
        }
        //logger.info("Delete Button clicked");
    }

    @FXML
    public void onClickEditBtn() {
        Article a = tableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleCreateEditView.fxml"));
            AnchorPane titledPane = loader.load();
            Scene scene = new Scene(titledPane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Edit Article");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            ArticleCreateEditViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setService(service);
            controller.setArticle(a);
            controller.init();
            stage.showAndWait();
            try {
                articles = service.getAllArticles();
                //logger.info("refresh here");
                refresh(articles);
            } catch (ServiceException e) {
                alertText(Alert.AlertType.ERROR, "Error in Edit Article", "Error while editing an article");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickNewBtn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleCreateEditView.fxml"));
            AnchorPane titledPane = loader.load();
            Scene scene = new Scene(titledPane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Create Article");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            ArticleCreateEditViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setService(service);
            controller.init();
            stage.showAndWait();
            try {
                articles = service.getAllArticles();
                //logger.info("refresh here");
                refresh(articles);
            } catch (ServiceException e) {
                alertText(Alert.AlertType.ERROR, "Error in Create Article", "Error while creating an article");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickResetBtn() {
        nameSearch.clear();
        priceFrom.clear();
        priceTo.clear();
        soldFrom.clear();
        soldTo.clear();

        try {
            articles = service.getAllArticles();
            refresh(articles);
        } catch (ServiceException e) {
            alertText(Alert.AlertType.WARNING, "Clear Search", "Clear search fields failed");
        }
    }

    @FXML
    public void searchOnEnter() {


            if(isInputValid()) {
                try {
                    logger.info(name + " " + priceFrom_ + " " +priceTo_+" "+ soldFrom_+" "+soldTo_);
                    articles = service.search(new Article(null, name, priceFrom_, null,soldFrom_,null,false),new Article(null, name, priceTo_, null,soldTo_,null,false));
                    refresh(articles);
                    if(articles.isEmpty()) {
                        alertText(Alert.AlertType.INFORMATION, "Article Search", "No Articles found with given information");
                    }
                } catch (ServiceException e) {
                    logger.info(e.getMessage());
                    alertText(Alert.AlertType.WARNING, "Search Article", "Article Search failed");
                }
            }
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void refresh (List<Article> articles) {
        tableView.setItems(FXCollections.observableArrayList(articles));
        tableView.getSelectionModel().selectFirst();
        tableView.getColumns().get(0).setVisible(false);
        tableView.getColumns().get(0).setVisible(true);
    }

    private boolean isInputValid() {
        String msg = "";

        if(nameSearch.getText() == null || nameSearch.getText().trim().length() < 0) {
            msg += "Invalid name input!\n";
        } else {
            name = nameSearch.getText().trim();
        }

        if(!priceFrom.getText().trim().isEmpty()) {
            if(!isDouble(priceFrom.getText().trim())) {
                msg += "Invalid priceFrom input!\n";
            } else {
                priceFrom_ = Double.parseDouble(priceFrom.getText().trim());
            }
        } else {
            priceFrom_ = 0.0;
        }

        if(!priceTo.getText().trim().isEmpty()) {
            if(!isDouble(priceTo.getText().trim())) {
                msg += "Invalid priceTo input!\n";
            } else {
                priceTo_ = Double.parseDouble(priceTo.getText().trim());
            }
        } else {
            priceTo_ = Double.MAX_VALUE;
        }

        if(!soldFrom.getText().trim().isEmpty()) {
            if(!isInteger(soldFrom.getText().trim())) {
                msg += "Invalid soldFrom input!\n";
            } else {
                soldFrom_ = Integer.parseInt(soldFrom.getText().trim());
            }
        } else {
            soldFrom_ = 0;
        }

        if(!soldTo.getText().trim().isEmpty()) {
            if(!isInteger(soldTo.getText().trim())) {
                msg += "Invalid soldTo input!\n";
            } else {
                soldTo_ = Integer.parseInt(soldTo.getText().trim());
            }
        } else {
            soldTo_ = Integer.MAX_VALUE;
        }

        if (priceFrom_ > priceTo_) {
            double temp = priceFrom_;
            priceFrom_ = priceTo_;
            priceTo_ = temp;
        }

        if(soldFrom_ > soldTo_) {
            int temp = soldFrom_;
            soldFrom_ = soldTo_;
            soldTo_ = temp;
        }

        if(priceFrom_ < 0.0 || priceTo_ < 0.0) {
            msg += "price input should be positive!\n";
        }

        if(soldFrom_ < 0 || soldTo_ <0) {
            msg += "sold input should be positive!\n";
        }


        if(msg.length() == 0) {
            return true;
        } else {
            alertText(Alert.AlertType.ERROR,"Search Article", msg);
            return false;
        }
    }
}
