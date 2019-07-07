package protections;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSA {
    public static final String Public_Key = ".\\PrKeyAndPuKey\\Public.key";
    public static final String Private_Key = ".\\PrKeyAndPuKey\\Private.key";

    RSA(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey,RSAPublicKeySpec.class);
            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey,RSAPrivateKeySpec.class);
            saveKey(Public_Key,rsaPublicKeySpec.getModulus(),rsaPublicKeySpec.getPublicExponent());
            saveKey(Private_Key,rsaPrivateKeySpec.getModulus(),rsaPrivateKeySpec.getPrivateExponent());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveKey(String filename, BigInteger mod,BigInteger exp) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mod);
            oos.writeObject(exp);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(oos != null){
                oos.close();
                if(fos != null){
                    fos.close();
                }
            }
        }
    }
    public String encrypt(String text){
        byte[] dataToEncrypt = text.getBytes();
        byte[] encryptData = null;
        try{
            PublicKey publicKey = readPuKey(Public_Key);
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE,publicKey);
            encryptData = c.doFinal(dataToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(new BASE64Encoder().encode(encryptData));
    }
    public String decrypt(String cipherText) throws IOException {
        byte[] dataTodecrypt = new BASE64Decoder().decodeBuffer(cipherText);
        byte[] decData = null;
        try{
            PrivateKey privateKey = readPrKey(Private_Key);
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE,privateKey);
            decData = c.doFinal(dataTodecrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decData);
    }
    public PublicKey readPuKey(String filename) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            BigInteger mod = (BigInteger) ois.readObject();
            BigInteger exp = (BigInteger) ois.readObject();
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(mod,exp);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ops from rsa");
            return null;
        }finally {
            if(ois != null){
                ois.close();
                if(fis != null){
                    fis.close();
                }
            }
        }
    }
    public PrivateKey readPrKey(String filename) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            BigInteger mod = (BigInteger) ois.readObject();
            BigInteger exp = (BigInteger) ois.readObject();
            RSAPrivateKeySpec rsaPrivateKeySpec= new RSAPrivateKeySpec(mod,exp);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ops from rsa private");
            return null;
        }finally {
            if(ois != null){
                ois.close();
                if(fis != null){
                    fis.close();
                }
            }
        }
    }
}
