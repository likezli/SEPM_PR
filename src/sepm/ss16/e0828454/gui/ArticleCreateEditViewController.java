package sepm.ss16.e0828454.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.service.Service;
import sepm.ss16.e0828454.service.ServiceException;

import java.io.File;
import java.net.MalformedURLException;


public class ArticleCreateEditViewController extends MainViewController{

    private static final Logger logger = LogManager.getLogger(ArticleCreateEditViewController.class);

    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_price;
    @FXML
    private TextField tf_producer;
    @FXML
    private Button btn_image;
    @FXML
    private Label picturePathLabel;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;


    private String name;
    private double price;
    private String producer;
    private String picture;

    private Stage stage;
    private Article article;
    private FileChooser imageChooser;

    public void init() {
        if(article != null) {
            logger.info("entering edit mode");
            tf_name.setText(String.valueOf(article.getName()));
            tf_price.setText(String.valueOf(article.getPrice()));
            tf_producer.setText(String.valueOf(article.getProducer()));
            picturePathLabel.setText(String.valueOf(article.getPicture()));
        } else {
            logger.info("entering create mode");
        }
    }

    @FXML
    public void onClickImageBtn() {
        imageChooser = new FileChooser();
        imageChooser.setTitle("Open Image File");
        imageChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif", "*.jpg"));
        File defaultDir = new File("./res/img");
        imageChooser.setInitialDirectory(defaultDir);
        File file = imageChooser.showOpenDialog(stage);
        if(file != null) {
            try {
                String path = file.toURI().toURL().toExternalForm().substring(46);
                //logger.info(path);
                picturePathLabel.setText(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void onClickSaveButton() {
        // EDIT Mode
        if(article != null) {
            if(changedArticle()) {
                if(inputIsValid()) {
                    try {
                        article.setName(name);
                        article.setPrice(price);
                        article.setProducer(producer);
                        article.setPicture(picture);
                        service.editArticle(article);
                        alertText(Alert.AlertType.INFORMATION, "Edit Article", "Article Edited successfully");
                        stage.close();
                    } catch (ServiceException e) {
                        alertText(Alert.AlertType.ERROR, "Edit Article", "Error in editing article");
                    }
                }
            } else {
                alertText(Alert.AlertType.INFORMATION, "Create/Edit Article", "Article attributes not changed");
            }
        // CREATE Mode
        } else {
            if(inputIsValid()) {
                article = new Article();
                article.setName(name);
                article.setPrice(price);
                article.setProducer(producer);
                article.setSold(0);
                article.setPicture(picture);
                article.setDeleted(false);
                try {
                    alertText(Alert.AlertType.INFORMATION, "Create Article", "Article Created successfully");
                    service.createArticle(article);
                    stage.close();
                } catch (ServiceException e) {
                    alertText(Alert.AlertType.ERROR, "Create Article", "Error in creating article");
                }
            }
        }
    }


    @FXML
    public void onClickCancelButton() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    private boolean changedArticle() {
        //logger.info(article.getName() +" | "+ tf_name.getText().trim());
        //logger.info(article.getProducer() +" | "+ tf_producer.getText().trim());
        //logger.info(article.getPrice().toString() +" | "+ tf_price.getText().trim());
        //logger.info(article.getPicture() +" | "+ picturePathLabel.getText().trim());
        if(article.getName().equals(tf_name.getText().trim()) && article.getProducer().equals(tf_producer.getText().trim()) &&
                article.getPrice().toString().equals(tf_price.getText().trim()) && article.getPicture().equals(picturePathLabel.getText().trim())) {
            return false;
        } else {
            return true;
        }
    }


    private boolean inputIsValid() {
        String msg = "";

        if(tf_name.getText() == null || tf_name.getText().trim().isEmpty()) {
            msg += "Name can not be empty!\n";
        }

        if(tf_price.getText() == null || tf_price.getText().trim().isEmpty()) {
            msg += "Price can not be empty!\n";
        } else {
            if(!isDouble(tf_price.getText().trim())) {
                msg += "Invalid price input!\n";
            } else {
                if(Double.parseDouble(tf_price.getText().trim()) <= 0) {
                    msg += "Price can not be negative!\n";
                }
            }
        }

        if(tf_producer.getText() == null || tf_producer.getText().trim().isEmpty()) {
            msg += "Producer can not be empty\n";
        }

        if(msg.length() == 0) {
            name = tf_name.getText().trim();
            price = (double)Math.round(Double.parseDouble(tf_price.getText().trim())* 100) / 100; // Round to 2 decimals after comma by value * 100 /100
            producer = tf_producer.getText().trim();
            picture = picturePathLabel.getText().trim();
            logger.info("name: " +name);
            return true;
        } else {
            alertText(Alert.AlertType.ERROR, "Create/Edit Article", msg);
            return false;
        }
    }
}
