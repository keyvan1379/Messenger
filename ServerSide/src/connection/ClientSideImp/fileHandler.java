package connection.ClientSideImp;

import java.io.*;
import java.net.Socket;

public class fileHandler implements Runnable{
    private Socket socket;
    private String fileName;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String path;

    public fileHandler(Socket socket, String fileName, InputStream inputStream, OutputStream outputStream,String path) {
        this.socket = socket;
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            File file = new File(path+"\\"+ fileName);
            byte[] data = new byte[8192];
            int count;
            FileOutputStream outputStream = new FileOutputStream(file);
            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            socket.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
