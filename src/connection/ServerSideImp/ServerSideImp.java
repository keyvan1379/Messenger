package connection.ServerSideImp;

import connection.ClientSideIF;
import connection.ServerSideIF;
import connection.SocketConnection.ClientHandler;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import dao.UserDao;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import dao.daoExc.Passex;
import dao.daoExc.UsernameEx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServerSideImp extends UnicastRemoteObject implements ServerSideIF {
    public ServerSideImp() throws RemoteException {
    }

    private UserDao userDao = new UserDaoImp();
    private MessageQuery messageQuery = new MessageQueryImp();
    private HashMap<String, ClientSideIF> clients = new HashMap<>();
    private ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(38474);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMsg(String FromUsername,String ToUsername, String msg) throws Exception {
        try {
            if (!messageQuery.isChatExist(FromUsername, ToUsername) & userDao.getUser(ToUsername)!=null)
                messageQuery.addChat(FromUsername, ToUsername);
        }catch (GetUserex ex){
            System.out.println("message to unknown user");
        }
        try {//need some security
            if(clients.get(ToUsername)==null){
                try {
                    userDao.getUser(ToUsername);
                    messageQuery.addMessage(msg,FromUsername,ToUsername,0);
                }catch (GetUserex ex){
                    System.out.println(ex.getMessage());
                    return;
                }
            }
            else {
                try {
                    userDao.getUser(ToUsername);
                    clients.get(ToUsername).getMessage(msg);
                    messageQuery.addMessage(msg,FromUsername,ToUsername,0);
                }catch (GetUserex ex){
                    System.out.println(ex.getMessage());
                    return;
                }
            }

            /*this block for send msg to database*/
        }catch (NullPointerException ex){
            /*

           this block is for check if username does exist then send msg
             */
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getAllOnlineUser() {
        return null;
    }

    @Override
    public String login(String username, String password,ClientSideIF clientSideIF) {
        try {
            userDao.login(username,password);
            clients.put(username,clientSideIF);
            return "wait for server";
        }catch (UsernameEx ex){
            return ex.getMessage();
        }catch (Passex ex){
            return ex.getMessage();
        }
    }

    @Override
    public boolean getUser(String username) throws GetUserex {
        try{
            userDao.getUser(username);
            return true;
        }catch (GetUserex ex){
            return false;
        }
    }

    @Override
    public boolean isActive(String username) throws GetUserex {
        return userDao.isActive(username);
    }

    @Override
    public Date lastSeen(String username) throws GetUserex {
        return userDao.lastSeen(username);
    }

    @Override
    public String getAllMessages(String username1) {
        return messageQuery.getAllChat(username1);
    }

    @Override
    public String getMessageBetween2Person(String username1, String username2) throws Exception {
        if(messageQuery.isChatExist(username1,username2))
            return messageQuery.getChatBetweenTwoPerson(username1,username2);
        else
            throw new IllegalArgumentException("chat dose not exist between this 2 username");
    }

    @Override
    public void uploadFile(String fromUser,String filename,String toUser) {
        try {
            if(!messageQuery.isChatExist(fromUser,toUser)) messageQuery.addChat(fromUser,toUser);
            //need to check if client online then send file
            //be careful download method must be on other side not in server interface
            String fileN = getAlphaNumericString(16);
            File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
            while(file.exists()){
                fileN = getAlphaNumericString(16);
                file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
            }
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler
                    (socket,socket.getInputStream(),socket.getOutputStream(),
                            (fileN+filename),fromUser,toUser,clients.get(toUser));
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}










/*@Override
    public void uploadFile(byte[] data, int length,String filename) {
        File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\" +
                filename);
        *//*while (file.exists()){
            file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+
                    filename);
        }*//*
        //file.mkdir();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            try {
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/








/*
try {
        File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\" +
        filename);
        Socket socket = serverSocket.accept();
        System.out.println("dlfksjdlfkjsdlkfjsdlkf");
        InputStream inputStream = socket.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(file);
        int count;
        byte[] data = new byte[8192];
        while ((count = inputStream.read(data))!=-1){
        outputStream.write(data,0,count);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        socket.close();
        serverSocket.close();
        } catch (IOException e) {
        e.printStackTrace();
        }*/
