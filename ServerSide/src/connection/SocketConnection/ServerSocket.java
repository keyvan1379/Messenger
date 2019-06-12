package connection.SocketConnection;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
    private static java.net.ServerSocket serverSocket;
    public static void start(){
        while (true){
            try {
                serverSocket = new java.net.ServerSocket(38474);
                Socket socket = serverSocket.accept();
                /*ClientHandler clientHandler = new ClientHandler
                        (socket,socket.getInputStream(),socket.getOutputStream());*/
                /*Thread thread = new Thread(clientHandler);
                thread.start();*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
