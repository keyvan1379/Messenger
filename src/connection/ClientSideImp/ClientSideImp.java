package connection.ClientSideImp;

import connection.ClientSideIF;
import connection.ServerSideIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientSideImp extends UnicastRemoteObject implements ClientSideIF,Runnable {
    ServerSideIF serverSideIF;
    public ClientSideImp(ServerSideIF serverSideIF) throws RemoteException {
        this.serverSideIF = serverSideIF;
    }

    @Override
    public void getMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        String msg;
        String touser;
        String payam;
        while (true){
            username = scanner.nextLine();
            password = scanner.nextLine();
            try {
                if(!(payam = serverSideIF.login(username,password,this)).equals("wait for server")){
                    System.out.println(payam);
                }else break;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        while (true){
            System.out.print("to user : ");
            touser=scanner.nextLine();
            System.out.print("msg : ");
            msg=scanner.nextLine();
            try {
                serverSideIF.sendMsg(username,touser,msg);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
