package connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientSideIF extends Remote {
    void getMessage(String FromUser,String message,long isfile) throws RemoteException;
    String getIpAddress() throws RemoteException;//in dev mode we return ipaddress localhost but when we are going to test program we need return local ip address
    void downloadFile(String fromUser,String fileName,String path) throws RemoteException;
}
