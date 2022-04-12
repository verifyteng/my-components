package com.tcomponent.secretkeeper;

import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class SecretKeeperApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SecretKeeperApplication.class, args);

        new CoreExecutor().doParseAndExecute(args);
    }

}
