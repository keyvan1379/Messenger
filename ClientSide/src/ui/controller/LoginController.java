package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.ClientSideImp.ClientSideImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;



public class LoginController {
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton loginButton;
    @FXML private JFXButton registerButton;
    @FXML private Label labelLogin;

    private double x;
    private double y;


    public void initialize() {
        this.username.setPromptText("Username");
        this.password.setPromptText("Password");
        this.loginButton.setText("Login");
        this.registerButton.setText("Register");
        this.labelLogin.setText("Login");
    }

    public void loginToAccount(MouseEvent mouseEvent) {
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert;
        String result;
        try {
            if ( ! (result = ClientSideImp.getInstance().login(username, password)).equals("wait for server"))
            {
                throw new Exception(result);
            }

            alert = new Alert(Alert.AlertType.CONFIRMATION, "Logged In Successfully!", ButtonType.OK);
            alert.setTitle("Welcome");
            alert.setHeaderText(null);
            alert.showAndWait();

            //register

            Parent root = FXMLLoader.load(getClass().getResource("../fxml/chat.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Register");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage lastStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            lastStage.close();

        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }

    public void createNewAccount(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../fxml/register.fxml"));
        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
        stage.setTitle("Register");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public void minimizeWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    public void windowDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if ( !( stage.isFullScreen() ) )
        {
            stage.setX( mouseEvent.getScreenX() - x );
            stage.setY( mouseEvent.getScreenY() - y );
        }
    }

    @FXML
    public void windowPressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }
}
