package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class loginController implements Initializable {
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton login;
    @FXML private JFXButton register;
    @FXML private Label label;
    @FXML private ImageView close;
    @FXML private ImageView minimize;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.username.setPromptText("Username");
        this.password.setPromptText("Password");
        this.login.setText("Login");
        this.register.setText("Register");
        this.label.setText("Login");
        this.close.setImage(new Image(new File("ui/icons/close.png").toURI().toString()));
        this.close.setFitWidth(20);
        this.close.setPreserveRatio(true);
        this.minimize.setImage(new Image(new File("ui/icons/minimize.png").toURI().toString()));
        this.minimize.setFitWidth(20);
        this.minimize.setPreserveRatio(true);
    }

    public void loginToAccount() {
        String username = this.username.getText();
        String password = this.password.getText();
        System.out.println(username + password);
        Alert alert;
        if (true)
        {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "Logged In Successfully!", ButtonType.OK);
            alert.setTitle("Welcome");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else
        {
            alert = new Alert(Alert.AlertType.ERROR, "Wrong Username or Password!", ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void createNewAccount(MouseEvent event) throws IOException {

//        Parent root = FXMLLoader.load(getClass().getResource("register/register.fxml"));
//        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
//        stage.setTitle("Register");
//        Scene scene = new Scene(root);
//        scene.setFill(Color.TRANSPARENT);
//        stage.setScene(scene);
//        stage.show();

    }
}
