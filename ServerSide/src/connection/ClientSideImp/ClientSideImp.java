package connection.ClientSideImp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.ClientSideIF;
import connection.ServerSideIF;
import dao.daoExc.GetUserex;
import models.ProfileInfo;
import models.User;
import protections.AES;
import protections.RSA;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class ClientSideImp extends UnicastRemoteObject implements ClientSideIF {
    private ServerSideIF serverSideIF;

    private ServerSocket serverSocket;

    private AES aes;

    private String AESKey;

    private String username = null;
    {
        try {
            serverSocket = new ServerSocket(40900);//more pc needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientSideImp(ServerSideIF serverSideIF,String AESKey) throws RemoteException {
        this.serverSideIF = serverSideIF;
        this.AESKey = AESKey;
    }

    @Override
    public void getMessage(String FromUser,String message,long isfile) {
        System.out.println(message);
    }

    @Override
    public String getIpAddress() throws RemoteException {
        return "localhost";//we need change this when we are going to test program
    }

    @Override
    public void downloadFile(String fromUser, String fileName,String path) throws RemoteException {
        try {
            Socket socket = serverSocket.accept();
            fileHandler fileHandler = new fileHandler(socket,fileName,socket.getInputStream(),socket.getOutputStream(),path);
            Thread t = new Thread(fileHandler);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String sign_up(User user){
        try {
            return serverSideIF.signUp(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("sign_up end of method");
        return null;
    }

    public String login(String username,String password){
        String res;
        try {
            if((res = serverSideIF.login(RSA.encrypt(username,serverSideIF.getKey()),
                    RSA.encrypt(password,serverSideIF.getKey()), this)).equals("wait for server")){
                serverSideIF.createCon(username,RSA.encrypt(AESKey,serverSideIF.getKey()));
                aes = new AES(AESKey);
                this.username = username;
                setStatusTo_Online();
                return res;
            }
            return res;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "server error";
    }

    public void sendmsg(String ToUsername,String msg){
        if(username==null){
            System.out.println("you must first login");
            return;
        }
        try {
            serverSideIF.sendMsg(username,ToUsername,aes.encrypt(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get_Status(String desusername) throws RemoteException, GetUserex {
        if(username==null){
            System.out.println("setStatusToOnline");
            return null;
        }
        return serverSideIF.isActive(username,desusername);
    }

    public void setStatusTo_Online(){
        if(username==null){
            System.out.println("setStatusToOnline");
            return;
        }
        try {
            serverSideIF.setStatus(username,"-1");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        }
    }

    public void setStatusTo_Offline(){
        if(username==null){
            System.out.println("setStatusToOffline");
            return;
        }
        try {
            serverSideIF.setStatus(username,"0");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        }
    }

    public void setStatusTo_Typing(String desuser){
        if(username==null){
            System.out.println("setStatusToTyping");
            return;
        }
        try {
            serverSideIF.setStatus(username,desuser);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        }
    }

    public Date get_lastseen(String username){
        if(username==null){
            System.out.println("get_lastseen");
            return null;
        }
        try {
            return serverSideIF.lastSeen(username);
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("get_lastseen end of method");
        return null;
    }

    public HashMap<Integer, ArrayList> getmsg_between_2person(String desusername){
        if(username==null){
            System.out.println("getmsg_between_2person");
            return null;
        }
        try {
            String res = serverSideIF.getMessageBetween2Person(username,desusername);
            res = aes.decrypt(res);
            HashMap<Integer, ArrayList> mess = new Gson().fromJson(res,
                    new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
            return mess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("getmsg_between_2person end of method");
        return null;
    }

    public HashMap<Integer, ArrayList> get_all_msg(){
        if(username==null){
            System.out.println("get_all_msg");
            return null;
        }
        try {
            String res = serverSideIF.getAllMessages(username);
            res = aes.decrypt(res);
            HashMap<Integer, ArrayList> mess = new Gson().fromJson(res,
                    new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
            return mess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("get_all_msg");
        return null;
    }

    public String delete_account(){
        if(username==null){
            System.out.println("delete_account");
            return null;
        }
        try {
            return serverSideIF.deleteProfile(username,this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("delete_account end of method");
        return null;
    }

    public String edit_profile(User user){
        if(username==null){
            System.out.println("edit_profile");
            return null;
        }
        try {
            return serverSideIF.editProfile(username,this,user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("edit_profile end of method");
        return null;
    }

    public boolean is_User_exist(String username){
        if(username==null){
            System.out.println("is_User_exist");
            return false;
        }
        try {
            return serverSideIF.getUser(username);
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("is_User_exist end of method");
        return false;
    }

    public ArrayList<String> get_All_User(){
        if(username==null){
            System.out.println("get_All_User");
            return null;
        }
        try {
            ArrayList<String> user = new Gson().fromJson(serverSideIF.getAllUser(),ArrayList.class);
            return user;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("get_All_User end of method");
        return null;
    }

    public ProfileInfo get_User_Profile(String username){
        if(username==null){
            System.out.println("get_User_Profile");
            return null;
        }
        try {
            return serverSideIF.getUserInfo(username);
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("get_User_Profile end of method");
        return null;
    }

    //need to change host
    //be careful to read and write file
    public void upload_File(File file,String filename,String toUser){
        if(username==null){
            System.out.println("upload_File");
            return;
        }
        byte[] mydata = new byte[8192];
        Thread thread = new Thread(() -> {
            try {
                serverSideIF.uploadFile(username,filename,toUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread1 = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 38474);//change localhost to server ip
                OutputStream outputStream = socket.getOutputStream();
                FileInputStream inputStream = new FileInputStream(file);
                int count;
                while ((count = inputStream.read(mydata)) != -1) {
                    outputStream.write(mydata, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
    }

    public void download_File(String path,String fromUsername, String fileName){
        if(username==null){
            System.out.println("download_File");
            return;
        }
        try {
            serverSideIF.downloadFileAgain(fromUsername,fileName,username,this,path);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

























    /*@Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        String msg;
        String touser;
        String payam;
        while (true) {
            username = scanner.nextLine();
            password = scanner.nextLine();
            try {
                if (!(payam = serverSideIF.login(RSA.encrypt(username,serverSideIF.getKey()),
                        RSA.encrypt(password,serverSideIF.getKey()), this)).equals("wait for server")) {
                    System.out.println(payam);
                } else break;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            serverSideIF.createCon(username,RSA.encrypt("1234567890qwerty",serverSideIF.getKey()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        AES aes = new AES("1234567890qwerty");
        System.out.println("slkfj");
        scanner.nextLine();
            System.out.print("to user : ");
            touser=scanner.nextLine();
            System.out.print("msg : ");
            msg=scanner.nextLine();
            try {
                serverSideIF.sendMsg(username,touser,aes.encrypt(msg));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        scanner.nextLine();
        File file = new File("C:\\Users\\ASuS\\Downloads\\ideaIU-2018.3.5.exe");
        byte[] mydata = new byte[8192];
        try {
            *//*Thread thread = new Thread(() -> {
                try {
                    serverSideIF.uploadFile(file.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            thread.start();*//*
            Thread thread = new Thread(() -> {
                try {
                    serverSideIF.uploadFile("ddddddd1",file.getName(),"ddddddd2");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            Socket socket = new Socket("localhost",38474);
            OutputStream outputStream = socket.getOutputStream();
            FileInputStream inputStream = new FileInputStream(file);
            int count;
            while((count = inputStream.read(mydata))!=-1){
                outputStream.write(mydata,0,count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done1");
        String filename = scanner.nextLine();
        try {
            serverSideIF.downloadFileAgain("ddddddd2",filename,"ddddddd1",this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/














/*Thread thread = new Thread(() -> {
                try {
                    serverSideIF.uploadFile("ddddddd1",file.getName(),"ddddddd2");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            Socket socket = new Socket("localhost",38474);
            OutputStream outputStream = socket.getOutputStream();
            FileInputStream inputStream = new FileInputStream(file);
            int count;
            while((count = inputStream.read(mydata))!=-1){
                outputStream.write(mydata,0,count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }*/









        /*FileInputStream in= null;
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("uploading to server...");
            try {
                int ops;
                while ((ops = in.read(mydata, 0, mydata.length)) > 0) {
                    serverSideIF.uploadFile(mydata, ops, file.getName());
                    System.out.println("done send part " + ops);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
}
