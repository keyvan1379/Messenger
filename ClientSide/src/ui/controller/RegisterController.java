package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.ClientSideImp.ClientSideImp;
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
import models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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

    File file = null;


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
        try {
            file = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
            if(file == null){
                return;
            }
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        Image image = new Image(file.toURI().toString());
        System.out.println(this.profilePicture.getStyle());
        this.profilePicture.setImage(image);
        System.out.println(this.profilePicture.getStyle());

    }

    public void registerAccount(MouseEvent mouseEvent) throws IOException {
        String username = this.username.getText();
        String lastname = this.lastname.getText();
        String firstname = this.firstname.getText();
        String email = this.email.getText();
        String password = this.password.getText();

        //image on file global
        //register
        try {
            if(file==null)
                throw new Exception("Please choose your profile picture!");

            if (username.equals(""))
                throw new Exception("Please enter a username!");
            if (lastname.equals(""))
                throw new Exception("Please enter your last name!");
            if (firstname.equals(""))
                throw new Exception("Please enter your first name!");
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
                throw new Exception("Please enter a valid email!");
            if (password.equals(""))
                throw new Exception("Please enter a password!");


            byte[] img = new byte[(int)file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(img);
            User user = new User(firstname, lastname, email, username, password, new Date(), new Date(), img);
            String result;
            if (!(result = ClientSideImp.getInstance().sign_up(user)).equals("successful"))
            {

                throw new Exception(result);
            }

            ClientSideImp.getInstance().login(username, password);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Welcome!", ButtonType.OK);
            alert.setTitle("Welcome");
            alert.setHeaderText(null);
            alert.showAndWait();
            Parent root = FXMLLoader.load(getClass().getResource("/ui/fxml/chat.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Register");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage lastStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            lastStage.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
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
