package connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientSideIF extends Remote {
    void getMessage(String message) throws RemoteException;
}
