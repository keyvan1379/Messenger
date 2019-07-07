package protections;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;

public class AES {
    private static final String ALGO = "AES";
    private byte[] Key;

    AES(String key){
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
        File file = new File(".\\UsersKey\\"+username);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.write(Key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static AES importKey(String username){
        File file = new File(".\\UsersKey\\"+username);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return new AES((String)objectInputStream.readObject());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("AES Failed");
        return null;
    }
}
