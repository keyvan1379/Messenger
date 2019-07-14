package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AddChannelController implements Initializable {
    @FXML private Label labelSearch;
    @FXML private JFXTextField searchTextField;
    @FXML private JFXButton searchButton;
    @FXML private JFXButton joinButton;
    @FXML private JFXButton createButton;
    @FXML private VBox vBox;
    private ToggleGroup channelsFound = new ToggleGroup();

    private double x;
    private double y;

//    public static ChatController chatController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelSearch.setText("Channel");
        searchTextField.setPromptText("Channel ID");
        searchButton.setText("\uf002");
        joinButton.setText("Join!");
        createButton.setText("Create!");
        addUser("one");
        addUser("two");

    }

    public void addUser(String name)
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
//            try {
            if (toggle.isSelected())
            {
                System.out.println(((RadioButton)toggle ).getText());

                //FXMLLoader fxmlLoader = new FXMLLoader();
                //fxmlLoader.setController(new chatController());

                    /*chatController chatController = (chatController) fxmlLoader.getController();
                    fxmlLoader.setLocation(chatController.class.getResource("D:\\IdeaProjects\\JavaFXTutorials\\src\\chat\\chat.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    System.out.println(fxmlLoader.getController().toString());*/

//                    System.out.println(chatController);
                SearchController.chatController.addChat(((RadioButton)toggle ).getText(), 2);
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
        String channelID = searchTextField.getText();
        //create the channel
        //error if it already exists
        SearchController.chatController.addChat(channelID, 2);
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
