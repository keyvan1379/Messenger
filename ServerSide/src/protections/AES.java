package protections;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.util.Scanner;

public class AES {
    private static final String ALGO = "AES";
    private byte[] Key;

    public AES(String key){
        Key = key.getBytes();
    }
    public String encrypt(String text) throws Exception {
        Key key = genKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] Val = c.doFinal(text.getBytes());
        String encVal = new BASE64Encoder().encode(Val);
        return encVal;
    }
    public String decrypt(String cipher) throws Exception{
        Key key = genKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodeVal = new BASE64Decoder().decodeBuffer(cipher);
        byte[] decvalue = c.doFinal(decodeVal);
        String plainText = new String(decvalue);
        return plainText;
    }

    private Key genKey(){
        java.security.Key key = new SecretKeySpec(Key,ALGO);
        return key;
    }
    public String getKey(){
        return new String(Key);
    }
    public void exportKey(String username){
        File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\ServerSide\\src\\protections\\UsersKey\\"+username+".txt");
        try {
            /*FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            System.out.println();
            objectOutputStream.write(Key);*/
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(new String(Key));
            printWriter.flush();
            /*objectOutputStream.flush();
            fileOutputStream.flush();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static AES importKey(String username){
        File file = new File("C:\\Users\\ASuS\\IdeaProjects\\ServerSide\\ServerSide\\src\\protections\\UsersKey\\"+username+".txt");
        try {
            /*FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);*/
            Scanner scanner = new Scanner(file);
            return new AES(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("AES Failed");
        return null;
    }
}
