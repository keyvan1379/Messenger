package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class AddGroupChatController implements Initializable {
    @FXML private Label labelSearch;
    @FXML private JFXTextField searchTextField;
    @FXML private JFXButton searchButton;
    @FXML private JFXButton startChattingButton;
    @FXML private VBox vBox;
    ArrayList<String> users = new ArrayList<>();

    private double x;
    private double y;

    public static ChatController chatController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelSearch.setText("Add Group Chat");
        searchTextField.setPromptText("Username");
        searchButton.setText("\uf002");
        startChattingButton.setText("Create the Group!");
        addUser("one");
        addUser("two");

    }

    public void addUser(String name)
    {
        CheckBox user = new CheckBox(name);
        user.setStyle("-fx-cursor: hand;");
        user.selectedProperty().addListener((observable, oldValue, newValue) -> users.add(user.getText()));
        vBox.getChildren().add(user);
    }


    public void startChattingWithUser(MouseEvent mouseEvent) throws IOException {
        Alert alert;
//        for (Node user :
//                vBox.getChildren()) {
////            try {
//            CheckBox u = (CheckBox) user;
//            if (u.isSelected())
//            {
//                System.out.println(u.getText());
//                userFound = true;
//
//                //FXMLLoader fxmlLoader = new FXMLLoader();
//                //fxmlLoader.setController(new chatController());
//
//                    /*chatController chatController = (chatController) fxmlLoader.getController();
//                    fxmlLoader.setLocation(chatController.class.getResource("D:\\IdeaProjects\\JavaFXTutorials\\src\\chat\\chat.fxml"));
//                    Parent root = (Parent) fxmlLoader.load();
//                    System.out.println(fxmlLoader.getController().toString());*/
//
////                    System.out.println(chatController);
////                chatController.addUser(((RadioButton)toggle ).getText());
//            }
////            }
////            catch (Exception e)
////            {
////                System.out.println(e.getCause());
////            }
//        }

        if (users.size() > 0)
        {
            //add
            Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
            stage.close();
        }
        else
        {
            alert = new Alert(Alert.AlertType.ERROR, "Please select a user!", ButtonType.OK);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
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
