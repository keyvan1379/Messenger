package connection.SocketConnection;

import connection.ClientSideIF;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientHandler implements Runnable{
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String filename;
    private String fromUser;
    private String toUser;
    private ClientSideIF clientSideIF=null;
    public ClientHandler(Socket socket, InputStream inputStream,
                         OutputStream outputStream,String filename,
                         String fromUser,String toUser,ClientSideIF clientSideIF) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.filename = filename;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.clientSideIF = clientSideIF;
    }

    @Override
    public void run() {
        try {
            File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+filename);
            MessageQuery messageQuery = new MessageQueryImp();
            int count;
            byte[] data = new byte[8192];
            FileOutputStream outputStream = new FileOutputStream(file);
            while ((count = inputStream.read(data))!=-1){
                outputStream.write(data,0,count);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            socket.close();
            messageQuery.addMessage(file.getPath(),fromUser,toUser,1);
            if(clientSideIF==null){
                return;
            }else{
                Thread t = new Thread(() -> {
                    try {
                        clientSideIF.downloadFile(fromUser,filename.substring(16));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
                t.start();
                Socket socket = new Socket("localhost",40900);//not local host must be clientsideif ip address
                FileInputStream fileInputStream = new FileInputStream(file);
                OutputStream outputStream1 = socket.getOutputStream();
                while ((count = fileInputStream.read(data))!=-1){
                    outputStream1.write(data,0,count);
                }
                outputStream1.flush();
                fileInputStream.close();
                outputStream1.close();
                socket.close();
            }
            //add client to send file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
