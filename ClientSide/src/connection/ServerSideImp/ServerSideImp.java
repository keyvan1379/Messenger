package connection.ServerSideImp;

import com.google.gson.Gson;
import connection.ClientSideIF;
import connection.ServerSideIF;
import connection.SocketConnection.ClientHandler;
import dao.ChannelDao;
import dao.ChannelDaoImp.ChannelDaoImp;
import dao.GroupDao;
import dao.GroupDaoImp.GroupDaoImp;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import dao.UserDao;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import dao.daoExc.Passex;
import dao.daoExc.UsernameEx;
import models.*;
import protections.AES;
import protections.MD5;
import protections.RSA;
import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.*;

public class ServerSideImp extends UnicastRemoteObject implements ServerSideIF {
    public ServerSideImp() throws RemoteException {
    }

    private UserDao userDao = new UserDaoImp();
    private MessageQuery messageQuery = new MessageQueryImp();
    private GroupDao groupDao = new GroupDaoImp();
    private ChannelDao channelDao = new ChannelDaoImp();
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
    public PublicKey getKey() throws RemoteException {
        return RSA.importKey().getPublic_Key();
    }


    @Override
    public void createCon(String username,String key) throws RemoteException {
        if(clients.get(username)==null){
            System.out.println("need to submit in login method");
            return;
        }
        AES aes = null;
        try {
            aes = new AES(RSA.importKey().decrypt(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        aes.exportKey(username);
    }

    //AES added
    //security needed
    @Override
    public void sendMsg(String FromUsername,String ToUsername, String msg) throws Exception {
        if(clients.get(FromUsername)==null){
            System.out.println("send meesage from known username");
            return;
        }
        AES aes = AES.importKey(FromUsername);
        msg = aes.decrypt(msg);
        try {
            //check if msg sended to channel
            if (ToUsername.startsWith("#")) {
                ToUsername = ToUsername.substring(1);
                Channel channel = channelDao.getChannel(ToUsername);
                if(channel.getAdmin().equals(FromUsername)){
                    ChannelMessage channelMessage = new ChannelMessage(FromUsername,msg,0,new Date());
                    channel.getChannelMessages().add(channelMessage);
                    channelDao.updateChannel(channel);
                    //send channel msg to online users
                    for (User user:
                         channel.getUsers()) {
                        if(clients.keySet().contains(user.getUserName())){
                            clients.get(user.getUserName()).getMessage(ToUsername,AES.importKey(user.getUserName()).encrypt(msg),0);
                        }
                    }
                }
                return;
            }
            //check if msg sended to group
            if(ToUsername.startsWith("$")){
                ToUsername = ToUsername.substring(1);
                Group group = groupDao.getGroup(ToUsername);
                User user = userDao.getUser(FromUsername);
                //check is username in group
                List<String> users = new ArrayList<>();
                group.getUsers().stream().forEach(x -> users.add(x.getUserName()));
                if(users.contains(user.getUserName())){
                    GroupMessage groupMessage = new GroupMessage(FromUsername,msg,0,new Date());
                    group.getGroupMessages().add(groupMessage);
                    groupDao.updateGroup(group);
                    //send group msg to online users
                    for (User user1:
                            group.getUsers()) {
                        if(user1.getUserName().equals(user.getUserName())){
                            continue;
                        }
                        if(clients.keySet().contains(user1.getUserName())){
                            clients.get(user1.getUserName()).getMessage(ToUsername,AES.importKey(user1.getUserName()).encrypt(msg),0);
                        }
                    }
                }
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
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
                    clients.get(ToUsername).getMessage(FromUsername,AES.importKey(ToUsername).encrypt(msg),0);
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
    public String getAllUser() throws RemoteException {
        try {
            List<String> alluser = userDao.getUsers();
            Gson gson = new Gson();
            return gson.toJson(alluser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "server error";
    }

    @Override
    public String getAllGroups() throws RemoteException {
        try {
            List<String> allGroup = groupDao.getAllGroup();
            Gson gson = new Gson();
            return gson.toJson(allGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Server error";
    }

    @Override
    public String getAllChannel() throws RemoteException {
        try {
            List<String> allChannel = channelDao.getAllChannel();
            Gson gson = new Gson();
            return gson.toJson(allChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Server error";
    }


    //AES added
    @Override
    public String login(String username, String password,ClientSideIF clientSideIF) {

        try {
            username = RSA.importKey().decrypt(username);
            password = RSA.importKey().decrypt(password);
            password = MD5.getMd5(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //need to add AES
    @Override
    public String signUp(User user) throws RemoteException{
        try{
            userDao.getUser(user.getUserName());
            return "this username exist pls pick another username";
        } catch (GetUserex getUserex) {
            try {
                user.setPassWord(RSA.importKey().decrypt(user.getPassWord()));
                user.setLastName(RSA.importKey().decrypt(user.getLastName()));
                user.setFistName(RSA.importKey().decrypt(user.getFistName()));
                user.setEmail(RSA.importKey().decrypt(user.getEmail()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            userDao.addUser(user);
            File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\ProfilePic\\"+user.getUserName()+".jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(user.getProfileImage());
                fileOutputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "successful";
        }
    }

    @Override
    public String editProfile(String username, ClientSideIF clientSideIF,User user) {
        if(clients.get(username)!=null){
            try {
                user.setPassWord(RSA.importKey().decrypt(user.getPassWord()));
                user.setLastName(RSA.importKey().decrypt(user.getLastName()));
                user.setFistName(RSA.importKey().decrypt(user.getFistName()));
                user.setEmail(RSA.importKey().decrypt(user.getEmail()));
                user.setLastSeen(new Date());
                user.setJoinTime(new Date());
                messageQuery.updateUser(username,user.getUserName());
                File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\profilePic\\"+username+".jpg");
                file.delete();
                file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\ProfilePic\\"+user.getUserName()+".jpg");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(user.getProfileImage());
                    fileOutputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.put(user.getUserName(),clientSideIF);
            try {
                User user1 = userDao.getUser(username);
                Set<Channel> channels = user1.getChannels();
                Set<Group> groups = user1.getGroups();
                clients.remove(username);
                userDao.deleteUser(username);
                userDao.addUser(user);
                for (Group g:
                     groups) {
                    g.getUsers().add(user);
                    user.getGroups().add(g);
                    groupDao.updateGroup(g);
                }
                for (Channel c:
                     channels) {
                    c.getUsers().add(user);
                    user.getChannels().add(c);
                    channelDao.updateChannel(c);
                }
                System.out.println(user.getUserName());
                return "successful";
            } catch (GetUserex getUserex) {
                getUserex.printStackTrace();
            }
        }
        else{
            return "unsuccessful";
        }
        return "unsuccessful";
    }

    @Override
    public String deleteProfile(String username, ClientSideIF clientSideIF) {
        if(clients.get(username)!=null & clients.get(username)==clientSideIF){
            userDao.deleteUser(username);
            clients.remove(clientSideIF);
            return "successful";
        }
        else{
            return "unsuccessful";
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
    public ProfileInfo getUserInfo(String username) throws GetUserex, RemoteException {
        User user = userDao.getUser(username);
        File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\ProfilePic\\"+user.getUserName()+".jpg");
        byte[] profile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(profile);
        } catch (FileNotFoundException e) {
            profile = null;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ProfileInfo(user.getUserName(),user.getFistName(),user.getLastName(),profile);
    }

    @Override
    public String isActive(String sourceUser,String username) throws GetUserex {
        if(userDao.isActive(username).equals(sourceUser)){
            return "typing...";
        }
        try {
            if (Integer.parseInt(userDao.isActive(username)) == -1) {
                return "online";
            }
            return "offline";
        }catch (NumberFormatException e){
            return "offline";
        }
    }

    @Override
    public void setStatus(String username,String status) throws RemoteException, GetUserex {
        if(clients.get(username)==null){
            return;
        }
        try{
            if(Integer.parseInt(status)==0){
                clients.remove(username);
            }
        }catch (NumberFormatException ex){
            System.out.println(username + "going to offline");
        }
        User user = userDao.getUser(username);
        user.setIsActive(status);
        userDao.updateUser(user);
    }

    @Override
    public Date lastSeen(String username) throws GetUserex {
        return userDao.lastSeen(username);
    }

    //AES Added
    //Security needed(check if client in list)
    @Override
    public String getAllMessages(String username1) {
        AES aes = AES.importKey(username1);
        try {
            return aes.encrypt(messageQuery.getAllChat(username1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //AES Added
    //Security needed(check if client in list)
    @Override
    public String getMessageBetween2Person(String username1, String username2) throws Exception {
        AES aes = AES.importKey(username1);
        if(messageQuery.isChatExist(username1,username2))
            return aes.encrypt(messageQuery.getChatBetweenTwoPerson(username1,username2));
        else
            throw new IllegalArgumentException("chat dose not exist between this 2 username");
    }

    @Override
    public void uploadFile(String fromUser,String filename,String toUser) {
        try {
            if(toUser.startsWith("#")){
                Channel channel = channelDao.getChannel(toUser.substring(1));
                if(channel.getAdmin().equals(fromUser)){
                    String fileN = getAlphaNumericString(16);
                    File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
                    while(file.exists()){
                        fileN = getAlphaNumericString(16);
                        file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
                    }
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler
                            (socket,socket.getInputStream(),socket.getOutputStream(),
                                    (fileN+filename),fromUser,toUser,null,null);
                    clientHandler.setClientsList(clients);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
                return;
            }
            if(toUser.startsWith("$")){
                Group group = groupDao.getGroup(toUser.substring(1));
                List<String> users = new ArrayList<>();
                group.getUsers().stream().forEach(x -> users.add(x.getUserName()));
                if(users.contains(fromUser)){
                    String fileN = getAlphaNumericString(16);
                    File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
                    while(file.exists()){
                        fileN = getAlphaNumericString(16);
                        file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\downloadFiles\\"+(fileN+filename));
                    }
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler
                            (socket,socket.getInputStream(),socket.getOutputStream(),
                                    (fileN+filename),fromUser,toUser,null,null);
                    clientHandler.setClientsList(clients);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
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
                            (fileN+filename),fromUser,toUser,clients.get(toUser),null);
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //check comment
    @Override
    public void downloadFileAgain(String fromUsername, String fileName,
                                  String username,ClientSideIF clientSideIF,String path) {
        //need to check if file in their message(Security warning)
        /*if(clients.get(username) != clientSideIF){
            System.out.println("hiiiiii");
            return;
        }*/
        ClientHandler clientHandler = new ClientHandler
                (null,null,null,
                        fileName,fromUsername,username,clientSideIF,path);
        System.out.println(fileName);
        Thread t = new Thread(() -> clientHandler.uploadFileToClient(new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\" +
                "downloadFiles\\"+fileName)));
        t.start();
    }

    @Override
    public String createChannel(Channel channel) {
        try {
            channelDao.addChannel(channel);
            return "successful";
        }catch (Exception e){
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String joinChannel(String channelUsername, String username) {
        try{
            User user = userDao.getUser(username);
            Channel channel = channelDao.getChannel(channelUsername);
            user.getChannels().add(channel);
            channel.getUsers().add(user);
            channelDao.updateChannel(channel);
            return "successful";
        }catch (Exception e){
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String getChannelMsgs(String username,String channelUsername) throws RemoteException {
        AES aes = AES.importKey(username);
        Gson gson = new Gson();
        HashMap<Integer, ArrayList> channelMsg = new HashMap<>();
        try {
            Channel channel = channelDao.getChannel(channelUsername);
            for (int i = 0; i < channel.getChannelMessages().size(); i++) {
                channelMsg.put(i,new ArrayList());
                channelMsg.get(i).add(channel.getChannelMessages().get(i).getMsg());
                channelMsg.get(i).add(channel.getChannelMessages().get(i).getIsFile());
                channelMsg.get(i).add(channel.getChannelMessages().get(i).getDate());
            }
            return aes.encrypt(gson.toJson(channelMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String createGroup(Group group) throws RemoteException {
        try {
            groupDao.addGroup(group);
            Group g1 = groupDao.getGroup(group.getUserName());
            User user = userDao.getUser(group.getAdmin());
            g1.getUsers().add(user);
            user.getGroups().add(g1);
            groupDao.updateGroup(g1);
            return "successful";
        }catch (Exception e){
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String joinGroup(String groupUsername, String username) throws RemoteException {
        try{
            User user = userDao.getUser(username);
            Group group = groupDao.getGroup(groupUsername);
            user.getGroups().add(group);
            group.getUsers().add(user);
            groupDao.updateGroup(group);
            return "successful";
        }catch (Exception e){
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String getGroupMsgs(String username, String groupUsername) throws RemoteException {
        AES aes = AES.importKey(username);
        Gson gson = new Gson();
        HashMap<Integer, ArrayList> groupMsg = new HashMap<>();
        try {
            Group group = groupDao.getGroup(groupUsername);
            for (int i = 0; i < group.getGroupMessages().size(); i++) {
                groupMsg.put(i,new ArrayList());
                groupMsg.get(i).add(group.getGroupMessages().get(i).getFromUser());
                groupMsg.get(i).add(group.getGroupMessages().get(i).getMessage());
                groupMsg.get(i).add(group.getGroupMessages().get(i).getIsFile());
                groupMsg.get(i).add(group.getGroupMessages().get(i).getSendDate());
            }
            return aes.encrypt(gson.toJson(groupMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return "unsuccessful";
        }
    }

    @Override
    public String getChatUsers(String username) throws RemoteException {
        Gson gson = new Gson();
        try {
            return gson.toJson(userDao.getChatUsersList(username));
        } catch (Exception e) {
            e.printStackTrace();
            return "server error";
        }
    }

    @Override
    public String getChatGroups(String username) throws RemoteException {
        Gson gson = new Gson();
        try {
            ArrayList<String> groups = new ArrayList<>();
            User user = userDao.getUser(username);
            user.getGroups().stream().forEach(x -> groups.add(x.getUserName()));
            return gson.toJson(groups);
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
            return "server error";
        }
    }

    @Override
    public String getChatChannels(String username) throws RemoteException {
        Gson gson = new Gson();
        try {
            ArrayList<String> channels = new ArrayList<>();
            User user = userDao.getUser(username);
            user.getChannels().stream().forEach(x -> channels.add(x.getUsername()));
            return gson.toJson(channels);
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
            return "server error";
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
