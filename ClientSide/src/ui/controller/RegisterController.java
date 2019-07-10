package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML private JFXTextField firstname;
    @FXML private JFXTextField lastname;
    @FXML private JFXTextField email;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton chooseProfilePictureButton;
    @FXML private ImageView profilePicture;
    @FXML private JFXButton registerButton;
    @FXML private Label label;


    private double x;
    private double y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.label.setText("Register");
        this.firstname.setPromptText("First Name");
        this.lastname.setPromptText("Last Name");
        this.email.setPromptText("Email");
        this.username.setPromptText("Username");
        this.password.setPromptText("Password");
        this.chooseProfilePictureButton.setText("Choose Your Profile Picture");
        this.profilePicture.setImage(null);
        this.profilePicture.setPreserveRatio(true);
        this.profilePicture.setFitWidth(70);
        this.registerButton.setText("Register");

    }

    public void chooseImageFromFiles(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        //Window window = ((Node) mouseEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        Image image = new Image(file.toURI().toString());
        System.out.println(this.profilePicture.getStyle());
        this.profilePicture.setImage(image);
        System.out.println(this.profilePicture.getStyle());
        System.out.println("A");

    }

    public void registerAccount(MouseEvent mouseEvent) throws IOException {
        //register

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Welcome!", ButtonType.OK);
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
