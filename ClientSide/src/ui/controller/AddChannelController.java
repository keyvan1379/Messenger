package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import connection.ClientSideImp.ClientSideImp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Channel;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;


public class AddChannelController implements Initializable {
    @FXML private Label labelCreate;
    @FXML private Label labelJoin;
    @FXML private JFXTextField searchTextField;
    @FXML private JFXTextField createTextField;
    @FXML private JFXButton searchButton;
    @FXML private JFXButton joinButton;
    @FXML private JFXButton createButton;
    @FXML private VBox vBox;
    private ToggleGroup channelsFound = new ToggleGroup();

    private double x;
    private double y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelJoin.setText("Join");
        labelCreate.setText("Create");
        searchTextField.setPromptText("Channel ID");
        searchButton.setText("\uf002");
        joinButton.setText("Join!");
        createButton.setText("Create!");
        createTextField.setPromptText("Channel ID");

        for (String username :
                ClientSideImp.getInstance().get_ALL_Channel()) {
            addChannel(username);
        }

    }

    public void addChannel(String name)
    {
        RadioButton user = new RadioButton(name);
        user.setStyle("-fx-cursor: hand;");
        user.setToggleGroup(channelsFound);
        vBox.getChildren().add(user);
    }


    public void joinChannel(MouseEvent mouseEvent) throws IOException {
        Alert alert;
        for (Toggle toggle :
                channelsFound.getToggles()) {
            if (toggle.isSelected())
            {
                String channelID = ((RadioButton)toggle ).getText();
                ClientSideImp.getInstance().joinChannel(channelID);
                SearchController.chatController.addChat(channelID, 2);
                Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
                stage.close();
                return;
            }

        }
        alert = new Alert(Alert.AlertType.ERROR, "Please select a channel!", ButtonType.OK);
        alert.setTitle("No Channel Selected");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void createChannel(MouseEvent mouseEvent) {
        String channelID = createTextField.getText();
        try {
            if (channelID.equals(""))
            {
                throw new Exception("Please enter a name for your channel!");
            }

            for (String id :
                    ClientSideImp.getInstance().get_ALL_Channel()) {
                if (channelID.equals(id))
                    throw new Exception("Channel already exists!");
            }
            ClientSideImp.getInstance().createChannel(new Channel(channelID, null, "", new Date()));
            ClientSideImp.getInstance().joinChannel(channelID);

            SearchController.chatController.addChat(channelID, 2);
            Stage stage = (Stage) ((Node)(mouseEvent.getSource())).getScene().getWindow();
            stage.close();

        } catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
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


    public void searchChannels(MouseEvent mouseEvent) {
        vBox.getChildren().clear();
        String search = searchTextField.getText();
        for (String username :
                ClientSideImp.getInstance().get_ALL_Channel()) {
            if (username.contains(search))
                addChannel(username);
        }
    }
}
