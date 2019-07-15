package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

class Message
{
    String user;
    String message;
    String time;
    int isFile;
    Message(String user, String message, int isFile,String time)
    {
        this.user = user;
        this.message = message;
        this.isFile = isFile;
        this.time = time;
    }

    public static String dateToString(Date date){
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public int getIsFile() {
        return isFile;
    }

    public String getTime() {
        return time;
    }
}

public class ChatController {


    ArrayList<Message> messages = new ArrayList<>();

    @FXML private HBox infoHbox;
    @FXML private JFXTextArea messageTextArea;
    @FXML private JFXButton sendButton;
    @FXML private JFXButton openEmojisButton;
    @FXML private JFXButton attachButton;
    @FXML private VBox messagesVBox;

    @FXML private Label labelChats;
    @FXML private Label labelSettings;
    @FXML private Text username;
    @FXML private Text status;
    @FXML private ImageView profilePicture;
    @FXML private VBox usersVBox;

    @FXML private JFXButton editProfileButton;
    @FXML private JFXButton logOutButton;
    @FXML private JFXButton deleteAccountButton;

    @FXML private ScrollPane emojiListPane;
    @FXML private ScrollPane messagepane;

    @FXML private JFXSlider slider;

    String sender = "userOne";


    private double x;
    private double y;
    private boolean isChatOpen;


    public void initialize() {

        SearchController.chatController = this;

        emojiListPane.setVisible(false);
        setEmojiList();

        sendButton.setText("");
        openEmojisButton.setText("");
        attachButton.setText("");
        messageTextArea.setPromptText("Message...");

        labelChats.setText("Chats");

        labelSettings.setText("Settings");

        editProfileButton.setText("Edit Your Profile");
        logOutButton.setText("Log Out");
        deleteAccountButton.setText("Delete Your Account");
        logOutButton.setMinWidth(deleteAccountButton.getMaxWidth());


        username.setText("\nNo User Selected!");
        status.setText("");
        Image image = new Image(new File("ClientSide/src/ui/images/user.png").toURI().toString());
        profilePicture.setImage(image);
        profilePicture.setFitHeight(60);
        profilePicture.setPreserveRatio(true);

        this.isChatOpen = false;

        messagesVBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-padding: 10");

        slider.setMin(10);
        slider.setMax(20);
        slider.setValue(14);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> messagesVBox.setStyle("-fx-font-size: " + newValue + "px; -fx-background-color: white; -fx-padding: 10"));


        //get messages
        Message m1 = new Message("userOne", "hello", 1,Message.dateToString(new Date()));
        Message m2 = new Message("userTwo", "hi", 1,Message.dateToString(new Date()));
        Message m3 = new Message("userTwo", "bye", 0,Message.dateToString(new Date()));


        messages.add(m1);
        messages.add(m2);
        messages.add(m3);


    }


    private void setEmojiList()
    {
        VBox emojiListVBox = (VBox) emojiListPane.getContent();
        for (Node hbox :
                emojiListVBox.getChildren()) {
            for (Node emoji :
                    ((HBox)hbox).getChildren()) {
                emoji.setCursor(Cursor.HAND);
                emoji.setOnMouseClicked(event ->
                {
                    messageTextArea.setText( messageTextArea.getText() + ((Text)emoji).getText() );
                    emojiListPane.setVisible(false);
                });
            }
        }
    }

    private void loadMessages (String chat, int type) // 0 = pv, 1 = gp, 2 = ch
    {
        infoHbox.setCursor(Cursor.HAND);
//        set username and profile picture and status
        if (type == 0)
        {
            this.username.setText(chat);
            this.status.setText("");
            Image image = new Image(new File("ClientSide/src/ui/images/user.png").toURI().toString());
            profilePicture.setImage(image);
            profilePicture.setFitHeight(60);
            profilePicture.setPreserveRatio(true);

            infoHbox.setOnMouseClicked(e-> {
                try {
                    showProfile(e, chat);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        else if (type == 1)
        {
            this.username.setText(chat);
            this.status.setText(""); //?
            Image image = new Image(new File("ClientSide/src/ui/images/gp.png").toURI().toString());
            profilePicture.setImage(image);
            profilePicture.setFitHeight(60);
            profilePicture.setPreserveRatio(true);
            infoHbox.setOnMouseClicked(e-> {
                try {
                    showUsers(e, chat);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        else if (type == 2)
        {
            this.username.setText(chat);
            this.status.setText(""); //?
            Image image = new Image(new File("ClientSide/src/ui/images/channel.png").toURI().toString());
            profilePicture.setImage(image);
            profilePicture.setFitHeight(60);
            profilePicture.setPreserveRatio(true);
            // if the channel writer is not the user
            messageTextArea.setDisable(true);
            sendButton.setDisable(true);
            attachButton.setDisable(true);
            openEmojisButton.setDisable(true);

            infoHbox.setOnMouseClicked(e-> {
                try {
                    showUsers(e, chat);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }



        for (Message m :
                messages) {
            addMessage(m);

        }
    }

    private void addMessage(Message m)
    {
        TextFlow textFlow;
        HBox hBox = new HBox(5);
        hBox.setPadding(new Insets(0, 5, 0, 5));
        Image image;
        ImageView imageView;
        imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setPreserveRatio(true);

        if (m.getIsFile() == 1)
        {
            Text text = new Text(m.getMessage());
            Text time = new Text(m.getTime());
            time.setStyle("-fx-font-size: 10px");

            textFlow = new TextFlow(text, new Text(System.lineSeparator() + "______" + System.lineSeparator()), time);

        }
        else
        {
            //need to add time to file
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.DOWNLOAD);
            icon.setFill(Color.WHITE);
            icon.setSize("26");
            icon.setOnMouseClicked(e -> {
                System.out.println("download file");
            });
            icon.setCursor(Cursor.HAND);
            Text fileName = new Text(m.getMessage());
            Text fileSize = new Text(" ( size )");
            Text time = new Text(m.getTime());
            time.setStyle("-fx-font-size: 10px");
            textFlow = new TextFlow(icon, new Text("  "), fileName, fileSize, new Text(System.lineSeparator() + "______" + System.lineSeparator()), time);

        }

        if (m.getUser().equals(sender))
        {
            image = new Image(new File("ClientSide/src/ui/images/user.png").toURI().toString()); //get user profile img
            textFlow.getStyleClass().add("text-flow-sender");
            imageView.setImage(image);
            hBox.setAlignment(Pos.TOP_RIGHT);
            hBox.getChildren().add(textFlow);
            hBox.getChildren().add(imageView);

        }
        else
        {
            image = new Image(new File("ClientSide/src/ui/images/user.png").toURI().toString()); //get other users profile img
            textFlow.getStyleClass().add("text-flow-receiver");
            hBox.setAlignment(Pos.TOP_LEFT);
            imageView.setImage(image);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(textFlow);

        }


        messagesVBox.getChildren().add(hBox);
        messagepane.vvalueProperty().bind(messagesVBox.heightProperty());
    }


    public void sendMessege(MouseEvent mouseEvent) {
        if ( (!(messageTextArea.getText().equals(""))) && (isChatOpen) )
        {
            Message message = new Message("userOne", messageTextArea.getText().trim(), 1,Message.dateToString(new Date()));
            messages.add(message);
            messageTextArea.setText("");
            addMessage(message);
        }

        else if ( !(isChatOpen) )
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a chat!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }

    public void openEmojis(MouseEvent mouseEvent) {
        emojiListPane.setVisible(true);
    }

    public void attachFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        //send file
    }

    public void showUsers(MouseEvent mouseEvent, String username) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/listOfUsers.fxml"));
        Parent root = fxmlLoader.load();
        listOfUsersController listOfUsersController = fxmlLoader.<listOfUsersController>getController();


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showProfile(MouseEvent mouseEvent, String username) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/profile.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.<ProfileController>getController();
        System.out.println(profileController);
        profileController.setUsername(username);
        profileController.setName("name");
        File file = new File("ClientSide/src/ui/images/user.png");
        Image image = new Image(file.toURI().toString());
        profileController.setProfilePicture(image);


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();

    }

    public void OpenAddUserWindow(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/search.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void OpenAddGPWindow(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/addGroupChat.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void OpenAddChannelWindow(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/addChannel.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void addChat(String username, int type) // 0 = pv, 1 = gp, 2 = ch
    {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(3, 5, 3, 10));
        hBox.setSpacing(10);
        ImageView profilePicture;//
        if (type == 0)
        {
            profilePicture = new ImageView(new Image(new File("ClientSide/src/ui/images/user.png").toURI().toString()));
        }
        else if (type == 1)
        {
            profilePicture = new ImageView(new Image(new File("ClientSide/src/ui/images/gp.png").toURI().toString()));
        }
        else //channel
        {
            profilePicture = new ImageView(new Image(new File("ClientSide/src/ui/images/channel.png").toURI().toString()));
        }

        profilePicture.setFitHeight(15);
        profilePicture.setPreserveRatio(true);
        Text user = new Text(username);
        user.setStyle("-fx-font-size: 14px");
        hBox.getChildren().add(profilePicture);
        hBox.getChildren().add(user);

        usersVBox.getChildren().add(0, hBox);
        hBox.setOnMouseClicked(e -> {
            messagesVBox.getChildren().clear();
            loadMessages(username, type);
            isChatOpen = true;
        });
        hBox.setStyle("-fx-cursor: hand;");
        //usersVBox.getChildren().add(1, new Separator());
    }



    public void editProfile(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/editProfile.fxml"));
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void deleteAccount(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Once you delete your account, ...", ButtonType.NO, ButtonType.YES);
        alert.setHeaderText(null);
        alert.setTitle("Are Your Sure?");


        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.YES))
        {
            //delete account
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage lastStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            lastStage.close();

        }
        else
        {
            alert.close();
        }
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.NO, ButtonType.YES);
        alert.setHeaderText(null);
        alert.setTitle("");


        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.YES))
        {
            System.out.println("log out");
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage lastStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            lastStage.close();

        }
        else
        {
            alert.close();
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
