package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {




    @FXML private ImageView profilePicture;
    @FXML private Text username;
    @FXML private Text name;
    @FXML private Text lastname;
    @FXML private Label labelProfile;
    @FXML private AnchorPane imagePane;
    @FXML private AnchorPane namePane;

    private double x;
    private double y;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("D:\\IdeaProjects\\JavaFXTutorials\\src\\profile\\pic.jpg");
        Image image = new Image(file.toURI().toString());

        setName("");
        setUsername("");
        setLastname("sfsf");
        setProfilePicture(image);

        this.labelProfile.setText("Profile");
    }

    public void setName(String name) {
        this.name.setText("First Name: " + name);
    }

    public void setUsername(String username) {
        this.username.setText("Username: " + username);
    }

    public void setLastname(String lastname) {
        this.lastname.setText("Last Name: " + lastname);
    }

    public void setProfilePicture(Image image) {
        this.profilePicture.setImage(image);
        this.profilePicture.setFitHeight(150);
        double newWidth = (image.getWidth() * 150 ) / image.getHeight();
        this.profilePicture.setFitWidth(newWidth);
        this.imagePane.setMinWidth(newWidth + 45);
        this.namePane.setMinWidth(358 - (newWidth));
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
