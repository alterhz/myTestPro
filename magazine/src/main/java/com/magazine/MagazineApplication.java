package com.magazine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.magazine")
public class MagazineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagazineApplication.class, args);
	}

}
