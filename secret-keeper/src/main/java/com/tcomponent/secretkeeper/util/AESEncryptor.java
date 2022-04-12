package com.tcomponent.secretkeeper.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;

import java.util.Objects;

@Slf4j
public class AESEncryptor {

    BasicTextEncryptor encryptor;

    public AESEncryptor(String password) {
        encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        log.info("AESEncryptor initialized.");
    }

    public String encrypt(String text) {
        if (Objects.isNull(encryptor)) {
            log.error("AESEncryptor not initialized.");
        }
        return encryptor.encrypt(text);
    }

    public String decrypt(String text) {
        if (Objects.isNull(encryptor)) {
            log.error("AESEncryptor not initialized.");
        }
        return encryptor.decrypt(text);
    }
}
