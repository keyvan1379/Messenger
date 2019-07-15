package connection.SocketConnection;

import connection.ClientSideIF;
import dao.ChannelDao;
import dao.ChannelDaoImp.ChannelDaoImp;
import dao.GroupDao;
import dao.GroupDaoImp.GroupDaoImp;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import models.Channel;
import models.ChannelMessage;
import models.Group;
import models.GroupMessage;
import protections.AES;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ClientHandler implements Runnable{
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String filename;
    private String fromUser;
    private String toUser;
    private ClientSideIF clientSideIF=null;
    private String path;
    private HashMap clientsList;
    public ClientHandler(Socket socket, InputStream inputStream,
                         OutputStream outputStream,String filename,
                         String fromUser,String toUser,ClientSideIF clientSideIF,String path) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.filename = filename;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.clientSideIF = clientSideIF;
        this.path = path;
    }

    public HashMap getClientsList() {
        return clientsList;
    }

    public void setClientsList(HashMap clientsList) {
        this.clientsList = clientsList;
    }

    public void uploadFileToClient(File file){
        int count;
        byte[] data = new byte[8192];
        try {
            if (clientSideIF == null) {
                return;
            } else {
                Thread t = new Thread(() -> {
                    try {
                        clientSideIF.downloadFile(fromUser, filename,path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
                t.start();
                Socket socket = new Socket(clientSideIF.getIpAddress(), 40900);//not local host must be clientsideif ip address
                FileInputStream fileInputStream = new FileInputStream(file);
                OutputStream outputStream1 = socket.getOutputStream();
                while ((count = fileInputStream.read(data)) != -1) {
                    outputStream1.write(data, 0, count);
                }
                outputStream1.flush();
                fileInputStream.close();
                outputStream1.close();
                socket.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
            if(toUser.startsWith("#")){
                ChannelMessage channelMessage = new ChannelMessage(fromUser,file.getPath(),file.length(),new Date());
                ChannelDao channelDao = new ChannelDaoImp();
                Channel channel = channelDao.getChannel(toUser.substring(1));
                channel.getChannelMessages().add(channelMessage);
                channelDao.updateChannel(channel);
                List<String> users = new ArrayList<>();
                channel.getUsers().stream().forEach(x -> users.add(x.getUserName()));
                for (String user:
                     users) {
                    if(clientsList.keySet().contains(user)){
                        ((ClientSideIF )clientsList.get(user)).getMessage(toUser, AES.importKey(user).encrypt(filename),file.length());
                    }
                }
                return;
            }
            if(toUser.startsWith("$")){
                GroupMessage groupMessage = new GroupMessage(fromUser,file.getPath(),(int)file.length(),new Date());
                GroupDao groupDao = new GroupDaoImp();
                Group group = groupDao.getGroup(toUser.substring(1));
                group.getGroupMessages().add(groupMessage);
                groupDao.updateGroup(group);
                List<String> users = new ArrayList<>();
                if(users.contains(fromUser)) users.remove(fromUser);
                group.getUsers().stream().forEach(x -> users.add(x.getUserName()));
                for (String user:
                        users) {
                    if(clientsList.keySet().contains(user)){
                        ((ClientSideIF )clientsList.get(user)).getMessage(toUser, AES.importKey(user).encrypt(filename),file.length());
                    }
                }
                return;
            }
            messageQuery.addMessage(file.getPath(),fromUser,toUser,(int)file.length());
            //uploadFileToClient(file);
            //add client to send file
            if(clientSideIF==null){
                return;
            }else{
                clientSideIF.getMessage(fromUser, AES.importKey(toUser).encrypt(filename),file.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
