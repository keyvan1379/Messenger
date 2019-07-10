import connection.ClientSideImp.ClientSideImp;
import connection.ServerSideIF;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Scanner scanner = new Scanner(System.in);
        String url = "rmi://localhost/Test";
        ServerSideIF chatServerIF = (ServerSideIF) Naming.lookup(url);
        new Thread(() -> {
            try {
                new ClientSideImp(chatServerIF,"1234567890qwerty");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
