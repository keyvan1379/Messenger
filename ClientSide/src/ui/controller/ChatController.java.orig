package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import connection.ClientSideImp.ClientSideImp;
import dao.daoExc.GetUserex;
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
import javafx.stage.*;
import models.ProfileInfo;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

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

    private double x;
    private double y;
    public String openChat;


    public void initialize() {

        SearchController.chatController = this;
        ClientSideImp.setChatController(this);

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

        this.openChat = null;

        messagesVBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-padding: 10");

        slider.setMin(10);
        slider.setMax(20);
        slider.setValue(14);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> messagesVBox.setStyle("-fx-font-size: " + newValue + "px; -fx-background-color: white; -fx-padding: 10"));

        try {
            for (String pv :
                    ClientSideImp.getInstance().getChatUsers()) {
                addChat(pv, 0);
            }

            for (String gp :
                    ClientSideImp.getInstance().getChatGroup()) {
                addChat(gp, 1);
            }

            for (String ch :
                    ClientSideImp.getInstance().getChatChannels()) {
                addChat(ch, 2);
            }
        } catch (Exception ex)
        {
            System.out.println("server error");
        }
        messageTextArea.focusedProperty().addListener(
                (observable, oldValue, newValue) ->{
                    ClientSideImp.getInstance().setStatusTo_Typing(openChat);
                    /*try {
            if(ClientSideImp.getInstance().get_Status(openChat).equals("typing..."))
                ClientSideImp.getInstance().setStatusTo_Online();
            else
                ClientSideImp.getInstance().setStatusTo_Typing(openChat);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        }*/
                });
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
            try {
                HashMap<Integer, ArrayList> msg = ClientSideImp.getInstance().getmsg_between_2person(chat);
                if (msg != null)
                {
                    for (int i = 0; i < msg.size(); i++) {
                        addMessage(new Message((String)msg.get(i).get(0), (String)msg.get(i).get(1), Integer.parseInt((String)msg.get(i).get(2)), (String)msg.get(i).get(3)));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            // userfrom msg isfile sendtime userto

            this.username.setText(chat);
            try {
                if(ClientSideImp.getInstance().get_Status(chat).equals("offline")){
                    this.status.setText(Message.dateToString(ClientSideImp.getInstance().get_lastseen(chat)));
                }else {
                    this.status.setText(ClientSideImp.getInstance().get_Status(chat));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (GetUserex getUserex) {
                getUserex.printStackTrace();
            }

            ProfileInfo profileInfo = ClientSideImp.getInstance().get_User_Profile(chat);

            File file = new File("ClientSide/src/ui/profilePictures/" + profileInfo.getUsername() + ".jpg");
            byte[] img = profileInfo.getProfile();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = new Image(file.toURI().toString());

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
            messageTextArea.setDisable(false);
            sendButton.setDisable(false);
            attachButton.setDisable(false);
            openEmojisButton.setDisable(false);
        }
        else if (type == 1) //gp
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

            try {
                HashMap<Integer, ArrayList> msg = ClientSideImp.getInstance().getGroupMsg(chat);
                //from msg isfile date
                if (msg != null)
                {
                    for (int i = 0; i < msg.size(); i++) {
                        addMessage(new Message( (String)msg.get(i).get(0), (String)msg.get(i).get(1),Math.round( (Double)msg.get(i).get(2) ), (String) msg.get(i).get(3)  ));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            messageTextArea.setDisable(false);
            sendButton.setDisable(false);
            attachButton.setDisable(false);
            openEmojisButton.setDisable(false);

        }
        else if (type == 2)
        {
            boolean admin = false;
            try {
                HashMap<Integer, ArrayList> msg = ClientSideImp.getInstance().getChannelMsg(chat);
                admin = ClientSideImp.getInstance().getUser().equals(ClientSideImp.getInstance().getChannel(chat).getAdmin());
                //admin msg isfile date
                if (msg != null)
                {
                    for (int i = 0; i < msg.size(); i++) {
                        addMessage(new Message((String)msg.get(i).get(0), (String)msg.get(i).get(1), Math.round( (Double)msg.get(i).get(2) ), (String)msg.get(i).get(3)));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            this.username.setText(chat);
            this.status.setText(""); //?
            Image image = new Image(new File("ClientSide/src/ui/images/channel.png").toURI().toString());
            profilePicture.setImage(image);
            profilePicture.setFitHeight(60);
            profilePicture.setPreserveRatio(true);

            if (!admin)
            {
                messageTextArea.setDisable(true);
                sendButton.setDisable(true);
                attachButton.setDisable(true);
                openEmojisButton.setDisable(true);
            }
            else
            {
                messageTextArea.setDisable(false);
                sendButton.setDisable(false);
                attachButton.setDisable(false);
                openEmojisButton.setDisable(false);
            }

            infoHbox.setOnMouseClicked(e-> {
                try {
                    showUsers(e, chat);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }


    }

    public void addMessage(Message m)
    {
        //from msg isfile sendtime
        TextFlow textFlow;
        HBox hBox = new HBox(5);
        hBox.setPadding(new Insets(0, 5, 0, 5));
        Image image;
        ImageView imageView;
        imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setPreserveRatio(true);
        Text id = new Text(m.getUser() + ":");
        id.setStyle("-fx-font-size: 10px");

        if (m.getIsFile() == 0) //not file
        {
            Text text = new Text(m.getMessage());
            Text time = new Text(m.getTime());
            time.setStyle("-fx-font-size: 10px");
            textFlow = new TextFlow(id, new Text(System.lineSeparator()), text, new Text(System.lineSeparator() + "______" + System.lineSeparator()), time);

        }
        else // file
        {
            //need to add time to file
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.DOWNLOAD);
            icon.setFill(Color.WHITE);
            icon.setSize("26");
            icon.setOnMouseClicked(e -> {
<<<<<<< HEAD
                ClientSideImp.getInstance().download_File("C:\\Users\\", ClientSideImp.getInstance().getUser(), m.getMessage());
=======
                DirectoryChooser folderChooser = new DirectoryChooser();
                File folder = folderChooser.showDialog( attachButton.getScene().getWindow() );
                ClientSideImp.getInstance().download_File(folder.getAbsolutePath(), ClientSideImp.getInstance().getUser(), m.getMessage());
>>>>>>> 52ff4e3a74073cfe4295ce18b50ce85ae1ad2f93
            });
            icon.setCursor(Cursor.HAND);
            Text fileName = new Text(m.getMessage());
            Text fileSize = new Text(" ( "+ m.getIsFile() +" )");
            Text time = new Text(m.getTime());
            time.setStyle("-fx-font-size: 10px");
            textFlow = new TextFlow(id, new Text(System.lineSeparator()),
                    icon, new Text("  "), fileName, fileSize,
                    new Text(System.lineSeparator() + "______" + System.lineSeparator()), time);

        }

        if (m.getUser().equals(ClientSideImp.getInstance().getUser()))
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
        if ( (!(messageTextArea.getText().equals(""))) && openChat != null)
        {
            Message message = new Message(ClientSideImp.getInstance().getUser(),
                    messageTextArea.getText().trim(), 0,Message.dateToString(new Date()));

            addMessage(message);
            ClientSideImp.getInstance().sendmsg(openChat, messageTextArea.getText().trim());

            messageTextArea.setText("");
        }

        else if ( openChat == null )
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a chat!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }

    public void openEmojis(MouseEvent mouseEvent) {
        if (emojiListPane.isVisible())
            emojiListPane.setVisible(false);
        else
            emojiListPane.setVisible(true);

    }

    public void attachFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        ClientSideImp.getInstance().upload_File(file, file.getName(), openChat);
        addMessage(new Message(ClientSideImp.getInstance().getUser(), file.getName(), file.length(), Message.dateToString(new Date())));
        //send file
    }

    public void showUsers(MouseEvent mouseEvent, String username) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/listOfUsers.fxml"));
        Parent root = fxmlLoader.load();
        listOfUsersController listOfUsersController = fxmlLoader.<listOfUsersController>getController();
        try {
            for (String u :
                    ClientSideImp.getInstance().getGroupUsers(username)) {
                listOfUsersController.addUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        ProfileInfo profileInfo = ClientSideImp.getInstance().get_User_Profile(username);
        Parent root = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.<ProfileController>getController();
        profileController.setUsername(profileInfo.getUsername());
        profileController.setName(profileInfo.getFirstname());
        profileController.setLastname(profileInfo.getLastname());

        File file = new File("ClientSide/src/ui/profilePictures/" + profileInfo.getUsername() + ".jpg");
        byte[] img = profileInfo.getProfile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(img);

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
            if (type == 0)
                openChat = username;
            else if (type == 1)
                openChat = "$" + username;
            else if (type == 2)
                openChat = "#" + username;
        });
        hBox.setStyle("-fx-cursor: hand;");
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

            ClientSideImp.getInstance().setStatusTo_Offline();
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
        ClientSideImp.getInstance().setStatusTo_Offline();
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
