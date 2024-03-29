

import connection.ClientSideImp.ClientSideImp;
import connection.ClientSideImp.ServerIp;
import connection.ServerSideIF;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ui/fxml/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(System.getProperty("user.dir"));
        System.out.println("Enter server ip");
        ServerIp.serverip = scanner.nextLine();
        connection.ClientSideImp.ServerIp.serverip = ServerIp.serverip;
        try {
            String url = "rmi://"+ServerIp.serverip+"/Test";

//            Registry registry = LocateRegistry.getRegistry(null);
            ServerSideIF serverSideIF = (ServerSideIF) Naming.lookup(url);
            ClientSideImp clientSideImp = ClientSideImp.getInstance(serverSideIF, "1234567890qwerty");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
