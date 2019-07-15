package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import connection.ClientSideImp.ClientSideImp;
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
    @FXML private Label labelCreate;
    @FXML private Label labelJoin;
    @FXML private JFXTextField joinSearchTextField;
    @FXML private JFXTextField createSearchTextField;
    @FXML private JFXButton createSearchButton;
    @FXML private JFXButton joinSearchButton;
    @FXML private JFXButton createGroupChatButton;
    @FXML private JFXTextField gpNameTextField;
    @FXML private VBox createVBox;
    @FXML private VBox joinVBox;

    @FXML public JFXButton joinGroupChatButton;
    ArrayList<String> users = new ArrayList<>();
    private ToggleGroup gpsFound = new ToggleGroup();

    private double x;
    private double y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        labelJoin.setText("Join");
        joinSearchTextField.setPromptText("Username");
        joinSearchButton.setText("\uf002");
        joinGroupChatButton.setText("Join!");

        labelCreate.setText("Create");
        createGroupChatButton.setText("Create!");
        createSearchButton.setText("\uf002");
        gpNameTextField.setPromptText("Group Name");


        for (String gp :
                ClientSideImp.getInstance().get_All_Group()) {
            addGroupChat(gp);
        }

        for (String username :
                ClientSideImp.getInstance().get_All_User()) {
            addUser(username);
        }

    }

    public void addUser(String name)
    {
        CheckBox user = new CheckBox(name);
        user.setStyle("-fx-cursor: hand;");
        user.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if ( !(users.contains(user.getText())) )
            {
                users.add(user.getText());
            }
            else
            {
                users.remove(user.getText());
            }
        });
        createVBox.getChildren().add(user);
    }

    public void addGroupChat(String name)
    {
        RadioButton gp = new RadioButton(name);
        gp.setStyle("-fx-cursor: hand;");
        gp.setToggleGroup(gpsFound);
        joinVBox.getChildren().add(gp);
    }


    public void createGroupChat(MouseEvent mouseEvent) throws IOException {
        Alert alert;

        if (users.size() > 0)
        {
            //add
            if (gpNameTextField.getText().equals(""))
            {
                alert = new Alert(Alert.AlertType.ERROR, "Please enter a name for your group chat!", ButtonType.OK);
                alert.setTitle("No Name Entered");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            else
            {
                SearchController.chatController.addChat(gpNameTextField.getText(), 1);
                System.out.println(users);
                Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
                stage.close();
            }
        }
        else
        {
            alert = new Alert(Alert.AlertType.ERROR, "Please select a user!", ButtonType.OK);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void joinGroupChat(MouseEvent mouseEvent) {

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
