package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class loginController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton login;
    @FXML private JFXButton register;
    @FXML private Label label;
    @FXML private ImageView close;
    @FXML private ImageView minimize;

    private double x;
    private double y;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.username.setPromptText("Username");
        this.password.setPromptText("Password");
        this.login.setText("Login");
        this.register.setText("Register");
        this.label.setText("Login");
//        File tmpFile = new File();
//        this.close.setImage(new Image(new File("ui/icons/close.png").toURI().toString()));
        this.close.setFitWidth(16);
        this.close.setPreserveRatio(true);
//        this.minimize.setImage(new Image(new File("ui/icons/minimize.png").toURI().toString()));
        this.minimize.setFitWidth(16);
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


    @FXML
    public void toolbarDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if ( !( stage.isFullScreen() ) )
        {
            stage.setX( mouseEvent.getScreenX() - x );
            stage.setY( mouseEvent.getScreenY() - y );
        }
    }

    @FXML
    public void toolbarPressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    @FXML
    public void minimizeWindow(MouseEvent mouseEvent) {

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), anchorPane);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), anchorPane);
        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!(newValue))
                {
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                }
            }
        });

        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> stage.setIconified(true));
        fadeOut.play();
    }




    @FXML
    public void closeWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        FadeTransition ft = new FadeTransition(Duration.millis(300), anchorPane);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished(ae -> stage.close());
    }
}
