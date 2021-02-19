package org.ziegler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunApplication {

    static {
        System.out.println("RunApplication.static initializer");
    }

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        System.out.println("RunApplication.main");
    }
}
