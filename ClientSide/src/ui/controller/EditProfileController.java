package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {
    @FXML private JFXTextField firstname;
    @FXML private JFXTextField lastname;
    @FXML private JFXTextField email;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton chooseProfilePicture;
    @FXML private ImageView profilePicture;
    @FXML private JFXButton saveButton;
    @FXML private JFXButton cancelButton;
    @FXML private Label label;

    private double x;
    private double y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.label.setText("Edit Profile");
        this.firstname.setPromptText("First Name");
        this.lastname.setPromptText("Last Name");
        this.email.setPromptText("Email");
        this.username.setPromptText("Username");
        this.password.setPromptText("Password");
        this.chooseProfilePicture.setText("Choose Your Profile Picture");
        this.profilePicture.setImage(null);//change here
        this.profilePicture.setPreserveRatio(true);
        this.profilePicture.setFitWidth(70);
        this.saveButton.setText("Save");
        this.cancelButton.setText("Cancel");
    }

    public void chooseImageFromFiles(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        Image image = new Image(file.toURI().toString());
        this.profilePicture.setImage(image);

    }

    public void saveChanges(MouseEvent mouseEvent) {
        //save
        Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
        stage.close();
    }

    public void cancel(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
        stage.close();
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
