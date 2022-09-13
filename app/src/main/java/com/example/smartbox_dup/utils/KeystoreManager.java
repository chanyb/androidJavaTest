package com.example.smartbox_dup.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import java.util.Base64;

public class KeystoreManager {
    private KeystoreManager() {}
    public final static String alias = "example";
    private static KeystoreManager instance = new KeystoreManager();

    public static KeystoreManager getInstance() {
        return instance;
    }

    public void init(Context context) { // KeyStore 초기화 메소드
        try {
            KeyStore keystore = KeyStore.getInstance("AndroidKeyStore"); // AndroidKeyStore를 정확히 입력해야 키 스토어에 접근 함
            keystore.load(null);

            if(!keystore.containsAlias(alias)) { // 지정된 alias로 키가 없을 시, 새로 생성
                // RSA KeyPair 생성
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                Calendar start = Calendar.getInstance(Locale.KOREA);
                Calendar end = Calendar.getInstance(Locale.KOREA);

                end.add(Calendar.YEAR, 1); // 만료기한

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias) // 첫 번째, 인자는 별칭
                        .setSubject(new X500Principal("CN=" + alias)) // 두 번째, 인자는 key 사용 목적
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                kpg.initialize(spec);
                kpg.generateKeyPair();

            }

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Log.i("this", "error", e);
        }

    }

    // KeyStore의 RSA로 암호화
    public static String keyStore_Encryption(String str){

        String keyStore_Encryption_DATA="";

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore"); // Android KeyStore 접근
            keyStore.load(null); // 로드

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias,null);
            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey(); // KeyStore의 공개키 반환

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 운용모드/패딩 셋업
            cipher.init(Cipher.ENCRYPT_MODE,publicKey); // 암호화 준비

            // RSA 암호화
            byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));

            // 암호화된 데이터, 인코딩 후 'String'으로 반환
            keyStore_Encryption_DATA = Base64.getEncoder().encodeToString(encrypted);

        } catch (NoSuchPaddingException e) {
            System.err.println("keyStore_Encryption NoSuchPaddingException error");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("keyStore_Encryption NoSuchAlgorithmException error");
        } catch (InvalidKeyException e) {
            System.err.println("keyStore_Encryption InvalidKeyException error");
        } catch (BadPaddingException e) {
            System.err.println("keyStore_Encryption BadPaddingException error");
        } catch (IllegalBlockSizeException e) {
            System.err.println("keyStore_Encryption IllegalBlockSizeException error");
        } catch (UnsupportedEncodingException e) {
            System.err.println("keyStore_Encryption UnsupportedEncodingException error");
        } catch (CertificateException e) {
            System.err.println("keyStore_Encryption CertificateException error");
        } catch (KeyStoreException e) {
            System.err.println("keyStore_Encryption KeyStoreException error");
        } catch (UnrecoverableEntryException e) {
            System.err.println("keyStore_Encryption UnrecoverableEntryException error");
        } catch (IOException e) {
            System.err.println("keyStore_Encryption IOException error");
        }
        return keyStore_Encryption_DATA;
    }

    // KeyStore의 RSA로 복호화
    public static String keyStore_Decryption(String str){
        String keyStore_Decryption_DATA="";
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore"); // Android KeyStore 접근
            keyStore.load(null); // 로드
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias,null);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey(); // KeyStore의 개인키 반환

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);


            // 암호화된 인코딩 데이터 -> 디코딩
            byte[] byteStr = Base64.getDecoder().decode(str);

            // 디코딩된 암호문 -> 복호화 후 'String'으로 반환
            keyStore_Decryption_DATA = new String(cipher.doFinal(byteStr));

        } catch (KeyStoreException e) {
            System.err.println("keyStore_Encryption KeyStoreException error");
        } catch (CertificateException e) {
            System.err.println("keyStore_Encryption CertificateException error");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("keyStore_Encryption NoSuchAlgorithmException error");
        } catch (IOException e) {
            System.err.println("keyStore_Encryption IOException error");
        } catch (UnrecoverableEntryException e) {
            System.err.println("keyStore_Encryption UnrecoverableEntryException error");
        } catch (InvalidKeyException e) {
            System.err.println("keyStore_Encryption InvalidKeyException error");
        } catch (NoSuchPaddingException e) {
            System.err.println("keyStore_Encryption NoSuchPaddingException error");
        } catch (BadPaddingException e) {
            System.err.println("keyStore_Encryption BadPaddingException error");
        } catch (IllegalBlockSizeException e) {
            System.err.println("keyStore_Encryption IllegalBlockSizeException error");
        }
        return keyStore_Decryption_DATA;
    }
}