package org.movoto.selenium.example.utils;

public class EncryptMain {
    public static void main(String[] args) {
        String src = "xyz";
        String encrypted = AESUtil.encrypt(src);
        String decrypted = AESUtil.decrypt(encrypted);
        System.out.println("src: " + src);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
    }
}


