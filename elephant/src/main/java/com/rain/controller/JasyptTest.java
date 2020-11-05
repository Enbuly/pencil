package com.rain.controller;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * spring boot yml数据库密码加密
 *
 * @author lazy cat
 * 2020-11-25
 **/
public class JasyptTest {
    public static void main(String[] args) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("Hello");
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "120157229";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);

        String encryptedTexts = "uDtOznN2aFC+o6bPerrljDaqGOEglWsL";
        String plainTexts = standardPBEStringEncryptor.decrypt(encryptedTexts);
        System.out.println(plainTexts);
    }
}
