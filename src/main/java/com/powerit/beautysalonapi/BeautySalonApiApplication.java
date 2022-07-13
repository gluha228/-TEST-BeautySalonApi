package com.powerit.beautysalonapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class BeautySalonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautySalonApiApplication.class, args);

        System.out.println("version: " + SpringVersion.getVersion());
    }

}
