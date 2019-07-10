package connection.ClientSideImp;

import connection.ClientSideIF;
import connection.ServerSideIF;
import protections.AES;
import protections.RSA;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientSideImp extends UnicastRemoteObject implements ClientSideIF,Runnable {
    private ServerSideIF serverSideIF;

    private ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(40900);//more pc needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientSideImp(ServerSideIF serverSideIF) throws RemoteException {
        this.serverSideIF = serverSideIF;
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
    public void downloadFile(String fromUser, String fileName) throws RemoteException {
        try {
            Socket socket = serverSocket.accept();
            fileHandler fileHandler = new fileHandler(socket,fileName,socket.getInputStream(),socket.getOutputStream());
            Thread t = new Thread(fileHandler);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
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
            /*Thread thread = new Thread(() -> {
                try {
                    serverSideIF.uploadFile(file.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            thread.start();*/
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
        }














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
        System.out.println("done");
    }
}
